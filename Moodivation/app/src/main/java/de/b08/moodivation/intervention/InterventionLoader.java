/*
 * MIT License
 *
 * Copyright (c) 2023 RUB-SE-LAB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.b08.moodivation.intervention;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class InterventionLoader {

    public static boolean loadAndStoreExternalFile(Uri source, Context context) {
        if (context.getFilesDir() == null)
            return false;
        File[] files = context.getFilesDir().listFiles();
        if (files == null)
            return false;

        if (Arrays.stream(files).noneMatch(f -> f.isDirectory() && f.getName().equals("interventions")))
           if (!new File(context.getFilesDir().getPath() + "/interventions/").mkdirs())
               return false;

        File destDir = new File(context.getFilesDir().getPath() + "/interventions/");

        try (InputStream in = context.getContentResolver().openInputStream(source)) {
            ZipInputStream zipInputStream = new ZipInputStream(in);
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().startsWith("_"))
                    continue;

                File entryDest = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    if (!entryDest.mkdirs())
                        return false;
                } else {
                    FileOutputStream fileOut = new FileOutputStream(entryDest);

                    int b;
                    while ((b = zipInputStream.read()) != -1) {
                        fileOut.write(b);
                    }

                    fileOut.flush();
                    fileOut.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        getAllInterventions(context, true);
        return true;
    }

    private static List<Runnable> interventionListUpdateHandlers = new ArrayList<>();

    private static List<InterventionBundle> interventions;

    public static Runnable addInterventionListUpdateHandler(Runnable runnable) {
        interventionListUpdateHandlers.add(runnable);
        return runnable;
    }

    public static void removeInterventionListUpdateHandler(Runnable runnable) {
        if (runnable == null)
            return;

        interventionListUpdateHandlers.remove(runnable);
    }

    public static List<InterventionBundle> getAllInterventions(Context context) {
        return getAllInterventions(context, false);
    }

    public static List<InterventionBundle> getAllInterventions(Context context, boolean forceReload) {
        if (interventions == null || forceReload) {
            interventions = loadFromAssets(context);
            interventions.addAll(loadFromExternalFiles(context));

            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> interventionListUpdateHandlers.forEach(Runnable::run));
        }

        return interventions;
    }

    public static Optional<InterventionBundle> getInterventionWithId(String id, Context context) {
        return getAllInterventions(context).stream().filter(i -> Objects.equals(i.getId(), id)).findFirst();
    }

    public static Intervention getLocalizedIntervention(InterventionBundle bundle) {
        if (bundle == null)
            return null;
        return bundle.getInterventionMap().entrySet().stream().filter(e -> e.getKey().equals(Locale.getDefault().getLanguage())).map(Map.Entry::getValue).findFirst().orElse(bundle.getInterventionMap().values().stream().findAny().get());
    }

    public static InterventionBundle getRandomIntervention(Context context) {
        List<InterventionBundle> interventionBundles = getAllInterventions(context);
        int index = new Random().nextInt(interventionBundles.size());
        return interventionBundles.get(index);
    }

    public static class InterventionBundleData {

        public String[] languages;
        public String id;

    }

    public static class InterventionData {

        public String title;
        public String description;
        public List<Intervention.DataType> displayedDataTypes;
        public InterventionMediaData interventionMediaData;
        public InterventionOptionsData interventionOptionsData;

    }

    public static class InterventionMediaData {
        String videoPath;
        String[] imagePaths;
    }

    public static class InterventionOptionsData {
        boolean showOptionalImages;
        boolean showOptionalVideo;
    }

    public static List<InterventionBundle> loadFromExternalFiles(Context context) {
        List<InterventionBundle> interventionBundles = new ArrayList<>();
        String[] paths = new File(context.getFilesDir().getPath() + "/interventions").list();

        if (paths == null)
            return Collections.emptyList();

        for (String p : paths) {
            if (p.matches("[A-Za-z]*.intervention")) {
                load(context, String.format("%s/interventions/%s/", context.getFilesDir().getPath(), p), false)
                        .ifPresent(interventionBundles::add);
            }
        }

        return interventionBundles;
    }

    public static List<InterventionBundle> loadFromAssets(Context context) {
        try {
            List<InterventionBundle> interventionBundles = new ArrayList<>();
            String[] paths = context.getAssets().list("interventions/");

            for (String p : paths) {
                if (p.matches("[A-Za-z]*.intervention")) {
                    load(context, String.format("interventions/%s/", p), true).ifPresent(interventionBundles::add);
                }
            }

            return interventionBundles;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Optional<InterventionBundle> load(Context context, String path, boolean internal) {
        try {
            Map<String, Intervention> interventionMap = new HashMap<>();
            Gson gson = new Gson();

            InputStream in = internal ? context.getAssets().open(path + "bundledata.json") :
                    Files.newInputStream(new File(path + "bundledata.json").toPath());

            InterventionBundleData bundleData = gson.fromJson(new InputStreamReader(in),
                    InterventionBundleData.class);

            for (String language : bundleData.languages) {
                String langPath = String.format("%s%s/", path, language);
                loadIntervention(context, langPath, bundleData, internal).ifPresent(i -> interventionMap.put(language, i));
            }

            return interventionMap.isEmpty() ? Optional.empty() : Optional.of(new InterventionBundle(bundleData.id, interventionMap));
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private static Optional<Intervention> loadIntervention(Context context, String path, InterventionBundleData bundleData, boolean internal) {
        try {
            Gson gson = new Gson();
            InputStream in = internal ? context.getAssets().open(path + "intervention.json")
                    : Files.newInputStream(new File(path + "intervention.json").toPath());

            InterventionData interventionData = gson.fromJson(new InputStreamReader(in),
                    InterventionData.class);

            InterventionMedia interventionMedia = null;
            if (interventionData.interventionMediaData != null) {
                List<String> imagePaths = interventionData.interventionMediaData.imagePaths != null ? Arrays.stream(interventionData.interventionMediaData.imagePaths)
                        .map(s -> getCompletePath(path, s)).collect(Collectors.toList()) : Collections.emptyList();
                String videoPath = interventionData.interventionMediaData.videoPath != null ?
                        getCompletePath(path, interventionData.interventionMediaData.videoPath) : null;
                interventionMedia = new InterventionMedia(videoPath, imagePaths, internal);
            }

            InterventionOptions options = null;
            if (interventionData.interventionOptionsData != null) {
                options = new InterventionOptions(interventionData.interventionOptionsData.showOptionalImages,
                        interventionData.interventionOptionsData.showOptionalVideo);
            }
            Intervention intervention = new Intervention(bundleData.id, interventionData.title,
                    interventionData.description, interventionMedia, interventionData.displayedDataTypes, options);

            return Optional.of(intervention);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private static String getCompletePath(String relativePath, String path) {
        String p = path;
        String r = relativePath;
        while (p.startsWith("../")) {
            p = p.replaceFirst("\\.\\./", "");
            r = r.replaceFirst("[ A-Za-z0-9]*\\/$", "");
        }
        return r + p;
    }

}

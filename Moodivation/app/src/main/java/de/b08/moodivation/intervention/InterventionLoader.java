package de.b08.moodivation.intervention;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class InterventionLoader {

    private static List<InterventionBundle> interventions;

    public static List<InterventionBundle> getAllInterventions(Context context) {
        if (interventions == null) {
            interventions = loadFromAssets(context);
        }

        return interventions;
    }

    public static Optional<InterventionBundle> getInterventionWithId(String id, Context context) {
        return getAllInterventions(context).stream().filter(i -> Objects.equals(i.getId(), id)).findFirst();
    }

    public static Intervention getLocalizedIntervention(InterventionBundle bundle) {
        // TODO: implement
        if (bundle == null)
            return null;
        return bundle.getInterventionMap().values().stream().findFirst().orElse(null);
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

    public static List<InterventionBundle> loadFromAssets(Context context) {
        try {
            List<InterventionBundle> interventionBundles = new ArrayList<>();
            String[] paths = context.getAssets().list("interventions/");

            for (String p : paths) {
                if (p.matches("[A-Za-z]*.intervention")) {
                    loadFromAssets(context, String.format("interventions/%s/", p)).ifPresent(interventionBundles::add);
                }
            }

            return interventionBundles;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Optional<InterventionBundle> loadFromAssets(Context context, String path) {
        try {
            Map<String, Intervention> interventionMap = new HashMap<>();
            Gson gson = new Gson();

            InterventionBundleData bundleData = gson.fromJson(new InputStreamReader(context.getAssets().open(path + "bundledata.json")),
                    InterventionBundleData.class);

            for (String language : bundleData.languages) {
                String langPath = String.format("%s%s/", path, language);
                loadInterventionFromAssets(context, langPath, bundleData).ifPresent(i -> interventionMap.put(language, i));
            }

            return interventionMap.isEmpty() ? Optional.empty() : Optional.of(new InterventionBundle(bundleData.id, interventionMap));
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private static Optional<Intervention> loadInterventionFromAssets(Context context, String path, InterventionBundleData bundleData) {
        try {
            Gson gson = new Gson();
            InterventionData interventionData = gson.fromJson(new InputStreamReader(context.getAssets().open(path + "intervention.json")),
                    InterventionData.class);

            InterventionMedia interventionMedia = null;
            if (interventionData.interventionMediaData != null) {
                List<String> imagePaths = interventionData.interventionMediaData.imagePaths != null ? Arrays.stream(interventionData.interventionMediaData.imagePaths)
                        .map(s -> getCompletePath(path, s)).collect(Collectors.toList()) : Collections.emptyList();
                String videoPath = interventionData.interventionMediaData.videoPath != null ?
                        getCompletePath(path, interventionData.interventionMediaData.videoPath) : null;
                interventionMedia = new InterventionMedia(videoPath, imagePaths, true);
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

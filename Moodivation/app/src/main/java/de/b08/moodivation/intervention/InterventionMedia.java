package de.b08.moodivation.intervention;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InterventionMedia implements Serializable {

    /**
     * Internal interventions are stored in assets and loaded differently.
     */
    private final boolean internalIntervention;

    private final List<String> imagePaths;

    private final String videoPath;

    public InterventionMedia(@Nullable String videoPath, @Nullable List<String> imagePaths, boolean internalIntervention) {
        this.imagePaths = imagePaths == null ? null : Collections.unmodifiableList(imagePaths);
        this.videoPath = videoPath;
        this.internalIntervention = internalIntervention;
    }

    public boolean isInternalIntervention() {
        return internalIntervention;
    }

    public @Nullable List<Drawable> getDrawableImages(Context context) {
        if (imagePaths == null)
            return null;

        return imagePaths.stream().map(p -> {
            try (InputStream in = internalIntervention ? context.getAssets().open(p) : Files.newInputStream(new File(p).toPath())) {
                return new BitmapDrawable(context.getResources(), in);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public @Nullable MediaItem getVideoMediaItem() {
        if (videoPath == null)
            return null;

        if (internalIntervention) {
            return MediaItem.fromUri(Uri.parse(String.format("asset:///%s", videoPath)));
        } else {
            return MediaItem.fromUri(videoPath);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterventionMedia)) return false;
        InterventionMedia that = (InterventionMedia) o;
        return internalIntervention == that.internalIntervention && Objects.equals(imagePaths, that.imagePaths) && Objects.equals(videoPath, that.videoPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalIntervention, imagePaths, videoPath);
    }
}

package de.b08.moodivation.intervention;

import java.io.Serializable;
import java.util.Objects;

public class InterventionOptions implements Serializable {

    private final boolean showOptionalImages;
    private final boolean showOptionalVideo;

    public InterventionOptions(boolean showOptionalImages, boolean showOptionalVideo) {
        this.showOptionalImages = showOptionalImages;
        this.showOptionalVideo = showOptionalVideo;
    }

    public boolean isShowOptionalImages() {
        return showOptionalImages;
    }

    public boolean isShowOptionalVideo() {
        return showOptionalVideo;
    }

    public static InterventionOptions getDefaultOptions() {
        return new InterventionOptions(true, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InterventionOptions)) return false;
        InterventionOptions options = (InterventionOptions) o;
        return showOptionalImages == options.showOptionalImages && showOptionalVideo == options.showOptionalVideo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(showOptionalImages, showOptionalVideo);
    }
}

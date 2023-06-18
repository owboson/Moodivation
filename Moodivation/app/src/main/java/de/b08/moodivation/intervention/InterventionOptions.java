package de.b08.moodivation.intervention;

import java.io.Serializable;

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

}

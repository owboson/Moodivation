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

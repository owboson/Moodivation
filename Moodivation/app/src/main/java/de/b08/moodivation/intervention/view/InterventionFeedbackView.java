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

package de.b08.moodivation.intervention.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import de.b08.moodivation.R;

/**
 * View for giving feedback to an intervention
 */
public class InterventionFeedbackView extends LinearLayout {

    TextView interventionRatingLabel;
    TextView interventionCommentLabel;
    RatingBar interventionRatingBar;
    EditText interventionCommentTextView;

    public InterventionFeedbackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.intervention_feedback_view,this);

        interventionRatingLabel = findViewById(R.id.interventionRatingLabel);
        interventionCommentLabel = findViewById(R.id.interventionCommentLabel);
        interventionRatingBar = findViewById(R.id.interventionRatingBar);
        interventionCommentTextView = findViewById(R.id.interventionCommentTextView);

        interventionRatingBar.setNumStars(5);
        interventionRatingBar.setStepSize(0.5F);
    }

    public float getRating() {
        return interventionRatingBar.getRating();
    }

    public String getComment() {
        return interventionCommentTextView.getText().toString();
    }

}

package de.b08.moodivation.intervention.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import de.b08.moodivation.R;

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

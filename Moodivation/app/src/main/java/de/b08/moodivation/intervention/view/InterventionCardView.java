package de.b08.moodivation.intervention.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;

import de.b08.moodivation.R;
import de.b08.moodivation.intervention.Intervention;

public class InterventionCardView extends LinearLayout {

    private InterventionView interventionView;

    public InterventionCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        CardView cardView = new CardView(getContext());
        cardView.setRadius(64);
        cardView.setCardElevation(24);

        cardView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) cardView.getLayoutParams();
        marginLayoutParams.setMargins(32,32,32,32);

        interventionView = new InterventionView(context, attrs);

        cardView.addView(interventionView);
        addView(cardView);
    }

    public void setIntervention(Intervention intervention) {
        interventionView.setIntervention(intervention);
    }

    public InterventionView getInterventionView() {
        return interventionView;
    }
}

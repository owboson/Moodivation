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
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import de.b08.moodivation.intervention.Intervention;

/**
 * An intervention view wrapped in a card view
 */
public class InterventionCardView extends LinearLayout {

    private final InterventionView interventionView;

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

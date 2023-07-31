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

package de.b08.moodivation.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;

import de.b08.moodivation.R;

public class DescribedValueView extends CardView {

    TextView valueTextField;
    TextView descriptionTextField;

    public DescribedValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.described_value_view, this);

        valueTextField = findViewById(R.id.valueTextField);
        descriptionTextField = findViewById(R.id.descriptionTextField);
        initValueTextView(valueTextField);

        setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        setCardElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
    }

    public void setValueText(String text) {
        valueTextField.setText(text);
    }

    public void setValueText(@StringRes int resId) {
        valueTextField.setText(resId);
    }

    public void setDescriptionText(String text) {
        descriptionTextField.setText(text);
    }

    public void setDescriptionText(@StringRes int resId) {
        descriptionTextField.setText(resId);
    }

    public TextView getValueTextField() {
        return valueTextField;
    }

    protected void initValueTextView(TextView textView) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    protected void replaceValueView(TextView newTextView) {
        replaceView(valueTextField, newTextView);
        valueTextField = newTextView;
        initValueTextView(newTextView);
    }

    private void replaceView(View current, View replacement) {
        ViewParent parent = current.getParent();
        if (!(parent instanceof ViewGroup))
            return;

        ViewGroup parentViewGroup = (ViewGroup) parent;
        int pos = parentViewGroup.indexOfChild(current);
        parentViewGroup.removeView(current);
        parentViewGroup.addView(replacement, pos);
    }
}

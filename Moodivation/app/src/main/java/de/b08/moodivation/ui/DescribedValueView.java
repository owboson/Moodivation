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

import de.b08.moodivation.R;

public class DescribedValueView extends LinearLayout {

    TextView valueTextField;
    TextView descriptionTextField;

    public DescribedValueView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.described_value_view, this);

        valueTextField = findViewById(R.id.valueTextField);
        descriptionTextField = findViewById(R.id.descriptionTextField);
        initValueTextView(valueTextField);
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

package de.b08.moodivation.ui;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

public class EditableRadioButton extends AppCompatRadioButton {
    public EditableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableRadioButton(Context context) {
        super(context);
    }

    public void setEditable(boolean editable) {
        setCursorVisible(editable);
        setFocusableInTouchMode(editable);
        setInputType(InputType.TYPE_CLASS_TEXT);
    }
}

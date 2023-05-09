package de.b08.moodivation.ui;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

public class EditableCheckBox extends AppCompatCheckBox {

    public EditableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableCheckBox(Context context) {
        super(context);
    }

    public void setEditable(boolean editable) {
        setCursorVisible(editable);
        setFocusableInTouchMode(editable);
        setInputType(InputType.TYPE_CLASS_TEXT);
    }

}

package de.b08.moodivation.ui;

import android.widget.NumberPicker;

public class CustomPickerAdapter implements NumberPicker.Formatter {
    private static final String[] VALUES1 = {"Morning", "Noon", "Evening"};
    private static final String[] VALUES2 = {"Morning & Noon", "Morning & Evening", "Noon & Evening"};
    private String[] values;

    public CustomPickerAdapter(int period) {
        if (period == 1) {
            values = VALUES1;
        } else if (period == 2) {
            values = VALUES2;
        }
    }

    @Override
    public String format(int value) {
        return values[value];
    }
}

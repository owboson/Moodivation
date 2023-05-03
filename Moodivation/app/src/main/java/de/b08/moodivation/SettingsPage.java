package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingsPage extends AppCompatActivity {

    // Find the switch and the LinearLayout in your activity or fragment
    boolean correctIntervals_morning = true;
    boolean correctIntervals_evening = true;
    boolean correctIntervals_day = true;

    NumberPicker morning_from_hour_picker;
    NumberPicker day_from_hour_picker;
    NumberPicker evening_from_hour_picker;
    NumberPicker morning_to_hour_picker;
    NumberPicker day_to_hour_picker;
    NumberPicker evening_to_hour_picker;


    NumberPicker morning_from_minute_picker;
    NumberPicker day_from_minute_picker;
    NumberPicker evening_from_minute_picker;
    NumberPicker morning_to_minute_picker;
    NumberPicker day_to_minute_picker;
    NumberPicker evening_to_minute_picker;

    NumberPicker hour_pickers[];
    NumberPicker minute_pickers[];

    Button saveButton;

    private void reset_pickers() {
        this.morning_from_hour_picker.setValue(9);
        this.morning_from_minute_picker.setValue(0);

        this.morning_to_hour_picker.setValue(10);
        this.morning_to_minute_picker.setValue(30);

        this.day_from_hour_picker.setValue(13);
        this.day_from_minute_picker.setValue(30);

        this.day_to_hour_picker.setValue(14);
        this.day_to_minute_picker.setValue(30);

        this.evening_from_hour_picker.setValue(18);
        this.evening_from_minute_picker.setValue(0);

        this.evening_to_hour_picker.setValue(19);
        this.evening_to_minute_picker.setValue(30);

        this.correctIntervals_morning = true;
        this.correctIntervals_evening = true;
        this.correctIntervals_day = true;

        for(NumberPicker picker: hour_pickers) {
            picker.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        for(NumberPicker picker: minute_pickers) {
            picker.setTextColor(ContextCompat.getColor(this, R.color.black));
        }

        this.saveButton.setEnabled(true);
    }

    private void picker_listener(int from_hour_picker_val, int to_hour_picker_val,
                                int from_minute_picker_val, int to_minute_picker_val,
                                NumberPicker from_hour_picker, NumberPicker to_hour_picker,
                                NumberPicker from_minute_picker, NumberPicker to_minute_picker, int type) {
        if (to_hour_picker_val < from_hour_picker_val) {
            if (type==0) {
                this.correctIntervals_morning = false;
            } else if (type==1) {
                this.correctIntervals_day = false;
            } else if (type==2) {
                this.correctIntervals_evening = false;
            }
            this.saveButton.setEnabled(false);
            from_hour_picker.setTextColor(ContextCompat.getColor(this, R.color.red));
            to_hour_picker.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else if (to_hour_picker_val == from_hour_picker_val &&
                to_minute_picker_val < from_minute_picker_val) {
            if (type==0) {
                this.correctIntervals_morning = false;
            } else if (type==1) {
                this.correctIntervals_day = false;
            } else if (type==2) {
                this.correctIntervals_evening = false;
            }

            this.saveButton.setEnabled(false);
            to_minute_picker.setTextColor(ContextCompat.getColor(this, R.color.red));
            from_minute_picker.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            if (type==0) {
                this.correctIntervals_morning = true;
            } else if (type==1) {
                this.correctIntervals_day = true;
            } else if (type==2) {
                this.correctIntervals_evening = true;
            }
            if (this.correctIntervals_morning && this.correctIntervals_day && this.correctIntervals_evening) {
                this.saveButton.setEnabled(true);
            }
            from_hour_picker.setTextColor(ContextCompat.getColor(this, R.color.black));
            to_hour_picker.setTextColor(ContextCompat.getColor(this, R.color.black));
            to_minute_picker.setTextColor(ContextCompat.getColor(this, R.color.black));
            from_minute_picker.setTextColor(ContextCompat.getColor(this, R.color.black));
        }




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Switch aSwitch = findViewById(R.id.notifications_switch);
        LinearLayout intervalsLayout = findViewById(R.id.intervals_layout);

        Switch dataCollectSwitch = findViewById(R.id.allow_data_collect_switch);
        RelativeLayout dataCollectionLayout = findViewById(R.id.data_collection_view);

        this.saveButton = findViewById(R.id.save_button);

        this.morning_from_hour_picker = findViewById(R.id.morning_from_hour_picker);
        this.day_from_hour_picker = findViewById(R.id.day_from_hour_picker);
        this.evening_from_hour_picker = findViewById(R.id.evening_from_hour_picker);
        this.morning_to_hour_picker = findViewById(R.id.morning_to_hour_picker);
        this.day_to_hour_picker = findViewById(R.id.day_to_hour_picker);
        this.evening_to_hour_picker = findViewById(R.id.evening_to_hour_picker);


        this.morning_from_minute_picker = findViewById(R.id.morning_from_minute_picker);
        this.day_from_minute_picker = findViewById(R.id.day_from_minute_picker);
        this.evening_from_minute_picker = findViewById(R.id.evening_from_minute_picker);
        this.morning_to_minute_picker = findViewById(R.id.morning_to_minute_picker);
        this.day_to_minute_picker = findViewById(R.id.day_to_minute_picker);
        this.evening_to_minute_picker = findViewById(R.id.evening_to_minute_picker);

        this.hour_pickers = new NumberPicker[]{morning_from_hour_picker, day_from_hour_picker, evening_from_hour_picker, morning_to_hour_picker, day_to_hour_picker, evening_to_hour_picker};
        this.minute_pickers = new NumberPicker[]{morning_from_minute_picker, day_from_minute_picker, evening_from_minute_picker, morning_to_minute_picker, day_to_minute_picker, evening_to_minute_picker};

        String[] hours  = new String[24];
        for(int i=0; i<24; i++){
            if (i < 10) {
                hours[i] = "0"+String.valueOf(i);
            } else {
                hours[i] = String.valueOf(i);
            }
        }

        String[] minutes  = new String[60];
        for(int i=0; i<60; i++){
            if (i < 10) {
                minutes[i] = "0"+String.valueOf(i);
            } else {
                minutes[i] = String.valueOf(i);
            }

        }

        for(NumberPicker picker : hour_pickers) {
            picker.setMinValue(0);
            picker.setMaxValue(23);
            picker.setDisplayedValues(hours);
            picker.setWrapSelectorWheel(true);
        }

        for(NumberPicker picker : minute_pickers) {
            picker.setMinValue(0);
            picker.setMaxValue(59);
            picker.setDisplayedValues(minutes);
            picker.setWrapSelectorWheel(true);
        }

        reset_pickers();


//        // Set the initial visibility of the time-intervals-layout and data-collection-layout based on the state of the switch
        intervalsLayout.setVisibility(aSwitch.isChecked() ? View.VISIBLE : View.GONE);
        dataCollectionLayout.setVisibility(dataCollectSwitch.isChecked() ? View.VISIBLE : View.GONE);

        //        // Add an OnCheckedChangeListener to the switch
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the visibility of the LinearLayout based on the state of the switch
                reset_pickers();
                intervalsLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        dataCollectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the visibility of the LinearLayout based on the state of the switch
                dataCollectionLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        morning_from_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(newVal, morning_to_hour_picker.getValue(),
                    morning_from_minute_picker.getValue(), morning_to_minute_picker.getValue(),
                    morning_from_hour_picker, morning_to_hour_picker,
                    morning_from_minute_picker, morning_to_minute_picker, 0);
        });

        morning_to_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(morning_from_hour_picker.getValue(), newVal,
                    morning_from_minute_picker.getValue(), morning_to_minute_picker.getValue(),
                    morning_from_hour_picker, morning_to_hour_picker,
                    morning_from_minute_picker, morning_to_minute_picker, 0);

        });

        morning_from_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(morning_from_hour_picker.getValue(), morning_to_hour_picker.getValue(),
                    newVal, morning_to_minute_picker.getValue(),
                    morning_from_hour_picker, morning_to_hour_picker,
                    morning_from_minute_picker, morning_to_minute_picker, 0);
        });


        morning_to_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(morning_from_hour_picker.getValue(), morning_to_hour_picker.getValue(),
                    morning_from_minute_picker.getValue(), newVal,
                    morning_from_hour_picker, morning_to_hour_picker,
                    morning_from_minute_picker, morning_to_minute_picker, 0);
        });

//        -----

        day_from_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(newVal, day_to_hour_picker.getValue(),
                    day_from_minute_picker.getValue(), day_to_minute_picker.getValue(),
                    day_from_hour_picker, day_to_hour_picker,
                    day_from_minute_picker, day_to_minute_picker, 1);
        });

        day_to_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(day_from_hour_picker.getValue(), newVal,
                    day_from_minute_picker.getValue(), day_to_minute_picker.getValue(),
                    day_from_hour_picker, day_to_hour_picker,
                    day_from_minute_picker, day_to_minute_picker, 1);

        });

        day_from_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(day_from_hour_picker.getValue(), day_to_hour_picker.getValue(),
                    newVal, day_to_minute_picker.getValue(),
                    day_from_hour_picker, day_to_hour_picker,
                    day_from_minute_picker, day_to_minute_picker, 1);
        });


        day_to_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(day_from_hour_picker.getValue(), day_to_hour_picker.getValue(),
                    day_from_minute_picker.getValue(), newVal,
                    day_from_hour_picker, day_to_hour_picker,
                    day_from_minute_picker, day_to_minute_picker, 1);
        });

// -------

        evening_from_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(newVal, evening_to_hour_picker.getValue(),
                    evening_from_minute_picker.getValue(), evening_to_minute_picker.getValue(),
                    evening_from_hour_picker, evening_to_hour_picker,
                    evening_from_minute_picker, evening_to_minute_picker, 2);
        });

        evening_to_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(evening_from_hour_picker.getValue(), newVal,
                    evening_from_minute_picker.getValue(), evening_to_minute_picker.getValue(),
                    evening_from_hour_picker, evening_to_hour_picker,
                    evening_from_minute_picker, evening_to_minute_picker, 2);
        });

        evening_from_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(evening_from_hour_picker.getValue(), evening_to_hour_picker.getValue(),
                    newVal, evening_to_minute_picker.getValue(),
                    evening_from_hour_picker, evening_to_hour_picker,
                    evening_from_minute_picker, evening_to_minute_picker, 2);
        });


        evening_to_minute_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(evening_from_hour_picker.getValue(), evening_to_hour_picker.getValue(),
                    evening_from_minute_picker.getValue(), newVal,
                    evening_from_hour_picker, evening_to_hour_picker,
                    evening_from_minute_picker, evening_to_minute_picker, 2);
        });
    }


}
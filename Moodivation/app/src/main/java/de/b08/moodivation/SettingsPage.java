package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Map;

import de.b08.moodivation.ui.CustomPickerAdapter;

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

    LinearLayout intervalsLayout;
    LinearLayout dataCollectionLayout;
    RelativeLayout intervalsSwitchLayout;

    TextView morningIntervalTextview;
    TextView dayIntervalTextview;
    TextView eveningIntervalTextview;

    LinearLayout morningInterval_layout;
    LinearLayout dayInterval_layout;
    LinearLayout evenigInterval_layout;


    Switch aSwitch;
    Switch changeIntervalsSwitch;
    Switch dataCollectSwitch;
    Switch googleApiSwitch;

    Switch questionnaireSwitch;
    Switch digitSpanSwitch;

    LinearLayout questionnaireLayout;
    RelativeLayout periodicityLayout;
    NumberPicker timesPerDayPicker;
    NumberPicker periodicityPicker;

    TextView errorText;
    Button saveButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void reset_pickers() {

        String morning_from = sharedPreferences.getString("morning_from", "9:0");
        String morning_to = sharedPreferences.getString("morning_to", "10:30");

        String day_from = sharedPreferences.getString("day_from", "13:30");
        String day_to = sharedPreferences.getString("day_to", "14:30");

        String evening_from = sharedPreferences.getString("evening_from", "18:0");
        String evening_to = sharedPreferences.getString("evening_to", "19:30");



        this.morning_from_hour_picker.setValue(Integer.parseInt(morning_from.split(":")[0]));
        this.morning_from_minute_picker.setValue(Integer.parseInt(morning_from.split(":")[1]));

        this.morning_to_hour_picker.setValue(Integer.parseInt(morning_to.split(":")[0]));
        this.morning_to_minute_picker.setValue(Integer.parseInt(morning_to.split(":")[1]));

        this.day_from_hour_picker.setValue(Integer.parseInt(day_from.split(":")[0]));
        this.day_from_minute_picker.setValue(Integer.parseInt(day_from.split(":")[1]));

        this.day_to_hour_picker.setValue(Integer.parseInt(day_to.split(":")[0]));
        this.day_to_minute_picker.setValue(Integer.parseInt(day_to.split(":")[1]));

        this.evening_from_hour_picker.setValue(Integer.parseInt(evening_from.split(":")[0]));
        this.evening_from_minute_picker.setValue(Integer.parseInt(evening_from.split(":")[1]));

        this.evening_to_hour_picker.setValue(Integer.parseInt(evening_to.split(":")[0]));
        this.evening_to_minute_picker.setValue(Integer.parseInt(evening_to.split(":")[1]));

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
                to_minute_picker_val <= from_minute_picker_val) {
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

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        aSwitch = findViewById(R.id.notifications_switch);
        intervalsLayout = findViewById(R.id.intervals_layout);
        intervalsSwitchLayout = findViewById(R.id.intervals_switch);

        morningIntervalTextview = findViewById(R.id.morning_interval_textview);
        dayIntervalTextview = findViewById(R.id.day_interval_textview);
        eveningIntervalTextview = findViewById(R.id.evening_interval_textview);

        morningInterval_layout = findViewById(R.id.morning_interval_layout);
        dayInterval_layout = findViewById(R.id.day_interval_layout);
        evenigInterval_layout = findViewById(R.id.evening_interval_layout);

        dataCollectSwitch = findViewById(R.id.allow_data_collect_switch);
        changeIntervalsSwitch = findViewById(R.id.change_intervals_switch);
        dataCollectionLayout = findViewById(R.id.data_collection_view);

        googleApiSwitch = findViewById(R.id.google_api_switch);
        questionnaireSwitch = findViewById(R.id.questionnaire_switch);
        questionnaireSwitch.setChecked(sharedPreferences.getInt("allow_questionnaire_data_collection", 0)==1);

        digitSpanSwitch = findViewById(R.id.digit_span_switch);

        questionnaireLayout = findViewById(R.id.questionnaire_layout);

        questionnaireLayout.setVisibility(sharedPreferences.getInt("allow_questionnaire_data_collection", 0)==1 ? View.VISIBLE : View.GONE);
        periodicityLayout = findViewById(R.id.periodicity_layout);

        saveButton = findViewById(R.id.save_button);

        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        timesPerDayPicker = findViewById(R.id.times_per_day_picker);
        timesPerDayPicker.setMinValue(1);
        timesPerDayPicker.setMaxValue(3);
        timesPerDayPicker.setValue(sharedPreferences.getInt("questionnaire_periodicity", 3));


        periodicityPicker = findViewById(R.id.periodicity_picker);
        CustomPickerAdapter formatter = new CustomPickerAdapter(2);
        periodicityPicker.setFormatter(formatter);
        periodicityPicker.setMinValue(0);
        periodicityPicker.setMaxValue(2);


        if (sharedPreferences.getInt("questionnaire_periodicity", 3) == 1){
            periodicityPicker.setFormatter(new CustomPickerAdapter(1));
            periodicityPicker.setValue(sharedPreferences.getInt("questionnaire_periodicity_details", 0));
        } else if (sharedPreferences.getInt("questionnaire_periodicity", 3) == 2){
            periodicityPicker.setValue(sharedPreferences.getInt("questionnaire_periodicity_details", 0));
            periodicityPicker.setFormatter(new CustomPickerAdapter(2));
        } else if (sharedPreferences.getInt("questionnaire_periodicity", 3) == 3) {
            periodicityLayout.setVisibility(View.GONE);
        }

        if (sharedPreferences.getInt("allow_digit_span_collection", 1) == 1) {
            digitSpanSwitch.setChecked(true);
        }

        aSwitch.setChecked(sharedPreferences.getInt("allow_notifs", 0) == 1);
        intervalsSwitchLayout.setVisibility(aSwitch.isChecked() ? View.VISIBLE : View.GONE);
        dataCollectSwitch.setChecked(sharedPreferences.getInt("allow_data_collection", 0) == 1);
        dataCollectionLayout.setVisibility(dataCollectSwitch.isChecked() ? View.VISIBLE : View.GONE);
        googleApiSwitch.setChecked(sharedPreferences.getInt("google_fit_collection", 0) == 1);

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
        intervalsLayout.setVisibility(changeIntervalsSwitch.isChecked() ? View.VISIBLE : View.GONE);
        dataCollectionLayout.setVisibility(dataCollectSwitch.isChecked() ? View.VISIBLE : View.GONE);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the visibility of the LinearLayout based on the state of the switch
                if (aSwitch.isChecked()) {
                    intervalsSwitchLayout.setVisibility(View.VISIBLE);
                } else {
                    intervalsSwitchLayout.setVisibility(View.GONE);
                    intervalsLayout.setVisibility(View.GONE);
                    changeIntervalsSwitch.setChecked(false);
                }
            }
        });

        //        // Add an OnCheckedChangeListener to the switch
        changeIntervalsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                if (isChecked) {
                    dataCollectionLayout.setVisibility(View.VISIBLE);
                } else {
                    dataCollectionLayout.setVisibility(View.GONE);
                    googleApiSwitch.setChecked(false);
                    questionnaireSwitch.setChecked(false);
                    questionnaireLayout.setVisibility(View.GONE);

                    digitSpanSwitch.setChecked(false);

                }
            }
        });

        questionnaireSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the visibility of the LinearLayout based on the state of the switch
                if (isChecked) {
                    questionnaireLayout.setVisibility(View.VISIBLE);
                } else {
                    questionnaireLayout.setVisibility(View.GONE);
                }
            }
        });

        timesPerDayPicker.setOnValueChangedListener((_v, _vv, newVal) -> {
            if(newVal < 3){
                periodicityPicker.setValue(0);
                periodicityPicker.setFormatter(new CustomPickerAdapter(newVal));
                periodicityLayout.setVisibility(View.VISIBLE);
                periodicityPicker.invalidate();
            } else {
                periodicityLayout.setVisibility(View.GONE);
                periodicityPicker.invalidate();
            }
        });



        morning_from_hour_picker.setOnValueChangedListener((_v, _vv, newVal) -> {
            picker_listener(morning_from_hour_picker.getValue(), morning_to_hour_picker.getValue(),
                    newVal, morning_to_minute_picker.getValue(),
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


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                System.out.println(periodicityPicker.getValue());
                int morning_from_hour_picker_val = morning_from_hour_picker.getValue();
                int morning_from_minute_picker_val = morning_from_minute_picker.getValue();
                int morning_to_hour_picker_val = morning_to_hour_picker.getValue();
                int morning_to_minute_picker_val = morning_to_minute_picker.getValue();

                int day_from_hour_picker_val = day_from_hour_picker.getValue();
                int day_from_minute_picker_val = day_from_minute_picker.getValue();
                int day_to_hour_picker_val = day_to_hour_picker.getValue();
                int day_to_minute_picker_val = day_to_minute_picker.getValue();

                int evening_from_hour_picker_val = evening_from_hour_picker.getValue();
                int evening_from_minute_picker_val = evening_from_minute_picker.getValue();
                int evening_to_hour_picker_val = evening_to_hour_picker.getValue();
                int evening_to_minute_picker_val = evening_to_minute_picker.getValue();

                if (day_from_hour_picker_val < morning_to_hour_picker_val ||
                        (day_from_hour_picker_val == morning_to_hour_picker_val && morning_to_minute_picker_val > day_from_minute_picker_val) ||
                    evening_from_hour_picker_val < day_to_hour_picker_val ||
                        (evening_from_hour_picker_val == day_to_hour_picker_val && day_to_minute_picker_val > evening_from_minute_picker_val)
                ) {
                    errorText.setVisibility(View.VISIBLE);
                    saveButton.setEnabled(false);
                } else {
                    errorText.setVisibility(View.GONE);
                    String morning_from = ((Integer) morning_from_hour_picker_val).toString() + ":" + ((Integer) morning_from_minute_picker_val).toString();
                    String morning_to = ((Integer)morning_to_hour_picker_val).toString() + ":" + ((Integer)morning_to_minute_picker_val).toString();

                    String day_from = ((Integer)day_from_hour_picker_val).toString() + ":" + ((Integer)day_from_minute_picker_val).toString();
                    String day_to = ((Integer)day_to_hour_picker_val).toString() + ":" + ((Integer)day_to_minute_picker_val).toString();

                    String evening_from = ((Integer)evening_from_hour_picker_val).toString() + ":" + ((Integer)evening_from_minute_picker_val).toString();
                    String evening_to = ((Integer)evening_to_hour_picker_val).toString() + ":" + ((Integer)evening_to_minute_picker_val).toString();

                    editor.putInt("allow_notifs", aSwitch.isChecked() ? 1 : 0);
                    if (aSwitch.isChecked()) {
                        editor.putString("morning_from", morning_from);
                        editor.putString("morning_to", morning_to);
                        editor.putString("day_from", day_from);
                        editor.putString("day_to", day_to);
                        editor.putString("evening_from", evening_from);
                        editor.putString("evening_to", evening_to);
                    }

                    editor.putInt("allow_data_collection", dataCollectSwitch.isChecked() ? 1 : 0);
                    editor.putInt("google_fit_collection", googleApiSwitch.isChecked() ? 1 : 0);

                    editor.putInt("allow_questionnaire_data_collection", questionnaireSwitch.isChecked() ? 1: 0);
                    editor.putInt("questionnaire_periodicity", timesPerDayPicker.getValue());
                    editor.putInt("questionnaire_periodicity_details", periodicityPicker.getValue());
                    editor.putInt("allow_digit_span_collection", digitSpanSwitch.isChecked() ? 1: 0);
                    editor.commit();

                    changeIntervalsSwitch.setChecked(false);
                    intervalsLayout.setVisibility(View.GONE);

                    new AlertDialog.Builder(SettingsPage.this)
                        .setTitle("Success")
                        .setMessage("All the changes were saved")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Perform action when "Discard" button is clicked
                            }
                        })
                        .show();

                }
            }
        });
    }


}
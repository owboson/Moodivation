package de.b08.moodivation.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.b08.moodivation.R;

@SuppressWarnings("unused")
public class SensorSettingsView<A, F> extends LinearLayout {

    @FunctionalInterface
    public interface OnSensorEnabledChangeListener {
        void onChange(boolean isEnabled);
    }

    private boolean accuracyEnabled;
    private boolean frequencyEnabled;
    private boolean sensorEnabled;

    private boolean blockSensorEnabledEvents = false;

    private final LinearLayout accuracyView;

    private final LinearLayout frequencyView;

    private final LinearLayout detailedSettingsView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final Switch sensorSwitch;

    private List<A> accuracyValues;

    private List<F> frequencyValues;

    private A defaultSelectedAccuracy;
    private F defaultSelectedFrequency;

    private final Spinner accuracySpinner;
    private final StringResourceArrayAdapter<Object> accuracyAdapter;
    private final Spinner frequencySpinner;
    private final StringResourceArrayAdapter<Object> frequencyAdapter;

    private Function<A, Integer> accuracyItemTitleStringResProvider;
    private Function<A, String> accuracyItemTitleProvider;

    private Function<F, Integer> frequencyItemTitleStringResProvider;
    private Function<F, String> frequencyItemTitleProvider;

    private OnSensorEnabledChangeListener onSensorEnabledChangeListener;

    public SensorSettingsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.sensor_settings_view, this);

        accuracyView = findViewById(R.id.accuracyView);
        frequencyView = findViewById(R.id.frequencyView);
        sensorSwitch = findViewById(R.id.sensorSwitch);
        detailedSettingsView = findViewById(R.id.sensorDetailedSettingsView);
        frequencySpinner = findViewById(R.id.frequencySpinner);
        accuracySpinner = findViewById(R.id.accuracySpinner);

        accuracyValues = new ArrayList<>();
        frequencyValues = new ArrayList<>();

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SensorSettingsView, 0, 0);
        boolean showAccuracy = attributes.getBoolean(R.styleable.SensorSettingsView_showAccuracy, false);
        String sensorTitle = attributes.getString(R.styleable.SensorSettingsView_sensorTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            attributes.close();
        }

        if (sensorTitle != null) {
            sensorSwitch.setText(sensorTitle);
        }

        setAccuracyViewVisible(showAccuracy);
        accuracyEnabled = showAccuracy;

        sensorSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDetailedSettingsViewVisible(isChecked);
            if (onSensorEnabledChangeListener != null && !blockSensorEnabledEvents)
                onSensorEnabledChangeListener.onChange(isChecked);
        });

        accuracyAdapter = new StringResourceArrayAdapter<>(getContext(), R.layout.spinner_item);
        accuracyAdapter.setNotifyOnChange(false);
        accuracySpinner.setAdapter(accuracyAdapter);

        frequencyAdapter = new StringResourceArrayAdapter<>(getContext(), R.layout.spinner_item);
        frequencyAdapter.setNotifyOnChange(false);
        frequencySpinner.setAdapter(frequencyAdapter);

        accuracyValues = new ArrayList<>();
        frequencyValues = new ArrayList<>();

        setDetailedSettingsViewVisible(sensorSwitch.isChecked());
    }

    public void setSensorTitle(@StringRes int resId) {
        sensorSwitch.setText(resId);
    }

    public void setSensorTitle(String title) {
        sensorSwitch.setText(title);
    }

    public void setAccuracyValues(@NonNull List<A> accuracies) {
        accuracyValues = new ArrayList<>(accuracies);
        accuracyAdapter.clear();

        Function<A, ?> mapFunc = accuracyItemTitleStringResProvider != null ? accuracyItemTitleStringResProvider :
                accuracyItemTitleProvider != null ? accuracyItemTitleProvider : Objects::toString;

        accuracyAdapter.addAll(accuracyValues.stream()
                .map(mapFunc)
                .collect(Collectors.toList()));

        accuracyAdapter.notifyDataSetChanged();
        accuracyAdapter.setNotifyOnChange(false);

        if (defaultSelectedAccuracy != null && accuracyValues.contains(defaultSelectedAccuracy)) {
            setSelectedAccuracy(defaultSelectedAccuracy);
        } else {
            defaultSelectedAccuracy = null;
        }
    }

    public void setFrequencyValues(@NonNull List<F> frequencies) {
        frequencyValues = new ArrayList<>(frequencies);
        frequencyAdapter.clear();

        Function<F, ?> mapFunc = frequencyItemTitleStringResProvider != null ? frequencyItemTitleStringResProvider :
                frequencyItemTitleProvider != null ? frequencyItemTitleProvider : Objects::toString;

        frequencyAdapter.addAll(frequencyValues.stream()
                .map(mapFunc)
                .collect(Collectors.toList()));

        frequencyAdapter.notifyDataSetChanged();
        frequencyAdapter.setNotifyOnChange(false);

        if (defaultSelectedFrequency != null && frequencyValues.contains(defaultSelectedFrequency)) {
            setSelectedFrequency(defaultSelectedFrequency);
        } else {
            defaultSelectedFrequency = null;
        }
    }

    public @Nullable A getSelectedAccuracy() {
        if (!accuracyEnabled || !sensorEnabled)
            return null;

        int index = accuracySpinner.getSelectedItemPosition();
        return accuracyValues.get(index);
    }

    public @Nullable F getSelectedFrequency() {
        if (!frequencyEnabled || !sensorEnabled)
            return null;

        int index = frequencySpinner.getSelectedItemPosition();
        return frequencyValues.get(index);
    }

    public void setDetailedSettingsViewVisible(boolean visible) {
        detailedSettingsView.setVisibility(visible ? VISIBLE : GONE);
        detailedSettingsView.setEnabled(visible);
        sensorEnabled = visible;
    }

    public void setSelectedFrequency(F frequency) {
        int index = frequencyValues.indexOf(frequency);

        if (index == -1) {
            defaultSelectedFrequency = frequency;
        } else {
            frequencySpinner.setSelection(index);
            defaultSelectedFrequency = null;
        }
    }

    public void setSelectedAccuracy(A accuracy) {
        int index = accuracyValues.indexOf(accuracy);

        if (index == -1) {
            defaultSelectedAccuracy = accuracy;
        } else {
            accuracySpinner.setSelection(index);
            defaultSelectedAccuracy = null;
        }
    }

    public void setAccuracyViewVisible(boolean visible) {
        accuracyView.setVisibility(visible ? VISIBLE : GONE);
        accuracyView.setEnabled(visible);
        accuracyEnabled = visible;
    }

    public void setFrequencyViewVisible(boolean visible) {
        frequencyView.setVisibility(visible ? VISIBLE : GONE);
        frequencyView.setEnabled(visible);
        frequencyEnabled = visible;
    }

    public void setAccuracyItemTitleStringResProvider(Function<A, Integer> accuracyItemTitleStringResProvider) {
        this.accuracyItemTitleStringResProvider = accuracyItemTitleStringResProvider;
    }

    public void setAccuracyItemTitleProvider(Function<A, String> accuracyItemTitleProvider) {
        this.accuracyItemTitleProvider = accuracyItemTitleProvider;
    }

    public void setFrequencyItemTitleProvider(Function<F, String> frequencyItemTitleProvider) {
        this.frequencyItemTitleProvider = frequencyItemTitleProvider;
    }

    public void setFrequencyItemTitleStringResProvider(Function<F, Integer> frequencyItemTitleStringResProvider) {
        this.frequencyItemTitleStringResProvider = frequencyItemTitleStringResProvider;
    }

    public void setOnSensorEnabledChangeListener(OnSensorEnabledChangeListener onSensorEnabledChangeListener) {
        this.onSensorEnabledChangeListener = onSensorEnabledChangeListener;
    }

    public void setSensorEnabled(boolean enabled) {
        setDetailedSettingsViewVisible(enabled);
        sensorSwitch.setChecked(enabled);
        sensorEnabled = enabled;
    }

    public void setSensorEnabledAndBlockEvent(boolean enabled) {
        setBlockSensorEnabledEvents(true);
        setDetailedSettingsViewVisible(enabled);
        sensorSwitch.setChecked(enabled);
        sensorEnabled = enabled;
        setBlockSensorEnabledEvents(false);
    }

    public boolean isSensorEnabled() {
        return sensorEnabled;
    }

    public void setBlockSensorEnabledEvents(boolean blockSensorEnabledEvents) {
        this.blockSensorEnabledEvents = blockSensorEnabledEvents;
    }
}

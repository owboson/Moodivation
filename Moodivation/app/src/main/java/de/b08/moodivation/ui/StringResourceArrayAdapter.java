package de.b08.moodivation.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public class StringResourceArrayAdapter<T> extends ArrayAdapter<T> {

    private final static String TAG = "StringResourceArrayAdapter";

    private final int textViewResourceId;

    public StringResourceArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        textViewResourceId = 0;
    }

    public StringResourceArrayAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.textViewResourceId = textViewResourceId;
    }

    public StringResourceArrayAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);
        textViewResourceId = 0;
    }

    public StringResourceArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    public StringResourceArrayAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        textViewResourceId = 0;
    }

    public StringResourceArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView text;
        if (view instanceof TextView) {
            text = (TextView) view;
        } else {
            text = view.findViewById(textViewResourceId);
        }

        manipulateText(text, position);

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView text;
        if (view instanceof TextView) {
            text = (TextView) view;
        } else {
            text = view.findViewById(textViewResourceId);
        }

        manipulateText(text, position);

        return view;
    }

    private void manipulateText(TextView text, int position) {
        T item = getItem(position);

        if (item instanceof Integer) {
            try {
                getContext().getResources().getText((Integer) item);
            } catch (Resources.NotFoundException ex) {
                Log.w(TAG, "item is integer but resource doesn't exist (displayed as integer; consider using ArrayAdapter)", ex);
                return;
            }

            text.setText((Integer) item);
        }
    }

}

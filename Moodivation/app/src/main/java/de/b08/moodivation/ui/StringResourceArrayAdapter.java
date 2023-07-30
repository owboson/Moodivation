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

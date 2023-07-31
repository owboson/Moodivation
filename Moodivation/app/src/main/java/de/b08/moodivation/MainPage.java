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

package de.b08.moodivation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.b08.moodivation.ui.WellbeingChart;

/**
 * The home fragment of the app.
 */
public class MainPage extends Fragment {

    protected SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = view.getContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        Button digitSpanTaskPreviewBtn = view.findViewById(R.id.digitSpanTaskPreviewButton);
        digitSpanTaskPreviewBtn.setOnClickListener(v -> MainPage.this.startActivity(new Intent(getActivity(), DigitSpanTask.class)));

        Button questionnairePreviewBtn = view.findViewById(R.id.questionnairePreviewButton);

        questionnairePreviewBtn.setOnClickListener(v -> {
            if (sharedPreferences.getInt("allow_questionnaire_data_collection", 0) == 1) {
                Intent questionnaireIntent = new Intent(getActivity(), QuestionnaireActivity.class);
                questionnaireIntent.putExtra("name", "main");
                MainPage.this.startActivity(questionnaireIntent);
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(view.getContext());
                builder.setMessage(R.string.allowQuestionnaireDataCollection)
                        .setTitle(R.string.settings)
                        .setPositiveButton("OK", (dialog, id) -> {});

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        WellbeingChart wellbeingChart = view.findViewById(R.id.wellbeingChart);
        wellbeingChart.setIntervalChangeAllowed(false);
        wellbeingChart.setManualReloadingAllowed(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            WellbeingChart chart = getView().findViewById(R.id.wellbeingChart);
            chart.syncChart();
        }
    }

}
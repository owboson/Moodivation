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

import de.b08.moodivation.services.ChartDataClass;
import de.b08.moodivation.ui.WellbeingChart;

public class MainActivity extends Fragment {

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
        digitSpanTaskPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(getActivity(), DigitSpanTask.class));
        });

        Button questionnairePreviewBtn = view.findViewById(R.id.questionnairePreviewButton);

        questionnairePreviewBtn.setOnClickListener(v -> {
            if (sharedPreferences.getInt("allow_questionnaire_data_collection", 0) == 1) {
                Intent questionnaireIntent = new Intent(getActivity(), QuestionnaireActivity.class);
                questionnaireIntent.putExtra("name", "main");
                MainActivity.this.startActivity(questionnaireIntent);
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
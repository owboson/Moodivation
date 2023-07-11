package de.b08.moodivation;

import android.content.Context;
import android.content.DialogInterface;
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

import de.b08.moodivation.services.СhartDataClass;

public class MainActivity extends Fragment {
    protected SharedPreferences sharedPreferences;

    СhartDataClass data;

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

        sharedPreferences = getContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        /*view.findViewById(R.id.uploadData).setOnClickListener(v -> {
            AsyncTask.execute(() -> {
                try {
                    String id = sharedPreferences.getString("uploadId", "null");
                    if (id.equals("null")) {
                        id = ExportUtils.generateId();
                        sharedPreferences.edit().putString("uploadId", id).apply();
                    }
                    Pair<Boolean, Integer> result = ExportUtils.exportDatabases(getActivity(), id);
                    if (!result.first) {
                        getActivity().runOnUiThread(() -> {
                            new android.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Data Upload")
                                    .setMessage("Something went wrong.")
                                    .setPositiveButton("OK", (dialog, which) -> {})
                                    .show();
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            new android.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Data Upload")
                                    .setMessage("Data was uploaded successfully.")
                                    .setPositiveButton("OK", (dialog, which) -> {})
                                    .show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> {
                        new android.app.AlertDialog.Builder(getActivity())
                                .setTitle("Data Upload")
                                .setMessage("Something went wrong.")
                                .setPositiveButton("OK", (dialog, which) -> {})
                                .show();
                    });
                }
            });
        });*/

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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setMessage("Allow data collection to be able to complete the questionnaire")
                        .setTitle("Settings")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        /*Button settingsPreviewBtn = view.findViewById(R.id.settingsPreviewButton);
        settingsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(getActivity(), SettingsPage.class));
        });*/

        /* ActivityResultLauncher<String> fileChooser = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> {
            result.forEach(u -> InterventionLoader.loadAndStoreExternalFile(u, getContext()));
        });

        view.findViewById(R.id.addInterventionBtn).setOnClickListener(v -> {
            fileChooser.launch("application/zip");
        }); */

        /*view.findViewById(R.id.interventionListBtn).setOnClickListener(v -> {
            Intent overviewIntent = new Intent(getActivity(), InterventionOverviewActivity.class);
            startActivity(overviewIntent);
        });

        Button recordsPreviewBtn = view.findViewById(R.id.recordsPreviewButton);
        recordsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(getActivity(), RecordsPage.class));
        });*/

        /*Button chartsPreviewBtn = view.findViewById(R.id.chartsPreviewButton);
        chartsPreviewBtn.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(getActivity(), DevelopmentVisualization.class));
        }); */

        /*Button rewardsPreviewButton = view.findViewById(R.id.rewardsPreviewButton);
        rewardsPreviewButton.setOnClickListener(v -> {
            MainActivity.this.startActivity(new Intent(getActivity(), Rewards.class));
        });*/


        data = new СhartDataClass(view.findViewById(R.id.barChart), getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        data = new СhartDataClass(getView().findViewById(R.id.barChart), getContext());
    }

}
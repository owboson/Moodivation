package de.b08.moodivation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.b08.moodivation.utils.ExportUtils;
import de.b08.moodivation.intervention.InterventionLoader;

public class MoodivationActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;

    BottomNavigationView navigationView;

    ActivityResultLauncher<String> interventionFileChooser;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DynamicColors.applyToActivitiesIfAvailable(MoodivationApplication.getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodivation_activity);

        interventionFileChooser = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> {
            result.forEach(u -> InterventionLoader.loadAndStoreExternalFile(u, getApplicationContext()));
        });

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        FragmentContainerView fragmentContainerView = findViewById(R.id.fragmentContainerView2);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.activitiesItem:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2, InterventionOverviewActivity.class, null)
                            .commit();
                    break;
                case R.id.homeItem:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2, MainActivity.class, null)
                            .commit();
                    break;
                case R.id.rewardsItem:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2, Rewards.class, null)
                            .commit();
                    break;
                case R.id.dataItem:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView2, DataFragment.class, null)
                            .commit();
                    break;
                default:
                    ;
            }
            return true;
        });

        navigationView.setSelectedItemId(R.id.homeItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        DynamicColors.applyToActivitiesIfAvailable(MoodivationApplication.getApplication());
        if (item.getItemId() == R.id.addInterventionItemId) {
            interventionFileChooser.launch("application/zip");
            return true;
        } else if (item.getItemId() == R.id.uploadDataItemId) {
            AsyncTask.execute(() -> {
                try {
                    String id = sharedPreferences.getString("uploadId", "null");
                    if (id.equals("null")) {
                        id = ExportUtils.generateId();
                        sharedPreferences.edit().putString("uploadId", id).apply();
                    }
                    Pair<Boolean, Integer> result = ExportUtils.exportDatabases(this, id);
                    if (!result.first) {
                        runOnUiThread(() -> {
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle("Data Upload")
                                    .setMessage("Something went wrong.")
                                    .setPositiveButton("OK", (dialog, which) -> {})
                                    .show();
                        });
                    } else {
                        runOnUiThread(() -> {
                            new MaterialAlertDialogBuilder(this)
                                    .setTitle("Data Upload")
                                    .setMessage("Data was uploaded successfully.")
                                    .setPositiveButton("OK", (dialog, which) -> {})
                                    .show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        new MaterialAlertDialogBuilder(this)
                                .setTitle("Data Upload")
                                .setMessage("Something went wrong.")
                                .setPositiveButton("OK", (dialog, which) -> {})
                                .show();
                    });
                }
            });
        } else if (item.getItemId() == R.id.settingsItemId) {
            startActivity(new Intent(this, SettingsPage.class));
        }
        return super.onOptionsItemSelected(item);
    }

}

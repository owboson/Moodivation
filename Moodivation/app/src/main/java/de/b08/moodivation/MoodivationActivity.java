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

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moodivation_activity);

        interventionFileChooser = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), result -> result.forEach(u -> {
            AsyncTask.execute(() -> {
                boolean success = InterventionLoader.loadAndStoreExternalFile(u, getApplicationContext());
                if (!success) {
                    runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.addInterventionItem)
                            .setMessage(R.string.dataUploadError)
                            .setPositiveButton(R.string.dataUploadOk, (dialog, which) -> {})
                            .show());
                }
            });
        }));

        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);

        navigationView = findViewById(R.id.navigationView);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.activitiesItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2, InterventionOverview.class, null)
                        .commit();
            } else if (item.getItemId() == R.id.homeItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2, MainPage.class, null)
                        .commit();
            } else if (item.getItemId() == R.id.rewardsItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2, Rewards.class, null)
                        .commit();
            } else if (item.getItemId() == R.id.dataItem) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView2, DataFragment.class, null)
                        .commit();
            }
            return true;
        });

        navigationView.setSelectedItemId(R.id.homeItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
                        runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                                .setTitle(R.string.dataUploadTitle)
                                .setMessage(R.string.dataUploadError)
                                .setPositiveButton(R.string.dataUploadOk, (dialog, which) -> {})
                                .show());
                    } else {
                        runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                                .setTitle(R.string.dataUploadTitle)
                                .setMessage(R.string.dataUploadSuccess)
                                .setPositiveButton(R.string.dataUploadOk, (dialog, which) -> {})
                                .show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> new MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.dataUploadTitle)
                            .setMessage(R.string.dataUploadError)
                            .setPositiveButton(R.string.dataUploadOk, (dialog, which) -> {})
                            .show());
                }
            });
        } else if (item.getItemId() == R.id.settingsItemId) {
            startActivity(new Intent(this, SettingsPage.class));
        }
        return super.onOptionsItemSelected(item);
    }

}

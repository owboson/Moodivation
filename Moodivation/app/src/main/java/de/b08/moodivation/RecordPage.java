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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;

/**
 * Displays a record and allows to delete the record
 */
public class RecordPage extends AppCompatActivity {

    TextView title;
    TextView from;
    TextView to;
    Button deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(v -> deleteRecord());
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("values");
        title = findViewById(R.id.title);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

        title.setText(data[0]);
        from.setText(data[1]);
        to.setText(data[2]);
    }

    private void deleteRecord() {
        InterventionRecordEntity record = (InterventionRecordEntity) getIntent().getExtras().get("record");
        AsyncTask.execute(() -> InterventionDatabase.getInstance(getApplicationContext()).interventionRecordDao().delete(record));
        finish();
    }

}
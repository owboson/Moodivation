package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import de.b08.moodivation.database.interventions.InterventionDatabase;
import de.b08.moodivation.database.interventions.entities.InterventionRecordEntity;

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
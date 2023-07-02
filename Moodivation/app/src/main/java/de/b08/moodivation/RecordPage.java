package de.b08.moodivation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.color.DynamicColors;

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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord("0");
            }
        });
        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("values");
        title = findViewById(R.id.title);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

        title.setText(data[0]);
        from.setText(data[1]);
        to.setText(data[2]);
    }
    private void deleteRecord(String interventionId) {
        System.out.println("print");
    }

}
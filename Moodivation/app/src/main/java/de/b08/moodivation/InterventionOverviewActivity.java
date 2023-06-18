package de.b08.moodivation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;
import de.b08.moodivation.intervention.view.InterventionCardView;

public class InterventionOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intervention_overview);

        List<InterventionBundle> interventionBundleList = InterventionLoader.getAllInterventions(getApplicationContext());

        ListView interventionListView = findViewById(R.id.interventionListView);
        interventionListView.setDivider(null);
        interventionListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return interventionBundleList.size();
            }

            @Override
            public InterventionBundle getItem(int position) {
                return interventionBundleList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return -1;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                InterventionBundle intervention = getItem(position);
                if (convertView == null) {
                    InterventionCardView interventionView = new InterventionCardView(getApplicationContext(), null);

                    interventionView.getInterventionView().setContentViewAllowed(false);
                    interventionView.setIntervention(InterventionLoader.getLocalizedIntervention(intervention));

                    interventionView.setOnClickListener(v -> {
                        Intent startInterventionIntent = new Intent(getApplicationContext(), InterventionActivity.class);
                        startInterventionIntent.putExtra(InterventionActivity.INTERVENTION_EXTRA_KEY,
                                InterventionLoader.getLocalizedIntervention(intervention));

                        startActivity(startInterventionIntent);
                    });

                    return interventionView;
                }

                InterventionCardView interventionView = (InterventionCardView) convertView;
                interventionView.getInterventionView().setContentViewAllowed(false);
                interventionView.setIntervention(InterventionLoader.getLocalizedIntervention(intervention));

                interventionView.setOnClickListener(v -> {
                    Intent startInterventionIntent = new Intent(getApplicationContext(), InterventionActivity.class);
                    startInterventionIntent.putExtra(InterventionActivity.INTERVENTION_EXTRA_KEY,
                            InterventionLoader.getLocalizedIntervention(intervention));

                    startActivity(startInterventionIntent);
                });
                return interventionView;
            }
        });
    }

}

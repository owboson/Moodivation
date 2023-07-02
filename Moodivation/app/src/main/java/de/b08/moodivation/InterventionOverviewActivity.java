package de.b08.moodivation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;
import de.b08.moodivation.intervention.view.InterventionCardView;

public class InterventionOverviewActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intervention_overview, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<InterventionBundle> interventionBundleList = InterventionLoader.getAllInterventions(view.getContext());

        ListView interventionListView = getView().findViewById(R.id.interventionListView);
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
                    InterventionCardView interventionView = new InterventionCardView(view.getContext(), null);

                    interventionView.getInterventionView().setContentViewAllowed(false);
                    interventionView.setIntervention(InterventionLoader.getLocalizedIntervention(intervention));

                    interventionView.setOnClickListener(v -> {
                        Intent startInterventionIntent = new Intent(view.getContext(), InterventionActivity.class);
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
                    Intent startInterventionIntent = new Intent(view.getContext(), InterventionActivity.class);
                    startInterventionIntent.putExtra(InterventionActivity.INTERVENTION_EXTRA_KEY,
                            InterventionLoader.getLocalizedIntervention(intervention));

                    startActivity(startInterventionIntent);
                });
                return interventionView;
            }
        });
    }

}
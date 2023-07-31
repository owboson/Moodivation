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

import java.util.ArrayList;
import java.util.List;

import de.b08.moodivation.intervention.InterventionBundle;
import de.b08.moodivation.intervention.InterventionLoader;
import de.b08.moodivation.intervention.view.InterventionCardView;

public class InterventionOverview extends Fragment {

    Runnable updateRunnable;

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
    public void onDestroy() {
        super.onDestroy();
        InterventionLoader.removeInterventionListUpdateHandler(updateRunnable);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView interventionListView = view.findViewById(R.id.interventionListView);
        interventionListView.setDivider(null);

        InterventionBaseAdapter interventionBaseAdapter = new InterventionBaseAdapter();

        interventionListView.setAdapter(interventionBaseAdapter);
        interventionBaseAdapter.updateList(InterventionLoader.getAllInterventions(view.getContext()));

        updateRunnable = InterventionLoader.addInterventionListUpdateHandler(() ->
                interventionBaseAdapter.updateList(InterventionLoader.getAllInterventions(getContext())));
    }

    public final class InterventionBaseAdapter extends BaseAdapter {

        ArrayList<InterventionBundle> interventionBundles = new ArrayList<>();

        public void updateList(List<InterventionBundle> interventionBundles) {
            this.interventionBundles.clear();
            this.interventionBundles.addAll(interventionBundles);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return interventionBundles.size();
        }

        @Override
        public InterventionBundle getItem(int position) {
            return interventionBundles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InterventionBundle intervention = getItem(position);
            if (convertView == null) {
                InterventionCardView interventionView = new InterventionCardView(getContext(), null);

                interventionView.getInterventionView().setContentViewAllowed(false);
                interventionView.setIntervention(InterventionLoader.getLocalizedIntervention(intervention));

                interventionView.setOnClickListener(v -> {
                    Intent startInterventionIntent = new Intent(getContext(), InterventionActivity.class);
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
                Intent startInterventionIntent = new Intent(getContext(), InterventionActivity.class);
                startInterventionIntent.putExtra(InterventionActivity.INTERVENTION_EXTRA_KEY,
                        InterventionLoader.getLocalizedIntervention(intervention));

                startActivity(startInterventionIntent);
            });
            return interventionView;
        }
    }

}
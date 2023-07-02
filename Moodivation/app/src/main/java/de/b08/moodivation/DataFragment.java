package de.b08.moodivation;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class DataFragment extends Fragment {

    ViewPager2 pager;
    TabLayout dataTabLayout;

    private final List<Pair<Fragment, Integer>> fragments = Arrays.asList(
            new Pair<>(new DevelopmentVisualization(), R.string.charts),
            new Pair<>(new RecordsPage(), R.string.records)
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.data_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = view.findViewById(R.id.pager);
        pager.setAdapter(new DataFragmentAdapter(this));

        dataTabLayout = view.findViewById(R.id.dataTabLayout);

        new TabLayoutMediator(dataTabLayout, pager,
                (tab, position) -> tab.setText(getResources().getText(fragments.get(position).second))
        ).attach();
    }

    public class DataFragmentAdapter extends FragmentStateAdapter {

        public DataFragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return DataFragment.this.fragments.get(position).first;
        }

        @Override
        public int getItemCount() {
            return DataFragment.this.fragments.size();
        }

    }

}

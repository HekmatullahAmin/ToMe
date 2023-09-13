package com.hekmatullahamin.plan.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hekmatullahamin.plan.fragments.CalculationFragment;
import com.hekmatullahamin.plan.fragments.TimeTableFragment;
import com.hekmatullahamin.plan.fragments.TodoFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final int numberOfTabs;
    private Fragment fragment = null;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        fragment = new TodoFragment();
        switch (position) {
            case 0:
                fragment = new TodoFragment();
                break;
            case 1:
                fragment = new CalculationFragment();
                break;
            case 2:
                fragment = new TimeTableFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}

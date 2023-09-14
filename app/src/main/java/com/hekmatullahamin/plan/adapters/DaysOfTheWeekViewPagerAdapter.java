package com.hekmatullahamin.plan.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hekmatullahamin.plan.fragments.DayOfTheWeekFragment;

public class DaysOfTheWeekViewPagerAdapter extends FragmentStateAdapter {

    private static final String KEY_DAY_OF_THE_WEEK = "DAY_OF_THE_WEEK";
    public DaysOfTheWeekViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new DayOfTheWeekFragment();
        Bundle myBundle = new Bundle();
        switch (position) {
            case 0:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Monday");
                break;
            case 1:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Tuesday");
                break;
            case 2:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Wednesday");
                break;
            case 3:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Thursday");
                break;
            case 4:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Friday");
                break;
            case 5:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Saturday");
                break;
            case 6:
                myBundle.putString(KEY_DAY_OF_THE_WEEK, "Sunday");
                break;
        }
        fragment.setArguments(myBundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}

package com.hekmatullahamin.plan.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hekmatullahamin.plan.fragments.DashboardFragment;
import com.hekmatullahamin.plan.fragments.FriendsFragment;
import com.hekmatullahamin.plan.fragments.TransactionsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private Fragment fragment = null;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        fragment = new DashboardFragment();
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                break;
            case 1:
                fragment = new FriendsFragment();
                break;
            case 2:
                fragment = new TransactionsFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

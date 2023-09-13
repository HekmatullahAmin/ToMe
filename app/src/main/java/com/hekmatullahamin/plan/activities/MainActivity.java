package com.hekmatullahamin.plan.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ViewPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //MA stands for mainActivity
    private ViewPager MAViewPager;
    private TabLayout MATabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private final int[] tabIcons = {
            R.drawable.ic_baseline_to_do,
            R.drawable.ic_baseline_calculation,
            R.drawable.ic_baseline_time_table_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        MAViewPager = findViewById(R.id.mainActivityViewPagerId);
        MATabLayout = findViewById(R.id.mainActivityTabLayoutId);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 3);
        MAViewPager.setAdapter(viewPagerAdapter);
        MATabLayout.setupWithViewPager(MAViewPager);

        for (int i = 0; i < MATabLayout.getTabCount(); i++) {
            Objects.requireNonNull(MATabLayout.getTabAt(i)).setIcon(tabIcons[i]);
        }
    }
}
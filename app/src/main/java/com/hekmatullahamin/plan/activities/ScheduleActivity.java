package com.hekmatullahamin.plan.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.DaysOfTheWeekViewPagerAdapter;

public class ScheduleActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    private ViewPager2 viewPager2;
    private DaysOfTheWeekViewPagerAdapter daysOfTheWeekViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        fieldsInitialization();
    }

    private void fieldsInitialization() {
        viewPager2 = findViewById(R.id.scheduleActivityViewPager2Id);
        daysOfTheWeekViewPagerAdapter = new DaysOfTheWeekViewPagerAdapter(this);
        viewPager2.setAdapter(daysOfTheWeekViewPagerAdapter);
    }
}
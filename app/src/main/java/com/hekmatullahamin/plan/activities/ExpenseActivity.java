package com.hekmatullahamin.plan.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ViewPagerAdapter;

public class ExpenseActivity extends AppCompatActivity {

    private ViewPager2 expensesViewPager2;
    private ViewPagerAdapter expensesViewPagerAdapter;
    private TabLayout expensesTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        fieldsInitialization();
    }

    private void fieldsInitialization() {
        expensesViewPager2 = findViewById(R.id.expensesActivityViewPager2Id);
        expensesTabLayout = findViewById(R.id.expensesActivityTabLayoutId);
        expensesViewPagerAdapter = new ViewPagerAdapter(this);
        expensesViewPager2.setAdapter(expensesViewPagerAdapter);

        int selectedTabIconColor = ContextCompat.getColor(this, R.color.black);
        int unSelectedTabIconColor = ContextCompat.getColor(this, R.color.white);

        new TabLayoutMediator(expensesTabLayout, expensesViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.dashboard, null));
                        tab.setText("Dashboard");
                        break;
                    case 1:
                        tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.group, null));
                        tab.setText("Friends");
                        break;
                    case 2:
                        tab.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.transactions, null));
                        tab.setText("Transactions");
                        break;
                }
            }
        }).attach();

//        for setting icon color selected and unselected when activity start
        expensesTabLayout.getTabAt(0).getIcon().setColorFilter(selectedTabIconColor, PorterDuff.Mode.SRC_IN);
        expensesTabLayout.getTabAt(1).getIcon().setColorFilter(unSelectedTabIconColor, PorterDuff.Mode.SRC_IN);
        expensesTabLayout.getTabAt(2).getIcon().setColorFilter(unSelectedTabIconColor, PorterDuff.Mode.SRC_IN);

//        for changing color icons of tab after clicking
        expensesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(ExpenseActivity.this, R.color.black);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(ExpenseActivity.this, R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
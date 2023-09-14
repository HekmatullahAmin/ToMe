package com.hekmatullahamin.plan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.AddMyActivityActivity;
import com.hekmatullahamin.plan.adapters.ScheduleRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;

public class DayOfTheWeekFragment extends Fragment {

    private TextView dayOfTheWeekTV;
    private TextView bottomBoxFromTimeTV, bottomAmountTimeRemained, bottomToTimeTV;
    private View bottomRelativeLayout;
    private RecyclerView recyclerView;
    private ScheduleRecyclerViewAdapter scheduleRecyclerViewAdapter;
    private static final String KEY_DAY_OF_THE_WEEK = "DAY_OF_THE_WEEK";
    private static final String BOXES_FROM_TIME = "FROM_TIME";
    private static final String BOXES_TO_TIME = "TO_TIME";
    private static final String KEY_ADD_OR_EDIT_ACTIVITY = "ADD_OR_EDIT_ACTIVITY";
    private static final String ADD_ACTIVITY = "ADD_ACTIVITY";

    private DatabaseHandler databaseHandler;
    private ArrayList<MyActivity> activitiesList;
    private MyActivity lastActivity;
    private String bottomBoxFromTimeString, bottomBoxToTimeString;
    private String dayOfTheWeekString;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public DayOfTheWeekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dayOfTheWeekView = inflater.inflate(R.layout.fragment_day_of_the_week, container, false);

        fieldsInitialization(dayOfTheWeekView);

        bottomAmountTimeRemained.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomBoxFromTimeString = bottomBoxFromTimeTV.getText().toString().trim();
                bottomBoxToTimeString = bottomToTimeTV.getText().toString();
                Intent addMyActivityIntent = new Intent(getContext(), AddMyActivityActivity.class);
                addMyActivityIntent.putExtra(KEY_DAY_OF_THE_WEEK, dayOfTheWeekString);
                addMyActivityIntent.putExtra(BOXES_FROM_TIME, bottomBoxFromTimeString);
                addMyActivityIntent.putExtra(BOXES_TO_TIME, bottomBoxToTimeString);
                addMyActivityIntent.putExtra(KEY_ADD_OR_EDIT_ACTIVITY, ADD_ACTIVITY);
                addMyActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addMyActivityIntent);
            }
        });

        return dayOfTheWeekView;
    }

    private void fieldsInitialization(View dayOfTheWeekView) {
//        dayOfTheWeekTV = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentDayOfTheWeekTextViewId);
        bottomBoxFromTimeTV = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentBottomRelativeLayoutFromTimeTextViewId);
        bottomAmountTimeRemained = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentBottomRelativeLayoutAmountOfTimeRemainedTextViewId);
        bottomToTimeTV = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentBottomRelativeLayoutToTimeTextViewId);
        bottomRelativeLayout = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentBottomRelativeLayoutId);


        collapsingToolbarLayout = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentCollapsingToolbarLayoutId);

        databaseHandler = new DatabaseHandler(getContext());
        activitiesList = new ArrayList<>();
        recyclerView = dayOfTheWeekView.findViewById(R.id.dayOfTheWeekFragmentRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void refreshActivityRecyclerView(String dayOfTheWeek) {
        activitiesList.clear();
        activitiesList = databaseHandler.getAllActivitiesOfSpecificDay(dayOfTheWeek);
        scheduleRecyclerViewAdapter = new ScheduleRecyclerViewAdapter(getContext(), activitiesList, dayOfTheWeekString);
        recyclerView.setAdapter(scheduleRecyclerViewAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        dayOfTheWeekString = getArguments().getString(KEY_DAY_OF_THE_WEEK);
        collapsingToolbarLayout.setTitle(dayOfTheWeekString);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white, null));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white, null));
//        check if in this day we have any activity
        int totalActivitiesCount = databaseHandler.totalActivityCount(dayOfTheWeekString);
        if (totalActivitiesCount >= 1) {
            refreshActivityRecyclerView(dayOfTheWeekString);

//            to getlast entry of activity
            lastActivity = databaseHandler.getLastActivity(dayOfTheWeekString);
            String lastActivityToTime = lastActivity.getActivityToTime();
            bottomBoxFromTimeTV.setText(lastActivityToTime);
            bottomBoxFromTimeString = lastActivityToTime;

            String bottomFromTimeString = bottomBoxFromTimeTV.getText().toString().trim();
            String bottomToTimeString = bottomToTimeTV.getText().toString();
            try {
                String bottomTimeTakenBetweenTwoTime = Utils.calculateDifferenceBetweenTwoTime(bottomFromTimeString, bottomToTimeString);
                bottomAmountTimeRemained.setText(bottomTimeTakenBetweenTwoTime);
                if (lastActivity.getActivityToTime().equals("00:00")) {
                    bottomRelativeLayout.setVisibility(View.GONE);
                } else {
                    bottomRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
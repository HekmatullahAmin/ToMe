package com.hekmatullahamin.plan.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.PlannedActivity;
import com.hekmatullahamin.plan.activities.SettingsActivity;
import com.hekmatullahamin.plan.activities.ShoppingActivity;
import com.hekmatullahamin.plan.activities.TodayActivity;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class TodoFragment extends Fragment implements View.OnClickListener {

    //    CV stands for card view
    private View todoFragmentView;
    private CardView todayCV, shoppingCV, plannedCV;
    private TextView userNameTV, partsOfTheDayTV, toDoAllTasksCountTV, toDoAllTasksTV, todayTasksCountTV, todayTaskTV, shoppingItemsCountTV,
            shoppingItemsTV, plannedTasksCountTV, plannedTaskTV;
    private Toolbar todoFragmentToolbar;
    private CircleImageView profilePicture;

    private SharedPreferences myPreferences;

    private DatabaseHandler databaseHandler;

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        todoFragmentView = inflater.inflate(R.layout.fragment_todo, container, false);
//        to initialize our fields line No.34
        fieldsInitialization();

        setPartsOfTheDay();

        todayCV.setOnClickListener(this);
        shoppingCV.setOnClickListener(this);
        plannedCV.setOnClickListener(this);

        return todoFragmentView;
    }

    private void fieldsInitialization() {
        todayCV = todoFragmentView.findViewById(R.id.todoFragmentTodayCardViewId);
        shoppingCV = todoFragmentView.findViewById(R.id.todoFragmentShoppingCardViewId);
        plannedCV = todoFragmentView.findViewById(R.id.todoFragmentPlannedCardViewId);
        userNameTV = todoFragmentView.findViewById(R.id.todoFragmentHelloToUserTextViewId);
        partsOfTheDayTV = todoFragmentView.findViewById(R.id.todoFragmentPartsOfTheDayTextViewId);
        toDoAllTasksCountTV = todoFragmentView.findViewById(R.id.todoFragmentAllTasksCountTextViewId);
        toDoAllTasksTV = todoFragmentView.findViewById(R.id.todoFragmentAllTasksTextViewId);
        todayTasksCountTV = todoFragmentView.findViewById(R.id.todoFragmentTodayTasksCountTextViewId);
        todayTaskTV = todoFragmentView.findViewById(R.id.todoFragmentTodayTasksTextViewId);
        shoppingItemsCountTV = todoFragmentView.findViewById(R.id.todoFragmentShoppingTasksCountTextViewId);
        shoppingItemsTV = todoFragmentView.findViewById(R.id.todoFragmentShoppingTasksTextViewId);
        plannedTasksCountTV = todoFragmentView.findViewById(R.id.todoFragmentPlansCountTextViewId);
        plannedTaskTV = todoFragmentView.findViewById(R.id.todoFragmentPlansTextViewId);
        profilePicture = todoFragmentView.findViewById(R.id.todoFragmentProfilePictureCircleImageViewId);

        todoFragmentToolbar = todoFragmentView.findViewById(R.id.todoFragmentToolbarId);
        todoFragmentToolbar.setTitle("To Do");
        todoFragmentToolbar.inflateMenu(R.menu.custom_todo_fragment_menu);
        todoFragmentToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.customTodoFragmentMenuSettingsItemId) {
                    Intent settingsActivityIntent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(settingsActivityIntent);
                }
                return true;
            }
        });

        databaseHandler = new DatabaseHandler(getContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.todoFragmentTodayCardViewId:
//                go to Today activity
                Intent todayActivityIntent = new Intent(getContext(), TodayActivity.class);
                todayActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(todayActivityIntent);
                break;
            case R.id.todoFragmentShoppingCardViewId:
//                go to Today activity
                Intent shoppingActivityIntent = new Intent(getContext(), ShoppingActivity.class);
                shoppingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(shoppingActivityIntent);
                break;
            case R.id.todoFragmentPlannedCardViewId:
//                go to Today activity
                Intent plannedActivityIntent = new Intent(getContext(), PlannedActivity.class);
                plannedActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(plannedActivityIntent);
                break;
        }
    }

    private void populateUserProfileAndName() {
        myPreferences = getContext().getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String name = myPreferences.getString(Constants.MY_SETTING_USER_NAME, null);
        String profile = myPreferences.getString(Constants.MY_SETTING_PROFILE_PICTURE, null);

        if (name != null && profile != null) {
            userNameTV.setText("Hi " + name);
            profilePicture.setImageURI(Uri.parse(profile));
        } else if (name != null && profile == null) {
            userNameTV.setText("Hi " + name);
            profilePicture.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.default_profile_picture));
        } else {
            userNameTV.setText(getResources().getString(R.string.to_do_fragment_hello_to_user_text_view));
            profilePicture.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.default_profile_picture));
        }
    }

    private void populateTotalCountsForEachCardView() {
        ArrayList<Plan> plans = databaseHandler.getAllPlansAddedDate();
        int numberOfTodayPlans = 0;
        for (Plan plan : plans) {
            if (System.currentTimeMillis() >= plan.getPlanDate() &&
                    System.currentTimeMillis() <= plan.getPlanDate() + TimeUnit.DAYS.toMillis(1) &&
                    plan.getPlanState() != 1) {
                numberOfTodayPlans++;
            }
        }
        int numberOfTodayTasks = databaseHandler.totalTodayTasksCount();
        int numberOfShoppingTasks = databaseHandler.totalShoppingTasksCount();
        int numberOfAllTask = numberOfTodayTasks + numberOfShoppingTasks + numberOfTodayPlans;

        if (numberOfTodayTasks > 1) {
            todayTaskTV.setText(getResources().getString(R.string.to_do_fragment_today_tasks_text_view));
        } else {
            todayTaskTV.setText(getResources().getString(R.string.to_do_fragment_today_task_text_view));
        }

        if (numberOfShoppingTasks > 1) {
            shoppingItemsTV.setText(getResources().getString(R.string.to_do_fragment_shopping_items_text_view));
        } else {
            shoppingItemsTV.setText(getResources().getString(R.string.to_do_fragment_shopping_item_text_view));
        }

        if (numberOfTodayPlans > 1) {
            plannedTaskTV.setText(getResources().getString(R.string.to_do_fragment_plans_for_today_text_view));
        } else {
            plannedTaskTV.setText(getResources().getString(R.string.to_do_fragment_plan_for_today_text_view));
        }

        if (numberOfAllTask > 1) {
            toDoAllTasksTV.setText(getResources().getString(R.string.to_do_fragment_all_tasks_text_view));
        } else {
            toDoAllTasksTV.setText(getResources().getString(R.string.to_do_fragment_all_task_text_view));
        }

        todayTasksCountTV.setText(String.valueOf(numberOfTodayTasks));
        shoppingItemsCountTV.setText(String.valueOf(numberOfShoppingTasks));
        plannedTasksCountTV.setText(String.valueOf(numberOfTodayPlans));
        toDoAllTasksCountTV.setText(String.valueOf(numberOfAllTask));
    }

    private void setPartsOfTheDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
        String time = dateFormat.format(new Date(System.currentTimeMillis()).getTime());
        String partsOfDay;
        int timeInInteger = Integer.parseInt(time);
        if (timeInInteger == 1200) {
            partsOfDay = "Good Noon";
        } else if (timeInInteger > 1200 && timeInInteger <= 1700) {
            partsOfDay = "Good After Noon";
        } else if (timeInInteger > 1700 && timeInInteger <= 2359) {
            partsOfDay = "Good Evening";
        } else if (timeInInteger == 0000) {
            partsOfDay = "Midnight";
        } else {
            partsOfDay = "Good Morning";
        }

        partsOfTheDayTV.setText(partsOfDay);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUserProfileAndName();
        populateTotalCountsForEachCardView();
    }
}
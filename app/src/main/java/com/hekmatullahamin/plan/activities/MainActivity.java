package com.hekmatullahamin.plan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView notesCV, plansCV, expensesCV, scheduleCV;
    private TextView userNameTV, partsOfTheDayTV;
    private CircleImageView profilePicture;
    private Toolbar mainActivityToolbar;
    private SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//
        fieldsInitialization();

        notesCV.setOnClickListener(this);
        plansCV.setOnClickListener(this);
        expensesCV.setOnClickListener(this);
        scheduleCV.setOnClickListener(this);
    }

    private void fieldsInitialization() {
        notesCV = (CardView) findViewById(R.id.mainActivityNotesCardViewId);
        plansCV = (CardView) findViewById(R.id.mainActivityPlansCardViewId);
        expensesCV = (CardView) findViewById(R.id.mainActivityExpensesCardViewId);
        scheduleCV = (CardView) findViewById(R.id.mainActivityScheduleCardViewId);
        userNameTV = findViewById(R.id.mainActivityHelloToUserTextViewId);
        partsOfTheDayTV = findViewById(R.id.mainActivityPartsOfTheDayTextViewId);
        profilePicture = findViewById(R.id.mainActivityProfilePictureCircleImageViewId);
        mainActivityToolbar = findViewById(R.id.mainActivityToolbarId);
        mainActivityToolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        mainActivityToolbar.setTitle("ToMe");
        mainActivityToolbar.inflateMenu(R.menu.custom_main_activity_menu);

        mainActivityToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.customMainActivityMenuProfileItemId) {
                    Intent mySettingActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    mySettingActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mySettingActivityIntent);
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainActivityNotesCardViewId:
                Intent notesActivityIntent = new Intent(this, AllNotesActivity.class);
                notesActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(notesActivityIntent);
                break;
            case R.id.mainActivityPlansCardViewId:
                Intent plansActivityIntent = new Intent(this, PlansActivity.class);
                plansActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(plansActivityIntent);
                break;
            case R.id.mainActivityExpensesCardViewId:
                Intent expensesActivityIntent = new Intent(this, ExpenseActivity.class);
                expensesActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(expensesActivityIntent);
                break;
            case R.id.mainActivityScheduleCardViewId:
                Intent scheduleActivityIntent = new Intent(this, ScheduleActivity.class);
                scheduleActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(scheduleActivityIntent);
                break;
        }
    }

    private void populateUserProfileAndName() {
        myPreferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String name = myPreferences.getString(Constants.MY_SETTING_USER_NAME, null);
        String profile = myPreferences.getString(Constants.MY_SETTING_PROFILE_PICTURE, null);

        if (name != null && profile != null) {
            userNameTV.setText("Hi " + name);
            profilePicture.setImageURI(Uri.parse(profile));
        } else if (name != null && profile == null) {
            userNameTV.setText("Hi " + name);
            profilePicture.setBackground(AppCompatResources.getDrawable(this, R.drawable.user));
        } else {
            userNameTV.setText(getResources().getString(R.string.main_activity_hello_to_user_text_view));
            profilePicture.setBackground(AppCompatResources.getDrawable(this, R.drawable.user));
        }
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
    protected void onResume() {
        super.onResume();
        populateUserProfileAndName();
        setPartsOfTheDay();
    }
}
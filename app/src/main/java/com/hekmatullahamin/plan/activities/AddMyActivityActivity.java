package com.hekmatullahamin.plan.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.MyActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.text.format.DateFormat.is24HourFormat;

public class AddMyActivityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView closeActivity, saveRecord, deleteRecord;
    private TextView titleOfActionBar, dayOfTheWeekTV, startTimeTV, endTimeTV, selectedTagNameTV;
    private ImageView selectedTagImageView;
    private CardView selectedTagCardView;
    private EditText descriptionET;
    private View breakTag, travelTag, eatingTag, houseworkTag, internetTag, gameTag, relationshipTag, sleepTag, studyingTag,
            sportTag, tvTag, workTag;
    private static final String LEFT_START_SIDE = "left";
    private static final String RIGHT_END_SIDE = "right";
    private String tagName;
    private int tagColor, tagImage;

    private DatabaseHandler databaseHandler;
    private Bundle myBundle;
    private MyActivity selectedActivity;
    private static final String DAY_OF_THE_WEEK = "DAY_OF_THE_WEEK";
    private static final String BOXES_FROM_TIME = "FROM_TIME";
    private static final String BOXES_TO_TIME = "TO_TIME";
    private static final String MY_SELECTED_ACTIVITY = "SELECTED_ACTIVITY";
    private static final String KEY_ADD_OR_EDIT_ACTIVITY = "ADD_OR_EDIT_ACTIVITY";
    private static final String ADD_ACTIVITY = "ADD_ACTIVITY";
    private String dayOfTheWeekString, boxFromTimeString, boxToTimeString, addOrEditActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_activity);

        fieldsInitialization();

        populateAddMyActivityFields();

        saveRecord.setOnClickListener(this);
        startTimeTV.setOnClickListener(this);
        endTimeTV.setOnClickListener(this);

        breakTag.setOnClickListener(this);
        travelTag.setOnClickListener(this);
        eatingTag.setOnClickListener(this);
        houseworkTag.setOnClickListener(this);
        internetTag.setOnClickListener(this);
        gameTag.setOnClickListener(this);
        relationshipTag.setOnClickListener(this);
        sleepTag.setOnClickListener(this);
        studyingTag.setOnClickListener(this);
        sportTag.setOnClickListener(this);
        tvTag.setOnClickListener(this);
        workTag.setOnClickListener(this);
        closeActivity.setOnClickListener(this);
        deleteRecord.setOnClickListener(this);
    }

    private void fieldsInitialization() {
        closeActivity = findViewById(R.id.addMyActivityCloseActivityImageViewId);
        saveRecord = findViewById(R.id.addMyActivitySaveActivityImageViewId);
        deleteRecord = findViewById(R.id.addMyActivityDeleteImageViewId);
        titleOfActionBar = findViewById(R.id.addMyActivityActionBarTitleTextViewId);
        dayOfTheWeekTV = findViewById(R.id.addMyActivityDayOfTheWeekTextViewId);
        startTimeTV = findViewById(R.id.addMyActivityStartTimeTextInputLayoutId);
        endTimeTV = findViewById(R.id.addMyActivityEndTimeTextInputLayoutId);
        selectedTagNameTV = findViewById(R.id.addMyActivitySelectedTagTextViewId);
        selectedTagImageView = findViewById(R.id.addMyActivitySelectedTagImageViewId);
        selectedTagCardView = findViewById(R.id.addMyActivitySelectedTagCardViewId);
        descriptionET = findViewById(R.id.addMyActivityDescriptionEditTextId);

//        Tags
        breakTag = findViewById(R.id.addMyActivityTagBreakId);
        travelTag = findViewById(R.id.addMyActivityTagTravelId);
        eatingTag = findViewById(R.id.addMyActivityTagEatingId);
        houseworkTag = findViewById(R.id.addMyActivityTagHouseworkId);
        internetTag = findViewById(R.id.addMyActivityTagInternetId);
        gameTag = findViewById(R.id.addMyActivityTagGameId);
        relationshipTag = findViewById(R.id.addMyActivityTagRelationshipId);
        sleepTag = findViewById(R.id.addMyActivityTagSleepId);
        studyingTag = findViewById(R.id.addMyActivityTagStudyingId);
        sportTag = findViewById(R.id.addMyActivityTagSportId);
        tvTag = findViewById(R.id.addMyActivityTagTVId);
        workTag = findViewById(R.id.addMyActivityTagWorkId);

        databaseHandler = new DatabaseHandler(this);
        myBundle = getIntent().getExtras();
        selectedActivity = (MyActivity) getIntent().getSerializableExtra(MY_SELECTED_ACTIVITY);
        dayOfTheWeekString = myBundle.getString(DAY_OF_THE_WEEK);
        boxFromTimeString = myBundle.getString(BOXES_FROM_TIME);
        boxToTimeString = myBundle.getString(BOXES_TO_TIME);
        addOrEditActivity = myBundle.getString(KEY_ADD_OR_EDIT_ACTIVITY);
    }

    private void populateAddMyActivityFields() {
        dayOfTheWeekTV.setText(dayOfTheWeekString);

        startTimeTV.setText(boxFromTimeString);
        endTimeTV.setText(boxToTimeString);

        if (selectedActivity != null) {
//            if we want to update this will not be null
            titleOfActionBar.setText("Edit record");
            selectedTagNameTV.setVisibility(View.VISIBLE);
            deleteRecord.setVisibility(View.VISIBLE);
            String description = selectedActivity.getActivityDescription();
            String startTimeString = selectedActivity.getActivityFromTime();
            String endTimeString = selectedActivity.getActivityToTime();
            //            if user did not changed the tag we update own values
            tagName = selectedActivity.getActivityTagName();
            tagColor = selectedActivity.getActivityTagColor();
            tagImage = selectedActivity.getActivityTagImage();

            if (description != null || !description.isEmpty()) {
                descriptionET.setText(description);
            }
            selectedTagNameTV.setText(tagName);
            startTimeTV.setText(startTimeString);
            endTimeTV.setText(endTimeString);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMyActivitySaveActivityImageViewId:
                saveActivityToDatabase();
                break;
            case R.id.addMyActivityCloseActivityImageViewId:
                finish();
                break;
            case R.id.addMyActivityDeleteImageViewId:
                openAlertDialog();
                break;
            case R.id.addMyActivityStartTimeTextInputLayoutId:
                openTimePickerDialog(LEFT_START_SIDE);
                break;
            case R.id.addMyActivityEndTimeTextInputLayoutId:
                openTimePickerDialog(RIGHT_END_SIDE);
                break;
            case R.id.addMyActivityTagBreakId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Break");
                tagColor = getResources().getColor(R.color.brown, null);
                tagImage = R.drawable.ic_baseline_free_break_24;
                break;
            case R.id.addMyActivityTagTravelId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Travel");
                tagColor = getResources().getColor(R.color.orange_400, null);
                tagImage = R.drawable.ic_baseline_travel_24;
                break;
            case R.id.addMyActivityTagEatingId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Eating");
                tagColor = getResources().getColor(R.color.amber_900, null);
                tagImage = R.drawable.ic_baseline_eating_24;
                break;
            case R.id.addMyActivityTagHouseworkId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Housework");
                tagColor = getResources().getColor(R.color.yellow_600, null);
                tagImage = R.drawable.ic_baseline_house_work_24;
                break;
            case R.id.addMyActivityTagInternetId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Internet");
                tagColor = getResources().getColor(R.color.yellow_900, null);
                tagImage = R.drawable.ic_baseline_internet_mac_24;
                break;
            case R.id.addMyActivityTagGameId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Game");
                tagColor = getResources().getColor(R.color.gray_500, null);
                tagImage = R.drawable.ic_baseline_game_24;
                break;
            case R.id.addMyActivityTagRelationshipId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Relationship");
                tagColor = getResources().getColor(R.color.red, null);
                tagImage = R.drawable.ic_baseline_relationship_24;
                break;
            case R.id.addMyActivityTagSleepId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Sleep");
                tagColor = getResources().getColor(R.color.blue_600, null);
                tagImage = R.drawable.ic_baseline_sleep_24;
                break;
            case R.id.addMyActivityTagStudyingId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Studying");
                tagColor = getResources().getColor(R.color.teal_300, null);
                tagImage = R.drawable.ic_baseline_studying_24;
                break;
            case R.id.addMyActivityTagSportId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Sport");
                tagColor = getResources().getColor(R.color.green, null);
                tagImage = R.drawable.ic_baseline_sport_24;
                break;
            case R.id.addMyActivityTagTVId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("TV");
                tagColor = getResources().getColor(R.color.pink, null);
                tagImage = R.drawable.ic_baseline_tv_24;
                break;
            case R.id.addMyActivityTagWorkId:
                selectedTagCardView.setVisibility(View.VISIBLE);
                selectedTagNameTV.setText("Work");
                tagColor = getResources().getColor(R.color.purple, null);
                tagImage = R.drawable.ic_baseline_work_24;
                break;
        }

        selectedTagImageView.setImageResource(tagImage);
        selectedTagCardView.setCardBackgroundColor(tagColor);
    }

    private void openTimePickerDialog(String leftOrRightSide) {
        boolean isSystem24Hour = is24HourFormat(this);
        int timeFormat = (isSystem24Hour == true) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H;
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setHour(0)
                .setMinute(0)
                .build();
        picker.show(getSupportFragmentManager(), "TAG");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.format("%02d", picker.getHour()) + ":" + String.format("%02d", picker.getMinute());
                if (leftOrRightSide.equals(LEFT_START_SIDE)) {
                    startTimeTV.setText(time);
                } else {
                    endTimeTV.setText(time);
                }
            }
        });
    }

    private void saveActivityToDatabase() {
        String description = descriptionET.getText().toString().trim();
        String startTime = startTimeTV.getText().toString().trim();
        String endTime = endTimeTV.getText().toString().trim();
        String tagNameString = selectedTagNameTV.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date fromDate = null, toDate = null;
        try {
            fromDate = dateFormat.parse(startTime);
            toDate = dateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long fromTimeLong = fromDate.getTime();
        long toTimeLong = toDate.getTime();

        MyActivity activity = new MyActivity();
        activity.setActivityDescription(description);
        activity.setActivityFromTime(startTime);
        activity.setActivityFromTimeLong(fromTimeLong);
        activity.setActivityTagName(tagNameString);
        activity.setActivityTagColor(tagColor);
        activity.setActivityTagImage(tagImage);
        activity.setActivityDayOfTheWeek(dayOfTheWeekString);

//        if user start time is bigger than end time (when it passes 00:00 and start another day) end the end time to 00:00
        if (fromTimeLong >= toTimeLong) {
            activity.setActivityToTime("00:00");
            if (selectedActivity != null) {
                selectedActivity.setActivityToTime("00:00");
            }
        } else {
            activity.setActivityToTime(endTime);
            if (selectedActivity != null) {
                selectedActivity.setActivityToTime(endTime);
            }
        }


        if (!tagNameString.isEmpty()) {
            if (addOrEditActivity.equals(ADD_ACTIVITY)) {
                databaseHandler.addActivity(activity);
            } else {

                selectedActivity.setActivityDescription(description);
                selectedActivity.setActivityFromTime(startTime);
                selectedActivity.setActivityFromTimeLong(fromTimeLong);
                selectedActivity.setActivityTagName(tagNameString);
                selectedActivity.setActivityTagColor(tagColor);
                selectedActivity.setActivityTagImage(tagImage);

                databaseHandler.updateActivity(selectedActivity);
            }
            finish();
        } else {
            Toast.makeText(this, "Tag can not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void openAlertDialog() {
        AlertDialog.Builder alertDialogBuilder;
        Dialog dialog;
        alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
        Button deleteButton, cancelButton;

//            inflating delete alert dialog
        View deleteAlertDialog = LayoutInflater.from(this).inflate(R.layout.custom_delete_or_archive_dialog, null);
        deleteButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
        cancelButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

        alertDialogBuilder.setView(deleteAlertDialog);
        dialog = alertDialogBuilder.create();
        dialog.show();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteActivity(selectedActivity.getActivityId());
                dialog.dismiss();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
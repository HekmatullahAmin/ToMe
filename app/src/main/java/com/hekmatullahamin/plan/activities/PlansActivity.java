package com.hekmatullahamin.plan.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.PlanRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.AlarmBroadcast;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.text.format.DateFormat.is24HourFormat;

public class PlansActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private FloatingActionButton addEventFAB;
    private CardView noEventCardView;
    private TextView noEventDateTV;
    private BottomSheetDialog bottomSheetDialog;
    private DatabaseHandler databaseHandler;
    private RecyclerView plannedRecyclerView;
    private ArrayList<Plan> plans;
    private String selectedDateString = "";
    private String todayDateString = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        fieldsInitialization();

        createNotificationChannel();

        populateRecyclerView();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                selectedDateString = String.format("%02d", dayOfMonth) + "/" + String.format("%02d", (month + 1)) + "/" + year;
                selectedDateString = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
                refreshRecyclerView(selectedDateString);
            }
        });

        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEventBottomSheetDialog();
            }
        });
    }

    private void fieldsInitialization() {
        calendarView = findViewById(R.id.plansActivityCalendarViewId);
        addEventFAB = findViewById(R.id.plansActivityAddEventFloatingActionButtonId);
        noEventCardView = findViewById(R.id.plansActivityNoEventCardViewId);
        noEventDateTV = findViewById(R.id.plansActivityNoEventDateTextViewId);
        toolbar = findViewById(R.id.plansActivityToolbarId);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Plans");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white_24);

        plannedRecyclerView = findViewById(R.id.plansActivityRecyclerViewId);
        plannedRecyclerView.setHasFixedSize(true);
        plannedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DatabaseHandler(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateRecyclerView() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        todayDateString = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
        refreshRecyclerView(todayDateString);
    }

    private void openAddEventBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(this).inflate(R.layout.custom_add_event_bottom_sheet_dialog, null);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        EditText typeEventET = customBottomSheetDialog.findViewById(R.id.customAddEventBottomSheetDialogTypeEventEditTextId);
        TextView pickDateTV = customBottomSheetDialog.findViewById(R.id.customAddEventBottomSheetDialogPickDateTextViewId);
        TextView pickTimeTV = customBottomSheetDialog.findViewById(R.id.customAddEventBottomSheetDialogPickTimeTextViewId);
//        EditText typeDateET = customBottomSheetDialog.findViewById(R.id.customAddPlanBottomSheetDialogTypeDateEditTextId);
        Button addEventBtn = customBottomSheetDialog.findViewById(R.id.customAddEventBottomSheetDialogAddEventButtonId);
        final long[] date = new long[1];
        final int[] hour = new int[1];
        final int[] min = new int[1];

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker datePicker = builder.build();
        pickDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(selection.toString()));
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        date[0] = calendar.getTimeInMillis();
                        pickDateTV.setText(Utils.formatDate(date[0]));
                    }
                });

                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        boolean isSystem24 = is24HourFormat(PlansActivity.this);
        int timeFormat = (isSystem24 == true) ? TimeFormat.CLOCK_24H : TimeFormat.CLOCK_12H;

        pickTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setHour(0)
                        .setMinute(0)
                        .setTimeFormat(timeFormat)
                        .build();
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hour[0] = picker.getHour();
                        min[0] = picker.getMinute();
                        String time = String.format("%02d", hour[0]) + ":" + String.format("%02d", min[0]);
                        pickTimeTV.setText(time);
                    }
                });
                picker.show(getSupportFragmentManager(), "TIME_PICKER");
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = typeEventET.getText().toString().trim();
                String eventDate = pickDateTV.getText().toString().trim();
                String eventTime = pickTimeTV.getText().toString().trim();

                if (!TextUtils.isEmpty(eventName) && !TextUtils.isEmpty(eventDate) && !TextUtils.isEmpty(eventTime)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(date[0]);
                    calendar.set(Calendar.HOUR_OF_DAY, hour[0]);
                    calendar.set(Calendar.MINUTE, min[0]);
                    addEventToDatabase(eventName, date[0], calendar.getTimeInMillis());
                } else {
                    Toast.makeText(PlansActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addEventToDatabase(String eventName, long eventDate, long eventDateAndTime) {
        Plan plan = new Plan();

        long currentTimeForNotificationID = System.currentTimeMillis();
        plan.setPlanNote(eventName);
        plan.setPlanDate(eventDate);
        plan.setPlanDateAndTime(eventDateAndTime);
        plan.setPlanNotificationId(currentTimeForNotificationID);
        databaseHandler.addPlan(plan);
        if (!selectedDateString.isEmpty()) {
            refreshRecyclerView(selectedDateString);
        } else {
            refreshRecyclerView(todayDateString);
        }

//        Utils.hideSoftKeyboard(this);

//        for notifying person by notification when date come
        setAlarmForPlan(plan.getPlanDateAndTime(), plan.getPlanNote(), plan.getPlanNotificationId());

        bottomSheetDialog.dismiss();
    }

    private long formatDate(String dateInString) {
        long dateToMillis = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = formatter.parse(dateInString);
            dateToMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToMillis;
    }

    private void refreshRecyclerView(String date) {
        long selectedDateToMillis = formatDate(date);
        plans = databaseHandler.getPlansForSpecificDate(selectedDateToMillis);
        PlanRecyclerViewAdapter planRecyclerViewAdapter = new PlanRecyclerViewAdapter(PlansActivity.this, plans);
        plannedRecyclerView.setAdapter(planRecyclerViewAdapter);
        planRecyclerViewAdapter.notifyDataSetChanged();
        if (plans.size() > 0) {
            noEventCardView.setVisibility(View.GONE);
        } else {
            noEventCardView.setVisibility(View.VISIBLE);
            if (!selectedDateString.isEmpty()) {
                noEventDateTV.setText(selectedDateString);
            } else {
                noEventDateTV.setText(todayDateString);
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void setAlarmForPlan(long notificationDate, String notificationMessage, long notificationId) {
        int requestCode = (int) notificationId;
        Intent broadcastIntent = new Intent(PlansActivity.this, AlarmBroadcast.class);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, notificationId);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PlansActivity.this, requestCode, broadcastIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationDate, AlarmManager.RTC_WAKEUP, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate, pendingIntent);
    }
}
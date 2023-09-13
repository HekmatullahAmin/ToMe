package com.hekmatullahamin.plan.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.PlanRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.AlarmBroadcast;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class PlannedActivity extends AppCompatActivity {

    private EditText typePlanET;
    private TextView pickDateTV;
    private Button addPlanButton;
    private RecyclerView plannedRecyclerView;
    private PlanRecyclerViewAdapter planRecyclerViewAdapter;
    private Toolbar plannedActivityToolbar;

    private AlertDialog.Builder alertDialogBuilder;
    private Dialog dialog;
    private ItemTouchHelper itemTouchHelper;

    private DatabaseHandler databaseHandler;
    private List<Plan> plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned);

        fieldsInitialization();

        createNotificationChannel();

        pickDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        addPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String planNote = typePlanET.getText().toString().trim();
                String planDate = pickDateTV.getText().toString().trim();
                if (!TextUtils.isEmpty(planNote) && !TextUtils.isEmpty(planDate)
                        && !planDate.equals(getResources().getString(R.string.planned_activity_pick_date_text_view))) {
                    addPlanToDatabase(planNote, planDate);
                } else {
                    Toast.makeText(PlannedActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fieldsInitialization() {
        typePlanET = findViewById(R.id.plannedActivityAddPlanHereEditTextId);
        pickDateTV = findViewById(R.id.plannedActivityPickDateTextViewId);
        addPlanButton = findViewById(R.id.plannedActivityAddPlanButtonId);
        plannedRecyclerView = findViewById(R.id.plannedActivityRecyclerViewId);
        plannedActivityToolbar = findViewById(R.id.plannedActivityToolbarId);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(plannedRecyclerView);

        setSupportActionBar(plannedActivityToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_divider_item_decoration));
        plannedRecyclerView.addItemDecoration(dividerItemDecoration);
        plannedRecyclerView.setHasFixedSize(true);
        plannedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHandler = new DatabaseHandler(PlannedActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(PlannedActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "/" + String.format("%02d", (month + 1)) + "/" + String.format("%02d", dayOfMonth);
                pickDateTV.setText(date);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void addPlanToDatabase(String planNote, String planDate) {
        Plan plan = new Plan();
        long dateToMillis = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date = formatter.parse(planDate);
            dateToMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long currentTimeForNotificationID = System.currentTimeMillis();
        plan.setPlanNote(planNote);
        plan.setPlanDate(dateToMillis);
        plan.setPlanNotificationId(currentTimeForNotificationID);
        databaseHandler.addPlan(plan);
        refreshRecyclerView();

        typePlanET.setText("");
        pickDateTV.setText("");
        Utils.hideSoftKeyboard(this);

//        for notifying person by notification when date come
//        and checking if selected date is bigger than right now date
        if (dateToMillis >= currentTimeForNotificationID) {
            setAlarmForPlan(plan.getPlanDate(), plan.getPlanNote(), plan.getPlanNotificationId());
        }
    }

    private void refreshRecyclerView() {
        plans = databaseHandler.getAllPlans();
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(PlannedActivity.this, plans);
        plannedRecyclerView.setAdapter(planRecyclerViewAdapter);

        typePlanET.setHint(getResources().getString(R.string.planned_activity_type_plan_here_edit_text));
        pickDateTV.setText(getResources().getString(R.string.planned_activity_pick_date_text_view));
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Plan plan = plans.get(position);

            alertDialogBuilder = new MaterialAlertDialogBuilder(PlannedActivity.this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
            Button deleteButton, cancelButton;

//            inflating delete alert dialog
            View deleteAlertDialog = LayoutInflater.from(PlannedActivity.this).inflate(R.layout.custom_delete_or_archive_dialog, null);
            deleteButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
            cancelButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

            alertDialogBuilder.setView(deleteAlertDialog);
            alertDialogBuilder.setCancelable(false);
            dialog = alertDialogBuilder.create();
            dialog.show();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseHandler.deletePlan(plan.getPlanId());
                    dialog.dismiss();
                    refreshRecyclerView();
//                    after deleting plan do not notify user
                    stopAlarm(plan.getPlanNotificationId());
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    refreshRecyclerView();
                }
            });
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(PlannedActivity.this, R.color.red_2))
                    .addActionIcon(R.drawable.ic_baseline_delete_30)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void setAlarmForPlan(long notificationDate, String notificationMessage, long notificationId) {
        int requestCode = (int) notificationId;
        Intent broadcastIntent = new Intent(PlannedActivity.this, AlarmBroadcast.class);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, notificationId);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PlannedActivity.this, requestCode, broadcastIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationDate, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void stopAlarm(long notificationId) {
        int requestCode = (int) notificationId;
        Intent broadcastIntent = new Intent(PlannedActivity.this, AlarmBroadcast.class);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(PlannedActivity.this, requestCode, broadcastIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
    }
}
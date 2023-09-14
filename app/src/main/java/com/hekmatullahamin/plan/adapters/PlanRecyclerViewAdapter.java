package com.hekmatullahamin.plan.adapters;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.AlarmBroadcast;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

public class PlanRecyclerViewAdapter extends RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Plan> planList;
    private ViewBinderHelper viewBinderHelper;
    private DatabaseHandler databaseHandler;

    public PlanRecyclerViewAdapter(Context context, List<Plan> planList) {
        this.context = context;
        this.planList = planList;
        viewBinderHelper = new ViewBinderHelper();
        databaseHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customPlanRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_plan_row, parent, false);
        return new ViewHolder(customPlanRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(plan.getPlanId()));
        viewBinderHelper.closeLayout(String.valueOf(plan.getPlanId()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(plan.getPlanDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
        Boolean isChecked = isChecked(plan.getPlanState());

        holder.planDate.setText(date);
        holder.checkBox.setChecked(isChecked);
//        put strike through on plan note if it is checked or date is passed
        if (isChecked || (System.currentTimeMillis() > plan.getPlanDate() + TimeUnit.DAYS.toMillis(1))) {
            if (System.currentTimeMillis() > plan.getPlanDate() + TimeUnit.DAYS.toMillis(1)) {
//                make checkbox disable if only time is passed
                holder.checkBox.setEnabled(false);
            }
            holder.planNote.setPaintFlags(holder.planNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.planNote.setText(plan.getPlanNote());
    }

    // for returning true if our status is 1 and false if status is 0
    public boolean isChecked(int oneOrZero) {
        return oneOrZero > 0;
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView planDate, planNote;
        private ImageView deleteEvent;
        private SwipeRevealLayout swipeRevealLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.customPlanRowCheckBoxId);
            planDate = itemView.findViewById(R.id.customPlanRowPlanDateTextViewId);
            planNote = itemView.findViewById(R.id.customPlanRowPlanNoteTextViewId);
            deleteEvent = itemView.findViewById(R.id.customPlanRowDeleteEventImageViewId);
            swipeRevealLayout = itemView.findViewById(R.id.customPlanRowSwipeRevealLayoutId);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Plan plan = planList.get(getAdapterPosition());
                    if (checkBox.isChecked()) {
                        plan.setPlanState(1);
                        planNote.setPaintFlags(planNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        plan.setPlanState(0);
                        planNote.setPaintFlags(planNote.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    databaseHandler.updatePlan(plan);

                }
            });

            deleteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Plan plan = planList.get(position);

                    Dialog dialog;
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
                    Button deleteButton, cancelButton;

//            inflating delete alert dialog
                    View deleteAlertDialog = LayoutInflater.from(context).inflate(R.layout.custom_delete_or_archive_dialog, null);
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
                            planList.remove(position);
                            notifyItemRemoved(position);
                            viewBinderHelper.closeLayout(String.valueOf(plan.getPlanId()));
                            dialog.dismiss();
//                    after deleting plan do not notify user
                            stopAlarm(plan.getPlanNotificationId());
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewBinderHelper.closeLayout(String.valueOf(plan.getPlanId()));
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }

    private void stopAlarm(long notificationId) {
        int requestCode = (int) notificationId;
        Intent broadcastIntent = new Intent(context, AlarmBroadcast.class);
        broadcastIntent.putExtra(Constants.NOTIFICATION_MANAGER_COMPAT_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, broadcastIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

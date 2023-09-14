package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.AddMyActivityActivity;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;

public class ScheduleRecyclerViewAdapter extends RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MyActivity> myActivities;
    private Context context;
    private static final String KEY_DAY_OF_THE_WEEK = "DAY_OF_THE_WEEK";
    private static final String BOXES_FROM_TIME = "FROM_TIME";
    private static final String BOXES_TO_TIME = "TO_TIME";
    private static final String MY_SELECTED_ACTIVITY = "SELECTED_ACTIVITY";
    private static final String KEY_ADD_OR_EDIT_ACTIVITY = "ADD_OR_EDIT_ACTIVITY";
    private static final String ADD_ACTIVITY = "ADD_ACTIVITY";
    private static final String EDIT_ACTIVITY = "EDIT_ACTIVITY";
    private String dayOfTheWeek;

    public ScheduleRecyclerViewAdapter(Context context, ArrayList<MyActivity> myActivities, String dayOfTheWeek) {
        this.context = context;
        this.myActivities = myActivities;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customTimeTableRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_schedule_row, parent, false);
        return new ViewHolder(customTimeTableRow);
    }

    //    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyActivity previousActivity = new MyActivity();
        MyActivity nextActivity = myActivities.get(position);
        MyActivity notActivity = new MyActivity();
        notActivity.setActivityFromTime("00:00");
        notActivity.setActivityToTime("00:00");

        if (position > 0) {
            previousActivity = myActivities.get(position - 1);
        } else {
            previousActivity = notActivity;
        }

//        for activity which not populated
        if (!previousActivity.getActivityToTime().equals(nextActivity.getActivityFromTime())) {
            holder.topBoxView.setVisibility(View.VISIBLE);
            holder.topBoxFromTV.setText(previousActivity.getActivityToTime());
            holder.topBoxToTV.setText(nextActivity.getActivityFromTime());

            String fromTime = holder.topBoxFromTV.getText().toString().trim();
            String toTime = holder.topBoxToTV.getText().toString().trim();
            String timeDifference = null;
            try {
                timeDifference = Utils.calculateDifferenceBetweenTwoTime(fromTime, toTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.topBoxTimeDifferenceTV.setText(timeDifference);
        } else {
            holder.topBoxView.setVisibility(View.GONE);
        }

//        for real activity
        String timeTaken = null;
        try {
            timeTaken = Utils.calculateDifferenceBetweenTwoTime(nextActivity.getActivityFromTime(), nextActivity.getActivityToTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.fromTimeTV.setText(nextActivity.getActivityFromTime());
        holder.toTimeTV.setText(nextActivity.getActivityToTime());
        holder.tagCardView.setCardBackgroundColor(nextActivity.getActivityTagColor());
        holder.tagNameTV.setText(nextActivity.getActivityTagName());
        holder.tagImageView.setImageResource(nextActivity.getActivityTagImage());
        if (!nextActivity.getActivityDescription().isEmpty()) {
            holder.activityDescriptionTV.setVisibility(View.VISIBLE);
            holder.activityDescriptionTV.setText(nextActivity.getActivityDescription());
        } else {
            holder.activityDescriptionTV.setVisibility(View.GONE);
        }
        if (timeTaken != null) {
            holder.timeTakenTV.setText(timeTaken);
        }
    }

    @Override
    public int getItemCount() {
        return myActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fromTimeTV, toTimeTV, tagNameTV, activityDescriptionTV, timeTakenTV;
        private ImageView tagImageView;
        private CardView tagCardView;
        private View topBoxView;
        private TextView topBoxFromTV, topBoxTimeDifferenceTV, topBoxToTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fromTimeTV = itemView.findViewById(R.id.customScheduleRowFromTimeTextViewId);
            toTimeTV = itemView.findViewById(R.id.customScheduleRowToTimeTextViewId);
            tagNameTV = itemView.findViewById(R.id.customScheduleRowTagNameTextViewId);
            activityDescriptionTV = itemView.findViewById(R.id.customScheduleRowDescriptionTextViewId);
            timeTakenTV = itemView.findViewById(R.id.customScheduleRowTimeTakenTextViewId);
            tagImageView = itemView.findViewById(R.id.customScheduleRowTagImageViewId);
            tagCardView = itemView.findViewById(R.id.customScheduleRowTagCardViewId);
            topBoxView = itemView.findViewById(R.id.customScheduleRowTopRelativeLayoutId);
            topBoxFromTV = itemView.findViewById(R.id.customScheduleRowTopRelativeLayoutFromTimeTextViewId);
            topBoxTimeDifferenceTV = itemView.findViewById(R.id.customScheduleRowTopRelativeLayoutDifferenceOfFromToToTimeTextViewId);
            topBoxToTV = itemView.findViewById(R.id.customScheduleRowTopRelativeLayoutToTimeTextViewId);

            topBoxTimeDifferenceTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addMyActivityIntent = new Intent(context, AddMyActivityActivity.class);
                    addMyActivityIntent.putExtra(KEY_DAY_OF_THE_WEEK, dayOfTheWeek);
                    addMyActivityIntent.putExtra(BOXES_FROM_TIME, topBoxFromTV.getText().toString().trim());
                    addMyActivityIntent.putExtra(BOXES_TO_TIME, topBoxToTV.getText().toString().trim());
                    addMyActivityIntent.putExtra(KEY_ADD_OR_EDIT_ACTIVITY, ADD_ACTIVITY);
                    addMyActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(addMyActivityIntent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActivity selectedActivity = myActivities.get(getAdapterPosition());
                    Intent addMyActivityIntent = new Intent(context, AddMyActivityActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putSerializable(MY_SELECTED_ACTIVITY, selectedActivity);
                    addMyActivityIntent.putExtra(KEY_DAY_OF_THE_WEEK, dayOfTheWeek);
                    addMyActivityIntent.putExtra(KEY_ADD_OR_EDIT_ACTIVITY, EDIT_ACTIVITY);
                    addMyActivityIntent.putExtras(myBundle);
                    addMyActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(addMyActivityIntent);
                }
            });
        }
    }
}

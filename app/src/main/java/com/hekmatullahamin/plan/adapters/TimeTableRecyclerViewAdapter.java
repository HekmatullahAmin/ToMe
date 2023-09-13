package com.hekmatullahamin.plan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.model.MyActivity;

import java.util.ArrayList;

public class TimeTableRecyclerViewAdapter extends RecyclerView.Adapter<TimeTableRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MyActivity> myActivities;

    public TimeTableRecyclerViewAdapter(ArrayList<MyActivity> myActivities) {
        this.myActivities = myActivities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customTimeTableRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_time_table_row, parent, false);
        return new ViewHolder(customTimeTableRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyActivity myActivity = myActivities.get(position);
        holder.activityName.setText(myActivity.getActivityName());
        holder.activityTime.setText(myActivity.getActivityFromTime() + " - " + myActivity.getActivityToTime());
    }

    @Override
    public int getItemCount() {
        return myActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView activityName, activityTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            activityName = itemView.findViewById(R.id.customTimeTableRowMyActivityNameTextViewId);
            activityTime = itemView.findViewById(R.id.customTimeTableRowMyActivityTimeTextViewId);
        }
    }
}

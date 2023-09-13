package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Task;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;
    private String activityName;
    private TextView totalTaskCountTV;

    private DatabaseHandler databaseHandler;

    public TaskRecyclerViewAdapter(Context context, List<Task> taskList, String activityName, TextView totalTaskCountTV) {
        this.context = context;
        this.taskList = taskList;
        this.activityName = activityName;
        this.totalTaskCountTV = totalTaskCountTV;
        databaseHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customTaskRow = LayoutInflater.from(context).inflate(R.layout.custom_task_row, parent, false);
        return new ViewHolder(customTaskRow);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewAdapter.ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskNote.setText(task.getTaskNote());
        holder.checkBox.setChecked(isChecked(task.getTaskState()));
    }

    // for returning true if our status is 1 or false if status is 0
    public boolean isChecked(int oneOrZero) {
        return oneOrZero > 0;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView taskNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.customTaskRowCheckBoxId);
            taskNote = itemView.findViewById(R.id.customTaskRowTaskNoteId);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        Task task = taskList.get(getAdapterPosition());
                        task.setTaskState(1);
                        if (activityName.equals(Constants.TODAY_ACTIVITY_NAME)) {
                            databaseHandler.updateTodayTask(task);
                        } else if (activityName.equals(Constants.SHOPPING_ACTIVITY_NAME)) {
                            databaseHandler.updateShoppingTask(task);
                        }
//                        for populating total tasks count after a task is checked
                        populateTotalTasksTextView();
                    } else {
                        Task task = taskList.get(getAdapterPosition());
                        task.setTaskState(0);
                        if (activityName.equals(Constants.TODAY_ACTIVITY_NAME)) {
                            databaseHandler.updateTodayTask(task);
                        } else if (activityName.equals(Constants.SHOPPING_ACTIVITY_NAME)) {
                            databaseHandler.updateShoppingTask(task);
                        }
                        populateTotalTasksTextView();
                    }
                }
            });

        }
    }

    private void populateTotalTasksTextView() {
        int numberOfTasks;
        if (activityName.equals(Constants.TODAY_ACTIVITY_NAME)) {
            numberOfTasks = databaseHandler.totalTodayTasksCount();
        } else {
            numberOfTasks = databaseHandler.totalShoppingTasksCount();
        }
        totalTaskCountTV.setText(numberOfTasks + " tasks");
    }
}

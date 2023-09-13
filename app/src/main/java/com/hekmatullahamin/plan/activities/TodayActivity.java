package com.hekmatullahamin.plan.activities;

import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.TaskRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Task;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TodayActivity extends AppCompatActivity {

    //    TV stands for text view
    private TextView todayTotalTasksTV;
    private FloatingActionButton addTaskFAB;
    private RecyclerView todayRecyclerView;
    private TaskRecyclerViewAdapter recyclerViewAdapter;
    private Toolbar todayActivityToolbar;

    private ItemTouchHelper itemTouchHelper;
    private BottomSheetDialog bottomSheetDialog;

    private DatabaseHandler databaseHandler;
    private ArrayList<Task> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

//        line No.72
        fieldsInitialization();

//        line No.98
        populateTotalTasksTextView();

//        adding new task
        addTaskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTaskBottomSheetDialog();
            }
        });
    }

    private void fieldsInitialization() {
        todayTotalTasksTV = findViewById(R.id.todayActivityTasksCountTextViewId);
        todayRecyclerView = findViewById(R.id.todayActivityRecyclerViewId);
        addTaskFAB = findViewById(R.id.todayActivityAddTaskFABId);
        todayActivityToolbar = findViewById(R.id.todayActivityToolbarId);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(todayRecyclerView);

        setSupportActionBar(todayActivityToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_divider_item_decoration));
        todayRecyclerView.addItemDecoration(dividerItemDecoration);
        todayRecyclerView.setHasFixedSize(true);
        todayRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHandler = new DatabaseHandler(this);
        allTasks = new ArrayList<>();
    }

    // fro going back to to-do fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void refreshRecyclerView() {
        allTasks = databaseHandler.getAllTodayTasks();
        recyclerViewAdapter = new TaskRecyclerViewAdapter(this, allTasks, Constants.TODAY_ACTIVITY_NAME, todayTotalTasksTV);
        todayRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void populateTotalTasksTextView() {
        int numberOfTasks = databaseHandler.totalTodayTasksCount();
        todayTotalTasksTV.setText(numberOfTasks + " tasks");
    }

    private void openAddTaskBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(TodayActivity.this).inflate(R.layout.custom_add_task_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(TodayActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        Button addTaskButton = (Button) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogAddButtonId);
        EditText typedTaskET = (EditText) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogTypeEditTextId);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wroteTask = typedTaskET.getText().toString().trim();
                if (!TextUtils.isEmpty(wroteTask)) {
//                    Line No. 128
                    addTaskToDatabase(wroteTask);
                } else {
                    Toast.makeText(TodayActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addTaskToDatabase(String taskNote) {
        Task task = new Task();
        task.setTaskNote(taskNote);
        databaseHandler.addTodayTask(task);
        refreshRecyclerView();
        int lastItemAddedPosition = allTasks.size() - 1;
        todayRecyclerView.smoothScrollToPosition(lastItemAddedPosition);
        bottomSheetDialog.dismiss();
        // to change the text of Total task
        populateTotalTasksTextView();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int taskPosition = viewHolder.getAdapterPosition();
            Task deletedTask = allTasks.get(taskPosition);
            databaseHandler.deleteTodayTask(deletedTask.getTaskId());
            allTasks.remove(taskPosition);
            recyclerViewAdapter.notifyItemRemoved(taskPosition);
            populateTotalTasksTextView();

            Snackbar.make(todayRecyclerView, "task deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseHandler.addTodayTask(deletedTask);
                            allTasks.add(taskPosition, deletedTask);
                            recyclerViewAdapter.notifyItemInserted(taskPosition);
                            populateTotalTasksTextView();
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(TodayActivity.this, R.color.red_2))
                    .addActionIcon(R.drawable.ic_baseline_delete_30)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
    }

}
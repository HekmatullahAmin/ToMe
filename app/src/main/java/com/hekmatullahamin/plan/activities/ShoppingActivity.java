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

public class ShoppingActivity extends AppCompatActivity {

    //    TV stands for text view
    private TextView shoppingTotalTasksTV;
    private FloatingActionButton addItemFAB;
    private RecyclerView shoppingRecyclerView;
    private TaskRecyclerViewAdapter recyclerViewAdapter;
    private Toolbar shoppingActivityToolbar;

    private ItemTouchHelper itemTouchHelper;
    private BottomSheetDialog bottomSheetDialog;

    private DatabaseHandler databaseHandler;
    private ArrayList<Task> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        //        line No.72
        fieldsInitialization();

//        Line No. 97
        populateTotalTasksTextView();

//        adding new task
        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                line No.102
                openAddItemBottomSheetDialog();
            }
        });
    }

    private void fieldsInitialization() {
        shoppingTotalTasksTV = findViewById(R.id.shoppingActivityTasksCountTextViewId);
        shoppingRecyclerView = findViewById(R.id.shoppingActivityRecyclerViewId);
        addItemFAB = findViewById(R.id.shoppingActivityAddItemFABId);
        databaseHandler = new DatabaseHandler(this);
        shoppingActivityToolbar = findViewById(R.id.shoppingActivityToolbarId);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(shoppingRecyclerView);

        setSupportActionBar(shoppingActivityToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(AppCompatResources.getDrawable(this, R.drawable.custom_divider_item_decoration));
        shoppingRecyclerView.addItemDecoration(dividerItemDecoration);
        shoppingRecyclerView.setHasFixedSize(true);
        shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // for going back to to-do fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void refreshRecyclerView() {
        allTasks = databaseHandler.getAllShoppingTasks();
        recyclerViewAdapter = new TaskRecyclerViewAdapter(this, allTasks, Constants.SHOPPING_ACTIVITY_NAME, shoppingTotalTasksTV);
        shoppingRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void populateTotalTasksTextView() {
        int numberOfTasks = databaseHandler.totalShoppingTasksCount();
        shoppingTotalTasksTV.setText(numberOfTasks + " tasks");
    }

    private void openAddItemBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(ShoppingActivity.this).inflate(R.layout.custom_add_task_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(ShoppingActivity.this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        TextView title = (TextView) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogTitleTextViewId);
        Button addTaskButton = (Button) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogAddButtonId);
        EditText typedTaskET = (EditText) customBottomSheetDialog.findViewById(R.id.customAddTaskBottomSheetDialogTypeEditTextId);
        title.setText("Item");
        addTaskButton.setText("ADD ITEM");
        typedTaskET.setHint("Type your item here");

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wroteTask = typedTaskET.getText().toString().trim();
                if (!TextUtils.isEmpty(wroteTask)) {
//                    Line No. 128
                    addTaskToDatabase(wroteTask);
                } else {
                    Toast.makeText(ShoppingActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addTaskToDatabase(String taskNote) {
        Task task = new Task();
        task.setTaskNote(taskNote);

        databaseHandler.addShoppingTask(task);
        refreshRecyclerView();
        int lastItemAddedPosition = allTasks.size() - 1;
        shoppingRecyclerView.smoothScrollToPosition(lastItemAddedPosition);
        bottomSheetDialog.dismiss();
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
            databaseHandler.deleteShoppingTask(deletedTask.getTaskId());
            allTasks.remove(taskPosition);
            recyclerViewAdapter.notifyItemRemoved(taskPosition);
            populateTotalTasksTextView();

            Snackbar.make(shoppingRecyclerView, "task deleted", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseHandler.addShoppingTask(deletedTask);
                            allTasks.add(taskPosition, deletedTask);
                            recyclerViewAdapter.notifyItemInserted(taskPosition);
                            populateTotalTasksTextView();
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(ShoppingActivity.this, R.color.red_2))
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
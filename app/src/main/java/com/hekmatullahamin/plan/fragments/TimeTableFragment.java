package com.hekmatullahamin.plan.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.captaindroid.tvg.Tvg;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.AddScheduleActivity;
import com.hekmatullahamin.plan.adapters.BookRecyclerViewAdapter;
import com.hekmatullahamin.plan.adapters.TimeTableRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Book;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TimeTableFragment extends Fragment implements View.OnClickListener {

    private View timeTableFragmentView;

    private RecyclerView bookRecyclerView, timeTableRecyclerView;
    private TimeTableRecyclerViewAdapter timeTableRecyclerViewAdapter;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private Button saturdayButton, sundayButton, mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton;
    private FloatingActionButton addScheduleFAB;
    private TextView clickDayOfTheWeekTV, addScheduleTV;
    private Toolbar timeTableToolbar;

    private BottomSheetDialog bottomSheetDialog;

    private DatabaseHandler databaseHandler;
    private ArrayList<Book> booksList;
    private ArrayList<MyActivity> activitiesList;

    private String clickedDayButtonName = Constants.SATURDAY;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        timeTableFragmentView = inflater.inflate(R.layout.fragment_time_table, container, false);

        fieldsInitialization();

        Tvg.change(clickDayOfTheWeekTV, new int[]{
                ContextCompat.getColor(getContext(), R.color.red),
                ContextCompat.getColor(getContext(), R.color.orange),
                ContextCompat.getColor(getContext(), R.color.purple),
                ContextCompat.getColor(getContext(), R.color.pink),
                ContextCompat.getColor(getContext(), R.color.green),
                ContextCompat.getColor(getContext(), R.color.blue),
        });

        refreshBookRecyclerView();

        addScheduleFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addScheduleActivityIntent = new Intent(getContext(), AddScheduleActivity.class);
                startActivity(addScheduleActivityIntent);
            }
        });

        saturdayButton.setOnClickListener(this);
        sundayButton.setOnClickListener(this);
        mondayButton.setOnClickListener(this);
        tuesdayButton.setOnClickListener(this);
        wednesdayButton.setOnClickListener(this);
        thursdayButton.setOnClickListener(this);
        fridayButton.setOnClickListener(this);

        return timeTableFragmentView;
    }

    private void fieldsInitialization() {
        clickDayOfTheWeekTV = timeTableFragmentView.findViewById(R.id.timeTableFragmentClickDayOfTheWeekTextViewId);
        saturdayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentSaturdayButtonId);
        sundayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentSundayButtonId);
        mondayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentMondayButtonId);
        tuesdayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentTuesdayButtonId);
        wednesdayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentWednesdayButtonId);
        thursdayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentThursdayButtonId);
        fridayButton = timeTableFragmentView.findViewById(R.id.timeTableFragmentFridayButtonId);
        addScheduleFAB = timeTableFragmentView.findViewById(R.id.timeTableFragmentAddScheduleFloatingActionButtonId);
        addScheduleTV = timeTableFragmentView.findViewById(R.id.timeTableFragmentAddScheduleTextViewId);
        bookRecyclerView = timeTableFragmentView.findViewById(R.id.timeTableFragmentTopLinearLayoutRecyclerViewId);
        timeTableRecyclerView = timeTableFragmentView.findViewById(R.id.timeTableFragmentLowLinearLayoutRecyclerViewId);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(timeTableRecyclerView);
        DividerItemDecoration activityDividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        activityDividerItemDecoration.setDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.custom_divider_item_decoration));
        timeTableRecyclerView.addItemDecoration(activityDividerItemDecoration);
        timeTableRecyclerView.setHasFixedSize(true);
        timeTableRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookRecyclerView.setHasFixedSize(true);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        timeTableToolbar = timeTableFragmentView.findViewById(R.id.timeTableFragmentToolbarId);
        timeTableToolbar.setTitle("Time Table");
        timeTableToolbar.inflateMenu(R.menu.custom_time_table_menu);
        timeTableToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.customTimeTableMenuAddBookItemId:
                        openAddBookBottomSheetDialog();
                        break;
                    case R.id.customTimeTableMenuEditScheduleItemId:
                        Intent addScheduleActivityIntent = new Intent(getContext(), AddScheduleActivity.class);
                        startActivity(addScheduleActivityIntent);
                }
                return true;
            }
        });

        databaseHandler = new DatabaseHandler(getContext());
        booksList = new ArrayList<>();
        activitiesList = new ArrayList<>();
    }

    private void openAddBookBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(getContext()).inflate(R.layout.custom_add_book_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        Button addBookToDatabaseButton = customBottomSheetDialog.findViewById(R.id.customAddBookBottomSheetDialogAddBookButtonId);
        EditText typeBookNameET = customBottomSheetDialog.findViewById(R.id.customAddBookBottomSheetDialogTypeBookNameEditTextId);
        EditText typeTotalNumberOfBookPagesET = customBottomSheetDialog.findViewById(R.id.customAddBookBottomSheetDialogTypeNumberOfBookPagesEditTextId);

        addBookToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = typeBookNameET.getText().toString().trim();
                int bookTotalPages = Integer.parseInt(typeTotalNumberOfBookPagesET.getText().toString().trim());
                if (!TextUtils.isEmpty(bookName) && !TextUtils.isEmpty(String.valueOf(bookTotalPages))) {
//                    Line No. 128
                    addBookToDatabase(bookName, bookTotalPages);
                } else {
                    Toast.makeText(getContext(), "Please write book name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addBookToDatabase(String bookName, int bookTotalPages) {
        Book book = new Book();
        book.setBookName(bookName);
        book.setBookTotalBookPages(bookTotalPages);
        databaseHandler.addBook(book);

        refreshBookRecyclerView();
        if (booksList.size() > 1) {
//          to go to end of recyclerview after adding new book
            bookRecyclerView.smoothScrollToPosition(booksList.size() - 1);
        }
        bottomSheetDialog.dismiss();
    }

    private void refreshBookRecyclerView() {
//        for refreshing of recycler view
        booksList = databaseHandler.getAllBooks();
        bookRecyclerViewAdapter = new BookRecyclerViewAdapter(getContext(), booksList);
        bookRecyclerView.setAdapter(bookRecyclerViewAdapter);
    }

    private void refreshActivityRecyclerView(String dayOfWeek) {
        activitiesList.clear();
        activitiesList = databaseHandler.getAllActivitiesOfSpecificDay(dayOfWeek);
        timeTableRecyclerViewAdapter = new TimeTableRecyclerViewAdapter(activitiesList);
        timeTableRecyclerView.setAdapter(timeTableRecyclerViewAdapter);

        shallFABBeVisible();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //        after clicking other days button update what ever swapped or not swapped in previous day
            case R.id.timeTableFragmentSaturdayButtonId:
                clickedDayButtonName = Constants.SATURDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.SATURDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentSaturdayButtonId);
                break;
            case R.id.timeTableFragmentSundayButtonId:
                clickedDayButtonName = Constants.SUNDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.SUNDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentSundayButtonId);
                break;
            case R.id.timeTableFragmentMondayButtonId:
                clickedDayButtonName = Constants.MONDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.MONDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentMondayButtonId);
                break;
            case R.id.timeTableFragmentTuesdayButtonId:
                clickedDayButtonName = Constants.TUESDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.TUESDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentTuesdayButtonId);
                break;
            case R.id.timeTableFragmentWednesdayButtonId:
                clickedDayButtonName = Constants.WEDNESDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.WEDNESDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentWednesdayButtonId);
                break;
            case R.id.timeTableFragmentThursdayButtonId:
                clickedDayButtonName = Constants.THURSDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.THURSDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentThursdayButtonId);
                break;
            case R.id.timeTableFragmentFridayButtonId:
                clickedDayButtonName = Constants.FRIDAY;
                databaseHandler.updateActivitiesAfterSwap(activitiesList);
                refreshActivityRecyclerView(Constants.FRIDAY);
                setBackgroundForOtherButtons(clickedDayButtonName, R.id.timeTableFragmentFridayButtonId);
                break;
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
            int draggedPosition = dragged.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();

            Collections.swap(activitiesList, draggedPosition, targetPosition);
            timeTableRecyclerViewAdapter.notifyItemMoved(draggedPosition, targetPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            MyActivity activity = activitiesList.get(position);
            databaseHandler.deleteActivity(activity.getActivityId());
            activitiesList.remove(position);
            refreshActivityRecyclerView(activity.getActivityDayOfTheWeek());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.red_2))
                    .addActionIcon(R.drawable.ic_baseline_delete_30)
                    .create()
                    .decorate();
        }
    };

    private void shallFABBeVisible() {
        //        for visibility of add schedule fab and fab TV
        int totalActivitiesCount = databaseHandler.totalActivityCount();
        if (totalActivitiesCount > 0) {
            addScheduleFAB.setVisibility(View.INVISIBLE);
            addScheduleTV.setVisibility(View.INVISIBLE);
        } else {
            addScheduleFAB.setVisibility(View.VISIBLE);
            addScheduleTV.setVisibility(View.VISIBLE);
        }
    }

    private void setBackgroundForOtherButtons(String clickedDayButtonName, int clickedDayButtonId) {
        Button clickedButton = getActivity().findViewById(clickedDayButtonId);
        clickedButton.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.custom_clicked_button_background));

        if (!clickedDayButtonName.equals(Constants.SATURDAY)) {
            saturdayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.purple_2));
        }

        if (!clickedDayButtonName.equals(Constants.SUNDAY)) {
            sundayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.red_2));
        }

        if (!clickedDayButtonName.equals(Constants.MONDAY)) {
            mondayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.yellow));
        }

        if (!clickedDayButtonName.equals(Constants.TUESDAY)) {
            tuesdayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.pink));
        }

        if (!clickedDayButtonName.equals(Constants.WEDNESDAY)) {
            wednesdayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.green_2));
        }

        if (!clickedDayButtonName.equals(Constants.THURSDAY)) {
            thursdayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.orange));
        }

        if (!clickedDayButtonName.equals(Constants.FRIDAY)) {
            fridayButton.setBackground(AppCompatResources.getDrawable(getContext(), R.color.blue_2));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int totalTotalActivitiesCount = databaseHandler.totalActivityCount();
        if (totalTotalActivitiesCount >= 1) {
            refreshActivityRecyclerView(clickedDayButtonName);
        }
    }
}
package com.hekmatullahamin.plan.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ItemRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.Person;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class EntriesActivity extends AppCompatActivity {

    private TextView fromDateTV, toDateTV, netBalanceTV, totalEntriesTV, currencySymbol;
    private Button filterButton, clearButton;
    private FloatingActionButton addItemFAB;
    private RecyclerView entryRecyclerView;
//    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private androidx.appcompat.widget.Toolbar entryActivityToolbar;

    private AlertDialog.Builder alertDialogBuilder;
    private Dialog dialog;
    private ItemTouchHelper itemTouchHelper;
    private BottomSheetDialog bottomSheetDialog;

    private DatabaseHandler databaseHandler;
    private ArrayList<Item> itemArrayList;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;

    private SharedPreferences mySettingPreferences;

    private Person personBundle;
    private int fromPersonId;
    private String fromPersonName;
    private String fromPersonMoneyGainOrLoss;
    private double fromPersonMoneyAmount;
    //    for passing personId to unarchive activity
    private static final String PERSON_ID = "PERSON_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

//        line No.135
        fieldsInitialization();

//        line No.560
        setCurrencySymbol();

//        line No.229
        populateTotalEntriesTextView();

//        line No.234
        populateNetBalanceTextView();

        fromDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate("left");
            }
        });

        toDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate("right");
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRecyclerView();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRecyclerView();
                populateNetBalanceTextView();
                populateTotalEntriesTextView();
                fromDateTV.setText(Constants.FROM_DATE);
                toDateTV.setText(Constants.TO_DATE);
            }
        });

        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemBottomSheetDialog();
            }
        });
    }

    private void fieldsInitialization() {
        personBundle = (Person) getIntent().getSerializableExtra(Constants.PERSON_BUNDLE);
        fromPersonId = personBundle.getPersonId();
        fromPersonName = personBundle.getPersonName();
        fromPersonMoneyGainOrLoss = personBundle.getPersonMoneyGainOrLoss();
        fromPersonMoneyAmount = personBundle.getPersonMoneyAmount();
        currencySymbol = findViewById(R.id.entriesActivityCurrencySymbolTextViewId);
        fromDateTV = findViewById(R.id.entriesActivityFromDateTextViewId);
        toDateTV = findViewById(R.id.entriesActivityToDateTextViewId);
        filterButton = findViewById(R.id.entriesActivityFilterRecyclerViewButtonId);
        clearButton = findViewById(R.id.entriesActivityClearSelectedDatesButtonId);

        netBalanceTV = findViewById(R.id.entriesActivityNetBalanceAmountTextViewId);
        totalEntriesTV = findViewById(R.id.entriesActivityTotalEntriesCountTextViewId);
        addItemFAB = findViewById(R.id.entriesActivityAddEntryFloatingActionButtonId);
        entryRecyclerView = findViewById(R.id.entriesActivityRecyclerViewId);
        entryActivityToolbar = findViewById(R.id.entriesActivityToolbarId);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(entryRecyclerView);

        setSupportActionBar(entryActivityToolbar);
        getSupportActionBar().setTitle(fromPersonName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_item_decoration));
        entryRecyclerView.addItemDecoration(dividerItemDecoration);
        entryRecyclerView.setHasFixedSize(true);
        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHandler = new DatabaseHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_entry_activiy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // for going back to calculation fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.customEntryActivityMenuUnarchiveItemId:
                Intent archiveActivityIntent = new Intent(EntriesActivity.this, ArchivedActivity.class);
                archiveActivityIntent.putExtra(PERSON_ID, fromPersonId);
                startActivity(archiveActivityIntent);
                break;
            case R.id.customEntryActivityMenuEditNameId:
                openEditPersonDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openEditPersonDialog() {
        View editPersonDialogView = LayoutInflater.from(EntriesActivity.this).inflate(R.layout.custom_rename_dialog, null);
        Button editPersonNameButton = editPersonDialogView.findViewById(R.id.customRenameDialogSubmitButtonId);
        EditText dialogWritePersonName = editPersonDialogView.findViewById(R.id.customRenameDialogTypeNameEditTextId);
        dialogWritePersonName.setText(fromPersonName);

        alertDialogBuilder = new AlertDialog.Builder(EntriesActivity.this);
        alertDialogBuilder.setView(editPersonDialogView);

        editPersonNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPersonName = dialogWritePersonName.getText().toString().trim();
                if (!TextUtils.isEmpty(newPersonName)) {
//                    Line No. 128
                    updatePersonName(newPersonName);
                } else {
                    Toast.makeText(EntriesActivity.this, "Please type person name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog = alertDialogBuilder.create();
        dialog.show();
    }

    private void updatePersonName(String newPersonName) {
        Person person = new Person();
        person.setPersonId(fromPersonId);
        person.setPersonName(newPersonName);
        person.setPersonMoneyGainOrLoss(fromPersonMoneyGainOrLoss);
        person.setPersonMoneyAmount(fromPersonMoneyAmount);
        databaseHandler.updatePerson(person);
        getSupportActionBar().setTitle(newPersonName);
        dialog.dismiss();
    }

    private void populateTotalEntriesTextView() {
        int numberOfTasks = databaseHandler.totalItemCount(fromPersonId);
        totalEntriesTV.setText(String.valueOf(numberOfTasks));
    }

    private void populateNetBalanceTextView() {
        Person person = new Person();
        double netBalance, spentAmount = 0.0, receivedAmount = 0.0;
        String netBalanceFormatted;
        ArrayList<Item> itemsMoneyAndType = databaseHandler.getAllMoneyAmountAndTypeOfSpecificPerson(fromPersonId);

//        for taking money and divide it to spent and received
        for (Item item : itemsMoneyAndType) {
            if (item.getItemMoneyType().equals(Constants.TYPE_SPENT)) {
                spentAmount += item.getItemMoneyAmount();
            } else {
                receivedAmount += item.getItemMoneyAmount();
            }
        }

        if (receivedAmount >= spentAmount) {
            netBalance = receivedAmount - spentAmount;
            netBalanceFormatted = Utils.formatMoney(netBalance);
            netBalanceTV.setText("+ " + netBalanceFormatted);
            netBalanceTV.setTextColor(ContextCompat.getColor(this, R.color.green));
            person.setPersonMoneyGainOrLoss(Constants.TYPE_GAIN);
        } else {
            netBalance = spentAmount - receivedAmount;
            netBalanceFormatted = Utils.formatMoney(netBalance);
            netBalanceTV.setText("- " + netBalanceFormatted);
            netBalanceTV.setTextColor(ContextCompat.getColor(this, R.color.red));
            person.setPersonMoneyGainOrLoss(Constants.TYPE_LOSS);
        }

        // for updating person table money amount and type column
        person.setPersonMoneyAmount(netBalance);
        person.setPersonId(fromPersonId);
        person.setPersonName(fromPersonName);
        databaseHandler.updatePerson(person);
    }

    private void filterRecyclerView() {
        String fromDateString = fromDateTV.getText().toString();
        String toDateString = toDateTV.getText().toString();
        ArrayList<Item> filteredList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        if (fromDateString.equals(Constants.FROM_DATE) && toDateString.equals(Constants.TO_DATE)) {
//            when both text date picker side is empty
            Toast.makeText(EntriesActivity.this, "Please pick a date first", Toast.LENGTH_SHORT).show();
        } else if (!fromDateString.equals(Constants.FROM_DATE) && fromDateString.length() > 0 &&
                toDateString.equals(Constants.TO_DATE)) {
//            when left date picker is not empty
            filterBaseOnFromDateSide(filteredList, fromDateString, dateFormat);
        } else if (!toDateString.equals(Constants.TO_DATE) && toDateString.length() > 0 &&
                fromDateString.equals(Constants.FROM_DATE)) {
//            when right date picker is not empty
            filterBaseOnToDateSide(filteredList, toDateString, dateFormat);
        } else {
//            when non of them are empty
            filterBaseOnBothDateSide(filteredList, fromDateString, toDateString, dateFormat);
        }

//        to change total entries and net balance text view after choosing date
        if (!fromDateString.equals(Constants.FROM_DATE) || !toDateString.equals(Constants.TO_DATE)) {
            totalEntriesTV.setText(String.valueOf(filteredList.size()));

            double netBalance, spentAmount = 0.0, receivedAmount = 0.0;
            String netBalanceFormatted;
            for (Item item : filteredList) {
                if (item.getItemMoneyType().equals(Constants.TYPE_SPENT)) {
                    spentAmount += item.getItemMoneyAmount();
                } else {
                    receivedAmount += item.getItemMoneyAmount();
                }

            }

            if (receivedAmount >= spentAmount) {
                netBalance = receivedAmount - spentAmount;
                netBalanceFormatted = Utils.formatMoney(netBalance);
                netBalanceTV.setText("+ " + netBalanceFormatted);
                netBalanceTV.setTextColor(ContextCompat.getColor(this, R.color.green));
            } else {
                netBalance = spentAmount - receivedAmount;
                netBalanceFormatted = Utils.formatMoney(netBalance);
                netBalanceTV.setText("- " + netBalanceFormatted);
                netBalanceTV.setTextColor(ContextCompat.getColor(this, R.color.red));
            }
        }

    }

    private void filterBaseOnFromDateSide(ArrayList<Item> filteredList, String fromDateString, SimpleDateFormat dateFormat) {
        Long longFromDate = null;
        try {
//            to change yyyy/MM/dd format to long
            Date fromDate = dateFormat.parse(fromDateString);
            longFromDate = fromDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        filteredList.clear();
        for (Item item : itemArrayList) {
            if (item.getItemAddedDate() >= longFromDate) {
                filteredList.add(item);
            }
        }
        itemRecyclerViewAdapter.filterRecyclerView(filteredList);
    }

    private void filterBaseOnToDateSide(ArrayList<Item> filteredList, String toDateString, SimpleDateFormat dateFormat) {
        Long longToDate = null;
        try {
//            to change yyyy/MM/dd format to long and it gives the start of the day long number not current time
            Date toDate = dateFormat.parse(toDateString);
            longToDate = toDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        filteredList.clear();
        for (Item item : itemArrayList) {
//            we have to convert one day to millisecond and add it to longToDate
            if (item.getItemAddedDate() < longToDate + TimeUnit.DAYS.toMillis(1)) {
                filteredList.add(item);
            }
        }
        itemRecyclerViewAdapter.filterRecyclerView(filteredList);
    }

    private void filterBaseOnBothDateSide(ArrayList<Item> filteredList, String fromDateString, String toDateString, SimpleDateFormat dateFormat) {
        Long longFromDate = null, longToDate = null;
        try {
//            to change yyyy/MM/dd format to long and it gives the start of the day long number not current time
            Date toDate = dateFormat.parse(toDateString);
            Date fromDate = dateFormat.parse(fromDateString);
            longFromDate = fromDate.getTime();
            longToDate = toDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        filteredList.clear();
        for (Item item : itemArrayList) {
//            we have to convert one day to millisecond and add it to longToDate
            if ((item.getItemAddedDate() >= longFromDate) && (item.getItemAddedDate() < longToDate + TimeUnit.DAYS.toMillis(1))) {
                filteredList.add(item);
            }
        }
        itemRecyclerViewAdapter.filterRecyclerView(filteredList);
    }

    private void pickDate(String datePickerPosition) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(EntriesActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "/" + String.format("%02d", (month + 1)) + "/" + String.format("%02d", dayOfMonth);
                if (datePickerPosition.equals("left")) {
                    fromDateTV.setText(date);
                } else {
                    toDateTV.setText(date);
                }
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void openAddItemBottomSheetDialog() {
        View customBottomSheetDialog = LayoutInflater.from(this).inflate(R.layout.custom_add_item_bottom_sheet_dialog, null);

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(customBottomSheetDialog);

        EditText dialogTypeItemName = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogTypeItemNameEditTextId);
        EditText dialogTypeItemAmount = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogTypeItemAmountEditTextId);
        RadioButton dialogReceivedRadioButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogReceivedRadioButtonId);
        RadioButton dialogSpentRadioButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogSpentRadioButtonId);
        Button addItemToDatabaseButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogAddButtonId);

        addItemToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = dialogTypeItemName.getText().toString().trim();
                String itemAmountString = dialogTypeItemAmount.getText().toString().trim();
                String moneyType = null;
                if (dialogReceivedRadioButton.isChecked()) {
                    String receivedRadioText = dialogReceivedRadioButton.getText().toString().toUpperCase();
                    moneyType = receivedRadioText;
                } else if (dialogSpentRadioButton.isChecked()) {
                    String spentRadioText = dialogSpentRadioButton.getText().toString().toUpperCase();
                    moneyType = spentRadioText;
                }
                if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(itemAmountString) && !TextUtils.isEmpty(moneyType)) {
//                    Line No. 128
                    double itemAmount = Double.parseDouble(itemAmountString);
                    addItemToDatabase(itemName, itemAmount, moneyType);
                } else {
                    Toast.makeText(EntriesActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void addItemToDatabase(String itemName, double itemAmount, String moneyType) {
        Item item = new Item();
        item.setItemFromPersonId(fromPersonId);
        item.setItemMoneyType(moneyType);
        item.setItemMoneyAmount(itemAmount);
        item.setItemName(itemName);
        databaseHandler.addItem(item);
        refreshRecyclerView();
        bottomSheetDialog.dismiss();
        populateNetBalanceTextView();
        populateTotalEntriesTextView();
    }

    private void refreshRecyclerView() {
        itemArrayList = databaseHandler.getAllUnarchivedItemsOfSpecificPerson(fromPersonId);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(this, itemArrayList, null);
        entryRecyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Item swipedItem = itemArrayList.get(position);
            alertDialogBuilder = new MaterialAlertDialogBuilder(EntriesActivity.this, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
//            inflating alert dialog
            View deleteOrArchiveAlertDialog = LayoutInflater.from(EntriesActivity.this).inflate(R.layout.custom_delete_or_archive_dialog, null);
            switch (direction) {
                case ItemTouchHelper.LEFT:

                    Button deleteButton, cancelDeleteButton;

                    deleteButton = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
                    cancelDeleteButton = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

                    alertDialogBuilder.setView(deleteOrArchiveAlertDialog);
                    alertDialogBuilder.setCancelable(false);
                    dialog = alertDialogBuilder.create();
                    dialog.show();

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseHandler.deleteItem(swipedItem.getItemId());
                            dialog.dismiss();
                            refreshRecyclerView();
                            populateTotalEntriesTextView();
                            populateNetBalanceTextView();
                        }
                    });

                    cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            refreshRecyclerView();
                        }
                    });
                    break;
                case ItemTouchHelper.RIGHT:

                    TextView title, message;
                    Button archiveButton, cancelArchiveButton;

                    title = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogTitleTextViewId);
                    message = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogMessageTextViewId);
                    archiveButton = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
                    cancelArchiveButton = deleteOrArchiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

                    title.setText("Archive?");
                    message.setText("Are you sure you want to archive this entry");
                    archiveButton.setText("Yes");
                    cancelArchiveButton.setText("No");

                    alertDialogBuilder.setView(deleteOrArchiveAlertDialog);
                    alertDialogBuilder.setCancelable(false);
                    dialog = alertDialogBuilder.create();
                    dialog.show();

                    archiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            swipedItem.setItemState(1);
                            databaseHandler.updateItem(swipedItem);
                            dialog.dismiss();
                            refreshRecyclerView();
                            populateNetBalanceTextView();
                            populateTotalEntriesTextView();
                        }
                    });

                    cancelArchiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            refreshRecyclerView();
                        }
                    });
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(EntriesActivity.this, R.color.red_2))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_30)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(EntriesActivity.this, R.color.green_2))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_30)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        currencySymbol.setText(currency);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecyclerView();
//        if the archived activity is finished populate again net balance and total entries
        populateTotalEntriesTextView();
        populateNetBalanceTextView();
    }
}
package com.hekmatullahamin.plan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.ItemRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.fragments.FriendsFragment;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

public class EntriesActivity extends AppCompatActivity {

    private TextView netBalanceTV, totalEntriesTV, currencySymbolTV, friendNameTV;
    private ImageView goBackImageView, goToArchivedActivityImageView;
    private FloatingActionButton addItemFAB;
    private RelativeLayout actionBarLayoutRelativeLayout;
    private RecyclerView entryRecyclerView;
    private ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private Toolbar toolbar;

    private DatabaseHandler databaseHandler;
    private ArrayList<Item> itemArrayList;

    private SharedPreferences mySettingPreferences;

    private Friend friendBundle;
    private int fromFriendId;
    private String fromFriendName;
    //    for passing personId to archive activity
    private static final String FRIEND_ID = "FRIEND_ID";

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

        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemBottomSheetDialog();
            }
        });
    }

    private void fieldsInitialization() {
        friendBundle = (Friend) getIntent().getSerializableExtra(Constants.FRIEND_BUNDLE);
        fromFriendId = friendBundle.getFriendId();
        fromFriendName = friendBundle.getFriendName();
        currencySymbolTV = findViewById(R.id.entriesActivityCurrencySymbolTextViewId);

        netBalanceTV = findViewById(R.id.entriesActivityNetBalanceAmountTextViewId);
        totalEntriesTV = findViewById(R.id.entriesActivityTotalEntriesCountTextViewId);
        addItemFAB = findViewById(R.id.entriesActivityAddEntryFloatingActionButtonId);
        toolbar = findViewById(R.id.entriesActivityToolbarId);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(fromFriendName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_white_24, null));
        entryRecyclerView = findViewById(R.id.entriesActivityRecyclerViewId);
        entryRecyclerView.setHasFixedSize(true);
        entryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHandler = new DatabaseHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.customItemsMenuArchivesItemId:
                Intent archiveActivityIntent = new Intent(EntriesActivity.this, ArchivedActivity.class);
                archiveActivityIntent.putExtra(FRIEND_ID, fromFriendId);
                startActivity(archiveActivityIntent);
                break;
        }
        return true;
    }


    private void populateTotalEntriesTextView() {
        int numberOfTasks = databaseHandler.totalItemCount(fromFriendId);
        totalEntriesTV.setText(String.valueOf(numberOfTasks));
    }

    private void populateNetBalanceTextView() {
        Friend friend = new Friend();
        double netBalance, spentAmount = 0.0, receivedAmount = 0.0;
        String netBalanceFormatted;
        ArrayList<Item> itemsMoneyAndType = databaseHandler.getAllMoneyAmountAndTypeOfSpecificPerson(fromFriendId);

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
            friend.setFriendMoneyGainOrLoss(Constants.TYPE_GAIN);
        } else {
            netBalance = spentAmount - receivedAmount;
            netBalanceFormatted = Utils.formatMoney(netBalance);
            netBalanceTV.setText("- " + netBalanceFormatted);
            netBalanceTV.setTextColor(ContextCompat.getColor(this, R.color.red));
            friend.setFriendMoneyGainOrLoss(Constants.TYPE_LOSS);
        }

        // for updating friend table money amount and type column
        friend.setFriendMoneyAmount(netBalance);
        friend.setFriendId(fromFriendId);
        friend.setFriendName(fromFriendName);
        databaseHandler.updateFriend(friend);
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
        item.setItemFromPersonId(fromFriendId);
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
        itemArrayList = databaseHandler.getAllUnarchivedItemsOfSpecificPerson(fromFriendId);
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(this, itemArrayList, netBalanceTV,
                totalEntriesTV, fromFriendId, fromFriendName/*, actionBarLayoutRelativeLayout*/);
        entryRecyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        currencySymbolTV.setText(currency);
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
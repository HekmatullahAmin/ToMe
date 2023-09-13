package com.hekmatullahamin.plan.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.AllTransactionsRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.Person;
import com.hekmatullahamin.plan.model.Transaction;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AllTransactionsActivity extends AppCompatActivity {

    //    private TextView
    private TextView totalSpentTV, totalReceivedTV, totalSpentCurrencySymbol, totalReceivedCurrencySymbol, noTransactionsYet;
    private RecyclerView allTransactionsRecyclerView;

    private DatabaseHandler databaseHandler;
    private List<Item> items;
    private List<Transaction> transactionList;

    private SharedPreferences mySettingPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);

//        line No.54
        fieldsInitialization();

//        Line No.100
        setCurrencySymbol();

//        line No.70
        fillTotalSpentAndReceivedAmount();

//        Line No.83
        populateRecyclerView();

        if (transactionList.size() > 0) {
            noTransactionsYet.setVisibility(View.GONE);
        } else {
            noTransactionsYet.setVisibility(View.VISIBLE);
        }
    }

    private void fieldsInitialization() {
        totalSpentTV = findViewById(R.id.allTransactionsActivityTotalSpentAmountTextViewId);
        totalReceivedTV = findViewById(R.id.allTransactionsActivityTotalReceivedAmountTextViewId);
        totalSpentCurrencySymbol = findViewById(R.id.allTransactionsActivitySpentAmountCurrencySymbolTextViewId);
        totalReceivedCurrencySymbol = findViewById(R.id.allTransactionsActivityReceivedAmountCurrencySymbolTextViewId);
        noTransactionsYet = findViewById(R.id.allTransactionsActivityNoTransactionsYetTextViewId);
        allTransactionsRecyclerView = findViewById(R.id.allTransactionsActivityRecyclerViewId);
        allTransactionsRecyclerView.setHasFixedSize(true);
        allTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AllTransactionsActivity.this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_item_decoration));
        allTransactionsRecyclerView.addItemDecoration(dividerItemDecoration);

        databaseHandler = new DatabaseHandler(this);
        items = databaseHandler.getAllItems();
    }

    private void fillTotalSpentAndReceivedAmount() {
        double totalSpentAmount = 0.0, totalReceivedAmount = 0.0;
        for (Item item : items) {
            if (item.getItemMoneyType().equals(Constants.TYPE_SPENT)) {
                totalSpentAmount += item.getItemMoneyAmount();
            } else {
                totalReceivedAmount += item.getItemMoneyAmount();
            }
        }
        totalSpentTV.setText(Utils.formatMoney(totalSpentAmount));
        totalReceivedTV.setText(Utils.formatMoney(totalReceivedAmount));
    }

    private void populateRecyclerView() {
        transactionList = new ArrayList<>();
        for (Item item : items) {
            Transaction transaction = new Transaction();
            transaction.setTransactionAddedDate(item.getItemAddedDate());
            transaction.setTransactionItemName(item.getItemName());
            transaction.setTransactionMoneyAmount(item.getItemMoneyAmount());
            transaction.setTransactionMoneyType(item.getItemMoneyType());
            Person person = databaseHandler.getPersonName(item.getItemFromPersonId());
            transaction.setTransactionFromPersonName(person.getPersonName());
            transactionList.add(transaction);
        }

        AllTransactionsRecyclerViewAdapter allTransactionsRecyclerViewAdapter = new AllTransactionsRecyclerViewAdapter(AllTransactionsActivity.this, transactionList);
        allTransactionsRecyclerView.setAdapter(allTransactionsRecyclerViewAdapter);
    }

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        totalSpentCurrencySymbol.setText(currency);

        totalReceivedCurrencySymbol.setText(currency);
    }
}
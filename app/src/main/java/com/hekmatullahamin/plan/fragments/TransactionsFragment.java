package com.hekmatullahamin.plan.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.adapters.AllTransactionsRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.Transaction;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private View transactionsFragmentView;
    private TextView totalSpentTV, totalReceivedTV, totalSpentCurrencySymbol, totalReceivedCurrencySymbol;
    private RecyclerView allTransactionsRecyclerView;

    private DatabaseHandler databaseHandler;
    private List<Item> items;
    private List<Transaction> transactionList;

    private SharedPreferences mySettingPreferences;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        transactionsFragmentView = inflater.inflate(R.layout.fragment_transactions, container, false);

        fieldsInitialization();

//        setCurrencySymbol();

//        fillTotalSpentAndReceivedAmount();

//        populateRecyclerView();

        return transactionsFragmentView;
    }

    private void fieldsInitialization() {
        totalSpentTV = transactionsFragmentView.findViewById(R.id.fragmentTransactionsTotalSpentAmountTextViewId);
        totalReceivedTV = transactionsFragmentView.findViewById(R.id.fragmentTransactionsTotalReceivedAmountTextViewId);
        totalSpentCurrencySymbol = transactionsFragmentView.findViewById(R.id.fragmentTransactionsSpentAmountCurrencySymbolTextViewId);
        totalReceivedCurrencySymbol = transactionsFragmentView.findViewById(R.id.fragmentTransactionsReceivedAmountCurrencySymbolTextViewId);

        allTransactionsRecyclerView = transactionsFragmentView.findViewById(R.id.fragmentTransactionsRecyclerViewId);
        allTransactionsRecyclerView.setHasFixedSize(true);
        allTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHandler = new DatabaseHandler(getContext());
    }

    private void fillTotalSpentAndReceivedAmount() {
        items = databaseHandler.getAllItems();
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
            Friend friend = databaseHandler.getFriendName(item.getItemFromPersonId());
            transaction.setTransactionFromPersonName(friend.getFriendName());
            transactionList.add(transaction);
        }

        AllTransactionsRecyclerViewAdapter allTransactionsRecyclerViewAdapter = new AllTransactionsRecyclerViewAdapter(getContext(), transactionList);
        allTransactionsRecyclerView.setAdapter(allTransactionsRecyclerViewAdapter);
    }

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = getContext().getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        totalSpentCurrencySymbol.setText(currency);

        totalReceivedCurrencySymbol.setText(currency);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrencySymbol();

        fillTotalSpentAndReceivedAmount();

        populateRecyclerView();
    }
}
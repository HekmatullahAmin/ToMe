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
import com.hekmatullahamin.plan.adapters.FriendsDetailsRecyclerViewAdapter;
import com.hekmatullahamin.plan.adapters.FriendsRecyclerViewAdapter;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private View dashboardFragmentView;
    private TextView totalIncomeTV, totalLostTV, incomeCurrencySymbol, lossCurrencySymbol;
    private DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardFragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fieldsInitialization();

        setCurrencySymbol();


        return dashboardFragmentView;
    }

    private void fieldsInitialization() {
        incomeCurrencySymbol = dashboardFragmentView.findViewById(R.id.dashboardFragmentReceiveCurrencySymbolTextViewId);
        lossCurrencySymbol = dashboardFragmentView.findViewById(R.id.dashboardFragmentLossCurrencySymbolTextViewId);
        totalIncomeTV = dashboardFragmentView.findViewById(R.id.dashboardFragmentTotalIncomeTextViewId);
        totalLostTV = dashboardFragmentView.findViewById(R.id.dashboardFragmentTotalLossTextViewId);
        recyclerView = dashboardFragmentView.findViewById(R.id.dashboardFragmentFriendsDetailsRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        databaseHandler = new DatabaseHandler(getContext());
    }

    private void populateTotalIncomeAndLoss(ArrayList<Friend> people) {
        double totalIncome = 0.0, totalLoss = 0.0;
        for (Friend friend : people) {
            if (friend.getFriendMoneyGainOrLoss().equals(Constants.TYPE_GAIN)) {
                totalIncome += friend.getFriendMoneyAmount();
            } else {
                totalLoss += friend.getFriendMoneyAmount();
            }
        }

        String totalIncomeString = Utils.formatMoney(totalIncome);
        String totalLossString = Utils.formatMoney(totalLoss);

        totalIncomeTV.setText(totalIncomeString);
        totalLostTV.setText(totalLossString);
    }

    private void setCurrencySymbol() {
        //        for adding currency symbol which is chose from currency picker activity
        SharedPreferences mySettingPreferences = getContext().getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        incomeCurrencySymbol.setText(currency);
        lossCurrencySymbol.setText(currency);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Friend> friendArrayList = databaseHandler.getAllFriends();
        FriendsDetailsRecyclerViewAdapter friendsDetailsRecyclerViewAdapter = new FriendsDetailsRecyclerViewAdapter(getContext(), friendArrayList);
        recyclerView.setAdapter(friendsDetailsRecyclerViewAdapter);
        populateTotalIncomeAndLoss(friendArrayList);
    }
}
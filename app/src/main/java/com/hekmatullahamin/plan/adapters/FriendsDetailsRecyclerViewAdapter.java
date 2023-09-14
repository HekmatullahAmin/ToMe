package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

public class FriendsDetailsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsDetailsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Friend> friends;
    private Context context;
    private DatabaseHandler databaseHandler;
    private ArrayList<View> friendsViewArrayList;

    public FriendsDetailsRecyclerViewAdapter(Context context, ArrayList<Friend> friends) {
        this.context = context;
        this.friends = friends;
        databaseHandler = new DatabaseHandler(context);
        friendsViewArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_friend_details_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        ArrayList<Item> itemsOfSpecificPerson = databaseHandler.getAllMoneyAmountAndTypeOfSpecificPersonArchivedAndUnArchived(friend.getFriendId());
        double totalSpentAmount = 0.0, totalGainAmount = 0.0;

        for (Item item : itemsOfSpecificPerson) {
            if (item.getItemMoneyType().equals(Constants.TYPE_SPENT)) {
                totalSpentAmount += item.getItemMoneyAmount();
            } else {
                totalGainAmount += item.getItemMoneyAmount();
            }
        }
        holder.nameTV.setText(friend.getFriendName());
        holder.spendAmountTV.setText(Utils.formatMoney(totalSpentAmount));
        holder.gainAmountTV.setText(Utils.formatMoney(totalGainAmount));
        setCurrencySymbol(holder.spendCurrencyTV, holder.gainCurrencyTV);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTV, spendAmountTV, gainAmountTV, spendCurrencyTV, gainCurrencyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendsViewArrayList.add(itemView);
            nameTV = itemView.findViewById(R.id.customFriendDetailsRowFriendNameTextViewId);
            spendAmountTV = itemView.findViewById(R.id.customFriendDetailsRowSpendAmountTextViewId);
            gainAmountTV = itemView.findViewById(R.id.customFriendDetailsRowGainAmountTextViewId);
            spendCurrencyTV = itemView.findViewById(R.id.customFriendDetailsRowSpendAmountCurrencyTextViewId);
            gainCurrencyTV = itemView.findViewById(R.id.customFriendDetailsRowGainAmountCurrencyTextViewId);

            changeFriendsWidthToMatchParent();
        }
    }

    private void setCurrencySymbol(TextView amountLostCurrencySymbol, TextView amountGainCurrencySymbol) {
        //        for adding currency symbol which is chose from currency picker activity
        SharedPreferences mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        amountLostCurrencySymbol.setText(currency);
        amountGainCurrencySymbol.setText(currency);
    }

    private void changeFriendsWidthToMatchParent() {
//            for making book match parent if there is only one book in recycler view
        if (databaseHandler.totalFriendsCount() == 1) {
            friendsViewArrayList.get(0).getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }
}

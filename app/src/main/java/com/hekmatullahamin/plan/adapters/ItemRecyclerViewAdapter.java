package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.ArchivedActivity;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;
    private List<Item> itemsToExportList = new ArrayList<>();
    private ArchivedActivity activity;
    private Button exportButton;

    private SharedPreferences mySettingPreferences;

    public ItemRecyclerViewAdapter(Context context, List<Item> items, Button exportButton) {
        this.context = context;
        this.items = items;
        this.exportButton = exportButton;
//        if the adapter is used by archive activity
        if (exportButton != null) {
            activity = (ArchivedActivity) context;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customItemRow = LayoutInflater.from(context).inflate(R.layout.custom_entry_row, parent, false);
        return new ViewHolder(customItemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("E, HH:mm");
        String date = dateFormatter.format(new Date(item.getItemAddedDate()));
        String time = timeFormatter.format(new Date(item.getItemAddedDate()));
        double moneyAmount = item.getItemMoneyAmount();

        holder.itemName.setText(item.getItemName());
        holder.itemDateAdded.setText(date + "\n" + time);
        setCurrencySymbol(holder.amountLostCurrencySymbol, holder.amountGainCurrencySymbol);
        if (item.getItemMoneyType().equals(Constants.TYPE_RECEIVED)) {
            holder.itemSpentAmount.setVisibility(View.INVISIBLE);
            holder.amountLostCurrencySymbol.setVisibility(View.INVISIBLE);
            holder.amountGainCurrencySymbol.setVisibility(View.VISIBLE);
            holder.itemReceivedAmount.setVisibility(View.VISIBLE);
            holder.itemReceivedAmount.setText(Utils.formatMoney(moneyAmount));
        } else {
            holder.itemReceivedAmount.setVisibility(View.INVISIBLE);
            holder.amountGainCurrencySymbol.setVisibility(View.INVISIBLE);
            holder.amountLostCurrencySymbol.setVisibility(View.VISIBLE);
            holder.itemSpentAmount.setVisibility(View.VISIBLE);
            holder.itemSpentAmount.setText(Utils.formatMoney(moneyAmount));
        }

//        visible checkbox if it is unarchived 0 for archive 1
        if (item.getItemState() == 0) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        if (exportButton != null) {
            exportButton.setOnClickListener(activity);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterRecyclerView(ArrayList<Item> filteredItems) {
        this.items = filteredItems;
        notifyDataSetChanged();
    }

    public List<Item> getExportedItems() {
        return itemsToExportList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemDateAdded, itemName, itemReceivedAmount, itemSpentAmount, amountLostCurrencySymbol, amountGainCurrencySymbol;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDateAdded = itemView.findViewById(R.id.customEntryRowDateAndTimeTextViewId);
            itemName = itemView.findViewById(R.id.customEntryRowItemNameTextViewId);
            itemReceivedAmount = itemView.findViewById(R.id.customEntryRowYouWillGetAmountColumnTextViewId);
            itemSpentAmount = itemView.findViewById(R.id.customEntryRowYouWillGiveAmountColumnTextViewId);
            amountLostCurrencySymbol = itemView.findViewById(R.id.customEntryRowAmountLostCurrencySymbol);
            amountGainCurrencySymbol = itemView.findViewById(R.id.customEntryRowAmountGainCurrencySymbol);
            checkBox = itemView.findViewById(R.id.customEntryRowCheckBoxId);


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);
                    if (checkBox.isChecked()) {
                        itemsToExportList.add(item);
                    } else {
                        itemsToExportList.remove(item);
                    }
                    if (itemsToExportList.size() > 0) {
                        exportButton.setVisibility(View.VISIBLE);
                    } else {
                        exportButton.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void setCurrencySymbol(TextView amountLostCurrencySymbol, TextView amountGainCurrencySymbol) {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        amountLostCurrencySymbol.setText(currency);
        amountGainCurrencySymbol.setText(currency);
    }
}

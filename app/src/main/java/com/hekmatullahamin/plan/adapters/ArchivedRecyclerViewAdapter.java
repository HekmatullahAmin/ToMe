package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.ArchivedActivity;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.MainViewModel;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArchivedRecyclerViewAdapter extends RecyclerView.Adapter<ArchivedRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> archivedItems;
    private Button exportButton;
    private ArchivedActivity activity;

    private DatabaseHandler databaseHandler;
    private MainViewModel mainViewModel;
    private boolean isEnable = false;
    private boolean isSelectAll = false;
    private ArrayList<Item> selectedList = new ArrayList<>();

    public ArchivedRecyclerViewAdapter(Context context, ArrayList<Item> archivedItems, Button exportButton) {
        this.context = context;
        this.archivedItems = archivedItems;
        this.exportButton = exportButton;
        this.activity = (ArchivedActivity) context;
        databaseHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ArchivedRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customItemRow = LayoutInflater.from(context).inflate(R.layout.custom_archived_entry_row, parent, false);
        mainViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainViewModel.class);
        return new ViewHolder(customItemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedRecyclerViewAdapter.ViewHolder holder, int position) {

        exportButton.setOnClickListener(activity);
        Item item = archivedItems.get(position);

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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                exportButton.setVisibility(View.VISIBLE);
                if (!isEnable) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.custom_action_mode_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            isEnable = true;
                            clickItem(holder);
                            mainViewModel.getText().observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    mode.setTitle(String.format("%s Selected", s));
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int menuId = item.getItemId();
                            switch (menuId) {
                                case R.id.customActionModeMenuDeleteId:
                                    for (Item itemToBeDeleted : selectedList) {
                                        databaseHandler.deleteItem(itemToBeDeleted.getItemId());
                                        archivedItems.remove(itemToBeDeleted);
                                    }
                                    mode.finish();
                                    break;
                                case R.id.customActionModeSelectAllId:
                                    if (selectedList.size() == archivedItems.size()) {
                                        isSelectAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(archivedItems);
                                    }
                                    mainViewModel.setText(String.valueOf(selectedList.size()));
                                    break;
                            }
                            notifyDataSetChanged();
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            isEnable = false;
                            isSelectAll = false;
                            selectedList.clear();
                            mode.finish();
//                            hide check box after action mode get destroyed
                            exportButton.setVisibility(View.GONE);
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) v.getContext()).startActionMode(callback);
                } else {
                    clickItem(holder);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    clickItem(holder);
                }
            }
        });

        if (isSelectAll) {
            holder.circleImageViewCheckBox.setVisibility(View.VISIBLE);
        } else {
            holder.circleImageViewCheckBox.setVisibility(View.GONE);
        }
    }

    private void clickItem(ViewHolder holder) {
        Item item = archivedItems.get(holder.getAdapterPosition());
        if (holder.circleImageViewCheckBox.getVisibility() == View.GONE) {
            holder.circleImageViewCheckBox.setVisibility(View.VISIBLE);
            selectedList.add(item);
        } else {
            holder.circleImageViewCheckBox.setVisibility(View.GONE);
            selectedList.remove(item);
        }
        mainViewModel.setText(String.valueOf(selectedList.size()));
    }

    public ArrayList<Item> getExportedItems() {
        return selectedList;
    }

    @Override
    public int getItemCount() {
        return archivedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDateAdded, itemName, itemReceivedAmount, itemSpentAmount,
                amountLostCurrencySymbol, amountGainCurrencySymbol;
        private CircleImageView circleImageViewCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDateAdded = itemView.findViewById(R.id.customArchivedEntryRowDateAndTimeTextViewId);
            itemName = itemView.findViewById(R.id.customArchivedEntryRowItemNameTextViewId);
            itemReceivedAmount = itemView.findViewById(R.id.customArchivedEntryRowYouWillGetAmountColumnTextViewId);
            itemSpentAmount = itemView.findViewById(R.id.customArchivedEntryRowYouWillGiveAmountColumnTextViewId);

            amountLostCurrencySymbol = itemView.findViewById(R.id.customArchivedEntryRowAmountLostCurrencySymbol);
            amountGainCurrencySymbol = itemView.findViewById(R.id.customArchivedEntryRowAmountGainCurrencySymbol);
            circleImageViewCheckBox = itemView.findViewById(R.id.customArchivedEntryRowCheckBoxCircleImageViewId);
        }
    }

    private void setCurrencySymbol(TextView amountLostCurrencySymbol, TextView amountGainCurrencySymbol) {
        //        for adding currency symbol which is chose from currency picker activity
        SharedPreferences mySettingPreferences;
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        amountLostCurrencySymbol.setText(currency);
        amountGainCurrencySymbol.setText(currency);
    }
}

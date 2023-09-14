package com.hekmatullahamin.plan.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.MainViewModel;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;
    private List<Item> itemsToExportList = new ArrayList<>();
    private ViewBinderHelper viewBinderHelper;
    private SharedPreferences mySettingPreferences;
    private DatabaseHandler databaseHandler;

    private MainViewModel mainViewModel;
    private boolean isEnable = false;
    private boolean isSelectAll = false;
    private ArrayList<Item> selectedList = new ArrayList<>();
    private TextView netBalanceTV, totalEntriesTV;
    private int friendId;
    private String friendName;

    public ItemRecyclerViewAdapter(Context context, List<Item> items, TextView netBalanceTV,
                                   TextView totalEntriesTV, int friendId, String friendName) {
        this.context = context;
        this.items = items;
        this.netBalanceTV = netBalanceTV;
        this.totalEntriesTV = totalEntriesTV;
        this.friendId = friendId;
        this.friendName = friendName;
        databaseHandler = new DatabaseHandler(context);
        viewBinderHelper = new ViewBinderHelper();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customItemRow = LayoutInflater.from(context).inflate(R.layout.custom_entry_row, parent, false);
        mainViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainViewModel.class);
        return new ViewHolder(customItemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(item.getItemId()));
        viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));

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

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                actionBar.setVisibility(View.GONE);
                if (!isEnable) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.custom_action_mode_items_menu, menu);
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
                                case R.id.customActionModeItemsMenuDeleteId:
                                    for (Item itemToBeDeleted : selectedList) {
                                        databaseHandler.deleteItem(itemToBeDeleted.getItemId());
                                        items.remove(itemToBeDeleted);
                                        populateNetBalanceTV();
                                        populateTotalEntriesTV();
                                    }
                                    mode.finish();
                                    break;

                                case R.id.customActionModeItemsArchiveId:
                                    for (Item itemToBeArchived : selectedList) {
                                        itemToBeArchived.setItemState(1);
                                        databaseHandler.updateItem(itemToBeArchived);
                                        items.remove(itemToBeArchived);
                                        populateNetBalanceTV();
                                        populateTotalEntriesTV();
                                    }
                                    mode.finish();
                                    break;
                                case R.id.customActionModeItemsSelectAllId:
                                    if (selectedList.size() == items.size()) {
                                        isSelectAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(items);
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

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        Item item = items.get(holder.getAdapterPosition());
        if (holder.circleImageViewCheckBox.getVisibility() == View.GONE) {
            holder.circleImageViewCheckBox.setVisibility(View.VISIBLE);
            selectedList.add(item);
        } else {
            holder.circleImageViewCheckBox.setVisibility(View.GONE);
            selectedList.remove(item);
        }
        mainViewModel.setText(String.valueOf(selectedList.size()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemDateAdded, itemName, itemReceivedAmount, itemSpentAmount, amountLostCurrencySymbol, amountGainCurrencySymbol;
        private SwipeRevealLayout swipeRevealLayout;
        private BottomSheetDialog bottomSheetDialog;
        private ImageView archiveImageView, editImageView, deleteImageView;
        private CircleImageView circleImageViewCheckBox;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemDateAdded = itemView.findViewById(R.id.customEntryRowDateAndTimeTextViewId);
            itemName = itemView.findViewById(R.id.customEntryRowItemNameTextViewId);
            itemReceivedAmount = itemView.findViewById(R.id.customEntryRowYouWillGetAmountColumnTextViewId);
            itemSpentAmount = itemView.findViewById(R.id.customEntryRowYouWillGiveAmountColumnTextViewId);

            amountLostCurrencySymbol = itemView.findViewById(R.id.customEntryRowAmountLostCurrencySymbol);
            amountGainCurrencySymbol = itemView.findViewById(R.id.customEntryRowAmountGainCurrencySymbol);

            swipeRevealLayout = itemView.findViewById(R.id.customEntryRowSwipeRevealLayoutId);
            archiveImageView = itemView.findViewById(R.id.customEntryRowArchiveImageViewIdId);
            editImageView = itemView.findViewById(R.id.customEntryRowEditImageViewIdId);
            deleteImageView = itemView.findViewById(R.id.customEntryRowDeleteImageViewIdId);
            circleImageViewCheckBox = itemView.findViewById(R.id.customEntryRowCheckBoxCircleImageViewId);

            linearLayout = itemView.findViewById(R.id.customEntryRowLinearLayoutId);

            archiveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);
                    AlertDialog.Builder alertDialogBuilder;
                    Dialog dialog;
                    alertDialogBuilder = new MaterialAlertDialogBuilder(context, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
                    View archiveAlertDialog = LayoutInflater.from(context).inflate(R.layout.custom_delete_or_archive_dialog, null);

                    TextView title, message;
                    Button archiveButton, cancelArchiveButton;

                    title = archiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogTitleTextViewId);
                    message = archiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogMessageTextViewId);
                    archiveButton = archiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
                    cancelArchiveButton = archiveAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

                    title.setText("Archive?");
                    message.setText("Are you sure you want to archive this entry");
                    archiveButton.setText("Yes");
                    cancelArchiveButton.setText("No");

                    alertDialogBuilder.setView(archiveAlertDialog);
                    alertDialogBuilder.setCancelable(false);
                    dialog = alertDialogBuilder.create();
                    dialog.show();

                    archiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.setItemState(1);
                            databaseHandler.updateItem(item);
                            items.remove(position);
                            notifyItemRemoved(position);
                            populateNetBalanceTV();
                            populateTotalEntriesTV();
                            dialog.dismiss();
                            viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));
                        }
                    });

                    cancelArchiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));
                        }
                    });
                }
            });

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);

                    View customBottomSheetDialog = LayoutInflater.from(context).inflate(R.layout.custom_add_item_bottom_sheet_dialog, null);
                    bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(customBottomSheetDialog);

                    EditText dialogTypeItemName = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogTypeItemNameEditTextId);
                    EditText dialogTypeItemAmount = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogTypeItemAmountEditTextId);
                    RadioButton dialogReceivedRadioButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogReceivedRadioButtonId);
                    RadioButton dialogSpentRadioButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogSpentRadioButtonId);
                    Button editItemButton = customBottomSheetDialog.findViewById(R.id.customAddItemBottomSheetDialogAddButtonId);

                    String receivedRadioText = dialogReceivedRadioButton.getText().toString().toUpperCase();
                    String spentRadioText = dialogSpentRadioButton.getText().toString().toUpperCase();
                    dialogTypeItemName.setText(item.getItemName());
                    dialogTypeItemAmount.setText(String.valueOf(item.getItemMoneyAmount()));

                    if (item.getItemMoneyType().equals(receivedRadioText)) {
                        dialogReceivedRadioButton.setChecked(true);
                    } else {
                        dialogSpentRadioButton.setChecked(true);
                    }
                    editItemButton.setText("Edit");

                    bottomSheetDialog.show();

                    editItemButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String itemName = dialogTypeItemName.getText().toString().trim();
                            String itemAmountString = dialogTypeItemAmount.getText().toString().trim();
                            String moneyType = null;
                            if (dialogReceivedRadioButton.isChecked()) {
                                moneyType = receivedRadioText;
                            } else if (dialogSpentRadioButton.isChecked()) {
                                moneyType = spentRadioText;
                            }
                            if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(itemAmountString) && !TextUtils.isEmpty(moneyType)) {
//                    Line No. 128
                                double itemAmount = Double.parseDouble(itemAmountString);
                                item.setItemName(itemName);
                                item.setItemMoneyAmount(itemAmount);
                                item.setItemMoneyType(moneyType);
                                editItemToDatabase(item, position);
                            } else {
                                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = items.get(position);
                    AlertDialog.Builder alertDialogBuilder;
                    Dialog dialog;
                    alertDialogBuilder = new MaterialAlertDialogBuilder(context, R.style.MyRounded_MaterialComponents_MaterialAlertDialog);
                    Button deleteButton, cancelButton;

//            inflating delete alert dialog
                    View deleteAlertDialog = LayoutInflater.from(context).inflate(R.layout.custom_delete_or_archive_dialog, null);
                    deleteButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogDeleteOrArchiveItButtonId);
                    cancelButton = deleteAlertDialog.findViewById(R.id.customDeleteOrArchiveDialogCancelButtonId);

                    alertDialogBuilder.setView(deleteAlertDialog);
                    alertDialogBuilder.setCancelable(false);
                    dialog = alertDialogBuilder.create();
                    dialog.show();

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseHandler.deleteItem(item.getItemId());
                            items.remove(position);
                            notifyItemRemoved(position);
                            populateNetBalanceTV();
                            populateTotalEntriesTV();
                            dialog.dismiss();
                            viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));
                        }
                    });

                }
            });

        }

        private void editItemToDatabase(Item item, int position) {
            databaseHandler.updateItem(item);
            notifyItemChanged(position);
            populateNetBalanceTV();
            populateTotalEntriesTV();
            bottomSheetDialog.dismiss();
            viewBinderHelper.closeLayout(String.valueOf(item.getItemId()));
        }
    }

    private void populateTotalEntriesTV() {
        int numberOfEntries = databaseHandler.totalItemCount(friendId);
        totalEntriesTV.setText(String.valueOf(numberOfEntries));
    }

    private void populateNetBalanceTV() {
        Friend friend = new Friend();
        double netBalance, spentAmount = 0.0, receivedAmount = 0.0;
        String netBalanceFormatted;
        ArrayList<Item> itemsMoneyAndType = databaseHandler.getAllMoneyAmountAndTypeOfSpecificPerson(friendId);

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
            netBalanceTV.setTextColor(ContextCompat.getColor(context, R.color.green));
            friend.setFriendMoneyGainOrLoss(Constants.TYPE_GAIN);
        } else {
            netBalance = spentAmount - receivedAmount;
            netBalanceFormatted = Utils.formatMoney(netBalance);
            netBalanceTV.setText("- " + netBalanceFormatted);
            netBalanceTV.setTextColor(ContextCompat.getColor(context, R.color.red));
            friend.setFriendMoneyGainOrLoss(Constants.TYPE_LOSS);
        }

        // for updating friend table money amount and type column
        friend.setFriendMoneyAmount(netBalance);
        friend.setFriendId(friendId);
        friend.setFriendName(friendName);
        databaseHandler.updateFriend(friend);
    }

    private void setCurrencySymbol(TextView amountLostCurrencySymbol, TextView amountGainCurrencySymbol) {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        amountLostCurrencySymbol.setText(currency);
        amountGainCurrencySymbol.setText(currency);
    }
}
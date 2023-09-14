package com.hekmatullahamin.plan.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.EntriesActivity;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

import static com.hekmatullahamin.plan.R.color.green;
import static com.hekmatullahamin.plan.R.color.red;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Friend> friendList;
    private DatabaseHandler databaseHandler;
    private ViewBinderHelper viewBinderHelper;

    private SharedPreferences mySettingPreferences;

    public FriendsRecyclerViewAdapter(Context context, ArrayList<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
        databaseHandler = new DatabaseHandler(context);
        viewBinderHelper = new ViewBinderHelper();
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customPersonRow = LayoutInflater.from(context).inflate(R.layout.custom_friend_row, parent, false);
        return new ViewHolder(customPersonRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(friend.getFriendId()));
        viewBinderHelper.closeLayout(String.valueOf(friend.getFriendId()));

        String moneyType = friend.getFriendMoneyGainOrLoss();
        double moneyAmount = friend.getFriendMoneyAmount();
        //        for adding currency symbol which is chose from currency picker activity
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");

//        for populating friend row name, money
        holder.personNameTV.setText(friend.getFriendName());
        holder.currencySymbol.setText(currency);
        if (moneyType.equals(Constants.TYPE_GAIN)) {
            holder.moneyYouWillGetOrGiveTV.setText(Constants.YOU_WILL_GET);
            holder.moneyYouWillGetOrGiveAmountTV.setText("+ " + Utils.formatMoney(moneyAmount));
            holder.moneyYouWillGetOrGiveAmountTV.setTextColor(ContextCompat.getColor(context, green));
        } else {
            holder.moneyYouWillGetOrGiveTV.setText(Constants.YOU_WILL_GIVE);
            holder.moneyYouWillGetOrGiveAmountTV.setText("- " + Utils.formatMoney(moneyAmount));
            holder.moneyYouWillGetOrGiveAmountTV.setTextColor(ContextCompat.getColor(context, red));
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void filterRecyclerView(ArrayList<Friend> filteredList) {
        this.friendList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView personNameTV, moneyYouWillGetOrGiveAmountTV, moneyYouWillGetOrGiveTV, currencySymbol;
        private SwipeRevealLayout swipeRevealLayout;
        private ImageView deleteImageView, editImageView;
        private BottomSheetDialog bottomSheetDialog;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personNameTV = itemView.findViewById(R.id.customFriendRowPersonNameTextViewId);
            moneyYouWillGetOrGiveTV = itemView.findViewById(R.id.customFriendRowFromPersonYouWillGetOrGiveTextViewId);
            moneyYouWillGetOrGiveAmountTV = itemView.findViewById(R.id.customFriendRowFromPersonYouWillGetOrGiveAmountTextViewId);
            currencySymbol = itemView.findViewById(R.id.customFriendRowCurrencySymbolTextViewId);

            swipeRevealLayout = itemView.findViewById(R.id.customFriendRowSwipeLayoutId);
            deleteImageView = itemView.findViewById(R.id.customFriendRowDeleteImageViewIdId);
            editImageView = itemView.findViewById(R.id.customFriendRowEditImageViewIdId);

            relativeLayout = itemView.findViewById(R.id.customFriendRowMainUiRelativeLayoutId);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Friend friend = friendList.get(getAdapterPosition());
                    Intent entriesActivityIntent = new Intent(context, EntriesActivity.class);
                    Bundle friendBundle = new Bundle();
                    friendBundle.putSerializable(Constants.FRIEND_BUNDLE, friend);
                    entriesActivityIntent.putExtras(friendBundle);
                    context.startActivity(entriesActivityIntent);
                }
            });

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Friend friend = friendList.get(position);

                    View customBottomSheetDialog = LayoutInflater.from(context).inflate(R.layout.custom_add_friend_bottom_sheet_dialog, null);
                    bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                    bottomSheetDialog.setContentView(customBottomSheetDialog);

                    Button editFriendButton = customBottomSheetDialog.findViewById(R.id.customAddFriendBottomSheetDialogAddFriendButtonId);
                    EditText typedFriendNameET = customBottomSheetDialog.findViewById(R.id.customAddFriendBottomSheetDialogTypeFriendEditTextId);
                    typedFriendNameET.setText(friend.getFriendName());
                    editFriendButton.setText("Edit");

                    editFriendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String friendName = typedFriendNameET.getText().toString().trim();
                            if (!TextUtils.isEmpty(friendName)) {
//                    Line No. 128
                                friend.setFriendName(friendName);
                                editFriendToDatabase(friend, position);
                            } else {
                                Toast.makeText(context, "Friend name can not be empty", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    bottomSheetDialog.show();
                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Friend friend = friendList.get(position);
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
                            databaseHandler.deleteAllItems(friend.getFriendId());
                            databaseHandler.deleteFriend(friend.getFriendId());
                            viewBinderHelper.closeLayout(String.valueOf(friend.getFriendId()));
                            dialog.dismiss();
                            friendList.remove(position);
                            notifyItemRemoved(position);
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            viewBinderHelper.closeLayout(String.valueOf(friend.getFriendId()));
                        }
                    });
                }
            });
        }

        private void editFriendToDatabase(Friend friend, int position) {
            databaseHandler.updateFriend(friend);
            notifyItemChanged(position);
            viewBinderHelper.closeLayout(String.valueOf(friend.getFriendId()));
            bottomSheetDialog.dismiss();
        }
    }
}

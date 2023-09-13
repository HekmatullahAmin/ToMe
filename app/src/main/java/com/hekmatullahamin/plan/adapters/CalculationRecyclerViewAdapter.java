package com.hekmatullahamin.plan.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.activities.EntriesActivity;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Person;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.util.ArrayList;

import static com.hekmatullahamin.plan.R.color.green;
import static com.hekmatullahamin.plan.R.color.red;

public class CalculationRecyclerViewAdapter extends RecyclerView.Adapter<CalculationRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Person> peopleList;
    private DatabaseHandler databaseHandler;

    private SharedPreferences mySettingPreferences;

    public CalculationRecyclerViewAdapter(Context context, ArrayList<Person> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
        databaseHandler = new DatabaseHandler(context);
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customPersonRow = LayoutInflater.from(context).inflate(R.layout.custom_person_row, parent, false);
        return new ViewHolder(customPersonRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = peopleList.get(position);
        String moneyType = person.getPersonMoneyGainOrLoss();
        double moneyAmount = person.getPersonMoneyAmount();
        //        for adding currency symbol which is chose from currency picker activity
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");

//        for populating person row name, money
        holder.personNameTV.setText(person.getPersonName());
        holder.currencySysmbol.setText(currency);
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
        return peopleList.size();
    }

    public void filterRecyclerView(ArrayList<Person> filteredList) {
        this.peopleList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView personNameTV, moneyYouWillGetOrGiveAmountTV, moneyYouWillGetOrGiveTV, currencySysmbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personNameTV = itemView.findViewById(R.id.customPersonRowPersonNameTextViewId);
            moneyYouWillGetOrGiveTV = itemView.findViewById(R.id.customPersonRowFromPersonYouWillGetOrGiveTextViewId);
            moneyYouWillGetOrGiveAmountTV = itemView.findViewById(R.id.customPersonRowFromPersonYouWillGetOrGiveAmountTextViewId);
            currencySysmbol = itemView.findViewById(R.id.customPersonRowCurrencySymbolTextViewId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person person = peopleList.get(getAdapterPosition());
                    Intent entriesActivityIntent = new Intent(context, EntriesActivity.class);
                    Bundle personBundle = new Bundle();
                    personBundle.putSerializable(Constants.PERSON_BUNDLE, person);
                    entriesActivityIntent.putExtras(personBundle);
                    context.startActivity(entriesActivityIntent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
                    int position = getAdapterPosition();
                    Person person = peopleList.get(position);

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
                            databaseHandler.deleteAllItems(person.getPersonId());
                            databaseHandler.deletePerson(person.getPersonId());
                            dialog.dismiss();
                            peopleList.remove(position);
                            notifyItemRemoved(position);
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                        }
                    });
                    return true;
                }
            });

        }
    }
}

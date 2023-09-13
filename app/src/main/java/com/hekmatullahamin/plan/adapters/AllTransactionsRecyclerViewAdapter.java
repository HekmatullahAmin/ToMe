package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.model.Transaction;
import com.hekmatullahamin.plan.utils.Constants;
import com.hekmatullahamin.plan.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllTransactionsRecyclerViewAdapter extends RecyclerView.Adapter<AllTransactionsRecyclerViewAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private Context context;

    private SharedPreferences mySettingPreferences;

    public AllTransactionsRecyclerViewAdapter(Context context, List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customTransactionRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transaction_row, parent, false);
        return new ViewHolder(customTransactionRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("E, HH:mm");
        String date = dateFormatter.format(new Date(transaction.getTransactionAddedDate()));
        String time = timeFormatter.format(new Date(transaction.getTransactionAddedDate()));
        double moneyAmount = transaction.getTransactionMoneyAmount();

        holder.transactionDate.setText(date + "\n" + time);
        holder.transactionItemName.setText(transaction.getTransactionItemName());
        holder.transactionFormPersonName.setText(transaction.getTransactionFromPersonName());
        holder.transactionAmount.setText(Utils.formatMoney(moneyAmount));
        setCurrencySymbol(holder.amountCurrencySymbol);
        if (transaction.getTransactionMoneyType().equals(Constants.TYPE_RECEIVED)) {
            holder.transactionSign.setText("+");
            holder.transactionSign.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.transactionSign.setText("-");
            holder.transactionSign.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView transactionDate, transactionItemName, transactionFormPersonName, transactionAmount, transactionSign, amountCurrencySymbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transactionDate = itemView.findViewById(R.id.customAllTransactionsRowDateAndTimeTextViewId);
            transactionItemName = itemView.findViewById(R.id.customAllTransactionsRowItemNameTextViewId);
            transactionFormPersonName = itemView.findViewById(R.id.customAllTransactionsRowFromPersonNameTextViewId);
            transactionAmount = itemView.findViewById(R.id.customAllTransactionsRowMoneyAmountTextViewId);
            transactionSign = itemView.findViewById(R.id.customAllTransactionsRowPlusOrMinusSignTextView);
            amountCurrencySymbol = itemView.findViewById(R.id.customAllTransactionsRowCurrencySymbolTextViewId);
        }
    }

    private void setCurrencySymbol(TextView amountCurrencySymbol) {
        //        for adding currency symbol which is chose from currency picker activity
        mySettingPreferences = context.getSharedPreferences(Constants.MY_SETTING_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String currency = mySettingPreferences.getString(Constants.MY_SETTING_CURRENCY_SYMBOL, "$");
        amountCurrencySymbol.setText(currency);
    }
}

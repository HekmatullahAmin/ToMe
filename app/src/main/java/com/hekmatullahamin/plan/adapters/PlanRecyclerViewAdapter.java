package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hekmatullahamin.plan.R;
import com.hekmatullahamin.plan.data.DatabaseHandler;
import com.hekmatullahamin.plan.model.Plan;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlanRecyclerViewAdapter extends RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Plan> planList;

    private DatabaseHandler databaseHandler;

    public PlanRecyclerViewAdapter(Context context, List<Plan> planList) {
        this.context = context;
        this.planList = planList;
        databaseHandler = new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View customPlanRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_plan_row, null);
        return new ViewHolder(customPlanRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(plan.getPlanDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + String.format("%02d", (month + 1)) + "/" + String.format("%02d", dayOfMonth);
        Boolean isChecked = isChecked(plan.getPlanState());

        holder.planDate.setText(date);
        holder.checkBox.setChecked(isChecked);
//        put strike through on plan note if it is checked or date is passed
        if (isChecked || (System.currentTimeMillis() > plan.getPlanDate() + TimeUnit.DAYS.toMillis(1))) {
            if (System.currentTimeMillis() > plan.getPlanDate() + TimeUnit.DAYS.toMillis(1)) {
//                make checkbox disable if only time is passed
                holder.checkBox.setEnabled(false);
            }
            holder.planNote.setPaintFlags(holder.planNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.planNote.setText(plan.getPlanNote());

        if ((System.currentTimeMillis() >= plan.getPlanDate()) && (System.currentTimeMillis() <= plan.getPlanDate() + TimeUnit.DAYS.toMillis(1))) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        }

    }

    // for returning true if our status is 1 and false if status is 0
    public boolean isChecked(int oneOrZero) {
        return oneOrZero > 0;
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView planDate, planNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.customPlanRowCheckBoxId);
            planDate = itemView.findViewById(R.id.customPlanRowPlanDateTextViewId);
            planNote = itemView.findViewById(R.id.customPlanRowPlanNoteTextViewId);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Plan plan = planList.get(getAdapterPosition());
                    if (checkBox.isChecked()) {
                        plan.setPlanState(1);
                        planNote.setPaintFlags(planNote.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        plan.setPlanState(0);
                        planNote.setPaintFlags(planNote.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                    databaseHandler.updatePlan(plan);

                }
            });
        }
    }
}

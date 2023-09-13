package com.hekmatullahamin.plan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hekmatullahamin.plan.R;

import java.util.List;

public class SpinnerDayOfWeekAdapter extends ArrayAdapter<String> {

    private int resource;
    private List<String> daysOfWeekList;

    public SpinnerDayOfWeekAdapter(@NonNull Context context, int resource, List<String> daysOfWeekList) {
        super(context, resource);
        this.resource = resource;
        this.daysOfWeekList = daysOfWeekList;
    }

    @Override
    public int getCount() {
        return daysOfWeekList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return daysOfWeekList.get(position);
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        TextView daysOfTheWeekNameTV;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
//            daysOfTheWeekNameTV = convertView.findViewById(R.id.customSpinnerDayOfTheWeekTextViewId);
//
//            String daysOfTheWeekString = daysOfWeekList.get(position);
//            daysOfTheWeekNameTV.setText(daysOfTheWeekString);
//        }
//        return convertView;
//    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView daysOfTheWeekNameTV;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            daysOfTheWeekNameTV = convertView.findViewById(R.id.customSpinnerDayOfTheWeekTextViewId);

            String daysOfTheWeekString = daysOfWeekList.get(position);
            daysOfTheWeekNameTV.setText(daysOfTheWeekString);
        }
        return convertView;
    }
}

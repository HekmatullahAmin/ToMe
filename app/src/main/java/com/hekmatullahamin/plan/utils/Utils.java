package com.hekmatullahamin.plan.utils;

import android.app.Activity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String formatMoney(double money) {
        String[] array = String.format("%,.2f", money).split("[.]");
        return array[0] + "." + array[1];
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String formatDate(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(date).getTime());
    }

    public static String calculateDifferenceBetweenTwoTime(String fromTime, String toTime) throws ParseException {
        if (fromTime.equals("00:00") && toTime.equals("00:00")) {
            return 24 + " h";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date fromDate = dateFormat.parse(fromTime);
        Date toDate = dateFormat.parse(toTime);

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(fromDate);
        int fromHour = fromCalendar.get(Calendar.HOUR_OF_DAY);
        int fromMin = fromCalendar.get(Calendar.MINUTE);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(toDate);
        int toHour = toCalendar.get(Calendar.HOUR_OF_DAY);
        int toMin = toCalendar.get(Calendar.MINUTE);

        if (toTime.equals("00:00")) {
            toHour = 24;
            toMin = 00;
        }

        int hour;
        int min;
        if (fromMin > toMin) {
            toMin = toMin + 60;
            toHour = toHour - 1;
        }
        hour = toHour - fromHour;
        min = toMin - fromMin;

        if (hour > 0 && min > 0) {
            return hour + " h " + min + " m";
        }else if (min == 0) {
            return hour + " h";
        }else {
            return min + " m";
        }

    }
}

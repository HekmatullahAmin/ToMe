package com.hekmatullahamin.plan.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

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
}

package com.hekmatullahamin.plan.utils;

public class Constants {
    //    Database name and version
    public static final String PLAN_DATABASE_NAME = "PLAN_DB";
    public static final int PLAN_DATABASE_VERSION = 2;
    //    table names
    public static final String TODAY_TASK_TABLE_NAME = "TODAY_TASK_TABLE";
    public static final String SHOPPING_TASK_TABLE_NAME = "SHOPPING_TASK_TABLE";
    public static final String PLAN_TABLE_NAME = "PLAN_TABLE";
    public static final String PLAN_NEW_TABLE_NAME = "PLAN_NEW_TABLE";
    public static final String PERSON_TABLE_NAME = "CALCULATION_TABLE";
    public static final String ITEM_TABLE_NAME = "ITEM_TABLE";
    public static final String BOOK_TABLE_NAME = "BOOK_TABLE";
    public static final String SCHEDULE_TABLE_NAME = "SCHEDULE_TABLE";
    public static final String SCHEDULE_NEW_TABLE_NAME = "SCHEDULE_NEW_TABLE";
    public static final String  NOTES_TABLE_NAME = "NOTES_TABLE";

    public static final String ID_COLUMN = "ID";
    //    columns of task table
//    public static final String TASK_NOTE_COLUMN = "NOTE";
//    public static final String TASK_STATE_CHECKED_OR_NOT_COLUMN = "STATE";
    //    columns of plan table
    public static final String PLAN_NOTE_COLUMN = "NAME";
    public static final String PLAN_STATE_CHECKED_OR_NOT_COLUMN = "STATE";
    public static final String PLAN_DATE_COLUMN = "DATE";
    public static final String PLAN_DATE_AND_TIME_COLUMN = "DATE_AND_TIME";
    public static final String PLAN_NOTIFICATION_UNIQUE_ID_COLUMN = "NOTIFICATION_ID";
    //    columns of person table
    public static final String PERSON_NAME_COLUMN = "NAME";
    public static final String PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN = "MONEY_AMOUNT";
    public static final String PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN = "MONEY_TYPE";
    //    columns of money table
    public static final String ITEM_FROM_WHICH_PERSON_ID_COLUMN = "FROM_PERSON";
    public static final String ITEM_TYPE_COLUMN = "TYPE";
    public static final String ITEM_MONEY_AMOUNT_COLUMN = "AMOUNT";
    public static final String ITEM_NAME_COLUMN = "ITEM_NAME";
    public static final String ITEM_ADDED_DATE_COLUMN = "DATE";
    public static final String ITEM_STATE_ARCHIVED_OR_NOT_COLUMN = "STATE";
    //    columns of book table
//    public static final String BOOK_NAME_COLUMN = "BOOK_NAME";
//    public static final String BOOK_NUMBER_OF_PAGES_READ_COLUMN = "BOOK_READ_PAGES";
//    public static final String BOOK_TOTAL_PAGES_COLUMN = "BOOK_TOTAL_PAGES";
    //    columns of schedule table
    public static final String SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN = "ACTIVITY";
    public static final String SCHEDULE_FROM_TIME_COLUMN = "FROM_TIME";
    public static final String SCHEDULE_FROM_TIME_LONG_COLUMN = "FROM_TIME_LONG";
    public static final String SCHEDULE_TO_TIME_COLUMN = "TO_TIME";
    public static final String SCHEDULE_DAY_OF_THE_WEEK_COLUMN = "DAY_OF_THE_WEEK";
    public static final String SCHEDULE_TAG_NAME_COLUMN = "TAG_NAME";
    public static final String SCHEDULE_TAG_COLOR_COLUMN = "TAG_COLOR";
    public static final String SCHEDULE_TAG_IMAGE_COLUMN = "TAG_IMAGE";

//    columns of notes table
//    public static final String NOTE_TEXT_COLOUR_COLUMN = "TEXT_COLOUR";
//    public static final String NOTE_TEXT_SIZE_COLUMN = "TEXT_SIZE";
//    public static final String NOTE_TEXT_STYLE_COLUMN = "TEXT_STYLE";
    public static final String NOTE_TITLE_COLUMN = "TEXT_TITLE";
    public static final String NOTE_TEXT_COLUMN = "TEXT_NOTE";
    public static final String NOTE_DATE_COLUMN = "TEXT_DATE";

    //    bundle name
    public static final String FRIEND_BUNDLE = "ENTRY_ACTIVITY_BUNDLE";
//            1. Notes bundle
    public static final String ACTIVITY_OR_ADAPTER_CLASS_KEY = "ACTIVITY_OR_ADAPTER_CLASS";
    public static final String ALL_NOTES_ACTIVITY_NAME = "ALL_NOTES_ACTIVITY";
    public static final String NOTES_RECYCLER_VIEW_ADAPTER = "NOTES_RECYCLER-VIEW_ADAPTER";
//        serializable
    public static final String NOTE_KEY = "NOTE";
    //    notification names
    public static final String NOTIFICATION_MANAGER_COMPAT_ID = "MANAGER_ID";
    public static final String NOTIFICATION_CHANNEL_ID = "CHANNEL_ID";
    public static final String NOTIFICATION_CHANNEL_NAME = "CHANNEL_NAME";
    public static final String NOTIFICATION_MESSAGE = "MESSAGE";

    //    today and shopping activity names
//    public static final String TODAY_ACTIVITY_NAME = "TODAY";
//    public static final String SHOPPING_ACTIVITY_NAME = "SHOPPING";
    //    item row money state
    public static final String TYPE_SPENT = "SPENT";
    public static final String TYPE_RECEIVED = "RECEIVED";
    //    person row money state and get/give text
    public static final String TYPE_GAIN = "GAIN";
    public static final String TYPE_LOSS = "LOSS";
    public static final String YOU_WILL_GET = "You will get";
    public static final String YOU_WILL_GIVE = "You will give";

    //    date picker from and to TextView text
//    public static final String FROM_DATE = "FROM DATE";
//    public static final String TO_DATE = "TO DATE";
    //    days of week
//    public static final String SATURDAY = "Saturday";
//    public static final String SUNDAY = "Sunday";
//    public static final String MONDAY = "Monday";
//    public static final String TUESDAY = "Tuesday";
//    public static final String WEDNESDAY = "Wednesday";
//    public static final String THURSDAY = "Thursday";
//    public static final String FRIDAY = "Friday";

    //    setting shared preferences constants
    public static final String MY_SETTING_PREFERENCE_NAME = "my_setting_preference_name";
    public static final String MY_SETTING_USER_NAME = "user_name";
    public static final String MY_SETTING_PROFILE_PICTURE = "profile_picture";
    public static final String MY_SETTING_CURRENCY_SYMBOL = "currency_symbol";
}

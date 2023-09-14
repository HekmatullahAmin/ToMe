package com.hekmatullahamin.plan.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hekmatullahamin.plan.model.Friend;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.model.Note;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.PLAN_DATABASE_NAME, null, Constants.PLAN_DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.d("TAGTAG", "on create");

//        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.NOTES_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.NOTE_TITLE_COLUMN + " TEXT, " +
//                Constants.NOTE_TEXT_COLUMN + " TEXT, " + Constants.NOTE_DATE_COLUMN + " LONG );";
//
//        String CREATE_NEW_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
//                Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_DATE_AND_TIME_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";
//
//        String CREATE_NEW_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN + " TEXT, " +
//                Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " LONG, " +
//                Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT, " +
//                Constants.SCHEDULE_TAG_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_TAG_COLOR_COLUMN + " INT, " +
//                Constants.SCHEDULE_TAG_IMAGE_COLUMN + " INT);";

//        Log.d("TAGTAG", "on create");
//        String CREATE_TODAY_TABLE = "CREATE TABLE " + Constants.TODAY_TASK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.TASK_NOTE_COLUMN + " TEXT, " + Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " INT );";
//        String CREATE_SHOPPING_TABLE = "CREATE TABLE " + Constants.SHOPPING_TASK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.TASK_NOTE_COLUMN + " TEXT, " + Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " INT );";
//        String CREATE_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
//                Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_DATE_AND_TIME_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";
        String CREATE_PERSON_TABLE = "CREATE TABLE " + Constants.PERSON_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.PERSON_NAME_COLUMN + " TEXT, " + Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN + " REAL, " +
                Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN + " TEXT );";
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Constants.ITEM_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " INT, " + Constants.ITEM_TYPE_COLUMN + " TEXT, " +
                Constants.ITEM_MONEY_AMOUNT_COLUMN + " REAL, " + Constants.ITEM_NAME_COLUMN + " TEXT, " + Constants.ITEM_ADDED_DATE_COLUMN +
                " LONG, " + Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " INT );";
        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.NOTES_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.NOTE_TITLE_COLUMN + " TEXT, " +
                Constants.NOTE_TEXT_COLUMN + " TEXT, " + Constants.NOTE_DATE_COLUMN + " LONG );";

        String CREATE_NEW_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
                Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_DATE_AND_TIME_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";

        String CREATE_NEW_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN + " TEXT, " +
                Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " LONG, " +
                Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT, " +
                Constants.SCHEDULE_TAG_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_TAG_COLOR_COLUMN + " INT, " +
                Constants.SCHEDULE_TAG_IMAGE_COLUMN + " INT);";
        db.execSQL(CREATE_PERSON_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NEW_PLAN_TABLE);
        db.execSQL(CREATE_NEW_SCHEDULE_TABLE);


//        String CREATE_BOOK_TABLE = "CREATE TABLE " + Constants.BOOK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.BOOK_NUMBER_OF_PAGES_READ_COLUMN + " INT, " + Constants.BOOK_TOTAL_PAGES_COLUMN +
//                " INT, " + Constants.BOOK_NAME_COLUMN + " TEXT );";
//        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " +
//                Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT );";
//        String CREATE_NEW_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN + " TEXT, " +
//                Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " LONG, " +
//                Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT, " +
//                Constants.SCHEDULE_TAG_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_TAG_COLOR_COLUMN + " INT, " +
//                Constants.SCHEDULE_TAG_IMAGE_COLUMN + " INT);";
//        db.execSQL(CREATE_TODAY_TABLE);
//        db.execSQL(CREATE_SHOPPING_TABLE);
//        db.execSQL(CREATE_PLAN_TABLE);
//        db.execSQL(CREATE_PERSON_TABLE);
//        db.execSQL(CREATE_ITEM_TABLE);
//        db.execSQL(CREATE_NOTES_TABLE);
//        db.execSQL(CREATE_NEW_PLAN_TABLE);
//        db.execSQL(CREATE_NEW_SCHEDULE_TABLE);
//        db.execSQL(CREATE_BOOK_TABLE);
//        db.execSQL(CREATE_NEW_SCHEDULE_TABLE);

//        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.NOTES_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.NOTE_TEXT_COLOUR_COLUMN + " INT, " + Constants.NOTE_TEXT_STYLE_COLUMN +
//                " INTEGER, " + Constants.NOTE_TEXT_SIZE_COLUMN + " REAL, " + Constants.NOTE_TITLE_COLUMN + " TEXT, " +
//                Constants.NOTE_TEXT_COLUMN + " TEXT, " + Constants.NOTE_DATE_COLUMN + " LONG );";
//        String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.NOTES_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.NOTE_TITLE_COLUMN + " TEXT, " +
//                Constants.NOTE_TEXT_COLUMN + " TEXT, " + Constants.NOTE_DATE_COLUMN + " LONG );";
//        db.execSQL(CREATE_NOTES_TABLE);
//        String CREATE_NEW_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
//                " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
//                Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_DATE_AND_TIME_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";
//        db.execSQL(CREATE_NEW_PLAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
//            Log.d("TAGTAG", "on upgrade");
            String CREATE_PERSON_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.PERSON_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                    " INTEGER PRIMARY KEY, " + Constants.PERSON_NAME_COLUMN + " TEXT, " + Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN + " REAL, " +
                    Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN + " TEXT );";
            String CREATE_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS " + Constants.ITEM_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                    " INTEGER PRIMARY KEY, " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " INT, " + Constants.ITEM_TYPE_COLUMN + " TEXT, " +
                    Constants.ITEM_MONEY_AMOUNT_COLUMN + " REAL, " + Constants.ITEM_NAME_COLUMN + " TEXT, " + Constants.ITEM_ADDED_DATE_COLUMN +
                    " LONG, " + Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " INT );";
            String CREATE_NOTES_TABLE = "CREATE TABLE " + Constants.NOTES_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.NOTE_TITLE_COLUMN + " TEXT, " +
                    Constants.NOTE_TEXT_COLUMN + " TEXT, " + Constants.NOTE_DATE_COLUMN + " LONG );";

            String CREATE_NEW_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                    " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
                    Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_DATE_AND_TIME_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";

            String CREATE_NEW_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_NEW_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                    " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN + " TEXT, " +
                    Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " LONG, " +
                    Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT, " +
                    Constants.SCHEDULE_TAG_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_TAG_COLOR_COLUMN + " INT, " +
                    Constants.SCHEDULE_TAG_IMAGE_COLUMN + " INT);";

            db.execSQL(CREATE_PERSON_TABLE);
            db.execSQL(CREATE_ITEM_TABLE);
            db.execSQL(CREATE_NOTES_TABLE);
            db.execSQL(CREATE_NEW_PLAN_TABLE);
            db.execSQL(CREATE_NEW_SCHEDULE_TABLE);

            db.execSQL("DROP TABLE IF EXISTS " + Constants.TODAY_TASK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.SHOPPING_TASK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.BOOK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.SCHEDULE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.PLAN_TABLE_NAME);
        }
    }

    public void addNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.NOTE_TITLE_COLUMN, note.getNoteTextTitle());
        values.put(Constants.NOTE_TEXT_COLUMN, note.getNoteText());
        values.put(Constants.NOTE_DATE_COLUMN, System.currentTimeMillis());
        database.insert(Constants.NOTES_TABLE_NAME, null, values);
        database.close();
    }

    public int updateNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.NOTE_TITLE_COLUMN, note.getNoteTextTitle());
        values.put(Constants.NOTE_TEXT_COLUMN, note.getNoteText());
        int rowUpdate = database.update(Constants.NOTES_TABLE_NAME, values, Constants.ID_COLUMN + " =? ",
                new String[]{String.valueOf(note.getNoteId())});
        database.close();
        return rowUpdate;
    }

    public void deleteNote(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.NOTES_TABLE_NAME, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(id)});
        database.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.NOTES_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.NOTE_TITLE_COLUMN,
                        Constants.NOTE_TEXT_COLUMN, Constants.NOTE_DATE_COLUMN}, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(id)}, null, null, null);
        Note note = new Note();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                note.setNoteId(cursor.getInt(cursor.getColumnIndex(Constants.ID_COLUMN)));
                note.setNoteTextTitle(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TITLE_COLUMN)));
                note.setNoteText(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TEXT_COLUMN)));
                note.setNoteTextDate(cursor.getLong(cursor.getColumnIndex(Constants.NOTE_DATE_COLUMN)));
            }
        }
        cursor.close();
        database.close();
        return note;
    }

    public ArrayList<Note> getAllNotes() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.NOTES_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.NOTE_TITLE_COLUMN,
                        Constants.NOTE_TEXT_COLUMN, Constants.NOTE_DATE_COLUMN}, null,
                null, null, null, Constants.NOTE_DATE_COLUMN + " ASC", null);
        ArrayList<Note> notes = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setNoteId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    note.setNoteTextTitle(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TITLE_COLUMN)));
                    note.setNoteText(cursor.getString(cursor.getColumnIndex(Constants.NOTE_TEXT_COLUMN)));
                    note.setNoteTextDate(cursor.getLong(cursor.getColumnIndex(Constants.NOTE_DATE_COLUMN)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return notes;
    }

    public void addPlan(Plan plan) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN, 0);
        values.put(Constants.PLAN_NOTE_COLUMN, plan.getPlanNote());
        values.put(Constants.PLAN_DATE_COLUMN, plan.getPlanDate());
        values.put(Constants.PLAN_DATE_AND_TIME_COLUMN, plan.getPlanDateAndTime());
        values.put(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN, plan.getPlanNotificationId());
        database.insert(Constants.PLAN_NEW_TABLE_NAME, null, values);
        database.close();
    }

    public void addFriend(Friend friend) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PERSON_NAME_COLUMN, friend.getFriendName());
        values.put(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN, friend.getFriendMoneyAmount());
        values.put(Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN, Constants.TYPE_GAIN);
        database.insert(Constants.PERSON_TABLE_NAME, null, values);
        database.close();
    }

    public void addItem(Item item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN, item.getItemFromPersonId());
        values.put(Constants.ITEM_TYPE_COLUMN, item.getItemMoneyType());
        values.put(Constants.ITEM_MONEY_AMOUNT_COLUMN, item.getItemMoneyAmount());
        values.put(Constants.ITEM_NAME_COLUMN, item.getItemName());
        values.put(Constants.ITEM_ADDED_DATE_COLUMN, System.currentTimeMillis());
        values.put(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN, item.getItemState());
        database.insert(Constants.ITEM_TABLE_NAME, null, values);
        database.close();
    }

    public void addActivity(MyActivity activity) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN, activity.getActivityDescription());
        values.put(Constants.SCHEDULE_FROM_TIME_COLUMN, activity.getActivityFromTime());
        values.put(Constants.SCHEDULE_FROM_TIME_LONG_COLUMN, activity.getActivityFromTimeLong());
        values.put(Constants.SCHEDULE_TO_TIME_COLUMN, activity.getActivityToTime());
        values.put(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN, activity.getActivityDayOfTheWeek());
        values.put(Constants.SCHEDULE_TAG_NAME_COLUMN, activity.getActivityTagName());
        values.put(Constants.SCHEDULE_TAG_COLOR_COLUMN, activity.getActivityTagColor());
        values.put(Constants.SCHEDULE_TAG_IMAGE_COLUMN, activity.getActivityTagImage());
        database.insert(Constants.SCHEDULE_NEW_TABLE_NAME, null, values);
        database.close();
    }

    //    get task
    public Friend getFriendName(int friendId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Friend friend = new Friend();
        Cursor cursor = database.query(Constants.PERSON_TABLE_NAME, new String[]{Constants.PERSON_NAME_COLUMN},
                Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(friendId)}, null,
                null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            friend.setFriendName(cursor.getString(cursor.getColumnIndex(Constants.PERSON_NAME_COLUMN)));
        }
        cursor.close();
        database.close();
        return friend;
    }

    public ArrayList<Plan> getAllPlans() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Plan> allPlans = new ArrayList<>();
        Cursor cursor = database.query(Constants.PLAN_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN,
                        Constants.PLAN_NOTE_COLUMN, Constants.PLAN_DATE_COLUMN, Constants.PLAN_DATE_AND_TIME_COLUMN,
                        Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN}, null, null, null, null,
                Constants.PLAN_DATE_COLUMN + " ASC", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Plan plan = new Plan();
                    plan.setPlanId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    plan.setPlanState(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN)));
                    plan.setPlanNote(cursor.getString(cursor.getColumnIndex(Constants.PLAN_NOTE_COLUMN)));
                    plan.setPlanDate(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_COLUMN)));
                    plan.setPlanDateAndTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_AND_TIME_COLUMN)));
                    plan.setPlanNotificationId(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN)));
                    allPlans.add(plan);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allPlans;
    }

    public ArrayList<Plan> getPlansForSpecificDate(long date) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Plan> plans = new ArrayList<>();
        Cursor cursor = database.query(Constants.PLAN_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN,
                        Constants.PLAN_NOTE_COLUMN, Constants.PLAN_DATE_COLUMN, Constants.PLAN_DATE_AND_TIME_COLUMN,
                        Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN}, Constants.PLAN_DATE_COLUMN + " = ? ",
                new String[]{String.valueOf(date)}, null, null,
                Constants.PLAN_DATE_AND_TIME_COLUMN + " ASC", null);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Plan plan = new Plan();
                    plan.setPlanId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    plan.setPlanState(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN)));
                    plan.setPlanNote(cursor.getString(cursor.getColumnIndex(Constants.PLAN_NOTE_COLUMN)));
                    plan.setPlanDate(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_COLUMN)));
                    plan.setPlanDateAndTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_AND_TIME_COLUMN)));
                    plan.setPlanNotificationId(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN)));
                    plans.add(plan);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return plans;
    }

    public ArrayList<Plan> getAllPlansAddedDate() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Plan> allPlans = new ArrayList<>();
        Cursor cursor = database.query(Constants.PLAN_NEW_TABLE_NAME, new String[]{Constants.PLAN_DATE_COLUMN, Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN}, null, null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Plan plan = new Plan();
                    plan.setPlanDate(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_COLUMN)));
                    plan.setPlanState(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN)));
                    allPlans.add(plan);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allPlans;
    }

    public ArrayList<Friend> getAllFriends() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Friend> allPeople = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.PERSON_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Friend friend = new Friend();
                    friend.setFriendId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    friend.setFriendName(cursor.getString(cursor.getColumnIndex(Constants.PERSON_NAME_COLUMN)));
                    friend.setFriendMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN)));
                    friend.setFriendMoneyGainOrLoss(cursor.getString(cursor.getColumnIndex(Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN)));
                    allPeople.add(friend);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allPeople;
    }

    public ArrayList<Item> getAllItems() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Item> allItems = new ArrayList<>();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN,
                        Constants.ITEM_TYPE_COLUMN, Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_NAME_COLUMN, Constants.ITEM_ADDED_DATE_COLUMN, Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN}, null,
                null, null, null, Constants.ITEM_ADDED_DATE_COLUMN + " DESC", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    item.setItemFromPersonId(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN)));
                    item.setItemMoneyType(cursor.getString(cursor.getColumnIndex(Constants.ITEM_TYPE_COLUMN)));
                    item.setItemMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.ITEM_MONEY_AMOUNT_COLUMN)));
                    item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.ITEM_NAME_COLUMN)));
                    item.setItemAddedDate(cursor.getLong(cursor.getColumnIndex(Constants.ITEM_ADDED_DATE_COLUMN)));
                    item.setItemState(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN)));
                    allItems.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allItems;
    }

    public ArrayList<Item> getAllUnarchivedItemsOfSpecificPerson(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Item> allItems = new ArrayList<>();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN,
                        Constants.ITEM_TYPE_COLUMN, Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_NAME_COLUMN, Constants.ITEM_ADDED_DATE_COLUMN, Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN},
                Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " = ? AND " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " = ?",
                new String[]{String.valueOf(0), String.valueOf(fromPersonId)}, null, null,
                Constants.ITEM_ADDED_DATE_COLUMN + " DESC", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    item.setItemFromPersonId(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN)));
                    item.setItemMoneyType(cursor.getString(cursor.getColumnIndex(Constants.ITEM_TYPE_COLUMN)));
                    item.setItemMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.ITEM_MONEY_AMOUNT_COLUMN)));
                    item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.ITEM_NAME_COLUMN)));
                    item.setItemAddedDate(cursor.getLong(cursor.getColumnIndex(Constants.ITEM_ADDED_DATE_COLUMN)));
                    item.setItemState(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN)));
                    allItems.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allItems;
    }

    public ArrayList<Item> getAllArchivedItemsOfSpecificPerson(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Item> allItems = new ArrayList<>();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN,
                        Constants.ITEM_TYPE_COLUMN, Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_NAME_COLUMN,
                        Constants.ITEM_ADDED_DATE_COLUMN, Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN},
                Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " = ? AND " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " = ?",
                new String[]{String.valueOf(1), String.valueOf(fromPersonId)}, null, null,
                Constants.ITEM_ADDED_DATE_COLUMN + " DESC", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    item.setItemFromPersonId(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN)));
                    item.setItemMoneyType(cursor.getString(cursor.getColumnIndex(Constants.ITEM_TYPE_COLUMN)));
                    item.setItemMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.ITEM_MONEY_AMOUNT_COLUMN)));
                    item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.ITEM_NAME_COLUMN)));
                    item.setItemAddedDate(cursor.getLong(cursor.getColumnIndex(Constants.ITEM_ADDED_DATE_COLUMN)));
                    item.setItemState(cursor.getInt(cursor.getColumnIndex(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN)));
                    allItems.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allItems;
    }

    public ArrayList<Item> getAllMoneyAmountAndTypeOfSpecificPerson(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Item> allItemsMoneyAndType = new ArrayList<>();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_TYPE_COLUMN},
                Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " = ? AND " + Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " = ? ",
                new String[]{String.valueOf(fromPersonId), String.valueOf(0)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemMoneyType(cursor.getString(cursor.getColumnIndex(Constants.ITEM_TYPE_COLUMN)));
                    item.setItemMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.ITEM_MONEY_AMOUNT_COLUMN)));
                    allItemsMoneyAndType.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allItemsMoneyAndType;
    }

    public ArrayList<Item> getAllMoneyAmountAndTypeOfSpecificPersonArchivedAndUnArchived(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Item> allItemsMoneyAndType = new ArrayList<>();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_TYPE_COLUMN},
                Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " = ? ",
                new String[]{String.valueOf(fromPersonId)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemMoneyType(cursor.getString(cursor.getColumnIndex(Constants.ITEM_TYPE_COLUMN)));
                    item.setItemMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.ITEM_MONEY_AMOUNT_COLUMN)));
                    allItemsMoneyAndType.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allItemsMoneyAndType;
    }

    public MyActivity getLastActivity(String dayOfTheWeek) {
        SQLiteDatabase database = this.getReadableDatabase();
        MyActivity activity = new MyActivity();
        Cursor cursor = database.query(Constants.SCHEDULE_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN,
                        Constants.SCHEDULE_FROM_TIME_COLUMN, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN, Constants.SCHEDULE_TO_TIME_COLUMN, Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN,
                        Constants.SCHEDULE_TAG_NAME_COLUMN, Constants.SCHEDULE_TAG_COLOR_COLUMN, Constants.SCHEDULE_TAG_IMAGE_COLUMN},
                Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " = ?",
                new String[]{dayOfTheWeek}, null,
                null, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " DESC", null);
        if (cursor.moveToFirst()) {
            activity.setActivityId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
            activity.setActivityDescription(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN)));
            activity.setActivityFromTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_COLUMN)));
            activity.setActivityFromTimeLong(cursor.getLong(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_LONG_COLUMN)));
            activity.setActivityToTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TO_TIME_COLUMN)));
            activity.setActivityDayOfTheWeek(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN)));
            activity.setActivityTagName(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TAG_NAME_COLUMN)));
            activity.setActivityTagColor(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_COLOR_COLUMN)));
            activity.setActivityTagImage(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_IMAGE_COLUMN)));
        }
        cursor.close();
        database.close();
        return activity;
    }

    public MyActivity getFirstActivity(String dayOfTheWeek) {
        SQLiteDatabase database = this.getReadableDatabase();
        MyActivity activity = new MyActivity();

        Cursor cursor = database.query(Constants.SCHEDULE_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN,
                        Constants.SCHEDULE_FROM_TIME_COLUMN, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN, Constants.SCHEDULE_TO_TIME_COLUMN, Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN,
                        Constants.SCHEDULE_TAG_NAME_COLUMN, Constants.SCHEDULE_TAG_COLOR_COLUMN, Constants.SCHEDULE_TAG_IMAGE_COLUMN},
                Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " = ?",
                new String[]{dayOfTheWeek}, null,
                null, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " ASC", null);
        if (cursor.moveToFirst()) {
            activity.setActivityId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
            activity.setActivityDescription(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN)));
            activity.setActivityFromTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_COLUMN)));
            activity.setActivityFromTimeLong(cursor.getLong(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_LONG_COLUMN)));
            activity.setActivityToTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TO_TIME_COLUMN)));
            activity.setActivityDayOfTheWeek(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN)));
            activity.setActivityTagName(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TAG_NAME_COLUMN)));
            activity.setActivityTagColor(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_COLOR_COLUMN)));
            activity.setActivityTagImage(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_IMAGE_COLUMN)));
        }
        cursor.close();
        database.close();
        return activity;
    }

    public ArrayList<MyActivity> getAllActivitiesOfSpecificDay(String dayOfTheWeek) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<MyActivity> allActivities = new ArrayList<>();
        Cursor cursor = database.query(Constants.SCHEDULE_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN,
                        Constants.SCHEDULE_FROM_TIME_COLUMN, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN, Constants.SCHEDULE_TO_TIME_COLUMN, Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN,
                        Constants.SCHEDULE_TAG_NAME_COLUMN, Constants.SCHEDULE_TAG_COLOR_COLUMN, Constants.SCHEDULE_TAG_IMAGE_COLUMN},
                Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " = ?",
                new String[]{dayOfTheWeek}, null,
                null, Constants.SCHEDULE_FROM_TIME_LONG_COLUMN + " ASC", null);
//        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.SCHEDULE_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    MyActivity activity = new MyActivity();
                    activity.setActivityId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    activity.setActivityDescription(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN)));
                    activity.setActivityFromTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_COLUMN)));
                    activity.setActivityFromTimeLong(cursor.getLong(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_LONG_COLUMN)));
                    activity.setActivityToTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TO_TIME_COLUMN)));
                    activity.setActivityDayOfTheWeek(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN)));
                    activity.setActivityTagName(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TAG_NAME_COLUMN)));
                    activity.setActivityTagColor(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_COLOR_COLUMN)));
                    activity.setActivityTagImage(cursor.getInt(cursor.getColumnIndex(Constants.SCHEDULE_TAG_IMAGE_COLUMN)));
                    allActivities.add(activity);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allActivities;
    }

    public int deletePlan(int planId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.PLAN_NEW_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(planId)});
        database.close();
        return rowDeleted;
    }

    public int deleteFriend(int friendId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.PERSON_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(friendId)});
        database.close();
        return rowDeleted;
    }

    public int deleteItem(int itemId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.ITEM_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(itemId)});
        database.close();
        return rowDeleted;
    }

    public void deleteAllItems(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Constants.ITEM_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.ITEM_NAME_COLUMN},
                Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " = ?", new String[]{String.valueOf(fromPersonId)}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Item item = new Item();
                    item.setItemId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.ITEM_NAME_COLUMN)));

                    int itemId = item.getItemId();
                    deleteItem(itemId);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
    }

    public int deleteBook(int bookId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.BOOK_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(bookId)});
        database.close();
        return rowDeleted;
    }

    public int deleteActivity(int activityId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.SCHEDULE_NEW_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(activityId)});
        database.close();
        return rowDeleted;
    }

    public int updatePlan(Plan plan) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PLAN_NOTE_COLUMN, plan.getPlanNote());
        values.put(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN, plan.getPlanState());
        values.put(Constants.PLAN_DATE_COLUMN, plan.getPlanDate());
        values.put(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN, plan.getPlanNotificationId());
        int rowUpdated = database.update(Constants.PLAN_NEW_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(plan.getPlanId())});
        database.close();
        return rowUpdated;
    }

    public int updateFriend(Friend friend) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ID_COLUMN, friend.getFriendId());
        values.put(Constants.PERSON_NAME_COLUMN, friend.getFriendName());
        values.put(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN, friend.getFriendMoneyAmount());
        values.put(Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN, friend.getFriendMoneyGainOrLoss());
        int rowUpdated = database.update(Constants.PERSON_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(friend.getFriendId())});
        database.close();
        return rowUpdated;
    }

    public int updateItem(Item item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ITEM_NAME_COLUMN, item.getItemName());
        values.put(Constants.ITEM_MONEY_AMOUNT_COLUMN, item.getItemMoneyAmount());
        values.put(Constants.ITEM_TYPE_COLUMN, item.getItemMoneyType());
        values.put(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN, item.getItemState());
        int rowUpdated = database.update(Constants.ITEM_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(item.getItemId())});
        database.close();
        return rowUpdated;
    }

    public int updateActivity(MyActivity activity) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN, activity.getActivityDescription());
        values.put(Constants.SCHEDULE_FROM_TIME_COLUMN, activity.getActivityFromTime());
        values.put(Constants.SCHEDULE_FROM_TIME_LONG_COLUMN, activity.getActivityFromTimeLong());
        values.put(Constants.SCHEDULE_TO_TIME_COLUMN, activity.getActivityToTime());
        values.put(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN, activity.getActivityDayOfTheWeek());
        values.put(Constants.SCHEDULE_TAG_NAME_COLUMN, activity.getActivityTagName());
        values.put(Constants.SCHEDULE_TAG_COLOR_COLUMN, activity.getActivityTagColor());
        values.put(Constants.SCHEDULE_TAG_IMAGE_COLUMN, activity.getActivityTagImage());
        int update = database.update(Constants.SCHEDULE_NEW_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(activity.getActivityId())});
        database.close();
        return update;
    }

    public int totalItemCount(int fromPersonId) {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.ITEM_TABLE_NAME + " WHERE " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN
                + " = ? AND " + Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " = ? ", new String[]{String.valueOf(fromPersonId), String.valueOf(0)});
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }

    public int totalActivityCount(String specificDayOfTheWeek) {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.query(Constants.SCHEDULE_NEW_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.SCHEDULE_ACTIVITY_DESCRIPTION_NAME_COLUMN,
                        Constants.SCHEDULE_FROM_TIME_COLUMN, Constants.SCHEDULE_TO_TIME_COLUMN, Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN,
                        Constants.SCHEDULE_TAG_NAME_COLUMN, Constants.SCHEDULE_TAG_COLOR_COLUMN, Constants.SCHEDULE_TAG_IMAGE_COLUMN},
                Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " = ?",
                new String[]{specificDayOfTheWeek}, null,
                null, null, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }

    public int totalFriendsCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.PERSON_TABLE_NAME, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }

    public int totalNotesCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.NOTES_TABLE_NAME, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }
}

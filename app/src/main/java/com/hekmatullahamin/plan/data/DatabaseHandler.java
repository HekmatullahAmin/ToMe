package com.hekmatullahamin.plan.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hekmatullahamin.plan.model.Book;
import com.hekmatullahamin.plan.model.Item;
import com.hekmatullahamin.plan.model.MyActivity;
import com.hekmatullahamin.plan.model.Person;
import com.hekmatullahamin.plan.model.Plan;
import com.hekmatullahamin.plan.model.Task;
import com.hekmatullahamin.plan.utils.Constants;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.PLAN_DATABASE_NAME, null, Constants.PLAN_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODAY_TABLE = "CREATE TABLE " + Constants.TODAY_TASK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.TASK_NOTE_COLUMN + " TEXT, " + Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " INT );";
        String CREATE_SHOPPING_TABLE = "CREATE TABLE " + Constants.SHOPPING_TASK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.TASK_NOTE_COLUMN + " TEXT, " + Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " INT );";
        String CREATE_PLAN_TABLE = "CREATE TABLE " + Constants.PLAN_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN + " INT, " + Constants.PLAN_NOTE_COLUMN + " TEXT, " +
                Constants.PLAN_DATE_COLUMN + " LONG, " + Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN + " LONG );";
        String CREATE_PERSON_TABLE = "CREATE TABLE " + Constants.PERSON_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.PERSON_NAME_COLUMN + " TEXT, " + Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN + " REAL, " +
                Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN + " TEXT );";
        String CREATE_ITEM_TABLE = "CREATE TABLE " + Constants.ITEM_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.ITEM_FROM_WHICH_PERSON_ID_COLUMN + " INT, " + Constants.ITEM_TYPE_COLUMN + " TEXT, " +
                Constants.ITEM_MONEY_AMOUNT_COLUMN + " REAL, " + Constants.ITEM_NAME_COLUMN + " TEXT, " + Constants.ITEM_ADDED_DATE_COLUMN +
                " LONG, " + Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN + " INT );";
        String CREATE_BOOK_TABLE = "CREATE TABLE " + Constants.BOOK_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.BOOK_NUMBER_OF_PAGES_READ_COLUMN + " INT, " + Constants.BOOK_TOTAL_PAGES_COLUMN +
                " INT, " + Constants.BOOK_NAME_COLUMN + " TEXT );";
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + Constants.SCHEDULE_TABLE_NAME + " ( " + Constants.ID_COLUMN +
                " INTEGER PRIMARY KEY, " + Constants.SCHEDULE_ACTIVITY_NAME_COLUMN + " TEXT, " + Constants.SCHEDULE_FROM_TIME_COLUMN + " TEXT, " +
                Constants.SCHEDULE_TO_TIME_COLUMN + " TEXT, " + Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " TEXT );";
        db.execSQL(CREATE_TODAY_TABLE);
        db.execSQL(CREATE_SHOPPING_TABLE);
        db.execSQL(CREATE_PLAN_TABLE);
        db.execSQL(CREATE_PERSON_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String TODAY_QUERY = "DROP TABLE IF EXISTS " + Constants.TODAY_TASK_TABLE_NAME;
        String SHOPPING_QUERY = "DROP TABLE IF EXISTS " + Constants.SHOPPING_TASK_TABLE_NAME;
        String PLAN_QUERY = "DROP TABLE IF EXISTS " + Constants.PLAN_TABLE_NAME;
        String PERSON_QUERY = "DROP TABLE IF EXISTS " + Constants.PERSON_TABLE_NAME;
        String ITEM_QUERY = "DROP TABLE IF EXISTS " + Constants.ITEM_TABLE_NAME;
        String BOOK_QUERY = "DROP TABLE IF EXISTS " + Constants.BOOK_TABLE_NAME;
        String SCHEDULE_QUERY = "DROP TABLE IF EXISTS " + Constants.SCHEDULE_TABLE_NAME;
        db.execSQL(TODAY_QUERY);
        db.execSQL(SHOPPING_QUERY);
        db.execSQL(PLAN_QUERY);
        db.execSQL(PERSON_QUERY);
        db.execSQL(ITEM_QUERY);
        db.execSQL(BOOK_QUERY);
        db.execSQL(SCHEDULE_QUERY);
    }

    //    add task to today activity and shopping activity
    public void addTodayTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TASK_NOTE_COLUMN, task.getTaskNote());
        values.put(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN, 0);
        database.insert(Constants.TODAY_TASK_TABLE_NAME, null, values);
        database.close();
    }

    public void addShoppingTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TASK_NOTE_COLUMN, task.getTaskNote());
        values.put(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN, 0);
        database.insert(Constants.SHOPPING_TASK_TABLE_NAME, null, values);
        database.close();
    }

    public void addPlan(Plan plan) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN, 0);
        values.put(Constants.PLAN_NOTE_COLUMN, plan.getPlanNote());
        values.put(Constants.PLAN_DATE_COLUMN, plan.getPlanDate());
        values.put(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN, plan.getPlanNotificationId());
        database.insert(Constants.PLAN_TABLE_NAME, null, values);
        database.close();
    }

    public void addPerson(Person person) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PERSON_NAME_COLUMN, person.getPersonName());
        values.put(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN, person.getPersonMoneyAmount());
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

    public void addBook(Book book) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.BOOK_NUMBER_OF_PAGES_READ_COLUMN, book.getBookNumberOfPagesRead());
        values.put(Constants.BOOK_TOTAL_PAGES_COLUMN, book.getBookTotalBookPages());
        values.put(Constants.BOOK_NAME_COLUMN, book.getBookName());
        database.insert(Constants.BOOK_TABLE_NAME, null, values);
        database.close();
    }

    public void addActivity(MyActivity activity) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.SCHEDULE_ACTIVITY_NAME_COLUMN, activity.getActivityName());
        values.put(Constants.SCHEDULE_FROM_TIME_COLUMN, activity.getActivityFromTime());
        values.put(Constants.SCHEDULE_TO_TIME_COLUMN, activity.getActivityToTime());
        values.put(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN, activity.getActivityDayOfTheWeek());
        database.insert(Constants.SCHEDULE_TABLE_NAME, null, values);
        database.close();
    }

    //    get task
    public Person getPersonName(int personId) {
        SQLiteDatabase database = this.getReadableDatabase();
        Person person = new Person();
        Cursor cursor = database.query(Constants.PERSON_TABLE_NAME, new String[]{Constants.PERSON_NAME_COLUMN},
                Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(personId)}, null,
                null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            person.setPersonName(cursor.getString(cursor.getColumnIndex(Constants.PERSON_NAME_COLUMN)));
        }
        cursor.close();
        database.close();
        return person;
    }

    //    get all tasks
    public ArrayList<Task> getAllTodayTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> allTasks = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.TODAY_TASK_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setTaskId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    task.setTaskNote(cursor.getString(cursor.getColumnIndex(Constants.TASK_NOTE_COLUMN)));
                    task.setTaskState(cursor.getInt(cursor.getColumnIndex(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN)));
                    allTasks.add(task);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allTasks;
    }

    public ArrayList<Task> getAllShoppingTasks() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Task> allTasks = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.SHOPPING_TASK_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setTaskId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    task.setTaskNote(cursor.getString(cursor.getColumnIndex(Constants.TASK_NOTE_COLUMN)));
                    task.setTaskState(cursor.getInt(cursor.getColumnIndex(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN)));
                    allTasks.add(task);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allTasks;
    }

    public ArrayList<Plan> getAllPlans() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Plan> allPlans = new ArrayList<>();
        Cursor cursor = database.query(Constants.PLAN_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN,
                        Constants.PLAN_NOTE_COLUMN, Constants.PLAN_DATE_COLUMN, Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN}, null,
                null, null, null, Constants.PLAN_DATE_COLUMN + " ASC", null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Plan plan = new Plan();
                    plan.setPlanId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    plan.setPlanState(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN)));
                    plan.setPlanNote(cursor.getString(cursor.getColumnIndex(Constants.PLAN_NOTE_COLUMN)));
                    plan.setPlanDate(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DATE_COLUMN)));
                    plan.setPlanNotificationId(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN)));
                    allPlans.add(plan);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allPlans;
    }

    public ArrayList<Plan> getAllPlansAddedDate() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Plan> allPlans = new ArrayList<>();
        Cursor cursor = database.query(Constants.PLAN_TABLE_NAME, new String[]{Constants.PLAN_DATE_COLUMN, Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN}, null, null, null,
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

    public ArrayList<Person> getAllPeople() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Person> allPeople = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.PERSON_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Person person = new Person();
                    person.setPersonId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    person.setPersonName(cursor.getString(cursor.getColumnIndex(Constants.PERSON_NAME_COLUMN)));
                    person.setPersonMoneyAmount(cursor.getDouble(cursor.getColumnIndex(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN)));
                    person.setPersonMoneyGainOrLoss(cursor.getString(cursor.getColumnIndex(Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN)));
                    allPeople.add(person);
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
                        Constants.ITEM_TYPE_COLUMN, Constants.ITEM_MONEY_AMOUNT_COLUMN, Constants.ITEM_NAME_COLUMN, Constants.ITEM_ADDED_DATE_COLUMN, Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN},
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

    public ArrayList<Book> getAllBooks() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Book> allBooks = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.BOOK_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Book book = new Book();
                    book.setBookId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    book.setBookNumberOfPagesRead(cursor.getInt(cursor.getColumnIndex(Constants.BOOK_NUMBER_OF_PAGES_READ_COLUMN)));
                    book.setBookTotalBookPages(cursor.getInt(cursor.getColumnIndex(Constants.BOOK_TOTAL_PAGES_COLUMN)));
                    book.setBookName(cursor.getString(cursor.getColumnIndex(Constants.BOOK_NAME_COLUMN)));
                    allBooks.add(book);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allBooks;
    }

    public ArrayList<MyActivity> getAllActivitiesOfSpecificDay(String dayOfTheWeek) {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<MyActivity> allActivities = new ArrayList<>();
        Cursor cursor = database.query(Constants.SCHEDULE_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.SCHEDULE_ACTIVITY_NAME_COLUMN,
                        Constants.SCHEDULE_FROM_TIME_COLUMN, Constants.SCHEDULE_TO_TIME_COLUMN, Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN},
                Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN + " = ?", new String[]{dayOfTheWeek}, null,
                null, null, null);
//        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.SCHEDULE_TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    MyActivity activity = new MyActivity();
                    activity.setActivityId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.ID_COLUMN))));
                    activity.setActivityName(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_ACTIVITY_NAME_COLUMN)));
                    activity.setActivityFromTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_FROM_TIME_COLUMN)));
                    activity.setActivityToTime(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_TO_TIME_COLUMN)));
                    activity.setActivityDayOfTheWeek(cursor.getString(cursor.getColumnIndex(Constants.SCHEDULE_DAY_OF_THE_WEEK_COLUMN)));
                    allActivities.add(activity);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return allActivities;
    }

    //    delete task
    public int deleteTodayTask(int taskId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.TODAY_TASK_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(taskId)});
        database.close();
        return rowDeleted;
    }

    public int deleteShoppingTask(int taskId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.SHOPPING_TASK_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(taskId)});
        database.close();
        return rowDeleted;
    }

    public int deletePlan(int planId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.PLAN_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(planId)});
        database.close();
        return rowDeleted;
    }

    public int deletePerson(int personId) {
        SQLiteDatabase database = this.getWritableDatabase();
        int rowDeleted;
        rowDeleted = database.delete(Constants.PERSON_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(personId)});
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
        rowDeleted = database.delete(Constants.SCHEDULE_TABLE_NAME, Constants.ID_COLUMN + " = ?", new String[]{String.valueOf(activityId)});
        database.close();
        return rowDeleted;
    }

    //    update task
    public int updateTodayTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TASK_NOTE_COLUMN, task.getTaskNote());
        values.put(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN, task.getTaskState());
        int rowUpdated = database.update(Constants.TODAY_TASK_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(task.getTaskId())});
        database.close();
        return rowUpdated;
    }

    public int updateShoppingTask(Task task) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.TASK_NOTE_COLUMN, task.getTaskNote());
        values.put(Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN, task.getTaskState());
        int rowUpdated = database.update(Constants.SHOPPING_TASK_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(task.getTaskId())});
        database.close();
        return rowUpdated;
    }

    public int updatePlan(Plan plan) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.PLAN_NOTE_COLUMN, plan.getPlanNote());
        values.put(Constants.PLAN_STATE_CHECKED_OR_NOT_COLUMN, plan.getPlanState());
        values.put(Constants.PLAN_DATE_COLUMN, plan.getPlanDate());
        values.put(Constants.PLAN_NOTIFICATION_UNIQUE_ID_COLUMN, plan.getPlanNotificationId());
        int rowUpdated = database.update(Constants.PLAN_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(plan.getPlanId())});
        database.close();
        return rowUpdated;
    }

    public int updatePerson(Person person) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ID_COLUMN, person.getPersonId());
        values.put(Constants.PERSON_NAME_COLUMN, person.getPersonName());
        values.put(Constants.PERSON_MONEY_AMOUNT_YOU_WILL_GET_OR_GIVE_COLUMN, person.getPersonMoneyAmount());
        values.put(Constants.PERSON_MONEY_STATE_GAIN_OR_LOSS_COLUMN, person.getPersonMoneyGainOrLoss());
        int rowUpdated = database.update(Constants.PERSON_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(person.getPersonId())});
        database.close();
        return rowUpdated;
    }

    public int updateItem(Item item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ITEM_STATE_ARCHIVED_OR_NOT_COLUMN, item.getItemState());
        int rowUpdated = database.update(Constants.ITEM_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(item.getItemId())});
        database.close();
        return rowUpdated;
    }

    public int updateBook(Book book) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.BOOK_NUMBER_OF_PAGES_READ_COLUMN, book.getBookNumberOfPagesRead());
        int rowUpdated = database.update(Constants.BOOK_TABLE_NAME, values, Constants.ID_COLUMN + " = ? ",
                new String[]{String.valueOf(book.getBookId())});
        database.close();
        return rowUpdated;
    }

    public void updateActivitiesAfterSwap(ArrayList<MyActivity> activities) {
        SQLiteDatabase database = this.getWritableDatabase();
//        for deleting activities from table
        for (MyActivity activity : activities) {
            deleteActivity(activity.getActivityId());
        }
//        for adding again to database
        for (MyActivity activity : activities) {
            addActivity(activity);
        }
        database.close();
    }

    //    get total count of tasks
    public int totalTodayTasksCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.query(Constants.TODAY_TASK_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.TASK_NOTE_COLUMN,
                        Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN}, Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " = ?", new String[]{String.valueOf(0)}, null, null,
                null, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }

    public int totalShoppingTasksCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.query(Constants.SHOPPING_TASK_TABLE_NAME, new String[]{Constants.ID_COLUMN, Constants.TASK_NOTE_COLUMN,
                        Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN}, Constants.TASK_STATE_CHECKED_OR_NOT_COLUMN + " = ?", new String[]{String.valueOf(0)}, null, null,
                null, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
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

    public int totalBookCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.BOOK_TABLE_NAME, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }

    public int totalActivityCount() {
        SQLiteDatabase database = this.getReadableDatabase();
        int totalCount;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.SCHEDULE_TABLE_NAME, null);
        totalCount = cursor.getCount();
        cursor.close();
        database.close();
        return totalCount;
    }
}

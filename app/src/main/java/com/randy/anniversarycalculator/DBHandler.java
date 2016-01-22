package com.randy.anniversarycalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = "MYD - DBHandler";

    // database version
    private static final int database_VERSION = 2;
    // database name
    private static final String database_NAME = "ANNIVERSARYCOUNT";
    // table name
    private static final String table_anniversary = "ANNIVERSARY";
    // colunm name
    private static final String anni_ID = "ID";
    private static final String anni_DATE = "DATE";
    private static final String anni_SENTENCE = "SENTENCE";
    private static final String anni_NOTIFLAG = "NOTIFLAG";
    private static final String anni_NOTIINTERVAL = "NOTIINTERVAL";
    private static final String anni_HOUR = "HOUR";
    private static final String anni_MIN = "MINUTE";

    private static final String[] COLUMNS = { anni_ID, anni_DATE, anni_SENTENCE, anni_NOTIFLAG, anni_NOTIINTERVAL, anni_HOUR, anni_MIN };

    public DBHandler(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS ANNIVERSARY ( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE TEXT, "
                + "SENTENCE TEXT, "
                + "NOTIFLAG INTEGER, "
                + "NOTIINTERVAL INTEGER, "
                + "HOUR INTEGER, "
                + "MINUTE INTEGER)";
        db.execSQL(CREATE_BOOK_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop ANNIVERSARY table if already exists
        db.execSQL("DROP TABLE IF EXISTS ANNIVERSARY");
        this.onCreate(db);
    }


    public long insertItem(Item item) {
        long lResult = 0;

        // get reference of the ANNIVERSARYCOUNT database
        SQLiteDatabase db = this.getWritableDatabase();

        /*
        Log.d(TAG, "Date : " + item.getDate());
        Log.d(TAG, "Sentence : " + item.getSentence());
        Log.d(TAG, "NotiFlag : " + item.getNotiFlag());
        Log.d(TAG, "NotiInterval : " + item.getiNotiInterval());
        Log.d(TAG, "Hour : " + item.getHour());
        Log.d(TAG, "Minute : " + item.getMin());
        */

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(anni_DATE, item.getDate());
        values.put(anni_SENTENCE, item.getSentence());
        values.put(anni_NOTIFLAG, item.getNotiFlag());
        values.put(anni_NOTIINTERVAL, item.getiNotiInterval());
        values.put(anni_HOUR, item.getHour());
        values.put(anni_MIN, item.getMin());

        // insert anniversary
        lResult = db.insert(table_anniversary, null, values);

        Log.d(TAG, "Insert result : " + lResult);

        // close database transaction
        db.close();

        return lResult;
    }


    public Item readItem(int id) {
        // get reference of the ANNIVERSARYCOUNT database
        SQLiteDatabase db = this.getReadableDatabase();

        // get item query
        Cursor cursor = db.query(table_anniversary, // a. table
                COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);

        // if results !=null, parse the first one
        if (cursor != null)
            cursor.moveToFirst();

        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setDate(cursor.getString(1));
        item.setSentence(cursor.getString(2));
        item.setNotiFlag(Integer.parseInt(cursor.getString(3)));
        item.setiNotiInterval(Integer.parseInt(cursor.getString(4)));
        item.setHour(Integer.parseInt(cursor.getString(5)));
        item.setMin(Integer.parseInt(cursor.getString(6)));

        return item;
    }


    public ArrayList getAllItems() {
        ArrayList items = new ArrayList();

        // select book query
        String query = "SELECT  * FROM " + table_anniversary;

        // get reference of the ANNIVERSARYCOUNT database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        Item item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setDate(cursor.getString(1));
                item.setSentence(cursor.getString(2));
                item.setNotiFlag(Integer.parseInt(cursor.getString(3)));
                item.setiNotiInterval(Integer.parseInt(cursor.getString(4)));
                item.setHour(Integer.parseInt(cursor.getString(5)));
                item.setMin(Integer.parseInt(cursor.getString(6)));

                // Add anniversary to items
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }


    public int updateItem(int id, Item item) {

        // get reference of the ANNIVERSARYCOUNT database
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(anni_DATE, item.getDate());
        values.put(anni_SENTENCE, item.getSentence());
        values.put(anni_NOTIFLAG, item.getNotiFlag());
        values.put(anni_NOTIINTERVAL, item.getiNotiInterval());
        values.put(anni_HOUR, item.getHour());
        values.put(anni_MIN, item.getMin());

        // update
        int i = db.update(table_anniversary, values, anni_ID + " = ?", new String[] { String.valueOf(id) });

        db.close();
        return i;
    }


    // Deleting single item
    public void deleteItem(int id) {

        // get reference of the ANNIVERSARYCOUNT database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete book
        db.delete(table_anniversary, anni_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}
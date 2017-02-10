package com.example.praveenkn.ilovezappos.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Praveen Kn on 02-01-2017.
 */

/**
 * Class Name: {@link CustomDatabase}
 * Handles: Database operations for the suggestion items
 */
public class CustomDatabase {
    public static final String DB_NAME = "SUG_DB";
    public final static String SUGGESTION_TABLE = "SUG_TB";
    public final static String SEARCH_ID = "_id";
    public final static String SUGGESTION_VAL = "suggestion";
    private SQLiteDatabase db;
    private Helper helper;

    /**
     * Method Name: {@link CustomDatabase}
     * Functionality: Acts as a constructor which creates helper and database objects for CRUD operation
     * @param context
     */
    public CustomDatabase(Context context) {
        helper = new Helper(context, DB_NAME, null, 1);
        db = helper.getWritableDatabase();
    }

    /**
     * Method Name: insertValues
     * Functionality: insert the given string into the database
     * @param str
     * @return long
     */
    public long insertValues(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUGGESTION_VAL, str);
        return db.insert(SUGGESTION_TABLE, null, contentValues);
    }

    /**
     * Method Name: getValues
     * Functionality: Performs select operation for the given string with LIKE keyword for search operation
     * @param text
     * @return Cursor
     */
    public Cursor getValues(String text) {
        return db.query(SUGGESTION_TABLE, new String[]{SEARCH_ID, SUGGESTION_VAL},
                SUGGESTION_VAL + " LIKE '%" + text + "%'", null, null, null, null);
    }

    /**
     * Class Name: {@link Helper}
     * Handles: Creation of table
     */
    private class Helper extends SQLiteOpenHelper {

        public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SUGGESTION_TABLE + " (" +
                    SEARCH_ID + " integer primary key autoincrement, " + SUGGESTION_VAL + " text UNIQUE);");
            //Log.d("SUGGESTION", "DB CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
}

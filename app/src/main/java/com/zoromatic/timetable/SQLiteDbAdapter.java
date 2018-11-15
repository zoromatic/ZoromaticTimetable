/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zoromatic.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDbAdapter {

	static final String KEY_ROW_ID = "_id";
	static final String KEY_DAY_INDEX = "day_index";
	static final String KEY_CLASS_INDEX = "class_index";
	static final String KEY_START_HOUR = "start_hour";
	static final String KEY_START_MINUTE = "start_minute";
	static final String KEY_END_HOUR = "end_hour";
	static final String KEY_END_MINUTE = "end_minute";
	static final String KEY_DESCRIPTION = "description";

    private static final String LOG_TAG = "SQLiteDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
        "create table timetables (_id integer primary key autoincrement, "
        + "day_index integer not null, class_index integer not null, "
        + "start_hour integer not null, start_minute integer not null, "
        + "end_hour integer not null, end_minute integer not null, "
        + "description text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "timetables";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS timetables");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param context the Context within which to work
     */
    SQLiteDbAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Open the timetables database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public SQLiteDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mSQLiteDatabase = mDbHelper.getWritableDatabase();
        return this;
    }

    void close() {
        mDbHelper.close();
    }


    /**
     * Create a new timetable class using data provided. If the timetable class is
     * successfully created return the new rowId for that timetable class, otherwise return
     * a -1 to indicate failure.
     *
     * @param day_index day index
     * @param class_index class index
     * @param start_hour start hour
     * @param start_minute start minute
     * @param end_hour end hour
     * @param end_minute end minute
     * @param description description
     * @return rowId or -1 if failed
     */
    long createTimetableClass(long day_index, int class_index,
                              int start_hour, int start_minute,
                              int end_hour, int end_minute,
                              String description) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DAY_INDEX, day_index);
        initialValues.put(KEY_CLASS_INDEX, class_index);
        initialValues.put(KEY_START_HOUR, start_hour);
        initialValues.put(KEY_START_MINUTE, start_minute);
        initialValues.put(KEY_END_HOUR, end_hour);
        initialValues.put(KEY_END_MINUTE, end_minute);
        initialValues.put(KEY_DESCRIPTION, description);

        return mSQLiteDatabase.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the timetable class with the given rowId
     *
     * @param rowId id of timetable class to delete
     * @return true if deleted, false otherwise
     */
    boolean deleteTimetableClass(long rowId) {
        return mSQLiteDatabase.delete(DATABASE_TABLE, KEY_ROW_ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all timetables in the database
     *
     * @return Cursor over all timetables
     */
    Cursor fetchAllTimetables() {
        return mSQLiteDatabase.query(DATABASE_TABLE,
        		new String[] {KEY_ROW_ID, KEY_DAY_INDEX, KEY_CLASS_INDEX,
        		KEY_START_HOUR, KEY_START_MINUTE,
        		KEY_END_HOUR, KEY_END_MINUTE, KEY_DESCRIPTION},
        		null, null, null, KEY_DAY_INDEX + ", " + KEY_START_HOUR, null);
    }

    /**
     * Return a Cursor over timetable by day
     * @param day_index day index
     * @return Cursor over timetable by day
     */
    Cursor fetchTimetableByDay(long day_index) {
        return mSQLiteDatabase.query(true, DATABASE_TABLE,
        		new String[] {KEY_ROW_ID, KEY_DAY_INDEX, KEY_CLASS_INDEX,
        		KEY_START_HOUR, KEY_START_MINUTE,
        		KEY_END_HOUR, KEY_END_MINUTE, KEY_DESCRIPTION},
        		KEY_DAY_INDEX + "=" + day_index,
        		null, null, null, KEY_DAY_INDEX + ", " + KEY_START_HOUR, null);
    }

    /**
     * Return a Cursor positioned at the timetable class that matches the given rowId
     *
     * @param rowId id of timetable class to retrieve
     * @return Cursor positioned to matching timetable class, if found
     * @throws SQLException if location timetable class not be found/retrieved
     */
    Cursor fetchTimetableRow(long rowId) throws SQLException {
        Cursor cursor =
            mSQLiteDatabase.query(true, DATABASE_TABLE,
            		new String[] {KEY_ROW_ID, KEY_DAY_INDEX, KEY_CLASS_INDEX,
            		KEY_START_HOUR, KEY_START_MINUTE,
            		KEY_END_HOUR, KEY_END_MINUTE, KEY_DESCRIPTION},
            		KEY_ROW_ID + "=" + rowId,
            		null, null, null, KEY_DESCRIPTION, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Update the location using the details provided. The location to be updated is
     * specified using the rowId, and it is altered to use the values passed in
     *
     * @param rowId row id of class to update
     * @param day_index day index
     * @param class_index class index
     * @param start_hour start hour
     * @param start_minute start minute
     * @param end_hour end hour
     * @param end_minute end minute
     * @param description description
     * @return true if the location was successfully updated, false otherwise
     */
    boolean updateTimetableClass(long rowId, long day_index, int class_index,
                                 int start_hour, int start_minute,
                                 int end_hour, int end_minute,
                                 String description) {
        ContentValues args = new ContentValues();
        args.put(KEY_DAY_INDEX, day_index);
        args.put(KEY_CLASS_INDEX, class_index);
        args.put(KEY_START_HOUR, start_hour);
        args.put(KEY_START_MINUTE, start_minute);
        args.put(KEY_END_HOUR, end_hour);
        args.put(KEY_END_MINUTE, end_minute);
        args.put(KEY_DESCRIPTION, description);        

        return mSQLiteDatabase.update(DATABASE_TABLE, args, KEY_ROW_ID + "=" + rowId, null) > 0;
    }
}

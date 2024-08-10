package com.example.seritifikasilogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "apk_kampus.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String TAG = "DataHelper";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +  // Add UNIQUE constraint to prevent duplicate emails
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Register a new user
    public boolean registerUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Log.e(TAG, "Email or Password is empty");
            return false;
        }

        if (checkUser(email)) {
            Log.e(TAG, "User already exists");
            return false; // User already exists
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = -1;
        try {
            result = db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add user to database", e);
        } finally {
            db.close();
        }

        return result != -1; // Returns true if registration is successful
    }

    // Check if a user exists by email only
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + "=?";
        String[] selectionArgs = {email};

        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            count = cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return count > 0; // Returns true if the user exists
    }

    // Check if a user exists with email and password
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {email, password};

        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            count = cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return count > 0; // Returns true if the user exists
    }
}

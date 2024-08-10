package com.example.seritifikasilogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kampus.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "mahasiswa";
    public static final String COL_1 = "NOMOR";
    public static final String COL_2 = "NAMA";
    public static final String COL_3 = "TANGGAL_LAHIR";
    public static final String COL_4 = "JENIS_KELAMIN";
    public static final String COL_5 = "ALAMAT";
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating table " + TABLE_NAME);
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER," +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT)";
        db.execSQL(createTableQuery);
        Log.d(TAG, "Table " + TABLE_NAME + " created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d(TAG, "Table " + TABLE_NAME + " upgraded successfully");
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'", null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d(TAG, "Table " + tableName + " exists: " + exists);
        return exists;
    }

    public boolean insertData(int nomor, String nama, String tanggal_lahir, String jenis_kelamin, String alamat) {
        if (!isTableExists(TABLE_NAME)) {
            Log.e(TAG, "Table " + TABLE_NAME + " does not exist. Cannot insert data.");
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, nomor);
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, tanggal_lahir);
        contentValues.put(COL_4, jenis_kelamin);
        contentValues.put(COL_5, alamat);
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "Inserting data: " + contentValues.toString() + ", Result: " + result);
        return result != -1;
    }

    public Cursor getAllData() {
        if (!isTableExists(TABLE_NAME)) {
            Log.e(TAG, "Table " + TABLE_NAME + " does not exist. Cannot retrieve data.");
            return null;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        Log.d(TAG, "Retrieving all data from table " + TABLE_NAME);
        return cursor;
    }

    public boolean updateData(String nomor, String nama, String tanggal_lahir, String jenis_kelamin, String alamat) {
        if (!isTableExists(TABLE_NAME)) {
            Log.e(TAG, "Table " + TABLE_NAME + " does not exist. Cannot update data.");
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, nomor);
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, tanggal_lahir);
        contentValues.put(COL_4, jenis_kelamin);
        contentValues.put(COL_5, alamat);
        int result = db.update(TABLE_NAME, contentValues, "NOMOR = ?", new String[]{nomor});
        Log.d(TAG, "Updating data for NOMOR: " + nomor + ", Result: " + result);
        return result > 0;
    }

    public int deleteData(String nomor) {
        if (!isTableExists(TABLE_NAME)) {
            Log.e(TAG, "Table " + TABLE_NAME + " does not exist. Cannot delete data.");
            return 0;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "NOMOR = ?", new String[]{nomor});
        Log.d(TAG, "Deleting data for NOMOR: " + nomor + ", Result: " + result);
        return result;
    }

    public boolean checkIsi(String nomor) {
        if (!isTableExists(TABLE_NAME)) {
            Log.e(TAG, "Table " + TABLE_NAME + " does not exist. Cannot check data.");
            return false;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE NOMOR = ?", new String[]{nomor});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        Log.d(TAG, "Checking existence for NOMOR: " + nomor + ", Exists: " + exists);
        return exists;
    }
}

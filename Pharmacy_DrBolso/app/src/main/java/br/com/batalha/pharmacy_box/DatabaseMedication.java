package br.com.batalha.pharmacy_box;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseMedication extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "medication.db";
    public static final String TABLE_NAME = "medication_table";
    public static final String TABLE_COL_1 = "id";
    public static final String TABLE_COL_2 = "barcode";
    public static final String TABLE_COL_3 = "name_remedy";
    public static final String TABLE_COL_4 = "description";
    public static final String TABLE_COL_5 = "buy_date";
    public static final String TABLE_COL_6 = "due_date";

    public DatabaseMedication(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, BARCODE TEXT, NAME_REMEDY TEXT, DESCRIPTION TEXT, BUY_DATE TEXT, DUE_DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Performing Insert Operation on Database
    public boolean insertData(String barcode,String name_remedy, String description, String buy_date, String due_date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_COL_2, barcode);
        contentValues.put(TABLE_COL_3, name_remedy);
        contentValues.put(TABLE_COL_4, description);
        contentValues.put(TABLE_COL_5, buy_date);
        contentValues.put(TABLE_COL_6, due_date);

        // Insert contents into database
        long success = db.insert(TABLE_NAME, null, contentValues);

        // when query not inserted into database
        return success != -1;
    }

    // Read all Data from Database using CURSOR to pick one by one row
    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    // Search all Data from Database using CURSOR
    public Cursor searchData(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_COL_2,new String[]{barcode});
        return cursor;
    }

    // Update Data of Database table
    public boolean updateData(String id, String barcode, String name_remedy, String description, String buy_date, String due_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // Accessing content for overwrite
        contentValues.put(TABLE_COL_1, id);
        contentValues.put(TABLE_COL_2, barcode);
        contentValues.put(TABLE_COL_3, name_remedy);
        contentValues.put(TABLE_COL_4, description);
        contentValues.put(TABLE_COL_5, buy_date);
        contentValues.put(TABLE_COL_6, due_date);

        db.update(TABLE_NAME, contentValues, "BARCODE = ?", new String[]{barcode});
        return true;
    }

    // Delete Data from Database table
    public Integer deleteData(String barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "BARCODE = ?", new String[]{barcode});
    }
}
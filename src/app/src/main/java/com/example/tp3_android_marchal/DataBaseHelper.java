package com.example.tp3_android_marchal;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_bank";
    public static final String TABLE_CONFIGS = "configs";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String KEY_ID = "id";
    public static final String KEY_Name = "name";
    public static final String KEY_LastName = "lastname";
    public static final String KEY_Password = "password";
    public static final String KEY_AccountName = "accountName";
    public static final String KEY_Amount = "amount";
    public static final String KEY_Iban = "iban";
    public static final String KEY_Currency = "currency";

    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_CONFIGS = "CREATE TABLE "
            + TABLE_CONFIGS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Name + " TEXT,"
            + KEY_LastName + " TEXT," + KEY_Password + " TEXT );";

    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE "
            + TABLE_ACCOUNTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_AccountName + " TEXT,"
            + KEY_Amount + " TEXT," + KEY_Iban + " TEXT," + KEY_Currency + " TEXT );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONFIGS);
        db.execSQL(CREATE_TABLE_ACCOUNTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CONFIGS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ACCOUNTS + "'");
        onCreate(db);
    }
}
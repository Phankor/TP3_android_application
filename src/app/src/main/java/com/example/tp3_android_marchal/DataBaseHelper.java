package com.example.tp3_android_marchal;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database_bank";
    public static final String TABLE_CONFIGS = "configs";
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String KEY_ID = "id";
    public static final String KEY_Name = "name";
    public static final String KEY_LastName = "lastname";
    public static final String KEY_AccountName = "accountName";
    public static final String KEY_Amount = "amount";
    public static final String KEY_Iban = "iban";
    public static final String KEY_Currency = "currency";

    //initialize the database
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_CONFIGS = "CREATE TABLE "
            + TABLE_CONFIGS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Name + " TEXT,"
            + KEY_LastName +  " TEXT );";

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

    public boolean addOneAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, account.getID2());
        cv.put(KEY_AccountName, account.getAccountName());
        cv.put(KEY_Amount, account.getAmount());
        cv.put(KEY_Iban, account.getIban());
        cv.put(KEY_Currency, account.getCurrency());

        long insert = db.insert(TABLE_ACCOUNTS, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addOneConfig(Config config){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, config.getID());
        cv.put(KEY_Name, config.getName());
        cv.put(KEY_LastName, config.getLastname());

        long insert = db.insert(TABLE_CONFIGS, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public List<Account> getEveryAccount() {
        List<Account> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int accountID = cursor.getInt(0);
                String accountName = new String(Base64.getDecoder().decode(cursor.getString(1)), StandardCharsets.UTF_8);
                String accountAmount = new String(Base64.getDecoder().decode(cursor.getString(2)), StandardCharsets.UTF_8);
                String accountIban = new String(Base64.getDecoder().decode(cursor.getString(3)), StandardCharsets.UTF_8);
                String accountCurrency = new String(Base64.getDecoder().decode(cursor.getString(4)), StandardCharsets.UTF_8);

                Account newAccount = new Account(accountID, accountName, accountAmount, accountIban, accountCurrency);
                returnList.add(newAccount);
            } while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<Config> getEveryConfig() {
        List<Config> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_CONFIGS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int configID = cursor.getInt(0);
                String configName = new String(Base64.getDecoder().decode(cursor.getString(1)), StandardCharsets.UTF_8);
                String configLastName = new String(Base64.getDecoder().decode(cursor.getString(2)), StandardCharsets.UTF_8);

                Config newConfig = new Config(configID, configName, configLastName);
                returnList.add(newConfig);
            } while (cursor.moveToNext());
        }else{

        }
        cursor.close();
        db.close();
        return returnList;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONFIGS, null, null);
        db.delete(TABLE_ACCOUNTS, null, null);
    }
}
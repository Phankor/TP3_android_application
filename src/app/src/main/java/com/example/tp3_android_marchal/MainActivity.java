package com.example.tp3_android_marchal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;
import java.util.List;

import static com.example.tp3_android_marchal.DataBaseHelper.KEY_AccountName;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_Amount;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_Currency;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_ID;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_Iban;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_LastName;
import static com.example.tp3_android_marchal.DataBaseHelper.KEY_Name;
import static com.example.tp3_android_marchal.DataBaseHelper.TABLE_ACCOUNTS;
import static com.example.tp3_android_marchal.DataBaseHelper.TABLE_CONFIGS;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    // references to buttons and other controls on the layout
    Button btn_connection;
    Button btn_refresh;
    Button btn_reset;
    EditText et_connectionId;
    ListView lv_config;
    ListView lv_account;
    ArrayAdapter bankArrayAdapter;
    String id_Account = "none";

    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        btn_connection = findViewById(R.id.btn_connection);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_reset = findViewById(R.id.btn_reset);
        et_connectionId = findViewById(R.id.et_connectionId);
        lv_config = findViewById(R.id.lv_config);
        lv_account = findViewById(R.id.lv_account);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

        btn_connection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {                               //function for the button on the login screen (activity_main)
                EditText text = findViewById(R.id.et_connectionId);            //the text case id on the main screen
                String id = text.getText().toString();

                //Account account;
                //account = new Account(7, "fate", "7777", "1234", "euro");
                //Config config;
                //config = new Config(7, "Fujimaru", "Ritsuka", "super");
                //dataBaseHelper.addOneAccount(account);
                //dataBaseHelper.addOneConfig(config);

                try {
                    if (id_Account.equals("none")) {
                        id_Account = encryptThisString(id);
                        Toast.makeText(getApplicationContext(), "You are connected", Toast.LENGTH_SHORT).show();
                    }
                    if (encryptThisString(id).equals(id_Account)) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Id or Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Error in the connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataBaseHelper.deleteAll();
                AndroidNetworking.get(BuildConfig.accounts_URL)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    try{
                                        JSONObject jresponse = response.getJSONObject(i);
                                        if (encryptThisString(jresponse.getString("id")).equals(id_Account))
                                        {
                                            SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

                                            ContentValues cv = new ContentValues();

                                            cv.put(KEY_ID, jresponse.getString("id"));
                                            cv.put(KEY_AccountName, Base64.getEncoder().encodeToString(jresponse.getString("accountName").getBytes()));
                                            cv.put(KEY_Amount, Base64.getEncoder().encodeToString(jresponse.getString("amount").getBytes()));
                                            cv.put(KEY_Iban, Base64.getEncoder().encodeToString(jresponse.getString("iban").getBytes()));
                                            cv.put(KEY_Currency, Base64.getEncoder().encodeToString(jresponse.getString("currency").getBytes()));

                                            long insert = db.insert(TABLE_ACCOUNTS, null, cv);
                                            if (insert == -1) {
                                                return;
                                            }
                                        }
                                    }catch (JSONException e){
                                        Toast.makeText(getApplicationContext(), "Error in refresh", Toast.LENGTH_SHORT).show();
                                    }

                                    List<Account> accounts = dataBaseHelper.getEveryAccount();

                                    bankArrayAdapter = new ArrayAdapter<Account>(MainActivity.this, android.R.layout.simple_list_item_1, accounts);
                                    lv_account.setAdapter(bankArrayAdapter);
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });

                AndroidNetworking.get(BuildConfig.config_URL)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++)
                                {
                                    try{
                                        JSONObject jresponse = response.getJSONObject(i);
                                        if (encryptThisString(jresponse.getString("id")).equals(id_Account))
                                        {
                                            SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

                                            ContentValues cv = new ContentValues();

                                            cv.put(KEY_ID, jresponse.getString("id"));
                                            cv.put(KEY_Name, Base64.getEncoder().encodeToString(jresponse.getString("name").getBytes()));
                                            cv.put(KEY_LastName, Base64.getEncoder().encodeToString(jresponse.getString("lastname").getBytes()));

                                            long insert = db.insert(TABLE_CONFIGS, null, cv);
                                            if (insert == -1) {
                                                return;
                                            }
                                        }
                                    }catch (JSONException e){
                                        Toast.makeText(getApplicationContext(), "Error in refresh", Toast.LENGTH_SHORT).show();
                                    }

                                    List<Config> configs = dataBaseHelper.getEveryConfig();

                                    bankArrayAdapter = new ArrayAdapter<Config>(MainActivity.this, android.R.layout.simple_list_item_1, configs);
                                    lv_config.setAdapter(bankArrayAdapter);
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                            }
                        });
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                id_Account = "none";
                dataBaseHelper.deleteAll();

                Toast.makeText(getApplicationContext(), "Reset of the database", Toast.LENGTH_SHORT).show();

                List<Config> configs = dataBaseHelper.getEveryConfig();
                List<Account> accounts = dataBaseHelper.getEveryAccount();

                bankArrayAdapter = new ArrayAdapter<Account>(MainActivity.this, android.R.layout.simple_list_item_1, accounts);
                lv_account.setAdapter(bankArrayAdapter);
                bankArrayAdapter = new ArrayAdapter<Config>(MainActivity.this, android.R.layout.simple_list_item_1, configs);
                lv_config.setAdapter(bankArrayAdapter);
            }
        });

    }
}
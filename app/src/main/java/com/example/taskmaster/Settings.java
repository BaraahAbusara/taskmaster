package com.example.taskmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public static final String USERNAME = "username";
    private static final String TAG = "test";
    private EditText mUsernameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button button = findViewById(R.id.save_btn);

        button.setOnClickListener(view -> {
            mUsernameEditText = findViewById(R.id.usernameInput);
            String username = mUsernameEditText.getText().toString();
            saveUsername(username);
            Toast.makeText(this, "username added "+username, Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUsername (String username){

        //create sharedPreference and set up an editor
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
        //save
        preferenceEditor.putString(USERNAME,username);
        Log.i(TAG, "onCreate: set username "+username);

        preferenceEditor.apply();
    }


}
package com.example.taskmaster;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        changeTaskName();
        changeTaskBody();
        changeTaskState();
    }

    private void changeTaskName() {
        TextView mTitle = findViewById(R.id.title);
        String title = getIntent().getStringExtra("title");
        mTitle.setText(title);
    }
    private void changeTaskBody() {
        TextView mBody = findViewById(R.id.body);
        String body = getIntent().getStringExtra("body");
        mBody.setText(body);
    }
    private void changeTaskState() {
        TextView mState = findViewById(R.id.state);
        String state = getIntent().getStringExtra("state");
        mState.setText(state);
    }
}
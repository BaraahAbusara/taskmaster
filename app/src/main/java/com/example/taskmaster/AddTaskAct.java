package com.example.taskmaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class AddTaskAct extends AppCompatActivity {

    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onCreate: adding done");

        });
    }
}
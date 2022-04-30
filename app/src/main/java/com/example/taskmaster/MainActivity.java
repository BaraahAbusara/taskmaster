package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.taskmaster.MESSAGE";
    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this,Settings.class);
            startActivity(settingsIntent);
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent addTaskIntent = new Intent(this,AddTaskAct.class);
            startActivity(addTaskIntent);
        });

        Button allTasksButton =  findViewById(R.id.allTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent allTasksIntent = new Intent(this,AllTasksAct.class);
            startActivity(allTasksIntent);
        });

        Button plansDesignButton =  findViewById(R.id.plansDesign);
        plansDesignButton.setOnClickListener(view -> {

            Intent plansDesignIntent = new Intent(this,TaskDetailActivity.class);
            plansDesignIntent.putExtra("title","Plans Design");
            startActivity(plansDesignIntent);
        });

        Button elevationsButton =  findViewById(R.id.elevations);
        elevationsButton.setOnClickListener(view -> {

            Intent elevationsIntent = new Intent(this,TaskDetailActivity.class);
            elevationsIntent.putExtra("title","Elevations");
            startActivity(elevationsIntent);
        });

        Button sectionsButton =  findViewById(R.id.sections);
        sectionsButton.setOnClickListener(view -> {

            Intent sectionsIntent = new Intent(this,TaskDetailActivity.class);
            sectionsIntent.putExtra("title","Sections");
            startActivity(sectionsIntent);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeUsername();
    }
    private void changeUsername(){
//        receive the username from settings
        TextView mUsernameHeader = findViewById(R.id.usernameHeader);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsernameHeader.setText(sharedPreferences.getString(Settings.USERNAME,"My")+"'s Tasks");
        Log.i(TAG, "Main ->setUsername : "+mUsernameHeader);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

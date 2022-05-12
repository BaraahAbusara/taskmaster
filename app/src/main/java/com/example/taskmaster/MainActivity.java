package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.taskmaster.MESSAGE";
    private static final String TAG = "test";
//    private Task[] tasks = new Task[]{
//            new Task("Plans","design 3 plans with furniture","New"),
//            new Task("Elevations","design all elevations","Assigned"),
//            new Task("sections","create 2 sections passing by the stairs and the main door ","Complete"),
//            new Task("Shots","Render at least 2 interior shots and 2 exterior shots","New")
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--------------------------------------------------------

        Button settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
        });

        Button addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent addTaskIntent = new Intent(this, AddTaskAct.class);
            startActivity(addTaskIntent);
        });

        Button allTasksButton = findViewById(R.id.allTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent allTasksIntent = new Intent(this, AllTasksAct.class);
            startActivity(allTasksIntent);
        });
        List<Task> tasks = AppDatabase.getInstance(getApplicationContext()).userDao().getAllTasks();

                ListView tasksList = findViewById(R.id.tasksList);
        ArrayAdapter<Task> taskArrayAdapter = new ArrayAdapter<Task>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                tasks
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView title = (TextView) view.findViewById(android.R.id.text1);
                TextView body = (TextView) view.findViewById(android.R.id.text2);

                title.setText(tasks.get(position).getTitle());
                body.setText(tasks.get(position).getBody());

                return view;
            }
        };
        tasksList.setAdapter(taskArrayAdapter);

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent taskIntent = new Intent(getApplicationContext(),TaskDetailActivity.class);
                taskIntent.putExtra("title",tasks.get(i).getTitle());
                taskIntent.putExtra("body",tasks.get(i).getBody());
                taskIntent.putExtra("state",tasks.get(i).getState());
                startActivity(taskIntent);
            }
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

    @SuppressLint("SetTextI18n")
    private void changeUsername(){
//        receive the username from settings
        TextView mUsernameHeader = findViewById(R.id.usernameHeader);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsernameHeader.setText(sharedPreferences.getString(Settings.USERNAME,"My")+ " Tasks");
        Log.i(TAG, "Main ->setUsername : "+mUsernameHeader);
    }
//    Hard coded tasks buttons --------------------------

    //        Button plansDesignButton =  findViewById(R.id.plansDesign);
//        plansDesignButton.setOnClickListener(view -> {
//
//            Intent plansDesignIntent = new Intent(this,TaskDetailActivity.class);
//            plansDesignIntent.putExtra("title","Plans Design");
//            startActivity(plansDesignIntent);
//        });
//
//        Button elevationsButton =  findViewById(R.id.elevations);
//        elevationsButton.setOnClickListener(view -> {
//
//            Intent elevationsIntent = new Intent(this,TaskDetailActivity.class);
//            elevationsIntent.putExtra("title","Elevations");
//            startActivity(elevationsIntent);
//        });
//
//        Button sectionsButton =  findViewById(R.id.sections);
//        sectionsButton.setOnClickListener(view -> {
//
//            Intent sectionsIntent = new Intent(this,TaskDetailActivity.class);
//            sectionsIntent.putExtra("title","Sections");
//            startActivity(sectionsIntent);
//        });
}

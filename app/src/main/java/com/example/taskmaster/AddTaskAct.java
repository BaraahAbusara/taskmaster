package com.example.taskmaster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskAct extends AppCompatActivity {

    private static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //------------------------------------spinner
        final String[] mState = new String[]{"New", "Assigned", "In progress", "complete"};

        Spinner taskStateSelector = findViewById(R.id.task_state_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this ,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                mState
        );
        taskStateSelector.setAdapter(spinnerAdapter);
        taskStateSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //------------------------------------------
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {

            EditText titleField = findViewById(R.id.title);
            String title = titleField.getText().toString();

            EditText bodyField = findViewById(R.id.body);
            String body = bodyField.getText().toString();

            String state = taskStateSelector.getSelectedItem().toString();

            Task newTask = new Task(title,body,state);
            Long newTaskId = AppDatabase.getInstance(getApplicationContext()).userDao().insertTask(newTask);


            Toast.makeText(this, "Task Added : "+newTask.getBody(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onCreate: adding done -> "+newTask.getId());

        });
    }

}
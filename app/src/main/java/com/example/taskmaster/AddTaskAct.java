package com.example.taskmaster;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;

public class AddTaskAct extends AppCompatActivity {

    private static final String TAG = "test";

    private Handler handler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        configureAmplify();

        //------------------------------------spinner

            final String[] mState = new String[]{"New", "Assigned", "In progress", "complete"};

            Spinner taskStateSelector = findViewById(R.id.task_state_spinner);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                    this,
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

        //------------------------------------------saving the task
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
        // take the data
            EditText titleField = findViewById(R.id.title);
            String title = titleField.getText().toString();

            EditText bodyField = findViewById(R.id.body);
            String body = bodyField.getText().toString();

            String state = taskStateSelector.getSelectedItem().toString();


            // create a task
            com.amplifyframework.datastore.generated.model.Task newTask = com.amplifyframework.datastore.generated.model.Task.builder()
                    .title(title)
                    .body(body)
                    .status(state)
                    .build();

            Amplify.API.query(ModelMutation.create(newTask),
                    success-> Log.i(TAG, "query created"),
                    error -> Log.e(TAG,"Error",error)
            );

            // save the task locally
            Amplify.DataStore.save(
                    newTask,
                    success -> {
                        Log.i(TAG, "onCreate: saving in datastore succeed ");
                    } ,
                    fail->{
                        Log.i(TAG, "onCreate: saving in datastore failed ");
                    }

            );

            // saves to the backend
            Amplify.API.mutate(ModelMutation.create(newTask)
                    , success -> {
                        Log.i(TAG, "onCreate: saving in API mutate");

                    }
                    , fail -> {
                        Log.i(TAG, "onCreate: NOT saving in API mutate");

                    }
            );

            Amplify.DataStore.observe(com.amplifyframework.datastore.generated.model.Task.class,
                    started -> {
                        Log.i(TAG, "onCreate: observation began");
                    },
                    change -> {
                        Log.i(TAG, change.item().toString());

                        Bundle bundle = new Bundle();
                        bundle.putString("data",change.item().toString());
                        Message message = new Message();
                        message.setData(bundle);

                        handler.sendMessage(message);
                    },
                    failure -> {
                        Log.e(TAG, "onCreate: Observation failed");
                    },
                    () -> {
                        Log.i(TAG, "onCreate: Observation complete");
                    }
            );

            Log.i(TAG, "onCreate: adding done -> "+newTask.getId());

        });



    }

    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException e) {
            Log.e(TAG, "Could not initialize Amplify", e);
        }
    }

}
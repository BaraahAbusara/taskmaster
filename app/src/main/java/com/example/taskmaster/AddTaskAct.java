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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class AddTaskAct extends AppCompatActivity {
    public static final String TEAM_ID = "teamId";
    private static final String TAG = "test";
    private static final String DATA = "data";
    Spinner taskStateSelector ;
    Spinner taskTeamSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        {
//                design = Team.builder()
//                        .name("Design")
//                        .build();
//                saveTeamInAPI(design);
//
//                render = Team.builder()
//                        .name("Render")
//                        .build();
//
//                poster = Team.builder()
//                        .name("Poster")
//                        .build();
//
//                saveTeamInAPI(design);
//                saveTeamInAPI(render);
//                saveTeamInAPI(poster);
//
//        }
//        setTeamSpinner();

        final String[] mTeam = new String[]{"Design", "Render", "Poster"};

        taskTeamSelector = findViewById(R.id.task_team_spinner);
        ArrayAdapter<String> teamSpinnerAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                mTeam
        );

        taskTeamSelector.setAdapter(teamSpinnerAdapter);
        taskTeamSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        setStateSpinner();
        final String[] mState = new String[]{"New", "Assigned", "In progress", "complete"};

        Spinner taskStateSelector = findViewById(R.id.task_state_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
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

        Button button = findViewById(R.id.button);

        //find the team
        button.setOnClickListener(view -> {
            String team = taskTeamSelector.getSelectedItem().toString();
            Amplify.API.query(ModelQuery
                            .list(Team.class, Team.NAME.eq(team)),
                    success->{
                        for (Team curTeam :
                                success.getData()) {
//                                        saveTaskToAPI(curTeam);
                            EditText titleField = findViewById(R.id.title);
                            String title = titleField.getText().toString();

                            EditText bodyField = findViewById(R.id.body);
                            String body = bodyField.getText().toString();

//                            taskStateSelector = findViewById(R.id.task_state_spinner);
                            String state = taskStateSelector.getSelectedItem().toString();

                            com.amplifyframework.datastore.generated.model.Task newTask = com.amplifyframework.datastore.generated.model.Task.builder()
                                    .title(title)
                                    .body(body)
                                    .status(state)
                                    .teamTasksId(curTeam.getId())
                                    .build();

                            Amplify.DataStore.save(newTask,
                                    saved -> Log.i(TAG, "Saved item: " + newTask.getTitle()),
                                    notSaved -> Log.e(TAG, "Could not save item to DataStore", notSaved)
                            );

                            Amplify.API.mutate(
                                    ModelMutation.create(newTask),
                                    taskSaved-> {
                                        Log.i(TAG, "Saved task: " + taskSaved.getData().getTeamTasksId());

//                    Bundle bundle = new Bundle();
//                    bundle.putString(TEAM_ID, taskSaved.getData().getTeamTasksId());
//
//                    Message message = new Message();
//                    message.setData(bundle);
//
//                    handler.sendMessage(message);
                                    },
                                    error -> {
                                        Log.e(TAG, "Could not save task to API", error);
                                    }
                            );




                        }
                        Log.i(TAG, "onCreate: looking for ->"+success.getData().getItems());

                        Log.i(TAG, "onCreate: team is found");
                    },
                    Fail->{
                        Log.i(TAG, "onCreate: can't find team");
                    }

            );

            Amplify.DataStore.observe(com.amplifyframework.datastore.generated.model.Task.class,
                    started -> {
                        Log.i(TAG, "onCreate: observation began");
                    },
                    change -> {
                        Log.i(TAG, change.item().toString());

//                        Bundle bundle = new Bundle();
//                        bundle.putString("data",change.item().toString());
//                        Message message = new Message();
//                        message.setData(bundle);
//
//                        handler.sendMessage(message);
                    },
                    failure -> {
                        Log.e(TAG, "onCreate: Observation failed");
                    },
                    () -> {
                        Log.i(TAG, "onCreate: Observation complete");
                    }
            );

        });
    }

    private void setTeamSpinner (){

        final String[] mState = new String[]{"Design", "Render", "Poster"};

        taskTeamSelector = findViewById(R.id.task_team_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                mState
        );

        taskTeamSelector.setAdapter(spinnerAdapter);
        taskTeamSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void setStateSpinner (){
        final String[] mState = new String[]{"New", "Assigned", "In progress", "complete"};

        Spinner taskStateSelector = findViewById(R.id.task_state_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
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
    }


    void saveTeamInAPI (Team team ){
        Amplify.API.mutate(
                ModelMutation.create(team),
                success->{
                    Log.i(TAG, "saveTeamInAPI: Team saved");
                },
                fail->{
                    Log.i(TAG, "saveTeamInAPI: failed to save the team");
                });
    }

    void saveTaskToAPI(Team team){
        EditText titleField = findViewById(R.id.title);
        String title = titleField.getText().toString();

        EditText bodyField = findViewById(R.id.body);
        String body = bodyField.getText().toString();

        taskStateSelector = findViewById(R.id.task_state_spinner);
        String state = taskStateSelector.getSelectedItem().toString();

        com.amplifyframework.datastore.generated.model.Task newTask = com.amplifyframework.datastore.generated.model.Task.builder()
                .title(title)
                .body(body)
                .status(state)
                .teamTasksId(team.getId())
                .build();

        Amplify.DataStore.save(newTask,
                success -> Log.i(TAG, "Saved item: " + success.item().getTitle()),
                error -> Log.e(TAG, "Could not save item to DataStore", error)
        );

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                taskSaved-> {
                    Log.i(TAG, "Saved task: " + taskSaved.getData().getTeamTasksId());

//                    Bundle bundle = new Bundle();
//                    bundle.putString(TEAM_ID, taskSaved.getData().getTeamTasksId());
//
//                    Message message = new Message();
//                    message.setData(bundle);
//
//                    handler.sendMessage(message);
                },
                error -> {
                    Log.e(TAG, "Could not save task to API", error);
                }
        );

    }
}



package com.example.taskmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

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
                            EditText titleField = findViewById(R.id.title);
                            String title = titleField.getText().toString();

                            EditText bodyField = findViewById(R.id.body);
                            String body = bodyField.getText().toString();

                            String state = taskStateSelector.getSelectedItem().toString();

                            com.amplifyframework.datastore.generated.model.Task newTask = com.amplifyframework.datastore.generated.model.Task.builder()
                                    .title(title)
                                    .body(body)
                                    .status(state)
                                    .teamTasksId(curTeam.getId())
                                    .build();

                            Amplify.DataStore.save(newTask,
                                    saved -> {},
                                    notSaved ->{}
                            );

                            Amplify.API.mutate(
                                    ModelMutation.create(newTask),
                                    taskSaved-> { },
                                    error -> { }
                            );
                        }
                    },
                    Fail->{
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
                },
                fail->{
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
                success ->{},
                error -> {}
        );

        Amplify.API.mutate(
                ModelMutation.create(newTask),
                taskSaved-> {
                },
                error -> {
                }
        );

    }
}



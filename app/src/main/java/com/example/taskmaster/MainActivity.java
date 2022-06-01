package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    public static final String EXTRA_MESSAGE = "com.example.taskmaster.MESSAGE";
    private static final String TAG = "test";
    private Handler handler;
    List<com.amplifyframework.datastore.generated.model.Task> tasks= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureAmplify();

        //-------------------------------------------------------- Buttons
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

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeUsername();
        changeTeamName();
        //printing tasks
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = sharedPreferences.getString(Settings.TEAM_NAME,"All");

        Amplify.API.query(ModelQuery
                        .list(Team.class, Team.NAME.eq(teamName)),
                success-> {
                    for (Team curTeam :
                            success.getData()) {
                        //teamId = curTeam.getId();
//                        ---------------------------------------------
                        //         take from the API
                        tasks = findTasksAPI(curTeam.getId());

                        // handler for showing tasks list from the API
                        handler = new Handler(Looper.getMainLooper() , msg -> {

                            ListView tasksList = findViewById(R.id.tasksList);
                            ArrayAdapter<com.amplifyframework.datastore.generated.model.Task> taskArrayAdapter = new ArrayAdapter<Task>(
                                    getApplicationContext(),
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
                                    taskIntent.putExtra("state",tasks.get(i).getStatus());
                                    startActivity(taskIntent);
                                }
                            });
                            return true  ;
                        });
//                        --------------------------------------------- picking tasks and rendering  them
                    }
                },
                error -> {
                    Log.e(TAG, "Could not save task to API", error);
                });


        Log.i(TAG, "tasks list from onResume");
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

    private void findTeamId(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = sharedPreferences.getString(Settings.TEAM_NAME,"All");
        String[] teamId = new String[1] ;
        Amplify.API.query(ModelQuery
                        .list(Team.class, Team.NAME.eq(teamName)),
                success-> {
                    for (Team curTeam :
                            success.getData()) {
                        teamId[0] = curTeam.getId();
                    }
                },
                    error -> {
                        Log.e(TAG, "Could not save task to API", error);
                    });


    }

    private void  changeTeamName(){
        //        receive the team name from settings
        TextView mTeamName = findViewById(R.id.team_name);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mTeamName.setText(sharedPreferences.getString(Settings.TEAM_NAME,"All")+ " Tasks");
        Log.i(TAG, "Main ->changeTeamName : "+mTeamName);

    }

    private void changeUsername(){
//        receive the username from settings
        TextView mUsernameHeader = findViewById(R.id.usernameHeader);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsernameHeader.setText(sharedPreferences.getString(Settings.USERNAME,"My")+ " Tasks");
        Log.i(TAG, "Main ->setUsername : "+mUsernameHeader);
    }

    private List<com.amplifyframework.datastore.generated.model.Task> findTasksAPI (String teamId){
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    tasks.clear();
                    ArrayList <Task> allTasks = new ArrayList<>();
                    Log.i(TAG, "findTasksAPI: ->"+teamId);

                    if (success.hasData()) {
                        for (Task task : success.getData()) {
                            if(task.getTeamTasksId()!=null)
                            if(task.getTeamTasksId().equals(teamId))
                                tasks.add(task);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("data","Done");
                        Message message = new Message();
                        message.setData(bundle);
                        handler.sendMessage(message);

                    }
                    },
                notFound->{
                    Log.i(TAG, "onCreate: can't find team in database");
                });
    return tasks;
    }

    private List<com.amplifyframework.datastore.generated.model.Task> findTasksDataStore (){
        List<com.amplifyframework.datastore.generated.model.Task> tasks = new ArrayList<>();

        Amplify.DataStore.query(
                com.amplifyframework.datastore.generated.model.Task.class,
                foundTasks->{
                    while (foundTasks.hasNext())
                    {
                        com.amplifyframework.datastore.generated.model.Task curTask = foundTasks.next();
                        tasks.add(curTask);
                    }

                },
                notFound->{
                    Log.i(TAG, "onCreate: can't return tasks from database");
                }
        );
        return tasks ;
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

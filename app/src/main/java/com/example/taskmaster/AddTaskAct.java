package com.example.taskmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public class AddTaskAct extends AppCompatActivity {
    private static final String TAG = "test";
    public static final int REQUEST_CODE = 123;

    Spinner taskStateSelector ;
    Spinner taskTeamSelector;
    String image ="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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

        Button uploadImageBtn = findViewById(R.id.addImageBtn);
        uploadImageBtn.setOnClickListener(view -> {
            pictureUpload();
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

                            //----------------------------------------------------------------

                            Log.i(TAG, "onCreate: imageKey is => "+image);

                            com.amplifyframework.datastore.generated.model.Task newTask = com.amplifyframework.datastore.generated.model.Task.builder()
                                    .title(title)
                                    .body(body)
                                    .status(state)
                                    .teamTasksId(curTeam.getId())
                                    .imagePath(image) // image key not path
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

    private void pictureUpload() {
        // Launches photo picker in single-select mode.
        // This means that the user can select one photo or video.
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            Log.e(TAG, "onActivityResult: Error getting image from device");
            return;
        }

        switch(requestCode) {
            case REQUEST_CODE:
                // Get photo picker response for single select.
                Uri currentUri = data.getData();

                // Do stuff with the photo/video URI.
                Log.i(TAG, "onActivityResult: the uri is => " + currentUri);

                try {
                    Bitmap bitmap = getBitmapFromUri(currentUri);

                    File file = new File(getApplicationContext().getFilesDir(), currentUri.getLastPathSegment());
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();

                    // upload to s3
                    // uploads the file
                    Amplify.Storage.uploadFile(
                            currentUri.getLastPathSegment(),
                            file,
                            result -> {
                                Log.i(TAG, "Successfully uploaded: " + result.getKey());
                                image = result.getKey();
                                Toast.makeText(getApplicationContext(), "image is uploaded", Toast.LENGTH_SHORT).show();
                            },
                            storageFailure -> Log.e(TAG, "Upload failed", storageFailure)
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }
    }

//    https://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }

}



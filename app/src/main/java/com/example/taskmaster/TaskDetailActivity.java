package com.example.taskmaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "test";
    String imageKey ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        changeTaskName();
        changeTaskBody();
        changeTaskState();

        imageKey = getIntent().getStringExtra("imageKey");

        if(imageKey!=null)
        pictureDownloadAndView(imageKey);
        else
            Log.i(TAG, "onCreate: imageKey is null->"+imageKey);

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
    private void pictureDownloadAndView(String imageKey) {

        Amplify.Storage.downloadFile(
                imageKey,
                new File( getApplicationContext().getFilesDir() + "/" + imageKey),
                result -> {
                    Log.i(TAG, "The root path is: " + getApplicationContext().getFilesDir());
                    Log.i(TAG, "Successfully downloaded: " + result.getFile().getName());

                    //https://iqcode.com/code/other/how-to-get-bitmap-from-file-in-android
                    // Rendering the image must be in here
                    ImageView image = findViewById(R.id.task_image);
                    Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getFilesDir()+"/"+result.getFile().getName());
                    image.setImageBitmap(bitmap);


                },
                error -> Log.e(TAG,  "Download Failure", error)
        );
    }
}
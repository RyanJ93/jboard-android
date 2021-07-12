package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jboard.task.UserGetTask;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserGetTask userGetTask = new UserGetTask(this);
        userGetTask.execute();
    }

    @Override
    public void onBackPressed(){}
}
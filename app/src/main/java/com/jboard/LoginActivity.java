package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.jboard.task.UserLoginTask;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClick(View view){
        int id = getResources().getIdentifier("account", "id", getPackageName());
        EditText editText = (EditText)this.findViewById(id);
        String account = editText.getText().toString();
        id = getResources().getIdentifier("password", "id", getPackageName());
        editText = (EditText)this.findViewById(id);
        String password = editText.getText().toString();
        UserLoginTask userLoginTask = new UserLoginTask(this, account, password);
        userLoginTask.execute();
    }
}
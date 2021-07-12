package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.jboard.task.UserLoginTask;

public class LoginActivity extends AppCompatActivity {
    private void onLoginButtonClick(View view){
        EditText loginPassword = this.findViewById(R.id.loginPassword);
        EditText loginAccount = this.findViewById(R.id.loginAccount);
        String password = loginPassword.getText().toString();
        String account = loginAccount.getText().toString();
        UserLoginTask userLoginTask = new UserLoginTask(this, account, password);
        userLoginTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.findViewById(R.id.loginButton).setOnClickListener(this::onLoginButtonClick);
    }

    @Override
    public void onBackPressed(){}
}
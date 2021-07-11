package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.LoginActivity;
import com.jboard.exception.*;
import com.jboard.service.UserService;

public class UserLogoutTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            UserService userService = new UserService();
            userService.logout();
            Intent intent = new Intent(this.context, LoginActivity.class);
            this.context.startActivity(intent);
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public UserLogoutTask(Context context){
        this.context = context;
    }
}

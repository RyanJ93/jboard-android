package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.LoginActivity;
import com.jboard.exception.*;
import com.jboard.model.User;
import com.jboard.service.UserService;

public class UserGetTask extends AsyncTask<Void, Void, Void> {
    private boolean isAuthenticated = false;
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            UserService userService = new UserService();
            userService.setContext(this.context).loadUserToken();
            this.isAuthenticated = UserService.isUserTokenDefined();
            if ( this.isAuthenticated ){
                User authenticatedUser = userService.fetchAuthenticatedUser();
                this.isAuthenticated = authenticatedUser != null;
            }
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    @Override
    public void onPostExecute(Void param){
        if ( this.isAuthenticated ){
            AvailableLessonListTask availableLessonListTask = new AvailableLessonListTask(this.context);
            availableLessonListTask.execute();
        }else{
            Intent intent = new Intent(this.context, LoginActivity.class);
            this.context.startActivity(intent);
        }
    }

    public UserGetTask(Context context){
        this.context = context;
    }
}

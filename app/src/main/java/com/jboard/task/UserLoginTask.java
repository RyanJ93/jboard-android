package com.jboard.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.jboard.exception.*;
import com.jboard.service.UserService;

public class UserLoginTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final String account;
    private final String password;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            UserService userService = new UserService();
            userService.setContext(this.context).login(this.account, this.password);
        }catch(OperationalException | InvalidInputException | UserNotFoundException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ((Activity)this.context).runOnUiThread(() -> {
                AlertDialog alertDialog = new AlertDialog.Builder(this.context).create();
                alertDialog.setMessage("Provided credentials are not valid.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (DialogInterface dialog, int which) -> alertDialog.dismiss());
                alertDialog.show();
            });
        }
        return null;
    }

    @Override
    public void onPostExecute(Void param){
        if ( UserService.isUserTokenDefined() ){
            AvailableLessonListTask availableLessonListTask = new AvailableLessonListTask(this.context);
            availableLessonListTask.execute();
        }
    }

    public UserLoginTask(Context context, String account, String password){
        this.context = context;
        this.account = account;
        this.password = password;
    }
}

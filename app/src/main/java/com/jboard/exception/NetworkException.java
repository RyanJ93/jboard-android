package com.jboard.exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

public class NetworkException extends JBoardException {
    private AsyncTask<Void, Void, Void> task;

    public NetworkException() {
        super();
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkException(AsyncTask<Void, Void, Void> task){
        super();
        this.task = task;
    }

    public NetworkException setTask(AsyncTask<Void, Void, Void> task){
        this.task = task;
        return this;
    }

    public AsyncTask<Void, Void, Void> getTask(){
        return this.task;
    }

    public void showAlert(Context context){
        if ( this.task == null ){
            super.showAlert(context);
        }else{
            Activity activity = (Activity)context;
            activity.runOnUiThread(() -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("An error occurred while contacting the server, please retry later.");
                alertDialogBuilder.setNegativeButton(android.R.string.ok, (dialog, whichButton) -> {});
                alertDialogBuilder.show();
            });
        }
    }
}

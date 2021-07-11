package com.jboard.exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class JBoardException extends Exception {
    public JBoardException() {
        super();
    }

    public JBoardException(String message) {
        super(message);
    }

    public JBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorMessage(){
        return "Unexpected error occurred, please retry later.";
    }

    public void showAlert(Context context){
        Activity activity = (Activity)context;
        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(this.getErrorMessage());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (DialogInterface dialog, int which) -> alertDialog.dismiss());
            alertDialog.show();
        });
    }
}

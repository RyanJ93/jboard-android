package com.jboard.exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Collection;
import java.util.HashMap;

public class InvalidInputException extends JBoardException {
    private static final String DEFAULT_ERROR_MESSAGE = "Invalid data provided.";

    private HashMap<String, String> validationMessages;

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(String message, HashMap<String, String> validationMessages) {
        super(message);
        this.validationMessages = validationMessages;
    }

    public void showAlert(Context context){
        String errorMessage = InvalidInputException.DEFAULT_ERROR_MESSAGE;
        if ( this.validationMessages != null && this.validationMessages.size() > 0 ){
            Collection<String> messages = this.validationMessages.values();
            StringBuilder stringBuilder = new StringBuilder();
            for ( String message : messages ){
                stringBuilder.append(message).append("\n");
            }
            errorMessage = stringBuilder.toString();
        }
        String finalErrorMessage = errorMessage;
        Activity activity = (Activity)context;
        activity.runOnUiThread(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(finalErrorMessage);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (DialogInterface dialog, int which) -> alertDialog.dismiss());
            alertDialog.show();
        });
    }

    public HashMap<String, String> getValidationMessages(){
        return this.validationMessages;
    }
}

package com.jboard.exception;

import android.content.Context;
import android.content.Intent;
import com.jboard.LoginActivity;

public class UnauthorizedException extends JBoardException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public void showLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

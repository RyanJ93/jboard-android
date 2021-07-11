package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.LessonsActivity;
import com.jboard.exception.*;
import com.jboard.model.LessonList;
import com.jboard.service.LessonService;

public class LessonListTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            LessonService lessonService = new LessonService();
            LessonList lessonList = lessonService.list();
            Intent intent = new Intent(this.context, LessonsActivity.class);
            intent.putExtra("lessonList", lessonList);
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

    public LessonListTask(Context context){
        this.context = context;
    }
}

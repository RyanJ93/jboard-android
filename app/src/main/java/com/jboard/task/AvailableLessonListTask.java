package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.AvailableLessonList;
import com.jboard.exception.*;
import com.jboard.model.AvailableCourseList;
import com.jboard.service.LessonService;

public class AvailableLessonListTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            LessonService lessonService = new LessonService();
            AvailableCourseList availableCourseList = lessonService.getAvailableCourses();
            Intent intent = new Intent(this.context, AvailableLessonList.class);
            intent.putExtra("availableCourseList", availableCourseList);
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

    public AvailableLessonListTask(Context context){
        this.context = context;
    }
}

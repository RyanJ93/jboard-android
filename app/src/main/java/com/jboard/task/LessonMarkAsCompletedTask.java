package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import com.jboard.R;
import com.jboard.adapter.ActiveLessonAdapter;
import com.jboard.exception.*;
import com.jboard.model.Lesson;
import com.jboard.service.LessonService;

public class LessonMarkAsCompletedTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Lesson lesson;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            LessonService lessonService = new LessonService(this.lesson);
            lessonService.mark(true);
            Activity activity = (Activity)this.context;
            activity.runOnUiThread(() -> {
                ListView activeLessonList = activity.findViewById(R.id.activeLessonList);
                ((ActiveLessonAdapter)activeLessonList.getAdapter()).moveLessonToCompletedList(this.lesson.getID());
            });
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public LessonMarkAsCompletedTask(Lesson lesson, Context context){
        this.context = context;
        this.lesson = lesson;
    }
}

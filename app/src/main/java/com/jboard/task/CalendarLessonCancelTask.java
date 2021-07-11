package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.jboard.adapter.LessonCalendarAdapter;
import com.jboard.exception.*;
import com.jboard.model.Lesson;
import com.jboard.service.LessonService;

public class CalendarLessonCancelTask extends AsyncTask<Void, Void, Void> {
    private final LessonCalendarAdapter lessonCalendarAdapter;
    private final Context context;
    private final Lesson lesson;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            LessonService lessonService = new LessonService(this.lesson);
            lessonService.cancel();
            ((Activity)this.context).runOnUiThread(() -> this.lessonCalendarAdapter.removeLesson(this.lesson));
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public CalendarLessonCancelTask(Lesson lesson, Context context, LessonCalendarAdapter lessonCalendarAdapter){
        this.lessonCalendarAdapter = lessonCalendarAdapter;
        this.context = context;
        this.lesson = lesson;
    }
}

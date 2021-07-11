package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.jboard.adapter.LessonCalendarAdapter;
import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.model.Lesson;
import com.jboard.model.LessonSlot;
import com.jboard.model.Teacher;
import com.jboard.service.LessonService;

public class LessonCreateTask extends AsyncTask<Void, Void, Void> {
    private final LessonCalendarAdapter lessonCalendarAdapter;
    private final LessonSlot lessonSlot;
    private final Teacher teacher;
    private final Context context;
    private final Course course;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            LessonService lessonService = new LessonService();
            Lesson lesson = lessonService.create(this.lessonSlot, this.teacher, this.course);
            ((Activity)this.context).runOnUiThread(() -> this.lessonCalendarAdapter.addLesson(lesson, lessonSlot));
        }catch(OperationalException | InvalidInputException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public LessonCreateTask(LessonSlot lessonSlot, Teacher teacher, Course course, Context context, LessonCalendarAdapter lessonCalendarAdapter){
        this.lessonCalendarAdapter = lessonCalendarAdapter;
        this.lessonSlot = lessonSlot;
        this.teacher = teacher;
        this.context = context;
        this.course = course;
    }
}

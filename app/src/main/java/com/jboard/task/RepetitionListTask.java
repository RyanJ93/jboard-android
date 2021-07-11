package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.RepetitionsManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.CourseList;
import com.jboard.model.RepetitionList;
import com.jboard.model.TeacherList;
import com.jboard.service.CourseService;
import com.jboard.service.RepetitionService;
import com.jboard.service.TeacherService;

public class RepetitionListTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            RepetitionService repetitionService = new RepetitionService();
            TeacherService teacherService = new TeacherService();
            CourseService courseService = new CourseService();
            RepetitionList repetitionList = repetitionService.list();
            TeacherList teacherList = teacherService.list();
            CourseList courseList = courseService.list();
            Intent intent = new Intent(this.context, RepetitionsManagementActivity.class);
            intent.putExtra("repetitionList", repetitionList);
            intent.putExtra("teacherList", teacherList);
            intent.putExtra("courseList", courseList);
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

    public RepetitionListTask(Context context){
        this.context = context;
    }
}

package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.CoursesManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.CourseList;
import com.jboard.service.CourseService;

public class CourseListTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            CourseService courseService = new CourseService();
            CourseList courseList = courseService.list();
            Intent intent = new Intent(this.context, CoursesManagementActivity.class);
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

    public CourseListTask(Context context){
        this.context = context;
    }
}

package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.CourseCreationActivity;
import com.jboard.CoursesManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.model.CourseList;
import com.jboard.service.CourseService;

public class CourseCreateTask extends AsyncTask<Void, Void, Void> {
    private final CourseList courseList;
    private final Context context;
    private final String title;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            CourseService courseService = new CourseService();
            Course course = courseService.create(this.title);
            this.courseList.getCourseList().add(course);
            Intent intent = new Intent(this.context, CoursesManagementActivity.class);
            intent.putExtra("courseList", this.courseList);
            this.context.startActivity(intent);
        }catch(InvalidInputException ex){
            CourseCreationActivity courseCreationActivity = (CourseCreationActivity)this.context;
            courseCreationActivity.runOnUiThread(() -> courseCreationActivity.showValidationMessages(ex.getValidationMessages()));
        }catch(OperationalException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public CourseCreateTask(String title, CourseList courseList, Context context){
        this.courseList = courseList;
        this.context = context;
        this.title = title;
    }
}

package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import com.jboard.R;
import com.jboard.adapter.CoursesManagementCourseAdapter;
import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.service.CourseService;

public class CourseDeleteTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Course course;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            CourseService courseService = new CourseService(this.course);
            courseService.delete();
            Activity activity = (Activity)this.context;
            activity.runOnUiThread(() -> {
                ListView coursesManagementCourseList = activity.findViewById(R.id.coursesManagementCourseList);
                ((CoursesManagementCourseAdapter)coursesManagementCourseList.getAdapter()).removeFromList(this.course.getID());
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

    public CourseDeleteTask(Course course, Context context){
        this.context = context;
        this.course = course;
    }
}

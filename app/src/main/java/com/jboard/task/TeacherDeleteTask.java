package com.jboard.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import com.jboard.R;
import com.jboard.adapter.TeachersManagementTeacherAdapter;
import com.jboard.exception.*;
import com.jboard.model.Teacher;
import com.jboard.service.TeacherService;

public class TeacherDeleteTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final Teacher teacher;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            TeacherService teacherService = new TeacherService(this.teacher);
            teacherService.delete();
            Activity activity = (Activity)this.context;
            activity.runOnUiThread(() -> {
                ListView teachersManagementTeacherList = activity.findViewById(R.id.teachersManagementTeacherList);
                ((TeachersManagementTeacherAdapter)teachersManagementTeacherList.getAdapter()).removeFromList(this.teacher.getID());
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

    public TeacherDeleteTask(Teacher teacher, Context context){
        this.context = context;
        this.teacher = teacher;
    }
}

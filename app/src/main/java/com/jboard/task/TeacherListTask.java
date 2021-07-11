package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.TeachersManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.TeacherList;
import com.jboard.service.TeacherService;

public class TeacherListTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            TeacherService teacherService = new TeacherService();
            TeacherList teacherList = teacherService.list();
            Intent intent = new Intent(this.context, TeachersManagementActivity.class);
            intent.putExtra("teacherList", teacherList);
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

    public TeacherListTask(Context context){
        this.context = context;
    }
}

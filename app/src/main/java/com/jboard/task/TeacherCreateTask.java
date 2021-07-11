package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.jboard.TeacherCreationActivity;
import com.jboard.TeachersManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.Teacher;
import com.jboard.model.TeacherList;
import com.jboard.service.TeacherService;

public class TeacherCreateTask extends AsyncTask<Void, Void, Void>  {
    private final TeacherList teacherList;
    private final Context context;
    private final String surname;
    private final String name;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            TeacherService teacherService = new TeacherService();
            Teacher teacher = teacherService.create(this.name, this.surname);
            this.teacherList.getTeacherList().add(teacher);
            Intent intent = new Intent(this.context, TeachersManagementActivity.class);
            intent.putExtra("teacherList", this.teacherList);
            this.context.startActivity(intent);
        }catch(InvalidInputException ex){
            TeacherCreationActivity teacherCreationActivity = (TeacherCreationActivity)this.context;
            teacherCreationActivity.runOnUiThread(() -> teacherCreationActivity.showValidationMessages(ex.getValidationMessages()));
        }catch(OperationalException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){ex.printStackTrace();
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public TeacherCreateTask(String name, String surname, TeacherList teacherList, Context context){
        this.teacherList = teacherList;
        this.context = context;
        this.surname = surname;
        this.name = name;
    }
}

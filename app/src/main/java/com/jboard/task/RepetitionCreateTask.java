package com.jboard.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.jboard.RepetitionCreationActivity;
import com.jboard.RepetitionsManagementActivity;
import com.jboard.exception.*;
import com.jboard.model.Course;
import com.jboard.model.Repetition;
import com.jboard.model.RepetitionList;
import com.jboard.model.Teacher;
import com.jboard.service.RepetitionService;

public class RepetitionCreateTask extends AsyncTask<Void, Void, Void> {
    private final RepetitionList repetitionList;
    private final Context context;
    private final Teacher teacher;
    private final Course course;

    @Override
    protected Void doInBackground(Void... voids){
        try{
            RepetitionService repetitionService = new RepetitionService();
            Repetition repetition = repetitionService.create(this.teacher, this.course);
            this.repetitionList.getRepetitionList().add(repetition);
            Intent intent = new Intent(this.context, RepetitionsManagementActivity.class);
            intent.putExtra("repetitionList", this.repetitionList);
            this.context.startActivity(intent);
        }catch(InvalidInputException ex){
            RepetitionCreationActivity repetitionCreationActivity = (RepetitionCreationActivity)this.context;
            repetitionCreationActivity.runOnUiThread(() -> repetitionCreationActivity.showValidationMessages(ex.getValidationMessages()));
        }catch(OperationalException ex){
            ex.showAlert(this.context);
        }catch(NetworkException ex){
            ex.setTask(this).showAlert(this.context);
        }catch(UnauthorizedException ex){
            ex.showLoginActivity(this.context);
        }
        return null;
    }

    public RepetitionCreateTask(Teacher teacher, Course course, RepetitionList repetitionList, Context context){
        this.repetitionList = repetitionList;
        this.teacher = teacher;
        this.context = context;
        this.course = course;
    }
}

package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jboard.model.TeacherList;
import com.jboard.task.TeacherCreateTask;

import java.util.HashMap;

public class TeacherCreationActivity extends AppCompatActivity {
    private TeacherList teacherList;

    private void loadTeacherList(){
        this.teacherList = null;
        Intent intent = this.getIntent();
        if ( intent != null ){
            Bundle extra = intent.getExtras();
            if ( extra != null && extra.containsKey("teacherList") ){
                this.teacherList = (TeacherList)extra.get("teacherList");
            }
        }
    }

    private boolean validateUserInput(){
        TextView teacherCreationInputSurnameErrorMsg = this.findViewById(R.id.teacherCreationInputSurnameErrorMsg);
        TextView teacherCreationInputNameErrorMsg = this.findViewById(R.id.teacherCreationInputNameErrorMsg);
        EditText teacherCreationInputSurname = this.findViewById(R.id.teacherCreationInputSurname);
        EditText teacherCreationInputName = this.findViewById(R.id.teacherCreationInputName);
        boolean isValid = true;
        teacherCreationInputSurnameErrorMsg.setVisibility(TextView.INVISIBLE);
        teacherCreationInputNameErrorMsg.setVisibility(TextView.INVISIBLE);
        teacherCreationInputSurnameErrorMsg.setText("");
        teacherCreationInputNameErrorMsg.setText("");
        if ( teacherCreationInputSurname.getText().toString().isEmpty() ){
            teacherCreationInputSurnameErrorMsg.setText("You must provide a valid surname.");
            teacherCreationInputSurnameErrorMsg.setVisibility(TextView.VISIBLE);
            isValid = false;
        }
        if ( teacherCreationInputName.getText().toString().isEmpty() ){
            teacherCreationInputNameErrorMsg.setText("You must provide a valid name.");
            teacherCreationInputNameErrorMsg.setVisibility(TextView.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    private void handleCreate(View view){
        if ( this.validateUserInput() ){
            EditText teacherCreationInputSurname = this.findViewById(R.id.teacherCreationInputSurname);
            EditText teacherCreationInputName = this.findViewById(R.id.teacherCreationInputName);
            String surname = teacherCreationInputSurname.getText().toString();
            String name = teacherCreationInputName.getText().toString();
            TeacherCreateTask teacherCreateTask = new TeacherCreateTask(name, surname, this.teacherList, this);
            teacherCreateTask.execute();
        }
    }

    private void handleCancel(View view){
        Intent intent = new Intent(this, TeachersManagementActivity.class);
        intent.putExtra("teacherList", this.teacherList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.loadTeacherList();
        setContentView(R.layout.activity_teacher_creation);
        Button teacherCreationCreateButton = this.findViewById(R.id.teacherCreationCreateButton);
        Button teacherCreationCancelButton = this.findViewById(R.id.teacherCreationCancelButton);
        teacherCreationCreateButton.setOnClickListener(this::handleCreate);
        teacherCreationCancelButton.setOnClickListener(this::handleCancel);
    }

    public TeacherCreationActivity showValidationMessages(HashMap<String, String> validationMessages){
        if ( validationMessages != null && validationMessages.size() > 0 ){
            TextView teacherCreationInputSurnameErrorMsg = this.findViewById(R.id.teacherCreationInputSurnameErrorMsg);
            TextView teacherCreationInputNameErrorMsg = this.findViewById(R.id.teacherCreationInputNameErrorMsg);
            if ( validationMessages.containsKey("surname") ){
                teacherCreationInputSurnameErrorMsg.setText(validationMessages.get("surname"));
                teacherCreationInputSurnameErrorMsg.setVisibility(TextView.VISIBLE);
            }
            if ( validationMessages.containsKey("name") ){
                teacherCreationInputNameErrorMsg.setText(validationMessages.get("name"));
                teacherCreationInputNameErrorMsg.setVisibility(TextView.VISIBLE);
            }
        }
        return this;
    }
}

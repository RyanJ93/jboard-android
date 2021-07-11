package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jboard.model.CourseList;
import com.jboard.task.CourseCreateTask;
import java.util.HashMap;

public class CourseCreationActivity extends AppCompatActivity {
    private CourseList courseList;

    private void loadCourseList(){
        this.courseList = null;
        Intent intent = this.getIntent();
        if ( intent != null ){
            Bundle extra = intent.getExtras();
            if ( extra != null && extra.containsKey("courseList") ){
                this.courseList = (CourseList)extra.get("courseList");
            }
        }
    }

    private boolean validateUserInput(){
        TextView courseCreationInputTitleErrorMsg = this.findViewById(R.id.courseCreationInputTitleErrorMsg);
        EditText courseCreationInputTitle = this.findViewById(R.id.courseCreationInputTitle);
        boolean isValid = true;
        courseCreationInputTitleErrorMsg.setVisibility(TextView.INVISIBLE);
        courseCreationInputTitleErrorMsg.setText("");
        if ( courseCreationInputTitle.getText().toString().isEmpty() ){
            courseCreationInputTitleErrorMsg.setText("You must provide a valid title.");
            courseCreationInputTitleErrorMsg.setVisibility(TextView.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    private void handleCreate(View view){
        if ( this.validateUserInput() ){
            EditText courseCreationInputTitle = this.findViewById(R.id.courseCreationInputTitle);
            String title = courseCreationInputTitle.getText().toString();
            CourseCreateTask courseCreateTask = new CourseCreateTask(title, this.courseList, this);
            courseCreateTask.execute();
        }
    }

    private void handleCancel(View view){
        Intent intent = new Intent(this, CoursesManagementActivity.class);
        intent.putExtra("courseList", this.courseList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadCourseList();
        setContentView(R.layout.activity_course_creation);
        Button courseCreationCreateButton = this.findViewById(R.id.courseCreationCreateButton);
        Button courseCreationCancelButton = this.findViewById(R.id.courseCreationCancelButton);
        courseCreationCreateButton.setOnClickListener(this::handleCreate);
        courseCreationCancelButton.setOnClickListener(this::handleCancel);
    }

    public CourseCreationActivity showValidationMessages(HashMap<String, String> validationMessages){
        if ( validationMessages != null && validationMessages.size() > 0 ){
            TextView courseCreationInputTitleErrorMsg = this.findViewById(R.id.courseCreationInputTitleErrorMsg);
            if ( validationMessages.containsKey("title") ){
                courseCreationInputTitleErrorMsg.setText(validationMessages.get("title"));
                courseCreationInputTitleErrorMsg.setVisibility(TextView.VISIBLE);
            }
        }
        return this;
    }
}
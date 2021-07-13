package com.jboard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jboard.model.Course;
import com.jboard.model.CourseList;
import com.jboard.model.RepetitionList;
import com.jboard.model.Teacher;
import com.jboard.model.TeacherList;
import com.jboard.task.RepetitionCreateTask;
import java.util.ArrayList;
import java.util.HashMap;

public class RepetitionCreationActivity extends AppCompatActivity {
    private RepetitionList repetitionList;
    private TeacherList teacherList;
    private CourseList courseList;

    private void loadAvailableTeachers(Spinner spinner){
        ArrayList<String> availableTeacherList = new ArrayList<>();
        for ( Teacher teacher : this.teacherList.getTeacherList() ){
            availableTeacherList.add(teacher.getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableTeacherList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadAvailableCourses(Spinner spinner){
        ArrayList<String> availableCourseList = new ArrayList<>();
        for ( Course course : this.courseList.getCourseList() ){
            availableCourseList.add(course.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableCourseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadEntityLists(){
        this.repetitionList = null;
        this.teacherList = null;
        this.courseList = null;
        Intent intent = this.getIntent();
        if ( intent != null ){
            Bundle extra = intent.getExtras();
            if ( extra != null ){
                if ( extra.containsKey("repetitionList") ){
                    this.repetitionList = (RepetitionList)extra.get("repetitionList");
                }
                if ( extra.containsKey("teacherList") ){
                    this.teacherList = (TeacherList)extra.get("teacherList");
                }
                if ( extra.containsKey("courseList") ){
                    this.courseList = (CourseList)extra.get("courseList");
                }
            }
        }
    }

    private boolean validateUserInput(){
        TextView repetitionCreationInputTeacherErrorMsg = this.findViewById(R.id.repetitionCreationInputTeacherErrorMsg);
        TextView repetitionCreationInputCourseErrorMsg = this.findViewById(R.id.repetitionCreationInputCourseErrorMsg);
        Spinner repetitionCreationInputTeacher = this.findViewById(R.id.repetitionCreationInputTeacher);
        Spinner repetitionCreationInputCourse = this.findViewById(R.id.repetitionCreationInputCourse);
        int selectedTeacherPosition = repetitionCreationInputTeacher.getSelectedItemPosition();
        int selectedCoursePosition = repetitionCreationInputCourse.getSelectedItemPosition();
        repetitionCreationInputTeacherErrorMsg.setVisibility(TextView.INVISIBLE);
        repetitionCreationInputCourseErrorMsg.setVisibility(TextView.INVISIBLE);
        repetitionCreationInputTeacherErrorMsg.setText("");
        repetitionCreationInputCourseErrorMsg.setText("");
        boolean isValid = true;
        if ( selectedTeacherPosition == -1 || this.teacherList.getTeacherList().size() < selectedTeacherPosition ){
            repetitionCreationInputTeacherErrorMsg.setText("You must select a teacher.");
            repetitionCreationInputTeacherErrorMsg.setVisibility(TextView.VISIBLE);
            isValid = false;
        }
        if ( selectedCoursePosition == -1 || this.courseList.getCourseList().size() < selectedCoursePosition ){
            repetitionCreationInputTeacherErrorMsg.setText("You must select a course.");
            repetitionCreationInputTeacherErrorMsg.setVisibility(TextView.VISIBLE);
            isValid = false;
        }
        return isValid;
    }

    private void handleCreate(View view){
        if ( this.validateUserInput() ){
            Spinner repetitionCreationInputTeacher = this.findViewById(R.id.repetitionCreationInputTeacher);
            Spinner repetitionCreationInputCourse = this.findViewById(R.id.repetitionCreationInputCourse);
            Teacher teacher = this.teacherList.getTeacherList().get(repetitionCreationInputTeacher.getSelectedItemPosition());
            Course course = this.courseList.getCourseList().get(repetitionCreationInputCourse.getSelectedItemPosition());
            RepetitionCreateTask repetitionCreateTask = new RepetitionCreateTask(teacher, course, this.repetitionList, this);
            repetitionCreateTask.setTeacherList(this.teacherList).setCourseList(this.courseList).execute();
        }
    }

    private void handleCancel(View view){
        Intent intent = new Intent(this, RepetitionsManagementActivity.class);
        intent.putExtra("repetitionList", this.repetitionList);
        intent.putExtra("teacherList", this.teacherList);
        intent.putExtra("courseList", this.courseList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadEntityLists();
        setContentView(R.layout.activity_repetition_creation);
        Spinner repetitionCreationInputTeacher = this.findViewById(R.id.repetitionCreationInputTeacher);
        Spinner repetitionCreationInputCourse = this.findViewById(R.id.repetitionCreationInputCourse);
        this.loadAvailableTeachers(repetitionCreationInputTeacher);
        this.loadAvailableCourses(repetitionCreationInputCourse);
        Button repetitionCreationCreateButton = this.findViewById(R.id.repetitionCreationCreateButton);
        Button repetitionCreationCancelButton = this.findViewById(R.id.repetitionCreationCancelButton);
        repetitionCreationCreateButton.setOnClickListener(this::handleCreate);
        repetitionCreationCancelButton.setOnClickListener(this::handleCancel);
    }

    public RepetitionCreationActivity showValidationMessages(HashMap<String, String> validationMessages){
        if ( validationMessages != null && validationMessages.size() > 0 ){
            TextView repetitionCreationInputTeacherErrorMsg = this.findViewById(R.id.repetitionCreationInputTeacherErrorMsg);
            TextView repetitionCreationInputCourseErrorMsg = this.findViewById(R.id.repetitionCreationInputCourseErrorMsg);
            if ( validationMessages.containsKey("courseID") ){
                repetitionCreationInputCourseErrorMsg.setText(validationMessages.get("courseID"));
                repetitionCreationInputCourseErrorMsg.setVisibility(TextView.VISIBLE);
            }
            if ( validationMessages.containsKey("teacherID") ){
                repetitionCreationInputTeacherErrorMsg.setText(validationMessages.get("teacherID"));
                repetitionCreationInputTeacherErrorMsg.setVisibility(TextView.VISIBLE);
            }
            if ( validationMessages.containsKey("_global") ){
                repetitionCreationInputTeacherErrorMsg.setText(validationMessages.get("_global"));
                repetitionCreationInputTeacherErrorMsg.setVisibility(TextView.VISIBLE);
            }
        }
        return this;
    }
}

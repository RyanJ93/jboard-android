package com.jboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.jboard.adapter.CoursesManagementCourseAdapter;
import com.jboard.model.CourseList;

public class CoursesManagementActivity extends AppCompatActivityWithNavigationDrawer {
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

    private void handleCreate(View view){
        Intent intent = new Intent(this, CourseCreationActivity.class);
        intent.putExtra("courseList", this.courseList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadCourseList();
        setContentView(R.layout.activity_courses_management);
        this.setupNavigationDrawer();
        ListView coursesManagementCourseList = this.findViewById(R.id.coursesManagementCourseList);
        coursesManagementCourseList.setAdapter(new CoursesManagementCourseAdapter(this.courseList, this));
        Button coursesManagementCreate = this.findViewById(R.id.coursesManagementCreate);
        coursesManagementCreate.setOnClickListener(this::handleCreate);
    }
}
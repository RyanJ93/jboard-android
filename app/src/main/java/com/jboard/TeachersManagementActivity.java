package com.jboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.jboard.adapter.CoursesManagementCourseAdapter;
import com.jboard.adapter.TeachersManagementTeacherAdapter;
import com.jboard.model.TeacherList;

public class TeachersManagementActivity extends AppCompatActivityWithNavigationDrawer {
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

    private void handleCreate(View view){
        Intent intent = new Intent(this, TeacherCreationActivity.class);
        intent.putExtra("teacherList", this.teacherList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadTeacherList();
        setContentView(R.layout.activity_teachers_management);
        this.setupNavigationDrawer();
        ListView teachersManagementTeacherList = this.findViewById(R.id.teachersManagementTeacherList);
        teachersManagementTeacherList.setAdapter(new TeachersManagementTeacherAdapter(this.teacherList, this));
        Button teachersManagementCreate = this.findViewById(R.id.teachersManagementCreate);
        teachersManagementCreate.setOnClickListener(this::handleCreate);
    }
}
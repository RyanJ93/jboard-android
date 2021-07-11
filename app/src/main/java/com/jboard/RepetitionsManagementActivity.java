package com.jboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.jboard.adapter.RepetitionsManagementRepetitionAdapter;
import com.jboard.model.CourseList;
import com.jboard.model.RepetitionList;
import com.jboard.model.TeacherList;
import java.util.HashMap;

public class RepetitionsManagementActivity extends AppCompatActivityWithNavigationDrawer {
    private RepetitionList repetitionList;
    private TeacherList teacherList;
    private CourseList courseList;

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

    private void handleCreate(View view){
        Intent intent = new Intent(this, RepetitionCreationActivity.class);
        intent.putExtra("repetitionList", this.repetitionList);
        intent.putExtra("teacherList", this.teacherList);
        intent.putExtra("courseList", this.courseList);
        this.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.loadEntityLists();
        setContentView(R.layout.activity_repetitions_management);
        this.setupNavigationDrawer();
        ListView repetitionsManagementRepetitionList = this.findViewById(R.id.repetitionsManagementRepetitionList);
        repetitionsManagementRepetitionList.setAdapter(new RepetitionsManagementRepetitionAdapter(this.repetitionList, this));
        Button repetitionsManagementCreate = this.findViewById(R.id.repetitionsManagementCreate);
        repetitionsManagementCreate.setOnClickListener(this::handleCreate);
    }
}

package com.jboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import com.jboard.adapter.AvailableLessonItemAdapter;
import com.jboard.model.AvailableCourseList;

public class AvailableLessonList extends AppCompatActivityWithNavigationDrawer {
    private AvailableCourseList availableCourseList;

    private void loadAvailableCourses(){
        this.availableCourseList = null;
        Intent intent = this.getIntent();
        if ( intent != null ){
            Bundle extra = intent.getExtras();
            if ( extra != null && extra.containsKey("availableCourseList") ){
                this.availableCourseList = (AvailableCourseList)extra.get("availableCourseList");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.loadAvailableCourses();
        this.setContentView(R.layout.activity_available_lesson_list);
        this.setupNavigationDrawer();
        ListView availableCourseList = this.findViewById(R.id.availableCourseList);
        availableCourseList.setAdapter(new AvailableLessonItemAdapter(this.availableCourseList, this));
    }

    public AvailableLessonList computeEligibility(){
        if ( this.availableCourseList != null ){
            ListView listview = this.findViewById(R.id.availableCourseList);
            this.availableCourseList.computeEligibility();
            ((AvailableLessonItemAdapter)listview.getAdapter()).notifyDataSetChanged();
        }
        return this;
    }
}

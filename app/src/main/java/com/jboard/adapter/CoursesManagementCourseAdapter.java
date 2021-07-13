package com.jboard.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.jboard.R;
import com.jboard.model.Course;
import com.jboard.model.CourseList;
import com.jboard.task.CourseDeleteTask;
import java.util.ArrayList;

public class CoursesManagementCourseAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private CourseList courseList;
    private Context context;

    private void setProperties(View view, Course course){
        TextView courseManagementCourseTitle = view.findViewById(R.id.courseManagementCourseTitle);
        courseManagementCourseTitle.setText(course.getTitle());
    }

    private void handleDelete(View view){
        View parent = (View)view.getParent();
        ListView listView = (ListView)parent.getParent();
        Course course = this.courseList.getCourseList().get(listView.getPositionForView(parent));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Do you really want to delete this course?");
        alertDialogBuilder.setNegativeButton(android.R.string.no, (dialog, whichButton) -> {});
        alertDialogBuilder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            CourseDeleteTask courseDeleteTask = new CourseDeleteTask(course, view.getContext());
            courseDeleteTask.execute();
        });
        alertDialogBuilder.show();
    }

    public CoursesManagementCourseAdapter(CourseList courseList, Context context){
        CoursesManagementCourseAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.courseList = courseList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.courseList.getCourseList().size();
    }

    @Override
    public Object getItem(int i){
        return this.courseList.getCourseList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.courseList.getCourseList().get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = CoursesManagementCourseAdapter.layoutInflater.inflate(R.layout.courses_management_course, null);
        }
        view.setTag(i);
        Course course = this.courseList.getCourseList().get(i);
        this.setProperties(view, course);
        Button courseManagementCourseDeleteButton = view.findViewById(R.id.courseManagementCourseDeleteButton);
        courseManagementCourseDeleteButton.setOnClickListener(this::handleDelete);
        return view;
    }

    public void removeFromList(int courseID){
        ArrayList<Course> courseList = this.courseList.getCourseList();
        for ( int i = 0 ; i < courseList.size() ; i++ ){
            if ( courseList.get(i).getID() == courseID ){
                courseList.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
}

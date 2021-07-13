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
import com.jboard.model.Teacher;
import com.jboard.model.TeacherList;
import com.jboard.task.TeacherDeleteTask;
import java.util.ArrayList;

public class TeachersManagementTeacherAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private TeacherList teacherList;
    private Context context;

    private void setProperties(View view, Teacher teacher){
        TextView teacherManagementTeacherFullName = view.findViewById(R.id.teacherManagementTeacherFullName);
        teacherManagementTeacherFullName.setText(teacher.getFullName());
    }

    private void handleDelete(View view){
        View parent = (View)view.getParent();
        ListView listView = (ListView)parent.getParent();
        Teacher teacher = this.teacherList.getTeacherList().get(listView.getPositionForView(parent));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Do you really want to delete this teacher?");
        alertDialogBuilder.setNegativeButton(android.R.string.no, (dialog, whichButton) -> {});
        alertDialogBuilder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            TeacherDeleteTask teacherDeleteTask = new TeacherDeleteTask(teacher, view.getContext());
            teacherDeleteTask.execute();
        });
        alertDialogBuilder.show();
    }

    public TeachersManagementTeacherAdapter(TeacherList teacherList, Context context){
        TeachersManagementTeacherAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.teacherList = teacherList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.teacherList.getTeacherList().size();
    }

    @Override
    public Object getItem(int i){
        return this.teacherList.getTeacherList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.teacherList.getTeacherList().get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = TeachersManagementTeacherAdapter.layoutInflater.inflate(R.layout.teachers_management_teacher, null);
        }
        view.setTag(i);
        Teacher teacher = this.teacherList.getTeacherList().get(i);
        this.setProperties(view, teacher);
        Button teacherManagementTeacherDeleteButton = view.findViewById(R.id.teacherManagementTeacherDeleteButton);
        teacherManagementTeacherDeleteButton.setOnClickListener(this::handleDelete);
        return view;
    }

    public void removeFromList(int teacherID){
        ArrayList<Teacher> teacherList = this.teacherList.getTeacherList();
        for ( int i = 0 ; i < teacherList.size() ; i++ ){
            if ( teacherList.get(i).getID() == teacherID ){
                teacherList.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
}

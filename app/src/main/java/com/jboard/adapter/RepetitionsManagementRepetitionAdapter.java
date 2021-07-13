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
import com.jboard.model.Repetition;
import com.jboard.model.RepetitionList;
import com.jboard.task.RepetitionDeleteTask;
import java.util.ArrayList;

public class RepetitionsManagementRepetitionAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private RepetitionList repetitionList;
    private Context context;

    private void setProperties(View view, Repetition repetition){
        TextView repetitionManagementCourseTitle = view.findViewById(R.id.repetitionManagementCourseTitle);
        repetitionManagementCourseTitle.setText(repetition.getCourse().getTitle());
        TextView repetitionManagementTeacher = view.findViewById(R.id.repetitionManagementTeacher);
        repetitionManagementTeacher.setText(repetition.getTeacher().getFullName());
    }

    private void handleDelete(View view){
        View parent = (View)view.getParent();
        ListView listView = (ListView)parent.getParent();
        Repetition repetition = this.repetitionList.getRepetitionList().get(listView.getPositionForView(parent));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Do you really want to delete this repetition?");
        alertDialogBuilder.setNegativeButton(android.R.string.no, (dialog, whichButton) -> {});
        alertDialogBuilder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            RepetitionDeleteTask repetitionDeleteTask = new RepetitionDeleteTask(repetition, view.getContext());
            repetitionDeleteTask.execute();
        });
        alertDialogBuilder.show();
    }

    public RepetitionsManagementRepetitionAdapter(RepetitionList repetitionList, Context context){
        RepetitionsManagementRepetitionAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.repetitionList = repetitionList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.repetitionList.getRepetitionList().size();
    }

    @Override
    public Object getItem(int i){
        return this.repetitionList.getRepetitionList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.repetitionList.getRepetitionList().get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = RepetitionsManagementRepetitionAdapter.layoutInflater.inflate(R.layout.repetitions_management_repetition, null);
        }
        view.setTag(i);
        Repetition repetition = this.repetitionList.getRepetitionList().get(i);
        this.setProperties(view, repetition);
        Button repetitionManagementRepetitionDeleteButton = view.findViewById(R.id.repetitionManagementRepetitionDeleteButton);
        repetitionManagementRepetitionDeleteButton.setOnClickListener(this::handleDelete);
        return view;
    }

    public void removeFromList(int repetitionID){
        ArrayList<Repetition> repetitionList = this.repetitionList.getRepetitionList();
        for ( int i = 0 ; i < repetitionList.size() ; i++ ){
            if ( repetitionList.get(i).getID() == repetitionID ){
                repetitionList.remove(i);
                break;
            }
        }
        this.notifyDataSetChanged();
    }
}

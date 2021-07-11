package com.jboard.adapter;

import android.app.Activity;
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
import com.jboard.model.Lesson;
import com.jboard.model.LessonList;
import com.jboard.model.User;
import com.jboard.service.UserService;
import com.jboard.task.LessonCancelTask;
import com.jboard.task.LessonMarkAsCompletedTask;
import java.util.ArrayList;

public class ActiveLessonAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private final LessonList lessonList;
    private final Context context;

    private void setProperties(View view, Lesson lesson){
        TextView activeLessonTeacher = view.findViewById(R.id.activeLessonTeacher);
        TextView activeLessonCourse = view.findViewById(R.id.activeLessonCourse);
        TextView activeLessonUser = view.findViewById(R.id.activeLessonUser);
        User authenticatedUser = UserService.getAuthenticatedUser();
        String teacher = "By " + lesson.getTeacher().getFullName();
        activeLessonCourse.setText(lesson.getCourse().getTitle());
        activeLessonTeacher.setText(teacher);
        String user = lesson.getDateTimeString();
        if ( authenticatedUser != null && authenticatedUser.isAdmin() ){
            user = "Booked by " + lesson.getUser().getAccount() + " on " + lesson.getDateTimeString();
        }
        activeLessonUser.setText(user);
    }

    private void handleMarkAsCompleted(View view){
        View parent = (View)view.getParent();
        ListView listView = (ListView)parent.getParent();
        Lesson lesson = this.lessonList.getActiveLessonList().get(listView.getPositionForView(parent));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Do you really want to mark this lesson as completed?");
        alertDialogBuilder.setNegativeButton(android.R.string.no, (dialog, whichButton) -> {});
        alertDialogBuilder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            LessonMarkAsCompletedTask lessonMarkAsCompletedTask = new LessonMarkAsCompletedTask(lesson, view.getContext());
            lessonMarkAsCompletedTask.execute();
        });
        alertDialogBuilder.show();
    }

    private void handleDelete(View view){
        View parent = (View)view.getParent();
        ListView listView = (ListView)parent.getParent();
        Lesson lesson = this.lessonList.getActiveLessonList().get(listView.getPositionForView(parent));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setMessage("Do you really want to cancel this lesson?");
        alertDialogBuilder.setNegativeButton(android.R.string.no, (dialog, whichButton) -> {});
        alertDialogBuilder.setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
            LessonCancelTask lessonCancelTask = new LessonCancelTask(lesson, view.getContext());
            lessonCancelTask.execute();
        });
        alertDialogBuilder.show();
    }

    private void refreshList(){
        ListView activeLessonList = ((Activity)this.context).findViewById(R.id.activeLessonList);
        ((ActiveLessonAdapter)activeLessonList.getAdapter()).refresh();
    }

    public ActiveLessonAdapter(LessonList lessonList, Context context){
        ActiveLessonAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lessonList = lessonList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.lessonList.getActiveLessonList().size();
    }

    @Override
    public Object getItem(int i){
        return this.lessonList.getActiveLessonList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.lessonList.getActiveLessonList().get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = ActiveLessonAdapter.layoutInflater.inflate(R.layout.active_lesson, null);
        }
        view.setTag(i);
        Lesson lesson = this.lessonList.getActiveLessonList().get(i);
        this.setProperties(view, lesson);
        Button lessonCompleteButton = view.findViewById(R.id.lessonCompleteButton);
        Button lessonDeleteButton = view.findViewById(R.id.lessonDeleteButton);
        lessonCompleteButton.setOnClickListener(this::handleMarkAsCompleted);
        lessonDeleteButton.setOnClickListener(this::handleDelete);
        return view;
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }

    public void moveLessonToCompletedList(int lessonID){
        ArrayList<Lesson> lessonList = this.lessonList.getActiveLessonList();
        for ( int i = 0 ; i < lessonList.size() ; i++ ){
            if ( lessonList.get(i).getID() == lessonID ){
                this.lessonList.getCompletedLessonList().add(lessonList.get(i));
                lessonList.remove(i);
                break;
            }
        }
        this.refreshList();
    }

    public void moveLessonToCanceledList(int lessonID){
        ArrayList<Lesson> lessonList = this.lessonList.getActiveLessonList();
        for ( int i = 0 ; i < lessonList.size() ; i++ ){
            if ( lessonList.get(i).getID() == lessonID ){
                this.lessonList.getCanceledLessonList().add(lessonList.get(i));
                lessonList.remove(i);
                break;
            }
        }
        this.refreshList();
    }
}

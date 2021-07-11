package com.jboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jboard.R;
import com.jboard.model.Lesson;
import com.jboard.model.LessonList;

public class CompletedLessonAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private final LessonList lessonList;
    private final Context context;

    private void setProperties(View view, Lesson lesson){
        String user = "Booked by " + lesson.getUser().getAccount() + " on " + lesson.getDateTimeString();
        String teacher = "By " + lesson.getTeacher().getFullName();
        TextView completedLessonTeacher = view.findViewById(R.id.completedLessonTeacher);
        TextView completedLessonCourse = view.findViewById(R.id.completedLessonCourse);
        TextView completedLessonUser = view.findViewById(R.id.completedLessonUser);
        completedLessonCourse.setText(lesson.getCourse().getTitle());
        completedLessonTeacher.setText(teacher);
        completedLessonUser.setText(user);
    }

    public CompletedLessonAdapter(LessonList lessonList, Context context){
        CompletedLessonAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lessonList = lessonList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.lessonList.getCompletedLessonList().size();
    }

    @Override
    public Object getItem(int i){
        return this.lessonList.getCompletedLessonList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.lessonList.getCompletedLessonList().get(i).getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = CompletedLessonAdapter.layoutInflater.inflate(R.layout.completed_lesson, null);
        }
        view.setTag(i);
        Lesson lesson = this.lessonList.getCompletedLessonList().get(i);
        this.setProperties(view, lesson);
        return view;
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }
}

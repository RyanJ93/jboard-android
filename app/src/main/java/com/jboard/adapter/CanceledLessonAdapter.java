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

public class CanceledLessonAdapter extends BaseAdapter {
    private static LayoutInflater layoutInflater = null;

    private final LessonList lessonList;
    private final Context context;

    public CanceledLessonAdapter(LessonList lessonList, Context context){
        CanceledLessonAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lessonList = lessonList;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.lessonList.getCanceledLessonList().size();
    }

    @Override
    public Object getItem(int i){
        return this.lessonList.getCanceledLessonList().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.lessonList.getCanceledLessonList().get(i).getID();
    }

    private void setProperties(View view, Lesson lesson){
        String user = "Booked by " + lesson.getUser().getAccount() + " on " + lesson.getDateTimeString();
        String teacher = "By " + lesson.getTeacher().getFullName();
        TextView canceledLessonTeacher = view.findViewById(R.id.canceledLessonTeacher);
        TextView canceledLessonCourse = view.findViewById(R.id.canceledLessonCourse);
        TextView canceledLessonUser = view.findViewById(R.id.canceledLessonUser);
        canceledLessonCourse.setText(lesson.getCourse().getTitle());
        canceledLessonTeacher.setText(teacher);
        canceledLessonUser.setText(user);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = CanceledLessonAdapter.layoutInflater.inflate(R.layout.canceled_lesson, null);
        }
        view.setTag(i);
        Lesson lesson = this.lessonList.getCanceledLessonList().get(i);
        this.setProperties(view, lesson);
        return view;
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }
}

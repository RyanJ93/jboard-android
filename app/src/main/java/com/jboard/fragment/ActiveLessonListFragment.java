package com.jboard.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.jboard.R;
import com.jboard.adapter.ActiveLessonAdapter;
import com.jboard.model.LessonList;

public class ActiveLessonListFragment extends Fragment {
    private LessonList lessonList;
    private Context context;

    public ActiveLessonListFragment(){}

    public ActiveLessonListFragment(LessonList lessonList, Context context){
        this.lessonList = lessonList;
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.active_lesson_list_fragment, container, false);
        ListView activeLessonList = view.findViewById(R.id.activeLessonList);
        activeLessonList.setAdapter(new ActiveLessonAdapter(this.lessonList, view.getContext()));
        return view;
    }
}

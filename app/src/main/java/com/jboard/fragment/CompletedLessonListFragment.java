package com.jboard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.jboard.R;
import com.jboard.adapter.CompletedLessonAdapter;
import com.jboard.model.LessonList;

public class CompletedLessonListFragment extends Fragment {
    private LessonList lessonList;

    public CompletedLessonListFragment(){}

    public CompletedLessonListFragment(LessonList lessonList){
        this.lessonList = lessonList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.completed_lesson_list_fragment, container, false);
        ListView completedLessonList = view.findViewById(R.id.completedLessonList);
        completedLessonList.setAdapter(new CompletedLessonAdapter(this.lessonList, view.getContext()));
        return view;
    }
}

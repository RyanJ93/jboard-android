package com.jboard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.jboard.R;
import com.jboard.adapter.CanceledLessonAdapter;
import com.jboard.model.LessonList;

public class CanceledLessonListFragment extends Fragment {
    private LessonList lessonList;

    public CanceledLessonListFragment(){}

    public CanceledLessonListFragment(LessonList lessonList){
        this.lessonList = lessonList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.canceled_lesson_list_fragment, container, false);
        ListView canceledLessonList = view.findViewById(R.id.canceledLessonList);
        canceledLessonList.setAdapter(new CanceledLessonAdapter(this.lessonList, view.getContext()));
        return view;
    }
}

package com.jboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jboard.R;
import com.jboard.model.AvailableCourse;
import com.jboard.model.AvailableCourseLesson;
import com.jboard.model.AvailableCourseList;
import com.jboard.model.User;
import com.jboard.service.UserService;
import java.util.ArrayList;

public class AvailableLessonItemAdapter extends BaseAdapter {
    private static final String USER_INSTRUCTIONS_TEXT = "Click on any available slot to book a lesson, click on red marked lessons to cancel your reservation.";
    private static final String ADMIN_INSTRUCTIONS_TEXT = "Click on any reserved slot to cancel that reservation, on any available slot to book a lesson.";

    private static LayoutInflater layoutInflater = null;

    private final AvailableCourseList availableCourseList;
    private final Context context;

    private void setProperties(View view, AvailableCourse availableCourse){
        int count = availableCourse.getAvailableCourseLessons().size();
        TextView instructions = view.findViewById(R.id.instructions);
        TextView teacherCount = view.findViewById(R.id.teacherCount);
        TextView courseName = view.findViewById(R.id.courseName);
        String instructionsText;
        courseName.setText(availableCourse.getCourse().getTitle());
        String counterText = count + " " + ( count == 1 ? "teacher" : "teachers" ) + " available";
        teacherCount.setText(counterText);
        User authenticatedUser = UserService.getAuthenticatedUser();
        if ( authenticatedUser != null && authenticatedUser.isAdmin() ){
            instructionsText = AvailableLessonItemAdapter.ADMIN_INSTRUCTIONS_TEXT;
        }else{
            instructionsText = AvailableLessonItemAdapter.USER_INSTRUCTIONS_TEXT;
        }
        instructions.setText(instructionsText);
    }

    private void toggleCalendarVisibility(View view){
        ConstraintLayout availableLessonItemDetailsBox = view.findViewById(R.id.availableLessonItemDetailsBox);
        if ( availableLessonItemDetailsBox.getVisibility() == ConstraintLayout.GONE ){
            availableLessonItemDetailsBox.setVisibility(ConstraintLayout.VISIBLE);
        }else{
            availableLessonItemDetailsBox.setVisibility(ConstraintLayout.GONE);
        }
    }

    public AvailableLessonItemAdapter(AvailableCourseList availableCourseList, Context context){
        AvailableLessonItemAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.availableCourseList = availableCourseList;
        this.context = context;
    }

    @Override
    public int getCount(){
        int count = 0;
        if ( this.availableCourseList != null ){
            ArrayList<AvailableCourse> availableCourses = this.availableCourseList.getAvailableCourses();
            count = availableCourses == null ? 0 : availableCourses.size();
        }
        return count;
    }

    @Override
    public Object getItem(int i){
        return this.availableCourseList.getAvailableCourses().get(i);
    }

    @Override
    public long getItemId(int i){
        return this.availableCourseList.getAvailableCourses().get(i).getCourse().getID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if ( view == null ){
            view = AvailableLessonItemAdapter.layoutInflater.inflate(R.layout.available_lesson_item, null);
        }
        view.setTag(i);
        AvailableCourse availableCourse = this.availableCourseList.getAvailableCourses().get(i);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        RecyclerView lessonCalendarList = view.findViewById(R.id.lessonCalendarList);
        ArrayList<AvailableCourseLesson> availableCourseLessons = availableCourse.getAvailableCourseLessons();
        lessonCalendarList.setAdapter(new LessonCalendarAdapter(availableCourseLessons, availableCourse.getCourse(), this.context));
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        lessonCalendarList.setLayoutManager(linearLayoutManager);
        this.setProperties(view, availableCourse);
        Button availableLessonItemDetailsBtn = view.findViewById(R.id.availableLessonItemDetailsBtn);
        final View finalView = view;
        availableLessonItemDetailsBtn.setOnClickListener((View currentView) -> this.toggleCalendarVisibility(finalView));
        return view;
    }
}

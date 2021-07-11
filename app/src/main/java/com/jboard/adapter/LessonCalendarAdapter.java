package com.jboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.jboard.AvailableLessonList;
import com.jboard.R;
import com.jboard.model.AvailableCourseLesson;
import com.jboard.model.Course;
import com.jboard.model.Lesson;
import com.jboard.model.LessonSlot;
import com.jboard.model.Teacher;
import com.jboard.service.UserService;
import com.jboard.task.CalendarLessonCancelTask;
import com.jboard.task.LessonCreateTask;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class LessonCalendarAdapter extends RecyclerView.Adapter<LessonCalendarAdapter.LessonCalendarViewHolder> {
    private static LayoutInflater layoutInflater = null;

    private final ArrayList<AvailableCourseLesson> availableCourseLessons;
    private final Context context;
    private final Course course;

    public LessonCalendarAdapter(ArrayList<AvailableCourseLesson> availableCourseLessons, Course course, Context context){
        LessonCalendarAdapter.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.availableCourseLessons = availableCourseLessons;
        this.context = context;
        this.course = course;
    }

    @Override
    public LessonCalendarAdapter.LessonCalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LessonCalendarAdapter.layoutInflater.inflate(R.layout.lesson_calendar, null);
        return new LessonCalendarViewHolder(view, this.course, this);
    }

    @Override
    public void onBindViewHolder(LessonCalendarAdapter.LessonCalendarViewHolder holder, int position){
        AvailableCourseLesson availableCourseLesson = this.availableCourseLessons.get(position);
        holder.getTeacherNameLabel().setText(availableCourseLesson.getTeacher().getFullName());
        holder.setAvailableCourseLesson(availableCourseLesson);
        for ( LessonSlot lessonSlot : availableCourseLesson.getLessonSlots() ){
            TextView hourText = holder.getHourTextByLessonSlot(lessonSlot);
            TextView dayText = holder.getDayTextByLessonSlot(lessonSlot);
            hourText.setTextColor(Color.parseColor("#ffffff"));
            dayText.setTextColor(Color.parseColor("#ffffff"));
            if ( !lessonSlot.isAvailable() ){
                TextView tick = holder.getTickByLessonSlot(lessonSlot);
                if ( tick != null ){
                    tick.setTextColor(Color.parseColor(lessonSlot.isMine() ? "#d35400" : "#fb782f"));
                    tick.setVisibility(TextView.VISIBLE);
                }
            }else if ( !lessonSlot.isEligible() ){
                hourText.setTextColor(Color.parseColor("#999999"));
                dayText.setTextColor(Color.parseColor("#999999"));
            }
        }
    }

    @Override
    public int getItemCount(){
        return this.availableCourseLessons == null ? 0 : this.availableCourseLessons.size();
    }

    public void addLesson(Lesson lesson, LessonSlot lessonSlot){
        for ( AvailableCourseLesson availableCourseLesson : this.availableCourseLessons ){
            if ( availableCourseLesson.getTeacher().getID() == lesson.getTeacher().getID() ){
                for ( LessonSlot currentLessonSlot : availableCourseLesson.getLessonSlots() ){
                    if ( currentLessonSlot.getDay() == lessonSlot.getDay() && currentLessonSlot.getHour() == lessonSlot.getHour() ){
                        currentLessonSlot.setLesson(lesson, true);
                        break;
                    }
                }
            }
        }
        this.notifyDataSetChanged();
        ((AvailableLessonList)this.context).computeEligibility();
    }

    public void removeLesson(Lesson lesson){
        for ( AvailableCourseLesson availableCourseLesson : this.availableCourseLessons ){
            if ( availableCourseLesson.getTeacher().getID() == lesson.getTeacher().getID() ){
                for ( LessonSlot currentLessonSlot : availableCourseLesson.getLessonSlots() ){
                    Lesson currentLesson = currentLessonSlot.getLesson();
                    if ( currentLesson != null && currentLesson.getID() == lesson.getID() ){
                        currentLessonSlot.setLesson(null, true);
                        break;
                    }
                }
            }
        }
        this.notifyDataSetChanged();
        ((AvailableLessonList)this.context).computeEligibility();
    }

    public final static class LessonCalendarViewHolder extends RecyclerView.ViewHolder {
        private final LessonCalendarAdapter lessonCalendarAdapter;
        private AvailableCourseLesson availableCourseLesson;
        private TextView teacherNameLabel;
        private final Course course;

        public LessonSlot getSlotByDateTime(int day, int hour){
            LessonSlot lessonSlotFound = null;
            if ( this.availableCourseLesson != null ){
                for ( LessonSlot lessonSlot : this.availableCourseLesson.getLessonSlots() ){
                    if ( lessonSlot.getDay() == day && lessonSlot.getHour() == hour ){
                        lessonSlotFound = lessonSlot;
                        break;
                    }
                }
            }
            return lessonSlotFound;
        }

        private void handleCalendarPick(View itemView, View view){
            String targetID = itemView.getResources().getResourceEntryName(view.getId());
            int hour = Integer.parseInt(targetID.substring(3, 5));
            int day = Integer.parseInt(targetID.substring(1, 2));
            TextView dayText = this.getDayTextByDateTime(day, hour);
            TextView tick = this.getTickByDateTime(day, hour);
            if ( tick != null && dayText != null ){
                boolean isEligible = dayText.getTextColors().getDefaultColor() != Color.parseColor("#999999");
                if ( tick.getVisibility() == TextView.VISIBLE ){
                    boolean isMine = tick.getTextColors().getDefaultColor() == Color.parseColor("#d35400");
                    if ( isMine || UserService.getAuthenticatedUser().isAdmin() ){
                        LessonSlot lessonSlot = this.getSlotByDateTime(day, hour);
                        if ( lessonSlot != null && lessonSlot.getLesson() != null ){
                            CalendarLessonCancelTask calendarLessonCancelTask = new CalendarLessonCancelTask(lessonSlot.getLesson(), itemView.getContext(), this.lessonCalendarAdapter);
                            calendarLessonCancelTask.execute();
                        }
                    }
                }else if ( isEligible ){
                    LessonSlot lessonSlot = this.getSlotByDateTime(day, hour);
                    if ( lessonSlot != null ){
                        Teacher teacher = this.availableCourseLesson.getTeacher();
                        LessonCreateTask lessonCreateTask = new LessonCreateTask(lessonSlot, teacher, this.course, itemView.getContext(), this.lessonCalendarAdapter);
                        lessonCreateTask.execute();

                    }
                }
            }
        }

        private void bindEvents(View itemView){
            for ( int day = 1 ; day <= 5 ; day++ ){
                for ( int hour = 15 ; hour <= 18 ; hour++ ){
                    try{
                        Field field = R.id.class.getDeclaredField("c" + day + "_" + hour);
                        int resourceID = field.getInt(field);
                        if ( resourceID != 0 ){
                            ConstraintLayout constraintLayout = itemView.findViewById(resourceID);
                            constraintLayout.setOnClickListener((View view) -> this.handleCalendarPick(itemView, view));
                        }
                    }catch(Exception ignored){}
                }
            }
        }

        private TextView getTextViewByID(String id){
            int resourceID = 0;
            try{
                Field field = R.id.class.getDeclaredField(id);
                resourceID = field.getInt(field);
            }catch(Exception ignored){}
            return resourceID == 0 ? null : this.itemView.findViewById(resourceID);
        }

        public LessonCalendarViewHolder(View itemView, Course course, LessonCalendarAdapter lessonCalendarAdapter){
            super(itemView);
            this.teacherNameLabel = itemView.findViewById(R.id.teacherName);
            this.lessonCalendarAdapter = lessonCalendarAdapter;
            this.course = course;
            this.bindEvents(itemView);
        }

        public TextView getTeacherNameLabel(){
            return this.teacherNameLabel;
        }

        public void setAvailableCourseLesson(AvailableCourseLesson availableCourseLesson){
            this.availableCourseLesson = availableCourseLesson;
        }

        public TextView getTickByLessonSlot(LessonSlot lessonSlot){
            return this.getTickByDateTime(lessonSlot.getDay(), lessonSlot.getHour());
        }

        public TextView getDayTextByLessonSlot(LessonSlot lessonSlot){
            return this.getDayTextByDateTime(lessonSlot.getDay(), lessonSlot.getHour());
        }

        public TextView getHourTextByLessonSlot(LessonSlot lessonSlot){
            return this.getHourTextByDateTime(lessonSlot.getDay(), lessonSlot.getHour());
        }

        public TextView getDayTextByDateTime(int day, int hour){
            return this.getTextViewByID("c" + day + "_" + hour + "_day");
        }

        public TextView getHourTextByDateTime(int day, int hour){
            return this.getTextViewByID("c" + day + "_" + hour + "_hour");
        }

        public TextView getTickByDateTime(int day, int hour){
            return this.getTextViewByID("c" + day + "_" + hour + "_tick");
        }
    }
}

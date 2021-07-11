package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AvailableCourseLesson implements Parcelable {
    public static final Parcelable.Creator<AvailableCourseLesson> CREATOR = new Creator<AvailableCourseLesson>(){
        @Override
        public AvailableCourseLesson createFromParcel(Parcel in){
            return new AvailableCourseLesson(in);
        }

        @Override
        public AvailableCourseLesson[] newArray(int size){
            return new AvailableCourseLesson[size];
        }
    };

    private ArrayList<LessonSlot> lessonSlots;
    private Teacher teacher;

    protected AvailableCourseLesson(Parcel parcel){
        this.lessonSlots = new ArrayList<>();
        parcel.readList(this.lessonSlots, LessonSlot.class.getClassLoader());
        this.teacher = parcel.readParcelable(Teacher.class.getClassLoader());
    }

    public AvailableCourseLesson(){}

    public AvailableCourseLesson(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public ArrayList<LessonSlot> getLessonSlots() {
        return this.lessonSlots;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public AvailableCourseLesson setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        JSONArray lessons = jsonObject.getJSONArray("slots");
        this.lessonSlots = new ArrayList<>();
        for ( int i = 0 ; i < lessons.length() ; i++ ){
            this.lessonSlots.add(new LessonSlot(lessons.getJSONObject(i)));
        }
        this.teacher = new Teacher(jsonObject.getJSONObject("teacher"));
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeList(this.lessonSlots);
        parcel.writeParcelable(this.teacher, flags);
    }
}

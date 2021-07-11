package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class AvailableCourse implements Parcelable {
    public static final Parcelable.Creator<AvailableCourse> CREATOR = new Creator<AvailableCourse>(){
        @Override
        public AvailableCourse createFromParcel(Parcel parcel){
            return new AvailableCourse(parcel);
        }

        @Override
        public AvailableCourse[] newArray(int size){
            return new AvailableCourse[size];
        }
    };

    private ArrayList<AvailableCourseLesson> availableCourseLessons;
    private Course course;

    protected AvailableCourse(Parcel parcel) {
        this.availableCourseLessons = new ArrayList<>();
        parcel.readList(this.availableCourseLessons, AvailableCourseLesson.class.getClassLoader());
        this.course = parcel.readParcelable(Course.class.getClassLoader());
    }

    public AvailableCourse(){}

    public AvailableCourse(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public Course getCourse(){
        return this.course;
    }

    public ArrayList<AvailableCourseLesson> getAvailableCourseLessons(){
        return this.availableCourseLessons;
    }

    public AvailableCourse setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.course = new Course().setPropertiesFromJSONObject(jsonObject.getJSONObject("course"));
        JSONArray lessons = jsonObject.getJSONArray("lessons");
        this.availableCourseLessons = new ArrayList<>();
        for ( int i = 0 ; i < lessons.length() ; i++ ){
            this.availableCourseLessons.add(new AvailableCourseLesson(lessons.getJSONObject(i)));
        }
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.availableCourseLessons);
        parcel.writeParcelable(this.course, flags);
    }
}

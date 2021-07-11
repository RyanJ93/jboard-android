package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class CourseList implements Parcelable {
    public static final Parcelable.Creator<CourseList> CREATOR = new Creator<CourseList>(){
        @Override
        public CourseList createFromParcel(Parcel parcel){
            return new CourseList(parcel);
        }

        @Override
        public CourseList[] newArray(int size){
            return new CourseList[size];
        }
    };

    private ArrayList<Course> courseList;

    public CourseList(Parcel parcel){
        this.courseList = new ArrayList<>();
        parcel.readList(this.courseList, Course.class.getClassLoader());
    }

    public CourseList(){}

    public CourseList(JSONArray jsonArray) throws JSONException {
        this.setPropertiesFromJSONObject(jsonArray);
    }

    public ArrayList<Course> getCourseList(){
        return this.courseList;
    }

    public CourseList setPropertiesFromJSONObject(JSONArray jsonArray) throws JSONException {
        this.courseList = new ArrayList<>();
        for ( int i = 0 ; i < jsonArray.length() ; i++ ){
            this.courseList.add(new Course(jsonArray.getJSONObject(i)));
        }
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.courseList);
    }
}

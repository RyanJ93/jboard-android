package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jboard.AvailableLessonList;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AvailableCourseList implements Parcelable {
    public static final Parcelable.Creator<AvailableCourseList> CREATOR = new Creator<AvailableCourseList>(){
        @Override
        public AvailableCourseList createFromParcel(Parcel parcel){
            return new AvailableCourseList(parcel);
        }

        @Override
        public AvailableCourseList[] newArray(int size){
            return new AvailableCourseList[size];
        }
    };

    private ArrayList<AvailableCourse> availableCourses;

    public AvailableCourseList(Parcel parcel){
        this.availableCourses = new ArrayList<>();
        parcel.readList(this.availableCourses, AvailableCourse.class.getClassLoader());
        this.computeEligibility();
    }

    public AvailableCourseList(){}

    public AvailableCourseList(JSONArray jsonArray) throws JSONException {
        this.setPropertiesFromJSONObject(jsonArray);
    }

    public ArrayList<AvailableCourse> getAvailableCourses(){
        return this.availableCourses;
    }

    public AvailableCourseList setPropertiesFromJSONObject(JSONArray jsonArray) throws JSONException {
        this.availableCourses = new ArrayList<>();
        for ( int i = 0 ; i < jsonArray.length() ; i++ ){
            this.availableCourses.add(new AvailableCourse(jsonArray.getJSONObject(i)));
        }
        return this.computeEligibility();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.availableCourses);
    }

    public AvailableCourseList computeEligibility(){
        for ( int i = 0 ; i < this.availableCourses.size() ; i++ ){
            ArrayList<AvailableCourseLesson> availableCourseLessons = this.availableCourses.get(i).getAvailableCourseLessons();
            HashSet<String> index = new HashSet<>();
            for ( int j = 0 ; j < availableCourseLessons.size() ; j++ ){
                ArrayList<LessonSlot> lessonSlots = availableCourseLessons.get(j).getLessonSlots();
                for ( int k = 0 ; k < lessonSlots.size() ; k++ ){
                    if ( !index.contains(j + ":" + k) ){
                        lessonSlots.get(k).setIsEligible(true);
                        if ( !lessonSlots.get(k).isAvailable() && lessonSlots.get(k).isMine() ){
                            ArrayList<AvailableCourseLesson> list = this.availableCourses.get(i).getAvailableCourseLessons();
                            for ( int n = 0 ; n < list.size() ; n++ ){
                                if ( n != j ){
                                    list.get(n).getLessonSlots().get(k).setIsEligible(false);
                                    index.add(n + ":" + k);
                                }
                            }
                        }
                    }
                }
            }
        }
        return this;
    }
}

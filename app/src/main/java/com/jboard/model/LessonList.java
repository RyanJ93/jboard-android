package com.jboard.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class LessonList implements Parcelable {
    public static final Parcelable.Creator<LessonList> CREATOR = new Creator<LessonList>(){
        @Override
        public LessonList createFromParcel(Parcel parcel){
            return new LessonList(parcel);
        }

        @Override
        public LessonList[] newArray(int size){
            return new LessonList[size];
        }
    };

    private ArrayList<Lesson> completedLessonList;
    private ArrayList<Lesson> canceledLessonList;
    private ArrayList<Lesson> activeLessonList;

    public LessonList(Parcel parcel){
        this.completedLessonList = new ArrayList<>();
        this.canceledLessonList = new ArrayList<>();
        this.activeLessonList = new ArrayList<>();
        parcel.readList(this.completedLessonList, Lesson.class.getClassLoader());
        parcel.readList(this.canceledLessonList, Lesson.class.getClassLoader());
        parcel.readList(this.activeLessonList, Lesson.class.getClassLoader());
    }

    public LessonList(){}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LessonList(JSONArray jsonArray) throws JSONException {
        this.setPropertiesFromJSONObject(jsonArray);
    }

    public ArrayList<Lesson> getCompletedLessonList(){
        return this.completedLessonList;
    }

    public ArrayList<Lesson> getCanceledLessonList(){
        return this.canceledLessonList;
    }

    public ArrayList<Lesson> getActiveLessonList(){
        return this.activeLessonList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void applyOrdering(){
        if ( this.completedLessonList != null && this.completedLessonList.size() > 0 ){
            this.completedLessonList.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        }
        if ( this.canceledLessonList != null && this.canceledLessonList.size() > 0 ){
            this.canceledLessonList.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        }
        if ( this.activeLessonList != null && this.activeLessonList.size() > 0 ){
            this.activeLessonList.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LessonList setPropertiesFromJSONObject(JSONArray jsonArray) throws JSONException {
        this.completedLessonList = new ArrayList<>();
        this.canceledLessonList = new ArrayList<>();
        this.activeLessonList = new ArrayList<>();
        for ( int i = 0 ; i < jsonArray.length() ; i++ ){
            Lesson lesson = new Lesson(jsonArray.getJSONObject(i));
            if ( lesson.getDeletedAt() != null ){
                this.canceledLessonList.add(lesson);
            }else if ( lesson.getCompleted() ){
                this.completedLessonList.add(lesson);
            }else{
                this.activeLessonList.add(lesson);
            }
        }
        this.applyOrdering();
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.completedLessonList);
        parcel.writeList(this.canceledLessonList);
        parcel.writeList(this.activeLessonList);
    }
}

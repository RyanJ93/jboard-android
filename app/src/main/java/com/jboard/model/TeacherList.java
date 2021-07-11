package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class TeacherList implements Parcelable {
    public static final Parcelable.Creator<TeacherList> CREATOR = new Creator<TeacherList>(){
        @Override
        public TeacherList createFromParcel(Parcel parcel){
            return new TeacherList(parcel);
        }

        @Override
        public TeacherList[] newArray(int size){
            return new TeacherList[size];
        }
    };

    private ArrayList<Teacher> teacherList;

    public TeacherList(Parcel parcel){
        this.teacherList = new ArrayList<>();
        parcel.readList(this.teacherList, Teacher.class.getClassLoader());
    }

    public TeacherList(){}

    public TeacherList(JSONArray jsonArray) throws JSONException {
        this.setPropertiesFromJSONObject(jsonArray);
    }

    public ArrayList<Teacher> getTeacherList(){
        return this.teacherList;
    }

    public TeacherList setPropertiesFromJSONObject(JSONArray jsonArray) throws JSONException {
        this.teacherList = new ArrayList<>();
        for ( int i = 0 ; i < jsonArray.length() ; i++ ){
            this.teacherList.add(new Teacher(jsonArray.getJSONObject(i)));
        }
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.teacherList);
    }
}

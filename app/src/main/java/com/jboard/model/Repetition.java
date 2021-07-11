package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Repetition implements Parcelable {
    public static final Parcelable.Creator<Repetition> CREATOR = new Creator<Repetition>(){
        @Override
        public Repetition createFromParcel(Parcel parcel){
            return new Repetition(parcel);
        }

        @Override
        public Repetition[] newArray(int size){
            return new Repetition[size];
        }
    };

    private int id = 0;
    private Teacher teacher = null;
    private Course course = null;

    protected Repetition(Parcel parcel){
        this.id = parcel.readInt();
        this.course = parcel.readParcelable(Course.class.getClassLoader());
        this.teacher = parcel.readParcelable(Teacher.class.getClassLoader());
    }

    public Repetition(){}

    public Repetition(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public int getID(){
        return this.id;
    }

    public Course getCourse(){
        return this.course;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public Repetition setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.course = new Course(jsonObject.getJSONObject("course"));
        this.teacher = new Teacher(jsonObject.getJSONObject("teacher"));
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(this.id);
        parcel.writeParcelable(this.course, flags);
        parcel.writeParcelable(this.teacher, flags);
    }
}

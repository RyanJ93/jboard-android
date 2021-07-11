package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Course implements Parcelable {
    public static final Parcelable.Creator<Course> CREATOR = new Creator<Course>(){
        @Override
        public Course createFromParcel(Parcel parcel){
            return new Course(parcel);
        }

        @Override
        public Course[] newArray(int size){
            return new Course[size];
        }
    };

    private String title;
    private int id;

    protected Course(Parcel parcel) {
        this.title = parcel.readString();
        this.id = parcel.readInt();
    }

    public Course(){}

    public Course(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public String getTitle(){
        return this.title;
    }

    public int getID(){
        return this.id;
    }

    public Course setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.title = jsonObject.getString("title");
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(this.title);
        parcel.writeInt(this.id);
    }
}

package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Teacher implements Parcelable {
    public static final Parcelable.Creator<Teacher> CREATOR = new Creator<Teacher>(){
        @Override
        public Teacher createFromParcel(Parcel parcel){
            return new Teacher(parcel);
        }

        @Override
        public Teacher[] newArray(int size){
            return new Teacher[size];
        }
    };

    private int id;
    private String name;
    private String surname;

    protected Teacher(Parcel parcel){
        this.id = parcel.readInt();
        this.name = parcel.readString();
        this.surname = parcel.readString();
    }

    public Teacher(){}

    public Teacher(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public int getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getFullName(){
        String fullName = "";
        if ( this.name != null && !this.name.isEmpty() ){
            fullName = this.name;
        }
        if ( this.surname != null && !this.surname.isEmpty() ){
            fullName += fullName.isEmpty() ? this.surname : ( " " + this.surname );
        }
        return fullName.isEmpty() ? null : fullName;
    }

    public Teacher setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.name = jsonObject.getString("name");
        this.surname = jsonObject.getString("surname");
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.surname);
    }
}

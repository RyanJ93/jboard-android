package com.jboard.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import com.jboard.service.UserService;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class Lesson implements Parcelable {
    public static final Parcelable.Creator<Lesson> CREATOR = new Creator<Lesson>(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Lesson createFromParcel(Parcel parcel){
            return new Lesson(parcel);
        }

        @Override
        public Lesson[] newArray(int size){
            return new Lesson[size];
        }
    };

    private int id;
    private User user;
    private Course course;
    private Teacher teacher;
    private int day;
    private int hour;
    private boolean completed = false;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Lesson(Parcel parcel){
        this.id = parcel.readInt();
        this.user = parcel.readParcelable(User.class.getClassLoader());
        this.course = parcel.readParcelable(Course.class.getClassLoader());
        this.teacher = parcel.readParcelable(Teacher.class.getClassLoader());
        this.day = parcel.readInt();
        this.hour = parcel.readInt();
        this.completed = parcel.readBoolean();
        this.createdAt = (Date)parcel.readSerializable();
        this.updatedAt = (Date)parcel.readSerializable();
        this.deletedAt = (Date)parcel.readSerializable();
    }

    public Lesson(){}

    public Lesson(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public int getID(){
        return this.id;
    }

    public User getUser(){
        return this.user;
    }

    public Course getCourse(){
        return this.course;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public int getDay() {
        return this.day;
    }

    public String getDayName(){
        String dayName = null;
        switch ( this.day ){
            case 1: {
                dayName = "Mon";
            }break;
            case 2: {
                dayName = "Tue";
            }break;
            case 3: {
                dayName = "Wen";
            }break;
            case 4: {
                dayName = "Thu";
            }break;
            case 5: {
                dayName = "Fri";
            }break;
        }
        return dayName;
    }

    public int getHour() {
        return this.hour;
    }

    public String getHourRange(){
        return this.hour + ":00 - " + ( this.hour + 1 ) + ":00";
    }

    public String getDateTimeString(){
        return this.getDayName() + ", " + this.getHourRange();
    }

    public boolean getCompleted(){
        return this.completed;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public Lesson setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.user = jsonObject.has("user") ? new User(jsonObject.getJSONObject("user")) : UserService.getAuthenticatedUser();
        this.course = new Course(jsonObject.getJSONObject("course"));
        this.teacher = new Teacher(jsonObject.getJSONObject("teacher"));
        this.day = jsonObject.getInt("day");
        this.hour = jsonObject.getInt("hour");
        this.completed = jsonObject.getBoolean("completed");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        this.createdAt = this.updatedAt = this.deletedAt = null;
        try{
            this.createdAt = dateFormat.parse(jsonObject.getString("createdAt"));
        }catch(ParseException ignored){}
        try{
            this.updatedAt = dateFormat.parse(jsonObject.getString("updatedAt"));
        }catch(ParseException ignored){}
        if ( jsonObject.has("deletedAt") ){
            try{
                this.deletedAt = dateFormat.parse(jsonObject.getString("deletedAt"));
            }catch(ParseException ignored){}
        }
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(this.id);
        parcel.writeParcelable(this.user, flags);
        parcel.writeParcelable(this.course, flags);
        parcel.writeParcelable(this.teacher, flags);
        parcel.writeInt(this.day);
        parcel.writeInt(this.hour);
        parcel.writeBoolean(this.completed);
        parcel.writeSerializable(this.createdAt);
        parcel.writeSerializable(this.updatedAt);
        parcel.writeSerializable(this.deletedAt);
    }
}

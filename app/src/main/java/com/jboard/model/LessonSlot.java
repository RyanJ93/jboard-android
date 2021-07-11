package com.jboard.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;

public class LessonSlot implements Parcelable {
    public static final Parcelable.Creator<LessonSlot> CREATOR = new Creator<LessonSlot>(){
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public LessonSlot createFromParcel(Parcel parcel){
            return new LessonSlot(parcel);
        }

        @Override
        public LessonSlot[] newArray(int size){
            return new LessonSlot[size];
        }
    };

    private boolean isAvailable;
    private boolean isEligible;
    private boolean isMine;
    private Lesson lesson;
    private int hour;
    private int day;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected LessonSlot(Parcel parcel){
        this.hour = parcel.readInt();
        this.isAvailable = parcel.readBoolean();
        this.isEligible = parcel.readBoolean();
        this.isMine = parcel.readBoolean();
        this.day = parcel.readInt();
        this.lesson = parcel.readParcelable(Lesson.class.getClassLoader());
    }

    public LessonSlot(){}

    public LessonSlot(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public boolean isAvailable(){
        return this.isAvailable;
    }

    public LessonSlot setIsEligible(boolean isEligible){
        this.isEligible = isEligible;
        return this;
    }

    public boolean isEligible(){
        return this.isEligible;
    }

    public boolean isMine(){
        return this.isMine;
    }

    public LessonSlot setLesson(Lesson lesson, boolean updateOwnership){
        this.lesson = lesson;
        if ( updateOwnership ){
            boolean isEmptyLesson = lesson == null;
            this.isAvailable = isEmptyLesson;
            this.isMine = !isEmptyLesson;
        }
        return this;
    }

    public Lesson getLesson(){
        return this.lesson;
    }

    public int getHour() {
        return this.hour;
    }

    public int getDay() {
        return this.day;
    }

    public LessonSlot setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.hour = jsonObject.getInt("hour");
        this.isAvailable = jsonObject.getInt("available") == 1;
        this.isMine = jsonObject.getInt("isMine") == 1;
        this.day = jsonObject.getInt("day");
        this.lesson = jsonObject.has("lesson") ? new Lesson(jsonObject.getJSONObject("lesson")) : null;
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(this.hour);
        parcel.writeBoolean(this.isAvailable);
        parcel.writeBoolean(this.isEligible);
        parcel.writeBoolean(this.isMine);
        parcel.writeInt(this.day);
        parcel.writeParcelable(this.lesson, flags);
    }
}

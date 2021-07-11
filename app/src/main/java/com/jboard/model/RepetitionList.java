package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class RepetitionList implements Parcelable {
    public static final Parcelable.Creator<RepetitionList> CREATOR = new Creator<RepetitionList>(){
        @Override
        public RepetitionList createFromParcel(Parcel parcel){
            return new RepetitionList(parcel);
        }

        @Override
        public RepetitionList[] newArray(int size){
            return new RepetitionList[size];
        }
    };

    private ArrayList<Repetition> repetitionList;

    public RepetitionList(Parcel parcel){
        this.repetitionList = new ArrayList<>();
        parcel.readList(this.repetitionList, Repetition.class.getClassLoader());
    }

    public RepetitionList(){}

    public RepetitionList(JSONArray jsonArray) throws JSONException {
        this.setPropertiesFromJSONObject(jsonArray);
    }

    public ArrayList<Repetition> getRepetitionList(){
        return this.repetitionList;
    }

    public RepetitionList setPropertiesFromJSONObject(JSONArray jsonArray) throws JSONException {
        this.repetitionList = new ArrayList<>();
        for ( int i = 0 ; i < jsonArray.length() ; i++ ){
            this.repetitionList.add(new Repetition(jsonArray.getJSONObject(i)));
        }
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeList(this.repetitionList);
    }
}

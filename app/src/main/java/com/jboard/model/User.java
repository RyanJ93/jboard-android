package com.jboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    public static final Parcelable.Creator<User> CREATOR = new Creator<User>(){
        @Override
        public User createFromParcel(Parcel parcel){
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size){
            return new User[size];
        }
    };

    private int id;
    private String role;
    private String account;

    protected User(Parcel parcel){
        this.id = parcel.readInt();
        this.role = parcel.readString();
        this.account = parcel.readString();
    }

    public User(){}

    public User(JSONObject jsonObject) throws JSONException {
        this.setPropertiesFromJSONObject(jsonObject);
    }

    public int getID(){
        return this.id;
    }

    public String getRole(){
        return this.role;
    }

    public String getAccount(){
        return this.account;
    }

    public boolean isAdmin(){
        return this.role.equals(User.ROLE_ADMIN);
    }

    public User setPropertiesFromJSONObject(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.role = jsonObject.getString("role");
        this.account = jsonObject.getString("account");
        return this;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeInt(this.id);
        parcel.writeString(this.role);
        parcel.writeString(this.account);
    }
}

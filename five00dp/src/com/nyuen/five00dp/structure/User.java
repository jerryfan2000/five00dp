package com.nyuen.five00dp.structure;

import com.nyuen.five00dp.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    
    public int id;
    public String username;
    public String firstname;
    public String lastname;
    public String city;
    public String country;
    public String fullname;
    public String userpic_url;
    public int upgrade_status;
    
    public User (Parcel in) {
        readFromParcelable(in);
    }
    
    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        username = ParcelUtils.readStringFromParcel(in);
        firstname = ParcelUtils.readStringFromParcel(in);
        lastname = ParcelUtils.readStringFromParcel(in);
        city = ParcelUtils.readStringFromParcel(in);
        country = ParcelUtils.readStringFromParcel(in);
        fullname = ParcelUtils.readStringFromParcel(in);
        userpic_url = ParcelUtils.readStringFromParcel(in);
        upgrade_status = in.readInt();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        ParcelUtils.writeStringToParcel(dest, username);
        ParcelUtils.writeStringToParcel(dest, firstname);
        ParcelUtils.writeStringToParcel(dest, lastname);
        ParcelUtils.writeStringToParcel(dest, city);
        ParcelUtils.writeStringToParcel(dest, country);
        ParcelUtils.writeStringToParcel(dest, fullname);
        ParcelUtils.writeStringToParcel(dest, userpic_url);
        dest.writeInt(upgrade_status);
    }
    
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in); 
        }

        @Override
        public User[] newArray(int size) {
            return new User [size];
        }
    };
}

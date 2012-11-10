package com.nyuen.test_fivehundred.structure;

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
        username = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        city = in.readString();
        country = in.readString();
        fullname = in.readString();
        userpic_url = in.readString();
        upgrade_status = in.readInt();
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(fullname);
        dest.writeString(userpic_url);
        dest.writeInt(upgrade_status);
    }

}

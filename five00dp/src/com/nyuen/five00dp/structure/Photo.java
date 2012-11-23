package com.nyuen.five00dp.structure;

import com.nyuen.five00dp.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public int id;
    public String name;
    public String description;
    public int times_viewed;
    public double rating;
    public String created_at;
    public int category;
    public boolean privacy;
    public int width;
    public int height;
    public int votes_count;
    public int favorites_count;
    public int comments_count;
    public boolean nsfw;
    public String image_url;
    public User user;
    
    //extended members
//    cameras
//    lens
//    focal_length
//    iso
//    shutter_speed
//    aperture
//    status
//    
    public Photo (Parcel in) {
        readFromParcelable(in);
    }
    
    private void readFromParcelable(Parcel in) {
        id = in.readInt();
        name = ParcelUtils.readStringFromParcel(in);
        description = ParcelUtils.readStringFromParcel(in);
        times_viewed = in.readInt();
        rating = in.readDouble();
        created_at = ParcelUtils.readStringFromParcel(in);
        category = in.readInt();
        privacy = (in.readInt() == 0) ? false : true;    
        width = in.readInt();
        height = in.readInt();
        votes_count = in.readInt();
        favorites_count = in.readInt();
        comments_count = in.readInt();
        nsfw = (in.readInt() == 0) ? false : true;;
        image_url = ParcelUtils.readStringFromParcel(in);
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        ParcelUtils.writeStringToParcel(dest, name);
        ParcelUtils.writeStringToParcel(dest, description);
        dest.writeInt(times_viewed);
        dest.writeDouble(rating);
        ParcelUtils.writeStringToParcel(dest, created_at);
        dest.writeInt(category);
        dest.writeInt(privacy ? 1 : 0);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(votes_count);
        dest.writeInt(favorites_count);
        dest.writeInt(comments_count);
        dest.writeInt(nsfw ? 1 : 0);
        ParcelUtils.writeStringToParcel(dest, image_url); 
        dest.writeParcelable(user, flags);   
    }
    
    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in); 
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo [size];
        }
    };

}

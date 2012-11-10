package com.nyuen.test_fivehundred.structure;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public String id;
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
    
    public Photo (Parcel in) {
        readFromParcelable(in);
    }
    
    private void readFromParcelable(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        times_viewed = in.readInt();
        rating = in.readDouble();
        created_at = in.readString();
        category = in.readInt();
        
        boolean [] b = new boolean[1]; 
        in.readBooleanArray(b);
        privacy = b[0];
        
        width = in.readInt();
        height = in.readInt();
        votes_count = in.readInt();
        favorites_count = in.readInt();
        comments_count = in.readInt();
        
        b = new boolean[1];
        in.readBooleanArray(b);
        nsfw = b[0];
        
        image_url = in.readString();
        user = new User(in); 
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        ParcelUtils.writeStringToParcel(dest, id);
//        ParcelUtils.writeStringToParcel(dest, name);
//        ParcelUtils.writeStringToParcel(dest, description);
//        dest.writeString(val)
//        ParcelUtils.writeStringToParcel(dest, created_at);
//        ParcelUtils.writeStringToParcel(dest, image_url);      
        
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(times_viewed);
        dest.writeDouble(rating);
        dest.writeString(created_at);
        dest.writeInt(category);
        dest.writeBooleanArray(new boolean[] {privacy});
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(votes_count);
        dest.writeInt(favorites_count);
        dest.writeInt(comments_count);
        dest.writeBooleanArray(new boolean[] {nsfw});
        dest.writeString(image_url);
        user.writeToParcel(dest, flags);
        
    }
}

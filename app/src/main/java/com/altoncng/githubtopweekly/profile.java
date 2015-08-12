package com.altoncng.githubtopweekly;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eye on 8/11/2015.
 */
public class profile implements Parcelable {

    String name;
    String profileLink;
    String pictureLink;

    public profile(){}

    public profile(String name, String profileLink, String pictureLink){
        this.name = name;
        this.profileLink = profileLink;
        this.pictureLink = pictureLink;
    }

    public profile(Parcel in) {
        readFromParcel(in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] ary = {name, profileLink, pictureLink};
        dest.writeStringArray(ary);
    }

    public void readFromParcel(Parcel in){
        String[] ary = new String[3];
        in.readStringArray(ary);
        name = ary[0];
        profileLink = ary[1];
        pictureLink = ary[2];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public profile createFromParcel(Parcel in) {
            return new profile(in);
        }

        public profile[] newArray(int size) {
            return new profile[size];
        }
    };
}

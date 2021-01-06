package com.example.lab2;

import android.os.Parcel;
import android.os.Parcelable;

public enum ActionType implements Parcelable {
    START,
    STOP,
    PREV,
    NEXT;


    public static final Creator<ActionType> CREATOR = new Creator<ActionType>() {
        @Override
        public ActionType createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public ActionType[] newArray(int size) {
            return new ActionType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }
}

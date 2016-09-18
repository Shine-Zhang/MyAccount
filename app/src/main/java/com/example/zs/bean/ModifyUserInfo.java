package com.example.zs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuqi on 2016/9/14 0014.
 */
public class ModifyUserInfo implements Parcelable {
    boolean isIncome;
    int id;
    int year;
    int month;
    int day;
    String remarks;
    String photo;
    int resourceID;
    String categoryName;

    public ModifyUserInfo( boolean isIncome, int id,  int year,int month, int day,
                           String remarks,String photo,  int resourceID,String categoryName ) {
        this.year = year;
        this.categoryName = categoryName;
        this.day = day;
        this.id = id;
        this.isIncome = isIncome;
        this.month = month;
        this.photo = photo;
        this.remarks = remarks;
        this.resourceID = resourceID;
    }

    public static final Creator<ModifyUserInfo> CREATOR = new Creator<ModifyUserInfo>() {
        @Override
        public ModifyUserInfo createFromParcel(Parcel in) {
            boolean isIncome = in.readByte() == 1;
            int id = in.readInt();
            int year = in.readInt();
            int month = in.readInt();
            int day = in.readInt();
            String remarks = in.readString();
            String photo = in.readString();
            int resourceID = in.readInt();
            String categoryName = in.readString();
            return new ModifyUserInfo(isIncome,id,year,month,day,remarks,photo,resourceID,categoryName);
        }

        @Override
        public ModifyUserInfo[] newArray(int size) {
            return new ModifyUserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isIncome?1:0));
        parcel.writeInt(id);
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
        parcel.writeString(remarks);
        parcel.writeString(photo);
        parcel.writeInt(resourceID);
        parcel.writeString(categoryName);
    }

}

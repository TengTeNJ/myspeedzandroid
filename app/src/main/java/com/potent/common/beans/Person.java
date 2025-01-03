package com.potent.common.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gaohaosk
 * @copyright: ©2014 RuanYun
 * @priject Motk
 * @description: TODO< 说明 >
 * @date: 2014/10/13 18:04
 */
public class Person implements Parcelable {
    private int hight;
    private int weight;
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            Person mBook = new Person();
            mBook.hight = source.readInt();
            mBook.weight = source.readInt();
            return mBook;
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(hight);
        parcel.writeInt(weight);
    }
}

package com.pato.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

public final class NoteInfo implements Parcelable {

    // Implement Parcelable interface to make it possible to pass reference-types as Intent-extras
    // Note: When making a type parcelable, all reference types in the class must implement Parcelable interface.

    private CourseInfo mCourse;
    private String mTitle;
    private String mText;

    //Implement Parcelable.Creator CREATOR field which is used to recreate the object from parcel.
    public static final Parcelable.Creator<NoteInfo> CREATOR = new Parcelable.Creator<NoteInfo>(){
        @Override
        public NoteInfo createFromParcel(Parcel parcel) {
            //Responsible to recreate new object-instance and set its member-data from parcel.
            //Note:- Order used to write to parcel must be used to Read from parcel.
            return new NoteInfo(parcel);  //Re-Create object instance from parcel.
        }

        @Override
        public NoteInfo[] newArray(int size) {
            return new NoteInfo[size]; //create array[size] of specified size.
        }
    };

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    private NoteInfo(Parcel parcel) {
        //Recreate object instance from parcel. This constructor is private. only called within this class.

        // A class loader provides information on how to create instance of an object.
        mCourse = parcel.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = parcel.readString();
        mText = parcel.readString();

    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    @Override
    public int describeContents() {
        //we don't have any special handling behavior therefore return o "zero".
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        //Responsible to write member-data to parcel. Order used to write must be used when reading from parcel.

        //Write a parcelable type to parcel. Parameter 2 indicates if we have special handling. most cases = 0
        parcel.writeParcelable(mCourse, 0);
        parcel.writeString(mTitle);
        parcel.writeString(mText);

    }

}

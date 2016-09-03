package com.example.richard_dt.visualisation.Helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.richard_dt.visualisation.gsApiClass.Classroom;

import java.util.ArrayList;

/**
 * Created by Richard-DT on 12/08/2016.
 */
public class ClassParameters implements Parcelable, Comparable<Classroom>{
    private Classroom classroom;
    private ArrayList intensitySelected;
    private ArrayList staffSelected;

    public ClassParameters(Classroom nClassroom, ArrayList<String> nIntensitySelected,ArrayList<String> nStaffSelected){
        classroom = nClassroom;
        intensitySelected = nIntensitySelected;
        staffSelected = nStaffSelected;
    }
    public ClassParameters(){}

    @Override
    public int compareTo(Classroom another) {
        return classroom.compareTo(another);
    }

    public ClassParameters(Classroom nClassroom){
        classroom = nClassroom;
    }

    public ArrayList getIntensitySelected() {
        return intensitySelected;
    }

    public void setIntensitySelected(ArrayList intensitySelected) {
        this.intensitySelected = intensitySelected;
    }

    public ArrayList getStaffSelected() {
        return staffSelected;
    }

    public void setStaffSelected(ArrayList staffSelected) {
        this.staffSelected = staffSelected;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(classroom,flags);
        dest.writeList(intensitySelected);
        dest.writeList(staffSelected);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ClassParameters> CREATOR = new Parcelable.Creator<ClassParameters>() {
        public ClassParameters createFromParcel(Parcel in) {
            return new ClassParameters(in);
        }
        public ClassParameters[] newArray(int size) {
            return new ClassParameters[size];
        }
    };

    private ClassParameters(Parcel in) {
        this.classroom = in.readParcelable(Classroom.class.getClassLoader());
        this.intensitySelected = in.readArrayList(ArrayList.class.getClassLoader());
        this.staffSelected = in.readArrayList(ArrayList.class.getClassLoader());
    }
}

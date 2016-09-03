package com.example.richard_dt.visualisation.Helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.richard_dt.visualisation.gsApiClass.Cours;

import java.util.HashMap;


public class ErreurItem implements Parcelable {
    protected ErreurItem(Parcel in) {
        classroom_name = in.readString();
        cours_date = in.readString();
        Intensite = in.readString();
        ErreurType = in.readString();
        classroomId = in.readInt();
        cours_id = in.readInt();
    }

    public static final Creator<ErreurItem> CREATOR = new Creator<ErreurItem>() {
        @Override
        public ErreurItem createFromParcel(Parcel in) {
            return new ErreurItem(in);
        }

        @Override
        public ErreurItem[] newArray(int size) {
            return new ErreurItem[size];
        }
    };

    public String getClassroom_name() {
        return classroom_name;
    }

    public String getCours_date() {
        return cours_date;
    }

    public String getIntensite() {
        return Intensite;
    }

    public String getErreurType() {
        return ErreurType;
    }

    public int getClassroomId() {
        return classroomId;
    }

    public int getCours_id(){return cours_id;}

    private String classroom_name;
    private String cours_date;
    private String Intensite;
    private String ErreurType;
    private int classroomId;
    private int cours_id;

    public ErreurItem(Cours C, String classroom_name){
        this.classroom_name=classroom_name;
        this.cours_date=C.getClass_starttime()+"//"+C.getClass_date();
        HashMap hm= Cours.getIntensity();
        this.Intensite= (String) hm.get(String.valueOf(C.getClass_level()));
        this.ErreurType=Testeur.test(C)[0];
        this.classroomId=C.getClassroom_id();
        this.cours_id=C.getClass_id();




    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classroom_name);
        dest.writeString(cours_date);
        dest.writeString(Intensite);
        dest.writeString(ErreurType);
        dest.writeInt(classroomId);
        dest.writeInt(cours_id);
    }
}

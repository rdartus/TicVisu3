package com.example.richard_dt.visualisation.gsApiClass;


import android.os.Parcel;
import android.os.Parcelable;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;

public class Staff extends WeekViewEvent implements Parcelable {

    String user_id;
    String user_nom;
    String user_prenom;
    String user_adresse;
    String user_adresse_mail;
    String staff_photo_url;
    String staff_photo_large_url;
    Boolean staff_is_global;
    Boolean staff_is_country ;
    Boolean staff_is_zone;
    Boolean staff_is_teacher;
    Boolean staff_is_host;
    ArrayList<DispoStaff> user_dipsonibilities;
    int staff_issue_type;

    public int getStaff_issue_type(){return staff_issue_type;}

    public String getNomComplet(){return user_prenom +" "+user_nom;}

    protected Staff(Parcel in) {
        user_id = in.readString();
        user_nom = in.readString();
        user_prenom = in.readString();
        user_adresse = in.readString();
        user_adresse_mail = in.readString();
        staff_photo_url = in.readString();
        staff_photo_large_url = in.readString();
        user_dipsonibilities = in.createTypedArrayList(DispoStaff.CREATOR);
        staff_issue_type=in.readInt();
        this.staff_is_host = in.readByte() !=0;
        this.staff_is_teacher = in.readByte() !=0;

    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_nom);
        dest.writeString(user_prenom);
        dest.writeString(user_adresse);
        dest.writeString(user_adresse_mail);
        dest.writeString(staff_photo_url);
        dest.writeString(staff_photo_large_url);
        dest.writeTypedList(user_dipsonibilities);
        dest.writeInt(staff_issue_type);
        dest.writeByte((byte) (staff_is_host? 1 : 0));
        dest.writeByte((byte) (staff_is_teacher? 1 : 0));
    }

    public String getStaff_photo_large_url() {
        return staff_photo_large_url;
    }

    public Boolean getStaff_is_country() {
        return staff_is_country;
    }

    public String getUser_nom() {
        return user_nom;
    }

    public Boolean isTeacher(){return staff_is_teacher;};

    public Boolean isHost(){return staff_is_host;};

    public String getUser_adresse() {
        return user_adresse;
    }

    public String getUser_adresse_mail() {
        return user_adresse_mail;
    }

    public String getUser_prenom() {
        return user_prenom;
    }

    public String getUser_id() {
        return user_id;
    }

    public ArrayList<DispoStaff> getUser_dipsonibilities() {
        return user_dipsonibilities;
    }
}
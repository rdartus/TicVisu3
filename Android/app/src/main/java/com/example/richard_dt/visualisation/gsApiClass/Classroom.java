package com.example.richard_dt.visualisation.gsApiClass;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by thibault on 05/05/2016.
 */
public class Classroom implements Parcelable{

    private  int classroom_id;
    private int location_id;

    private int region_id;

    private String classroom_country_code;

    private String classroom_name;
    private String classroom_address;



    private String classroom_zip;
    private String classroom_city;
    private float classroom_gps_lat;
    private float classroom_gps_lon;
    private  boolean classroom_access;
    private int classroom_type;
    private int classroom_capacity;
    private String classroom_description;
    private  boolean classroom_display;
    private  boolean classroom_feature_parking;
    private  boolean classroom_feature_shower;
    private  boolean classroom_feature_locker;
    private  boolean classroom_feature_credit;
    private String classroom_photo_url;


    public Classroom(boolean classroom_feature_credit, int classroom_id, String classroom_name, String classroom_address, String classroom_zip, String classroom_city, String classroom_country_code, int classroom_gps_lat, int classroom_gps_lon, boolean classroom_access, int classroom_type, int classroom_capacity, String classroom_description, boolean classroom_display, boolean classroom_feature_parking, boolean classroom_feature_shower, boolean classroom_feature_locker, String classroom_photo_url, int location_id) {

        this.classroom_feature_credit = classroom_feature_credit;
        this.classroom_id = classroom_id;
        this.classroom_name = classroom_name;
        this.classroom_address = classroom_address;
        this.classroom_zip = classroom_zip;
        this.classroom_city = classroom_city;
        this.classroom_country_code = classroom_country_code;
        this.classroom_gps_lat = classroom_gps_lat;
        this.classroom_gps_lon = classroom_gps_lon;
        this.classroom_access = classroom_access;
        this.classroom_type = classroom_type;
        this.classroom_capacity = classroom_capacity;
        this.classroom_description = classroom_description;
        this.classroom_display = classroom_display;
        this.classroom_feature_parking = classroom_feature_parking;
        this.classroom_feature_shower = classroom_feature_shower;
        this.classroom_feature_locker = classroom_feature_locker;
        this.classroom_photo_url = classroom_photo_url;
        this.location_id = location_id;
    }

    public int getClassroom_id() {
        return classroom_id;
    }

    public String getClassroom_name() {
        return classroom_name;
    }


    public String getClassroom_address() {
        return classroom_address;
    }



    public String getClassroom_zip() {
        return classroom_zip;
    }


    public String getClassroom_city() {
        return classroom_city;
    }


    public String getClassroom_country_code() {
        return classroom_country_code;
    }



    public float getClassroom_gps_lat() {
        return classroom_gps_lat;
    }




    public float getClassroom_gps_lon() {
        return classroom_gps_lon;
    }



    public boolean isClassroom_access() {
        return classroom_access;
    }



    public int getClassroom_type() {
        return classroom_type;
    }


    public int getClassroom_capacity() {
        return classroom_capacity;
    }




    public String getClassroom_description() {
        return classroom_description;
    }

    public void setRegion_id(int region_id){ this.region_id=region_id;  Log.d("tu bodge connard","oui je suis a changer");}

    public boolean isClassroom_display() {
        return classroom_display;
    }



    public boolean isClassroom_feature_parking() {
        return classroom_feature_parking;
    }



    public boolean isClassroom_feature_shower() {
        return classroom_feature_shower;
    }



    public boolean isClassroom_feature_locker() {
        return classroom_feature_locker;
    }


    public boolean isClassroom_feature_credit() {
        return classroom_feature_credit;
    }




    public String getClassroom_photo_url() {
        return classroom_photo_url;
    }




    public int getLocation_id() {
        return location_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(classroom_id);
        dest.writeString(classroom_name);
        dest.writeString(classroom_address);
        dest.writeString(classroom_zip);
        dest.writeString (classroom_city);
        dest.writeString (classroom_country_code);
        dest.writeFloat(classroom_gps_lat);
        dest.writeFloat(classroom_gps_lon);
        dest.writeByte((byte) (classroom_access ? 1 : 0));
        dest.writeInt(classroom_capacity);
        dest.writeInt(classroom_type);
        dest.writeString(classroom_description);
        dest.writeByte((byte) (classroom_display ? 1 : 0));
        dest.writeByte((byte) (classroom_feature_parking ? 1 : 0));
        dest.writeByte((byte) (classroom_feature_shower ? 1 : 0));
        dest.writeByte((byte) (classroom_feature_locker? 1 : 0));
        dest.writeByte((byte) (classroom_feature_credit? 1 : 0));
        dest.writeString(classroom_photo_url);
        dest.writeInt(location_id);
        dest.writeInt(region_id);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Classroom> CREATOR = new Parcelable.Creator<Classroom>() {
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };

    private Classroom(Parcel in) {
        this.classroom_id = in.readInt();
        this.classroom_name = in.readString();
        this.classroom_address = in.readString();
        this.classroom_zip = in.readString();
        this.classroom_city = in.readString();
        this.classroom_country_code = in.readString();
        this.classroom_gps_lat = in.readFloat();
        this.classroom_gps_lon = in.readFloat();
        this.classroom_access  = in.readByte() != 0;
        this.classroom_type  = in.readInt();
        this.classroom_capacity = in.readInt();
        this.classroom_description = in.readString();
        this.classroom_display = in.readByte() != 0;
        this.classroom_feature_parking = in.readByte() != 0;
        this.classroom_feature_shower = in.readByte() != 0;
        this.classroom_feature_locker = in.readByte() != 0;
        this.classroom_feature_credit = in.readByte() != 0;
        this.classroom_photo_url = in.readString();
        this.location_id = in.readInt();
        this.region_id=in.readInt();
    }
}

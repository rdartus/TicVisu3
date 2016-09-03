package com.example.richard_dt.visualisation.gsApiClass;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by thibault on 05/05/2016.
 */
public class Cours extends WeekViewEvent implements Parcelable{
    private int class_id;
    private int class_type;
    private String class_date;
    private String class_startdate;



    private int class_dayofweek;
    private String class_starttime;
    private int  class_duration;
    private int class_capacity;
    private int class_bookable_capacity;
    private int class_level;
    private String class_level_text;
    private String class_level_icon_URL;
    private boolean class_isclosed;
    private boolean class_hasbooking;
    private boolean class_ispacked;

    private int location_id;

    private int classroom_id;
    private ArrayList teacher_list=new ArrayList();
    private ArrayList host_list=new ArrayList();
    private boolean class_pass_green;
    private boolean class_pass_blue;
    private boolean class_pass_yellow;
    private boolean class_pass_white;
    private boolean class_is_free;
    private boolean class_unit_value;
    private String currency;

    private String classroom_country_code;
    private boolean class_has_issue;
    private int class_issue_code;//0pas d'erreur 1 pas de prof 2 pas de host

    public static HashMap<String,String> getIntensity(){

        HashMap<String,String> intensityCorrespondance = new HashMap<>();
        intensityCorrespondance.put("10","Junior");
        intensityCorrespondance.put("20","Basic");
        intensityCorrespondance.put("30","Standard");
        intensityCorrespondance.put("31","75");
        intensityCorrespondance.put("45", "Cardio Flex");
        intensityCorrespondance.put("46", "Cardio Pump");
        intensityCorrespondance.put("47", "Circuit");
        intensityCorrespondance.put("49", "Running");
        intensityCorrespondance.put("50", "Modere");
        intensityCorrespondance.put("60", "Famille");
        intensityCorrespondance.put("65", "Clubbing");
        intensityCorrespondance.put("67", "Dance");
        intensityCorrespondance.put("70", "Stretching");
        intensityCorrespondance.put("90", "Qigong");
        intensityCorrespondance.put("95", "Core");
        intensityCorrespondance.put("96", "Flex");
        intensityCorrespondance.put("97", "Autre");
        return intensityCorrespondance;
    }
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    public ArrayList getTeacher_list() {
        return teacher_list;
    }

    public ArrayList getHost_list() {
        return host_list;
    }

    //long id, String name, String location, Calendar startTime, Calendar endTime
    public  void CalendarSet() {
        if (class_startdate == null) {Log.d("CalendarSet","erreur calendar");}
        else {
            String[] class_Starttime, class_Date;

            int hour, min, year, month, day, duration, min_end, hour_end;

            Calendar startTime = Calendar.getInstance();

            class_Date = this.getClass_date().split("-");
            class_Starttime = this.getClass_starttime().split(":");

            year = Integer.valueOf(class_Date[0]);
            month = Integer.valueOf(class_Date[1]);


                day = Integer.valueOf(class_Date[2]);
                duration = this.getClass_duration();
                min_end = duration % 60;
                hour = Integer.valueOf(class_Starttime[0]);
                min = Integer.valueOf(class_Starttime[1]);
                hour_end = hour + ((duration - min_end) / 60);
                startTime.set(year, month - 1, day, hour, min);
                Calendar endTime = Calendar.getInstance();
                endTime.set(year, month - 1, day, hour_end, min);
                if (min != 0) {
                    endTime.add(Calendar.MINUTE, min_end);
                }
                super.setEndTime(endTime);
                super.setId(this.class_id);
                super.setStartTime(startTime);
                super.setName("Cours :"+getIntensity().get(Integer.toString(this.getClass_level())));
                super.setLocation("");
        }
    }

    public String getClassroom_country_code() {
        return "FR";
    }

    public Boolean hasIssue(){return class_has_issue;}

    public int IssueType(){return class_issue_code;}

    public Cours(int class_id,int class_type, String class_date, String class_startdate, int class_dayofweek, String class_starttime, int class_duration, int class_capacity, int class_bookable_capacity, int class_level, String class_level_text, String class_level_icon_URL, boolean class_isclosed, boolean class_hasbooking, boolean class_ispacked, int location_id, int classroom_id, boolean class_pass_green, boolean class_pass_blue, boolean class_pass_yellow, boolean class_pass_white, boolean class_is_free, boolean class_unit_value, String currency,ArrayList teacher_list, ArrayList host_list) {

        this.class_id=class_id;
        this.class_type = class_type;
        this.class_date = class_date;
        this.class_startdate = class_startdate;
        this.class_dayofweek = class_dayofweek;
        this.class_starttime = class_starttime;
        this.class_duration = class_duration;
        this.class_capacity = class_capacity;
        this.class_bookable_capacity = class_bookable_capacity;
        this.class_level = class_level;
        this.class_level_text = class_level_text;
        this.class_level_icon_URL = class_level_icon_URL;
        this.class_isclosed = class_isclosed;
        this.class_hasbooking = class_hasbooking;
        this.class_ispacked = class_ispacked;
        this.location_id = location_id;
        this.classroom_id = classroom_id;
        this.teacher_list = teacher_list;
        this.host_list = host_list;
        this.class_pass_green = class_pass_green;
        this.class_pass_blue = class_pass_blue;
        this.class_pass_yellow = class_pass_yellow;
        this.class_pass_white = class_pass_white;
        this.class_is_free = class_is_free;
        this.class_unit_value = class_unit_value;
        this.currency = currency;
    }
    public Cours(int class_id,int class_type, String class_date, String class_startdate, int class_dayofweek, String class_starttime, int  class_duration, int class_capacity, int class_bookable_capacity, int class_level, String class_level_text, String class_level_icon_URL, boolean class_isclosed, boolean class_hasbooking, boolean class_ispacked, int location_id, int classroom_id,ArrayList teacher_list,ArrayList host_list, boolean class_pass_green, boolean class_pass_blue, boolean class_pass_yellow, boolean class_pass_white, boolean class_is_free, boolean class_unit_value, String currency) {
        this.class_id=class_id;
        this.class_type = class_type;
        this.class_date = class_date;
        this.class_startdate = class_startdate;
        this.class_dayofweek = class_dayofweek;
        this.class_starttime = class_starttime;
        this.class_duration = class_duration;
        this.class_capacity = class_capacity;
        this.class_bookable_capacity = class_bookable_capacity;
        this.class_level = class_level;
        this.class_level_text = class_level_text;
        this.class_level_icon_URL = class_level_icon_URL;
        this.class_isclosed = class_isclosed;
        this.class_hasbooking = class_hasbooking;
        this.class_ispacked = class_ispacked;
        this.location_id = location_id;
        this.classroom_id = classroom_id;
        this.class_pass_green = class_pass_green;
        this.class_pass_blue = class_pass_blue;
        this.class_pass_yellow = class_pass_yellow;
        this.class_pass_white = class_pass_white;
        this.class_is_free = class_is_free;
        this.class_unit_value = class_unit_value;
        this.currency = currency;
    }

    public int getClass_id() {
        return class_id;
    }

    public int getClass_type() {
        return class_type;
    }



    public String getClass_date() {
        return class_date;
    }



    public String getClass_startdate() {
        return class_startdate;
    }



    public int getClass_dayofweek() {
        return class_dayofweek;
    }



    public String getClass_starttime() {
        return class_starttime;
    }



    public int  getClass_duration() {
        return class_duration;
    }


    public int  getClass_capacity() {
        return class_capacity;
    }



    public int  getClass_bookable_capacity() {
        return class_bookable_capacity;
    }



    public int  getClass_level() {
        return class_level;
    }



    public String getClass_level_text() {
        return class_level_text;
    }


    public String getClass_level_icon_URL() {
        return class_level_icon_URL;
    }


    public boolean isClass_isclosed() {
        return class_isclosed;
    }



    public boolean isClass_hasbooking() {
        return class_hasbooking;
    }


    public boolean isClass_ispacked() {
        return class_ispacked;
    }



    public int getLocation_id() {
        return location_id;
    }



    public int getClassroom_id() {
        return classroom_id;
    }

//
//    public Teacher getTeacher_list() {
//        return teacher_list;
//    }
//
//    public Staff getHost_list() {
//        return host_list;
//    }



    public boolean isClass_pass_green() {
        return class_pass_green;
    }


    public boolean isClass_pass_blue() {
        return class_pass_blue;
    }



    public boolean isClass_pass_yellow() {
        return class_pass_yellow;
    }



    public boolean isClass_pass_white() {
        return class_pass_white;
    }

    public boolean isClass_is_free() {
        return class_is_free;
    }



    public boolean isClass_unit_value() {
        return class_unit_value;
    }

    @Override
    public Calendar getEndTime() {
        return super.getEndTime();
    }

    @Override
    public Calendar getStartTime() {
        return super.getStartTime();
    }

    public String getCurrency() {
        return currency;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(class_id);
        dest.writeInt(class_type);
        dest.writeString(class_date);
        dest.writeString(class_startdate);
        dest.writeInt(class_dayofweek);
        dest.writeString(class_starttime);
        dest.writeInt(class_duration);
        dest.writeInt(class_capacity);
        dest.writeInt(class_bookable_capacity);
        dest.writeInt(class_level);
        dest.writeString (class_level_text);
        dest.writeString(class_level_icon_URL);
        dest.writeByte((byte) (class_isclosed ? 1 : 0));
        dest.writeByte((byte) (class_hasbooking ? 1 : 0));
        dest.writeByte((byte) (class_ispacked ? 1 : 0));
        dest.writeInt(location_id);
        dest.writeInt(classroom_id);
        dest.writeList(host_list);
        dest.writeList(teacher_list);
        dest.writeByte((byte) (class_pass_green? 1 : 0));
        dest.writeByte((byte) (class_pass_blue? 1 : 0));
        dest.writeByte((byte) (class_pass_yellow? 1 : 0));
        dest.writeByte((byte) (class_pass_white? 1 : 0));
        dest.writeByte((byte) (class_is_free? 1 : 0));
        dest.writeByte((byte) (class_unit_value ? 1 : 0));
        dest.writeString (currency);
        dest.writeString (classroom_country_code);
        dest.writeByte((byte) (class_has_issue? 1 : 0));
        dest.writeInt(class_issue_code);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Cours> CREATOR = new Parcelable.Creator<Cours>() {
        public Cours createFromParcel(Parcel in) {
            return new Cours(in);
        }

        public Cours[] newArray(int size) {
            return new Cours[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Cours(Parcel in) {
        this.class_id = in.readInt();
        this.class_type = in.readInt();
        this.class_date = in.readString();
        this.class_startdate = in.readString();
        this.class_dayofweek = in.readInt();
        this.class_starttime = in.readString();
        this.class_duration = in.readInt();
        this.class_capacity = in.readInt();
        this.class_bookable_capacity = in.readInt();
        this.class_level  = in.readInt();
        this.class_level_text  = in.readString();
        this.class_level_icon_URL = in.readString();
        this.class_isclosed = in.readByte() != 0;
        this.class_hasbooking = in.readByte() != 0;
        this.class_ispacked = in.readByte() != 0;
        this.location_id = in.readInt();
        this.classroom_id = in.readInt();
        this.teacher_list = in.readArrayList(ArrayList.class.getClassLoader());
        this.host_list = in.readArrayList(ArrayList.class.getClassLoader());
        this.class_pass_green = in.readByte() !=0;
        this.class_pass_blue = in.readByte() !=0;
        this.class_pass_yellow = in.readByte() !=0;
        this.class_pass_white = in.readByte() !=0;
        this.class_is_free = in.readByte() !=0;
        this.class_unit_value = in.readByte() !=0;
        this.currency = in.readString();
        this.classroom_country_code = in.readString();
        this.class_has_issue= in.readByte() != 0;
        this.class_issue_code=in.readInt();
    }
}

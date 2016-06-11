package com.example.richard_dt.visualisation.gsApiClass;

import android.os.Parcel;
import android.os.Parcelable;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class DispoStaff extends WeekViewEvent implements Parcelable {

    String id;
    String id_user;
    String date_start;
    String date_end;
    ArrayList classrooms_id;
    public String getUser_id(){return id_user;}
   public  void CalendarSet() {

            String[] class_Starttime, class_Date,splitter;

            int hour, min, year, month, day, duration, min_end, hour_end;

            Calendar startTime = Calendar.getInstance();

       //2016-06-01 12:12:00

            splitter = this.date_start.split(" ");
            class_Date = splitter[0].split("-");

            year = Integer.valueOf(class_Date[0]);
            month = Integer.valueOf(class_Date[1]);
            day = Integer.valueOf(class_Date[2]);

            class_Starttime=splitter[1].split(":");

           hour = Integer.valueOf(class_Starttime[0]);
           min = Integer.valueOf(class_Starttime[1]);

            startTime.set(year, month - 1, day, hour, min);



             splitter=this.date_end.split(" ");
               class_Starttime=splitter[1].split(":");

               hour = Integer.valueOf(class_Starttime[0]);
               min = Integer.valueOf(class_Starttime[1]);

            Calendar endTime = Calendar.getInstance();


            endTime.set(year, month - 1, day, hour, min);


            super.setEndTime(endTime);
            super.setId(Integer.valueOf(this.id));
            super.setStartTime(startTime);
            super.setName("Disponibilité");
            super.setLocation("");

    }

    protected DispoStaff(Parcel in) {
        id = in.readString();
        id_user = in.readString();
        date_start = in.readString();
        date_end = in.readString();
        classrooms_id=in.readArrayList(ArrayList.class.getClassLoader());
    }

    public static final Creator<DispoStaff> CREATOR = new Creator<DispoStaff>() {
        @Override
        public DispoStaff createFromParcel(Parcel in) {
            return new DispoStaff(in);
        }

        @Override
        public DispoStaff[] newArray(int size) {
            return new DispoStaff[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_user);
        dest.writeString(date_start);
        dest.writeString(date_end);
        dest.writeList(classrooms_id);
    }
}

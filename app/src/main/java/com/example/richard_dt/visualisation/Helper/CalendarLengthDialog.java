package com.example.richard_dt.visualisation.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.alamkanak.weekview.WeekView;
import com.example.richard_dt.visualisation.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Richard-DT on 09/08/2016.
 */
public class CalendarLengthDialog extends AlertDialog.Builder{

    public CalendarLengthDialog(Context context) {
        super(context);
    }
    /**
     * Create an alert dialog to change the calendar lenght
     * @param context The Activity's context.
     * @param bNextWeek The button to go to next days.
     * @param bPreviousWeek The button to go to previous days.
     * @param startHour The hour to show.
     * @param mWV The whole calendar.
     */
    public CalendarLengthDialog(final Context context, final View bNextWeek, final View bPreviousWeek, final int startHour, final WeekView mWV) {
        super(context);
        int checkedLength = mWV.getNumberOfVisibleDays();
        final ArrayList<String> seletedLengthItems = new ArrayList<>();
        final ArrayList<String> tmpAl = new ArrayList<>();
        tmpAl.add(1 + " " + context.getString(R.string.day));
        tmpAl.add(3 + " " + context.getString(R.string.days));
        tmpAl.add(7 + " " + context.getString(R.string.days));

        for (String s : tmpAl) {
            if (Integer.valueOf(s.split(" ")[0]) == checkedLength) {
                checkedLength = tmpAl.indexOf(s);
            }
        }
        String[] item2 = new String[tmpAl.size()];
        item2 = tmpAl.toArray(item2);
        this.setTitle(context.getString(R.string.calendarFilter));
        this.setSingleChoiceItems(item2, checkedLength, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                seletedLengthItems.clear();
                seletedLengthItems.add(tmpAl.get(which));
            }
        }).setPositiveButton(context.getString(R.string.validate), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String[] strings = seletedLengthItems.get(0).split(" ");
                Calendar dayFromPosition = mWV.getMidlleCalendarDayFromPosition();
                System.out.println("month :"+dayFromPosition.get(Calendar.MONTH)+"day" + dayFromPosition.get(Calendar.DATE));
                mWV.setNumberOfVisibleDays(Integer.valueOf(strings[0]));
                int dayoffset=0;
                switch (mWV.getNumberOfVisibleDays()){
                    case 1:
//                        ((Button)bNextWeek).setText(context.getText(R.string.nextDay));
//                        ((Button)bPreviousWeek).setText(context.getText(R.string.previousDay));
                        break;
                    case 3:
//                        ((Button)bNextWeek).setText(context.getText(R.string.next3Days));
//                        ((Button)bPreviousWeek).setText(context.getText(R.string.previous3Days));
                        dayoffset = 1;
                        break;
                    case 7:
//                        ((Button)bNextWeek).setText(context.getText(R.string.nextWeek));
//                        ((Button)bPreviousWeek).setText(context.getText(R.string.previousWeek));
                        dayoffset = 3;
                        break;
                }
                long dayInMillis = dayFromPosition.getTimeInMillis() - dayoffset * 24 * 60 *60 *1000;
                dayFromPosition.setTimeInMillis(dayInMillis);
                mWV.goToMidlleCalendarDate(dayFromPosition);
                mWV.goToHour(startHour);
            }
        }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }
        );
    }

}

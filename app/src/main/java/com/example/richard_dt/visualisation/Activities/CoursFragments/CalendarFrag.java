package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.Activities.MainFrag;
import com.example.richard_dt.visualisation.Helper.CalendarLengthDialog;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.pref;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.DispoStaff;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Richard-DT on 03/06/2016.
 */
public class CalendarFrag extends Fragment
        implements WeekView.EventClickListener,
        WeekView.EventLongPressListener,
        MonthLoader.MonthChangeListener,
        WeekView.EmptyViewLongPressListener,
        View.OnClickListener {

    private WeekView mWV;


    Bundle b;
    pref p;

    ImageButton ibNextWeek, ibPreviousWeek, ibToday, ibCalendarLength, ibIntensityFilter;
//    Button ibNextWeek, ibPreviousWeek, ibToday, ibCalendarLength, ibIntensityFilter;
    private AlertDialog filterDialog;
    private AlertDialog calendarDialog;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    private ArrayList<Cours> coursDisplayed;
    private ArrayList<Cours> coursList;
    private ArrayList<Staff> staffList;
    private List<String> listPossibleIntensity;
    private ArrayList<String> intensityFilter;
    boolean errorFilter = false;
    private ItemAdapter itemListAdapter;
    private ListView lv;
    private Classroom classroom;
    private DateTimeInterpreter timeInterpreter;


    private static Calendar selectedCalendar;
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_WEEK_VIEW;
    private final static int k = 5;

    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener dsl = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.YEAR, year);
                selectedCalendar.set(Calendar.MONTH, monthOfYear);
                selectedCalendar.set(Calendar.DATE, dayOfMonth);
                mWV.goToMidlleCalendarDate(selectedCalendar);
                mWV.goToHour(p.getInt("startHour"));
            }
        };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_calendrier, null);

        staffList = new ArrayList<>();
        p = new pref(this.getContext());
        this.getContext().getSharedPreferences("mypref", 0).getString("hour", "defautlt");

        timeInterpreter = new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM", Locale.getDefault());
                    return sdf.format(date.getTime()).toUpperCase();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            public String interpretTime(Calendar date) {
                try {
                    SimpleDateFormat sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                    return sdf.format(date.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            public String interpretTime(int hour) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);

                try {
                    SimpleDateFormat sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                    return sdf.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        };

        mWV = (WeekView) rootView.findViewById(R.id.weekView);
        mWV.setOnEventClickListener(this);
        mWV.setMonthChangeListener(this);
        mWV.setEventLongPressListener(this);
        mWV.setNumberOfVisibleDays(7);
        mWV.setDateTimeInterpreter(timeInterpreter);

        mWV.setZoom(p.getInt("zoom"));
        mWV.setZoom(70);
        p.setInt("zoom", 70);


        b = this.getArguments();

        ClassParameters classParameters = b.getParcelable("classParameters")== null?
                new ClassParameters():(ClassParameters)b.getParcelable("classParameters");

            classroom = classParameters.getClassroom();
            coursList = (ArrayList<Cours>) MainActivity.getCoursList(
                    Integer.toString(classParameters.getClassroom().getClassroom_id()), getContext());
            listPossibleIntensity = MainActivity.getListPossibleIntensity(coursList);
            intensityFilter = classParameters.getIntensitySelected() == null ? new ArrayList<>() : classParameters.getIntensitySelected();
            ArrayList<String> selectedStaff = classParameters.getStaffSelected() == null? new ArrayList<>():classParameters.getStaffSelected();
            for (String idStaff : selectedStaff) {
                staffList.addAll(MainFrag.getStaff(idStaff, getContext()));
            }

        boolean setLock = b.getBoolean("setLock");
        Calendar datePicked = Calendar.getInstance();
        datePicked.setTimeInMillis(b.getLong("millis"));


        mWV.goToMidlleCalendarDate(datePicked);
        mWV.goToHour(p.getInt("startHour"));

        ibNextWeek = (ImageButton) rootView.findViewById(R.id.btNextWeek);
        ibPreviousWeek = (ImageButton) rootView.findViewById(R.id.btPreviousWeek);
        ibToday = (ImageButton) rootView.findViewById(R.id.btGoTo);
        ibIntensityFilter = (ImageButton) rootView.findViewById(R.id.bintensityFilter);
//        ibIntensityFilter = (Button) rootView.findViewById(R.id.bintensityFilter);
        ibCalendarLength = (ImageButton) rootView.findViewById(R.id.dateLength);
        ibIntensityFilter.setOnClickListener(this);
        ibPreviousWeek.setOnClickListener(this);
        ibToday.setOnClickListener(this);
        ibNextWeek.setOnClickListener(this);
        ibCalendarLength.setOnClickListener(this);

        //classroomList
        if (listPossibleIntensity == null) {
            listPossibleIntensity = new ArrayList();
            listPossibleIntensity.add("Basic");
        }

        lv = (ListView) rootView.findViewById(R.id.lvCalendar);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar c = (Calendar) itemListAdapter.getItem(position).getId();
                mWV.goToMidlleCalendarDate(c);
                mWV.goToHour(p.getInt("startHour"));
            }
        });

        //creation filter Dialog
        String[] item = new String[listPossibleIntensity.size()];
        item = (String[]) listPossibleIntensity.toArray(item);
        final boolean[] checkedIntensity = new boolean[item.length];
        if (intensityFilter != null && intensityFilter.size() > 0) {
            for (int i = 0; i < item.length; i++) {
                String str = item[i];
                HashMap hm = Cours.getIntensity();
                if (intensityFilter.contains(Cours.getKeyByValue(hm, str))) {
                    checkedIntensity[i] = true;
                } else {
                    checkedIntensity[i] = false;
                }
            }
        } else {
            for (boolean b : checkedIntensity) {
                b = true;
            }
        }

        final ArrayList seletedFilterItems = new ArrayList();
        AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getActivity());
        filterBuilder.setTitle(getString(R.string.intensityfilter));
        filterBuilder.setMultiChoiceItems(item, checkedIntensity,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            // write your code when user checked the checkbox
                            seletedFilterItems.add(indexSelected);
                        } else if (seletedFilterItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            // write your code when user Uchecked the checkbox
                            seletedFilterItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        HashMap hm = Cours.getIntensity();
                        intensityFilter = new ArrayList();
//                        for (int i=0 ; i < seletedFilterItems.size() ; i++) {
//                            intensityFilter.add((String) Cours.getKeyByValue(hm, listPossibleIntensity.get((int) (seletedFilterItems.get(i)))));
//                        }
                        for (int i = 0; i < checkedIntensity.length; i++) {
                            if (checkedIntensity[i]) {
                                intensityFilter.add((String) Cours.getKeyByValue(hm, listPossibleIntensity.get(i)));
                            }
                        }
                        mWV.goToMidlleCalendarDate(mWV.getDayFromPosition());
                        mWV.goToHour(p.getInt("startHour"));
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel

                            }
                        }

                );
        filterDialog = filterBuilder.create();//AlertDialog filterDialog; create like this outside onClick


//        int checkedLength = mWV.getNumberOfVisibleDays();
//        final ArrayList<String> seletedLengthItems = new ArrayList<>();
//        final ArrayList<String> tmpAl = new ArrayList<>();
//        tmpAl.add(1 + " " + getString(R.string.day));
//        tmpAl.add(3 + " " + getString(R.string.days));
//        tmpAl.add(7 + " " + getString(R.string.days));
//
//        for (String s : tmpAl) {
//            if (Integer.valueOf(s.split(" ")[0]) == checkedLength) {
//                checkedLength = tmpAl.indexOf(s);
//            }
//        }
//        String[] item2 = new String[tmpAl.size()];
//        item2 = tmpAl.toArray(item2);
//        AlertDialog.Builder calendarLengthBuilder = new AlertDialog.Builder(getActivity());
//        calendarLengthBuilder.setTitle(getString(R.string.calendarFilter));
//        calendarLengthBuilder.setSingleChoiceItems(item2, checkedLength, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                seletedLengthItems.clear();
//                seletedLengthItems.add(tmpAl.get(which));
//            }
//        }).setPositiveButton(getString(R.string.validate), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                String[] strings = seletedLengthItems.get(0).split(" ");
//                Calendar dayFromPosition = mWV.getMidlleCalendarDayFromPosition();
//                System.out.println("month :"+dayFromPosition.get(Calendar.MONTH)+"day" + dayFromPosition.get(Calendar.DATE));
//                mWV.setNumberOfVisibleDays(Integer.valueOf(strings[0]));
//                int dayoffset=0;
//                switch (mWV.getNumberOfVisibleDays()){
//                    case 1:
//                        ibNextWeek.setText(getText(R.string.nextDay));
//                        ibPreviousWeek.setText(getText(R.string.previousDay));
//                        break;
//                    case 3:
//                        ibNextWeek.setText(getText(R.string.next3Days));
//                        ibPreviousWeek.setText(getText(R.string.previous3Days));
//                        dayoffset = 1;
//                        break;
//                    case 7:
//                        ibNextWeek.setText(getText(R.string.nextWeek));
//                        ibPreviousWeek.setText(getText(R.string.previousWeek));
//                        dayoffset = 3;
//                        break;
//                }
//                long dayInMillis = dayFromPosition.getTimeInMillis() - dayoffset * 24 * 60 *60 *1000;
//                dayFromPosition.setTimeInMillis(dayInMillis);
//                mWV.goToMidlleCalendarDate(dayFromPosition);
//                mWV.goToHour(p.getInt("startHour"));
//            }
//        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                }
//        );

        AlertDialog.Builder calendarLengthBuilder = new CalendarLengthDialog(getContext(), ibNextWeek, ibPreviousWeek,p.getInt("startHour"),mWV);
        calendarDialog = calendarLengthBuilder.create();


        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                dsl,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle(getString(R.string.mdtp_select_day));

//        if (setLock) {
            mWV.lockScroll();
//        } else {
//            mWV.unlockScroll();
//        }
        return rootView;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        if (event.getClass() == Cours.class) {

//            Classroom classroom = MainActivity.getClassroom(Long.toString(((Cours) event).getClassroom_id()),getContext());
            ArrayList<Staff> staffListCoursSelected = new ArrayList<>();
            if (((Cours)event).getHost_list() != null
                    && ((Cours)event).getHost_list().size() != 0) {
                for (Object sId : ((Cours) event).getHost_list()) {
                    staffListCoursSelected.addAll((ArrayList<Staff>) MainActivity.getStaff((String) sId, getContext()));
                }
            }
            if(((Cours)event).getTeacher_list() != null
                    && ((Cours)event).getTeacher_list().size() != 0){
                for(Object sId : ((Cours)event).getTeacher_list())
                {
                    staffListCoursSelected.addAll((ArrayList<Staff>)MainActivity.getStaff((String)sId, getContext()));
                }
            }
            startActivity(new Intent(getContext(), CoursActivity.class)
                    .putExtra("classroom", classroom)
                    .putExtra("staffList", staffListCoursSelected)
                    .putExtra("cours", (Parcelable) event));
        } else if (event.getClass() == DispoStaff.class) {
            DispoStaff d = (DispoStaff) event;
            ArrayList<Staff> stafflist = (ArrayList<Staff>) MainActivity.getStaff(d.getUser_id(), getContext());
//            ArrayList<Staff> stafflist=new ArrayList<>();
//            Parser parser=new Parser(getContext());
//            try {
//                parser.execute(parser.EqualQuery("staff","user_id",d.getUser_id())).get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//            try {
//                stafflist.add(gson.fromJson(String.valueOf(parser.getReturned().get(0)),Staff.class));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            startActivity(new Intent(getContext(), ClassroomDetail.class)
                    .putExtra("stafflist", stafflist)
                    .putExtra("classroom", "staff"));
        }
    }


    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    private ArrayList<Cours> filterIntensity(ArrayList<Cours> listToFilter) {
        ArrayList<Cours> listFiltered = new ArrayList<>();
        for (Cours cours : listToFilter) {
            if (intensityFilter != null && intensityFilter.size() > 0) {
                for (String intensity : intensityFilter) {
                    if (cours.getClass_level() == Integer.valueOf(intensity)) {
                        listFiltered.add(cours);
                    }
                }
            } else {
                return listToFilter;
            }
        }
        return listFiltered;
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        ArrayList<Cours> filteredList = filterIntensity(coursList);
        coursDisplayed = new ArrayList<>();
        coursDisplayed.addAll(filteredList);
        List<WeekViewEvent> events = new ArrayList<>();
        //Log.d("coursfilter",filteredList.getString(0).getLocation_id()+"");


        //adding the events in the Calendar
        for (Cours cours : filteredList) {
            cours.CalendarSet();
            if (cours.getStartTime().isSet(Calendar.MONTH) && (cours.getStartTime().get(Calendar.MONTH) == newMonth)) {
                if (cours.hasIssue()) {
                    cours.setColor(getResources().getColor(R.color.event_color_02));
                    events.add(cours);
                } else {
                    cours.setColor(getResources().getColor(R.color.event_color_01));
                    events.add(cours);
                }
            }
        }

        if (staffList != null) {
            for (Staff staff : staffList) {
                for (DispoStaff dispoStaff : staff.getUser_dipsonibilities()) {
                    dispoStaff.CalendarSet();
                    if (dispoStaff.getStartTime().isSet(Calendar.MONTH) && (dispoStaff.getStartTime().get(Calendar.MONTH) == newMonth)) {
                        dispoStaff.setColor(getResources().getColor(R.color.event_color_03));
                        events.add(dispoStaff);
                    }
                }
            }
        }

        List<Item> al = new ArrayList<>();
        for (Cours c : coursDisplayed) {
            if (c.hasIssue() == true) {
                al.add(al.size(), new Item(timeInterpreter.interpretDate(c.getStartTime()) + " " +
                        timeInterpreter.interpretTime(c.getStartTime()), c.getStartTime()));
            }
        }

        itemListAdapter = new ItemAdapter(al, getActivity());
        lv.setAdapter(itemListAdapter);
        return events;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btGoTo:
                try {
                    final Activity activity = (Activity) getContext();
                    datePickerDialog.show(activity.getFragmentManager(), "dat");
                }catch (Exception e)
                {
                    Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
                }
//                mWV.goToToday();
//                mWV.goToHour(p.getInt("startHour"));
                return;

            case R.id.btNextWeek:
                mWV.goToNextDays();
                mWV.goToHour(p.getInt("startHour"));
                return;
            case R.id.btPreviousWeek:
                mWV.goToPreviousDays();
                mWV.goToHour(p.getInt("startHour"));
                return;

            case R.id.bintensityFilter:
                filterDialog.show();
                break;
            case R.id.dateLength:
                calendarDialog.show();
                break;
        }

    }
}

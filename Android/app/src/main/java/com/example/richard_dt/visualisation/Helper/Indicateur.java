package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Indicateur {

    public static ArrayList<ArrayList> ErreurIteration(String Rcc) {

        ArrayList<ArrayList> ArrayCouple = new ArrayList<>();
        ArrayList<String> testedString = new ArrayList<>();
        ArrayList<String> iteration = new ArrayList<>();

        String test[] = Rcc.split(",");


        ArrayList<String> testing = new ArrayList<>();

        for (int i = 0; i < test.length; i++) {
            testing.add(test[i]);
        }
        Set<String> uniqueSet = new HashSet<>(testing);
        for (String temp : uniqueSet) {


            Log.d("temp", uniqueSet.size() + "");

            testedString.add(String.valueOf(temp));
            iteration.add(String.valueOf(Collections.frequency(testing, temp)));


        }

        ArrayCouple.add(testedString);
        ArrayCouple.add(iteration);
        return ArrayCouple;
    }


    public static List<Item> getSuggestion(float lat, float lon, Context context,final String start,final int end) throws ExecutionException, InterruptedException, JSONException {
        pref  pref=new pref(context);

        int rayon=pref.getInt("rayon");
        Parser p = new Parser(context);
        ArrayList<String> classrooms_id=new ArrayList<>();
        ArrayList<Item> returned=new ArrayList<>();

        p.execute("suggestion/" + lon + "/" + lat+"/"+rayon).get();
        String idClassroom;
        idClassroom = (String) p.getReturned1().get("userids");
        Collections.addAll(classrooms_id, idClassroom.split(","));


        for (int i = 0; i <classrooms_id.size(); i++) {
            returned.addAll(getStafflistSpecial(classrooms_id.get(i),context,start,end));
        }

        return returned;


    }

    public static ArrayList<Item> getStaffList(String ClassRoomId, Context context) {

        ArrayList<Item> StaffList = new ArrayList<>();
        Gson gson=new Gson();

        Parser SpecialP = new Parser(context);

        try {
            SpecialP.execute("dispo/crid/" + ClassRoomId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String idStaff = "";
        try {
            idStaff = (String) SpecialP.getReturned1().get("userids");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Parser Staffparser = new Parser(context);

        try {
            Staffparser.execute(Staffparser.ListQuery("staff", "user_id", idStaff)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        if (StaffList.size() != 0) {
            StaffList.clear();
        }


        for (int i = 0; i < Staffparser.getReturned().length(); i++) {
            Staff s = null;
            try {
                s = gson.fromJson(String.valueOf(Staffparser.getReturned().get(i)), Staff.class);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (s != null) {
                StaffList.add(new Item(s.getName(), s.getUser_id()));
            }

        }
        return StaffList;
    }
    public static ArrayList<Item> getStafflistSpecial(String ClassRoomId, Context context,String start,int end) {

        ArrayList<Item> StaffList = new ArrayList<>();
        Gson gson=new Gson();

        Parser SpecialP = new Parser(context);

        try {
            SpecialP.execute("dispo/crid2/" + ClassRoomId+"/"+start+"/"+end).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String idStaff = "";
        try {
            idStaff = (String) SpecialP.getReturned1().get("userids");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Parser Staffparser = new Parser(context);

        try {
            Staffparser.execute(Staffparser.ListQuery("staff", "user_id", idStaff)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        if (StaffList.size() != 0) {
            StaffList.clear();
        }


        for (int i = 0; i < Staffparser.getReturned().length(); i++) {
            Staff s = null;
            try {
                s = gson.fromJson(String.valueOf(Staffparser.getReturned().get(i)), Staff.class);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (s != null && s.getStaff_issue_type()==0) {
                StaffList.add(new Item(s.getNomComplet(), s.getUser_id()));
            }

        }
        return StaffList;
    }

    public static void recurApplyColor(final ArrayList array, final ArrayList grey, final ListView lvSelect, final ItemAdapter itemListAdapter, final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(lvSelect.getChildCount() == 0)) {

                    for (int child = 0; child < lvSelect.getChildCount(); child++) {
                        if (array.contains(itemListAdapter.getItem(child).getStrId())) {
                            TextView te = (TextView) lvSelect.getChildAt(child).findViewById(R.id.tvItem);
                            te.setTextColor(context.getResources().getColor(R.color.event_color_02));
                        }

                    }
                    if (grey != null) {
                        for (int child = 0; child < lvSelect.getChildCount(); child++) {
                            if (grey.contains(itemListAdapter.getItem(child).getStrId())) {
                                TextView te = (TextView) lvSelect.getChildAt(child).findViewById(R.id.tvItem);
                                te.setTextColor(Color.GRAY);
                            }

                        }
                    }
                }
            }
        }, 1500);
    }

    public static void recurNotifier(final ArrayList<ArrayList> PaysE,final ListView lvSelect, final ItemAdapter itemListAdapter, final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(lvSelect.getChildCount() == 0)) {


                    for (int u = 0; u < PaysE.get(0).size(); u++) {

                        if (!PaysE.get(0).get(u).equals("0")) {
                            for (int y = 0; y < lvSelect.getChildCount(); y++) {


                                if (itemListAdapter.getItem(y).getStrId().equals(PaysE.get(0).get(u))) {
                                    String newText = itemListAdapter.getItem(y).getItemName() + " - " + "19 erreurs";
                                    ((TextView) lvSelect.getChildAt(y).findViewById(R.id.tvItem)).setText(newText);
                                }
                            }
                        }
                    }


                }
            }
        }, 1000);
    }
}

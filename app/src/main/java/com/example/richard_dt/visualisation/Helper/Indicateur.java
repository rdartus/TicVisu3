package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.Activities.MainFrag;
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
public static ArrayList<ArrayList>RegionErreurIteration(String regionIdError ,ArrayList<ArrayList> Clist){
        ArrayList<ArrayList> regionArrayArrayList = new ArrayList<>();

    ArrayList arrayListRegionId = new ArrayList();
    String []regionIdArray = regionIdError.split(",");
    for (String id : regionIdArray){
        arrayListRegionId.add(id);
    }
    ArrayList arrayListClassroomErrorCount = Clist.get(1);
        for (int i=0;i<arrayListRegionId.size();i++) {
            String Rid = (String) arrayListRegionId.get(i);
            if(regionArrayArrayList.size()!=0) {
                for (int j = 0; j < regionArrayArrayList.get(0).size(); j++) {
                    if (regionArrayArrayList.size() != 0) {
                        if (regionArrayArrayList.get(0).contains(Rid)) {
                            String str = (String) regionArrayArrayList.get(1).get(regionArrayArrayList.get(0).indexOf(Rid));
                            double d = Double.valueOf(str) + Double.valueOf((String) arrayListClassroomErrorCount.get(j));

                        }
                    } else {
                        ArrayList al1 = new ArrayList();
                        ArrayList al2 = new ArrayList();
                        al1.add(Rid);
                        al2.add(arrayListClassroomErrorCount.get(j));
                        regionArrayArrayList.add(al1);
                        regionArrayArrayList.add(al2);
                    }
                }
            }
            else {
                    ArrayList al1 = new ArrayList();
                    ArrayList al2 = new ArrayList();
                    al1.add(Rid);
                    al2.add(arrayListClassroomErrorCount.get(i));
                    regionArrayArrayList.add(al1);
                    regionArrayArrayList.add(al2);
            }
        }
    return regionArrayArrayList;
    }


    public static List<Item<String>> getSuggestion(float lat,
                                                   float lon,
                                                   Context context,
                                                   final String start,
                                                   final int end)
            throws ExecutionException, InterruptedException, JSONException {
        pref  pref=new pref(context);

        int rayon=pref.getInt("rayon");
        Parser p = new Parser(context);
        ArrayList<String> classrooms_id=new ArrayList<>();
        ArrayList<Item<String>> returned=new ArrayList<>();

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

    public static ArrayList<Item<String>> getStafflistSpecial(String ClassRoomId, Context context,String start,int end) {

        ArrayList<Item<String>> StaffList = new ArrayList<>();
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

    public static void recurNotifier(final ArrayList<ArrayList> PaysE,final ListView lvSelect, final LocalisationItemAdapter itemListAdapter, final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(lvSelect.getChildCount() == 0)) {
                    Log.d("blbl", "run: ");

                    for (int u = 0; u < PaysE.get(0).size(); u++) {

                        if (!PaysE.get(0).get(u).equals("0")) {
                            for (int y = 0; y < itemListAdapter.getCount(); y++) {


                                if (itemListAdapter.getItem(y).getId().equals(PaysE.get(0).get(u))) {
                                    itemListAdapter.getItem(y).setNbErrors(Integer.valueOf((String)PaysE.get(1).get(u)));

//                                    String newText = itemListAdapter.getItem(y).getItemName() + " - " + "19 erreurs";
//                                    ((TextView) lvSelect.getChildAt(y).findViewById(R.id.tvItem)).setText(newText);
                                }
                            }
                        }
                    }
                    itemListAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }

//    public static void recurNotifier(final ArrayList<ArrayList> PaysE,final ListView lvSelect, final ItemAdapter itemListAdapter, final Context context) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!(lvSelect.getChildCount() == 0)) {
//
//
//                    for (int u = 0; u < PaysE.get(0).size(); u++) {
//
//                        if (!PaysE.get(0).get(u).equals("0")) {
//                            for (int y = 0; y < lvSelect.getChildCount(); y++) {
//
//
//                                if (itemListAdapter.getItem(y).getStrId().equals(PaysE.get(0).get(u))) {
////                                    itemListAdapter.getItem(y).setNbErrors(19);
//                                    String newText = itemListAdapter.getItem(y).getItemName() + " - " + "19 erreurs";
//                                    ((TextView) lvSelect.getChildAt(y).findViewById(R.id.tvItem)).setText(newText);
//                                }
//                            }
//                        }
//                    }
//
//
//                }
//            }
//        }, 1000);
//    }
}

package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.widget.ListView;

import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Country;
import com.example.richard_dt.visualisation.gsApiClass.Region;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Richard-DT on 13/08/2016.
 */
public class PopulateList {

    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// API actions Get
    //////
    //////////////////////////////////////////////////////////////////////////

    public static void getCountryFromDB(Context context,
                                 ArrayList<LocalisationItem> localisationItemArrayList,
                                 LocalisationItemAdapter localisationItemAdapter,
                                 ListView lv,
                                 ArrayList<ArrayList> countryList) {

        Parser CountryParser;
        Gson gson = new Gson();
        CountryParser = new Parser(context);

        try {
            CountryParser.execute("country").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        if (localisationItemArrayList != null) {
            if (localisationItemArrayList.size() != 0) {
                localisationItemArrayList.clear();
            }
            //REST API
            if (CountryParser.getReturned() != null) {
                for (int i = 0; i < CountryParser.getReturned().length(); i++) {
                    Country C;
                    try {
                        C = gson.fromJson(String.valueOf(CountryParser.getReturned().get(i)), Country.class);
                        localisationItemArrayList.add(new LocalisationItem(C.getCountry_name(),C.getCountry_code()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            localisationItemAdapter = new LocalisationItemAdapter(context,0,localisationItemArrayList);
            lv.setAdapter(localisationItemAdapter);
            if (countryList != null && countryList.size() != 0) {
                Indicateur.recurNotifier(countryList, lv, localisationItemAdapter, context);

            }

        }
    }

    public static void getRegionFromDB(String idPays,
                                Context context,
                                ArrayList<LocalisationItem> localisationItemArrayList,
                                LocalisationItemAdapter localisationItemAdapter,
                                ListView lv,
                                ArrayList<ArrayList> regionList) {
        Gson gson = new Gson();
        Parser newP = new Parser(context);
        try {
            newP.execute(newP.EqualQuery("region", "region_country_code", idPays)).get();
            if (localisationItemArrayList != null) {
                if (localisationItemArrayList.size() != 0) {
                    localisationItemArrayList.clear();
                }


                for (int i = 0; i < newP.getReturned().length(); i++) {
                    Region r = null;
                    try {
                        r = gson.fromJson(String.valueOf(newP.getReturned().get(i)), Region.class);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                lv.add(new Item(r.getRegion_name(), r.getRegion_id()));
                    localisationItemArrayList.add(new LocalisationItem(r.getRegion_name(), r.getRegion_id()));
                }
                
                localisationItemAdapter = new LocalisationItemAdapter(context,0,localisationItemArrayList);
                lv.setAdapter(localisationItemAdapter);

                Indicateur.recurNotifier(regionList, lv, localisationItemAdapter, context);

            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void getClassroomList(String iditem,
                                 String countryCode,
                                 Context context,
                                 ArrayList<LocalisationItem> localisationItemArrayList,
                                 LocalisationItemAdapter localisationItemAdapter,
                                 ListView lv,
                                 ArrayList<ArrayList> classList) throws JSONException {
        Gson gson = new Gson();
        ArrayList<String> GreyedValue = new ArrayList<>();

        Parser newP = new Parser(context);
        try {
            newP.execute(newP.AddQuery(newP.EqualQuery("region", "region_id", iditem + ""), newP.EqualQuery("region", "region_country_code", countryCode+ ""))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Region R = gson.fromJson(String.valueOf(newP.getReturned().get(0)), Region.class);

        String cstring = R.getClassroom_list().get(0) + "";

        for (int i = 1; i < R.getClassroom_list().size(); i++) {
            cstring = cstring + "," + R.getClassroom_list().get(i);
        }
//Retu
        Parser P = new Parser(context);
        try {
            P.execute(P.AddQuery(P.ListQuery("classroom", "classroom_id", cstring), P.EqualQuery("classroom", "classroom_country_code",countryCode))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (localisationItemArrayList != null) {
            if (localisationItemArrayList.size() != 0) {
                localisationItemArrayList.clear();
            }
            for (int i = 0; i < P.getReturned().length(); i++) {
                Classroom C = null;
                try {
                    C = gson.fromJson(String.valueOf(P.getReturned().get(i)), Classroom.class);
                    C.setRegion_id(0);


                    Parser Par = new Parser(context);
                    try {
                        Par.execute(P.EqualQuery("class", "classroom_id", String.valueOf(C.getClassroom_id()))).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (Par.getReturned() == null && Par.getReturned1() == null) {
                        GreyedValue.add(String.valueOf(C.getClassroom_id()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LocalisationItem localisationItem = new LocalisationItem(C.getClassroom_name(),""+C.getClassroom_id());
                localisationItemArrayList.add(localisationItem);
            }
            localisationItemAdapter = new LocalisationItemAdapter(context,0,localisationItemArrayList);
            lv.setAdapter(localisationItemAdapter);
            localisationItemAdapter.notifyDataSetChanged();
        }

        Indicateur.recurNotifier(classList, lv, localisationItemAdapter, context);

    }
}

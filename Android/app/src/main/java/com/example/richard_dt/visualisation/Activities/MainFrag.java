package com.example.richard_dt.visualisation.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.Testeur;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Country;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Region;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.richard_dt.visualisation.R.id.popupdial;

/**
 * Created by Richard-DT on 07/06/2016.
 */
public class MainFrag extends Fragment implements  View.OnClickListener, SwitchCompat.OnCheckedChangeListener  {

    private View rootView;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog  datePickerDialog;
    
    private int month_x, day_x, year_x;
    private static final int DIALOG_ID = 0;
    private boolean isDisplayed = false;
    private boolean compIsDisplayed = false;

    private ItemAdapter itemListAdapter;

    private ArrayList<Item> ListLV = new ArrayList<>();
    private ArrayList<Item> intensityPossibleItemList;
    private ArrayList<Item> staffItemList;


    private ArrayList<String> idClassList = new ArrayList<>();
    private ArrayList<Classroom> ClassList = new ArrayList<>();
    private ArrayList<Cours> coursList = new ArrayList<>();


    private List<String> listPossibleIntensity = new ArrayList<>();

    private ArrayList<ArrayList> Clist = new ArrayList<>();
    private ArrayList<ArrayList> Rlist = new ArrayList<>();
    private ArrayList<ArrayList> Countrylist = new ArrayList<>();

    private ArrayList<String> path = new ArrayList<>();

    //private Menu menu;

    private Button bt2;
    private Button pathUp;

    private SwitchCompat switchStaff;
    private SwitchCompat switchIntensity;
    private List<String> selectedIntensity = new ArrayList<>();
    private List<String> selectedItemStaff = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private ListView lvSelect = null;
    private ListView lvIntensity = null;
    private ListView lvStaff = null;
    Bundle b;

    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener dsl = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            day_x = dayOfMonth;
            month_x = monthOfYear;
            String date = day_x + "/" + (month_x+1) + "/" + year_x;
            bt2.setText(date);

        }
    };
//    private DatePickerDialog.OnDateSetListener dapickListener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            year_x = year;
//            day_x = dayOfMonth;
//            month_x = monthOfYear + 1;
//            String date = day_x + "/" + month_x + "/" + year_x;
//            bt2.setText(date);
//        }
//    };
    private Gson gson;



    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Activity Life Cycle
    //////
    //////////////////////////////////////////////////////////////////////////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_main,null);

        //Récupération de nos objets graphique

        lvSelect = (ListView) rootView.findViewById(R.id.lLocation);
        lvIntensity = (ListView) rootView.findViewById(R.id.lvIntensity);
        lvStaff = (ListView) rootView.findViewById(R.id.lTeacher);

        Button bTest = (Button) rootView.findViewById(R.id.button);
        bt2 = (Button) rootView.findViewById(popupdial);
        pathUp = (Button) rootView.findViewById(R.id.pathUp);

        switchIntensity = (SwitchCompat) rootView.findViewById(R.id.switchIntensity);
        switchIntensity.setOnCheckedChangeListener(this);
        switchStaff = (SwitchCompat) rootView.findViewById(R.id.switchStaff);
        switchStaff.setOnCheckedChangeListener(this);

        b=getArguments();

        serialArrayArray Clistserial= (serialArrayArray) b.getSerializable("clist");
        serialArrayArray Rlistserial=(serialArrayArray) b.getSerializable("Rlist");
        serialArrayArray Countrylistserial=(serialArrayArray) b.getSerializable("Countrylist");

        if (Clistserial != null) {
            Clist = Clistserial.getList();
        }
        if (Rlistserial != null) {
            Rlist = Rlistserial.getList();
        }
        if (Countrylistserial != null) {
            Countrylist = Countrylistserial.getList();
        }

        //Récupération de la date du jour pour le calendrier
        //Récupération de la date du jour pour le calendrier

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        String date = day_x + "/" + month_x + "/" + year_x;
        bt2.setText(date);

        //Création d'un Parser qui nous permet de récuperer un jsonarray depuis une url

        gson = new Gson();


        //Setters

        if (bTest != null) {
            bTest.setOnClickListener(this);
        }
        bt2.setOnClickListener(this);
        pathUp.setOnClickListener(this);

        // Init Component

        getCountryFromDB();
        initStaffList("1000");
        lvSelect.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Boolean Click = true;
                String idItem = "";
                if (path.size() == 3) {
                    path.remove(path.size() - 1);
                }


                if (path.size() == 0) {

                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click = false;
                }
                if (path.size() == 1 && !Click) {

                    getRegionFromDB(idItem);


                }
                if (path.size() == 1 && Click) {
                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click = false;
                }
                if (path.size() == 2 && !Click) {
                    try {
                        getClassroomList(idItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    idClassList = new ArrayList();
                }
                if (path.size() == 2 && Click) {
                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    if(lvSelect.getChoiceMode() == AbsListView.CHOICE_MODE_MULTIPLE) {
                        Classroom classroom = getClassroom(idItem,getContext());
                        if (!ClassList.contains(classroom)) {
                            ClassList.add(classroom);
                        }
                        else {
                            ClassList.add(classroom);
                        }
                        if (!idClassList.contains(idItem)) {
                            idClassList.add(idItem);
                        }
                        else {
                            idClassList.remove(idItem);
                        }
                        Testeur.ColorListViewMulti(lvSelect, itemListAdapter, getContext(), idClassList);
                    }
                    else{
                        Classroom classroom = getClassroom(idItem,getContext());
                        if (ClassList.size() != 0) {
//                            Classroom tmp = ClassList.get(0);
                            ClassList.clear();
                            ClassList.add(classroom);
                        }
                        else {
                            ClassList.add(classroom);
                        }
                        if (idClassList.size()!=0) {
//                            String tmp = idClassList.get(0);
                            idClassList.clear();
                            idClassList.add(idItem);
                        }
                        else {
                            idClassList.add(idItem);
                        }
                        Testeur.ColorListViewSolo(position,lvSelect, itemListAdapter, getContext());
                    }
                    coursList = (ArrayList) getCoursList(path.get(2), parent.getContext());
                    listPossibleIntensity = getListPossibleIntensity(coursList);
                    initIntensityList();
                    initStaffList(idClassList.get(idClassList.size()-1));


                }
                pathUp.setText(path.size() + "");

            }


        });
        lvIntensity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedIntensity.contains(((Item) lvIntensity.getAdapter().getItem(position)).getStrId())) {
                    selectedIntensity.add(((Item) lvIntensity.getAdapter().getItem(position)).getStrId());
                } else {
                    selectedIntensity.remove(((Item) lvIntensity.getAdapter().getItem(position)).getStrId());
                }
                Testeur.ColorListViewMulti(lvIntensity, (ItemAdapter) lvIntensity.getAdapter(), getContext(), selectedIntensity);
            }
        });
        lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!selectedItemStaff.contains(((Item) lvStaff.getAdapter().getItem(position)).getStrId())) {
                    selectedItemStaff.add(((Item) lvStaff.getAdapter().getItem(position)).getStrId());
                } else {
                    selectedItemStaff.remove(((Item) lvStaff.getAdapter().getItem(position)).getStrId());
                }
                staffList = new ArrayList<Staff>();
                for(String idStaff : selectedItemStaff){
                    staffList.addAll(getStaff(idStaff,getContext()));
                }
                Testeur.ColorListViewMulti(lvStaff, (ItemAdapter) lvStaff.getAdapter(), getContext(), selectedItemStaff);
            }
        });

        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(dsl,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle(getString(R.string.mdtp_select_day));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(idClassList != null){
            idClassList.clear();
        }else {
            idClassList = new ArrayList<>();
        }
        if(ClassList != null){
            ClassList.clear();
        }else {
            ClassList= new ArrayList<>();
        }
        if(selectedIntensity != null){
            selectedIntensity.clear();
        }else {
            selectedIntensity= new ArrayList<>();
        }
        if(staffList!= null){
            staffList.clear();
        }else {
            staffList= new ArrayList<>();
        }
        if(selectedItemStaff!= null){
            selectedItemStaff.clear();
        }else {
            selectedItemStaff= new ArrayList<>();
        }
        if(listPossibleIntensity!= null){
            listPossibleIntensity.clear();
        }else {
            listPossibleIntensity= new ArrayList<>();
        }
        if(intensityPossibleItemList != null){
            intensityPossibleItemList.clear();
        }else {
            intensityPossibleItemList = new ArrayList<>();
        }
        if(staffItemList != null){
            staffItemList .clear();
        }else {
            staffItemList = new ArrayList<>();
        }


        if (path.size() == 0) {
            getCountryFromDB();
        }
        if (path.size() == 1) {
            getRegionFromDB(path.get(0));
        }
        if (path.size() >= 2) {
            try {
                getClassroomList(path.get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initIntensityList();
//    }


    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// VIews Actions
    //////
    //////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.button:

                if (path.size() == 3) {

                    startActivity(new Intent(getContext(), CalendarActivity.class)
                            .putExtra("path", path)
                            .putExtra("day_x", day_x)
                            .putExtra("month_x", month_x)
                            .putExtra("year_x", year_x)
                            .putExtra("idclassroomList", idClassList)
                            .putExtra("classroomList", ClassList)
                            .putExtra("coursList",coursList)
                            .putExtra("staffList",(ArrayList) staffList)
                            .putExtra("selectedIntensity", (ArrayList) selectedIntensity)
                            .putExtra("listPossibleIntensity", (ArrayList) listPossibleIntensity));
                } else {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.ErrorSelectClassroom), Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case popupdial:
                final Activity activity = (Activity) getContext();
                datePickerDialog.show(activity.getFragmentManager(),"d");
                break;
            case R.id.pathUp:
                if (path.size() == 1) {
                    getCountryFromDB();
                    path.remove(path.size() - 1);


                } else if (path.size() == 2) {
                    getRegionFromDB(path.get(0));
                    path.remove(path.size() - 1);

                } else if (path.size() == 3) {
                    getRegionFromDB(path.get(0));
                    path.remove(path.size() - 1);
                    path.remove(path.size() - 1);

                    //comeback
                }
                break;


        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (switchStaff.isChecked() || switchIntensity.isChecked()) {
            lvSelect.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            if(idClassList!=null && idClassList.size()!=0){
                String selectedClass = idClassList.get(0);
                idClassList.clear();
                idClassList.add(selectedClass);
            }
        } else {
            lvSelect.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            if(selectedItemStaff!=null && selectedItemStaff.size()!= 0){
                String selectedStaff = selectedItemStaff.get(0);
                selectedItemStaff.clear();
                selectedItemStaff.add(selectedStaff);
            }
            if(selectedIntensity!=null && selectedIntensity.size()!= 0){
                String selectedIdIntensity = selectedIntensity.get(0);
                selectedIntensity.clear();
                selectedIntensity.add(selectedIdIntensity);
            }
        }
        switch (buttonView.getId()) {
            case R.id.switchStaff:
                if (switchStaff.isChecked()) {
                    (rootView.findViewById(R.id.lTeacher)).setVisibility(View.VISIBLE);
                    (rootView.findViewById(R.id.profText)).setVisibility(View.VISIBLE);
                    if(idClassList.size()!=0) {
                        initStaffList(idClassList.get(0));
                    }
                } else {
                    (rootView.findViewById(R.id.lTeacher)).setVisibility(View.INVISIBLE);
                    (rootView.findViewById(R.id.profText)).setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.switchIntensity:
                if (switchIntensity.isChecked()) {
                    (rootView.findViewById(R.id.lvIntensity)).setVisibility(View.VISIBLE);
                    (rootView.findViewById(R.id.textView)).setVisibility(View.VISIBLE);
                    initIntensityList();
                } else {
                    (rootView.findViewById(R.id.lvIntensity)).setVisibility(View.INVISIBLE);
                    (rootView.findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
                }
                break;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// API actions Get
    //////
    //////////////////////////////////////////////////////////////////////////


    public void getCountryFromDB() {

        Parser CountryParser;

        CountryParser = new Parser(getContext());

        try {
            CountryParser.execute("country").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        if (ListLV != null) {
            if (ListLV.size() != 0) {
                ListLV.clear();
            }
            //REST API
            if (CountryParser.getReturned() != null) {
                for (int i = 0; i < CountryParser.getReturned().length(); i++) {
                    Country C;
                    try {
                        C = gson.fromJson(String.valueOf(CountryParser.getReturned().get(i)), Country.class);
                        ListLV.add(new Item(C.getCountry_name(), C.getCountry_code()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            itemListAdapter = new ItemAdapter(ListLV, getContext());
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

            if (Countrylist != null && Countrylist.size() != 0) {
                Indicateur.recurNotifier(Countrylist, lvSelect, itemListAdapter, getContext());

                Indicateur.recurApplyColor(Countrylist.get(0), null, lvSelect, itemListAdapter, getContext());
            }

        }
    }

    public void getRegionFromDB(String idPays) {
        Parser newP = new Parser(this.getContext());
        try {
            newP.execute(newP.EqualQuery("region", "region_country_code", idPays)).get();
        if (ListLV != null) {
            if (ListLV.size() != 0) {
                ListLV.clear();
            }


            for (int i = 0; i < newP.getReturned().length(); i++) {
                Region r = null;
                try {
                    r = gson.fromJson(String.valueOf(newP.getReturned().get(i)), Region.class);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ListLV.add(new Item(r.getRegion_name(), r.getRegion_id()));

            }

            itemListAdapter = new ItemAdapter(ListLV, getContext());
//            if (ListLV.size() == 1) {
//                itemListAdapter.add(ListLV.get(0));
//            } else {
//                itemListAdapter.addAll(ListLV);
//            }
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

            Indicateur.recurNotifier(Rlist, lvSelect, itemListAdapter, getContext());

            Indicateur.recurApplyColor(Rlist.get(0), null, lvSelect, itemListAdapter, getContext());
        }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void getClassroomList(String iditem) throws JSONException {
        ArrayList<String> GreyedValue = new ArrayList<>();

        Parser newP = new Parser(this.getContext());
        try {
            newP.execute(newP.AddQuery(newP.EqualQuery("region", "region_id", iditem + ""), newP.EqualQuery("region", "region_country_code", path.get(0) + ""))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Region R = gson.fromJson(String.valueOf(newP.getReturned().get(0)), Region.class);

        String cstring = R.getClassroom_list().get(0) + "";

        for (int i = 1; i < R.getClassroom_list().size(); i++) {
            cstring = cstring + "," + R.getClassroom_list().get(i);
        }
//Retu
        Parser P = new Parser(this.getContext());
        try {
            P.execute(P.AddQuery(P.ListQuery("classroom", "classroom_id", cstring), P.EqualQuery("classroom", "classroom_country_code", path.get(0)))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (ListLV != null) {
            if (ListLV.size() != 0) {
                ListLV.clear();
            }
            for (int i = 0; i < P.getReturned().length(); i++) {
                Classroom C = null;
                try {
                    C = gson.fromJson(String.valueOf(P.getReturned().get(i)), Classroom.class);
                    C.setRegion_id(0);


                    Parser Par = new Parser(getContext());
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
                ListLV.add(new Item(C.getClassroom_name(), "" + C.getClassroom_id()));
            }


            itemListAdapter = new ItemAdapter(ListLV, getContext());
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
        }

        Indicateur.recurNotifier(Clist, lvSelect, itemListAdapter, getContext());

        Indicateur.recurApplyColor(Clist.get(0), null, lvSelect, itemListAdapter, getContext());

    }


    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Reusable API Methods
    //////
    //////////////////////////////////////////////////////////////////////////


    public static List<String> getListPossibleIntensity(ArrayList<Cours> coursList) {
        if (coursList != null) {
            ArrayList listPossibleIntensity = new ArrayList<>();
            HashMap hm = Cours.getIntensity();
//            for(List list : container)
            for (Cours cours : coursList) {
                String intensity = (String) hm.get(cours.getClass_level() + "");
                if (!listPossibleIntensity.contains(intensity)) {
                    listPossibleIntensity.add(intensity);
                }
            }
            return listPossibleIntensity;
        }
        return null;
    }

    public static List<Staff> getStaffList(String classroomId, Context context) {
        Gson gson = new Gson();
        ArrayList<Staff> staffList = new ArrayList<>();

        Parser SpecialP = new Parser(context);

        try {
            SpecialP.execute("dispo/crid/" + classroomId).get();
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


        if (staffList.size() != 0) {
            staffList.clear();
        }


        for (int i = 0; i < Staffparser.getReturned().length(); i++) {
            Staff s = null;
            try {
                s = gson.fromJson(String.valueOf(Staffparser.getReturned().get(i)), Staff.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            staffList.add(s);
        }
        return staffList;
    }

    public static List<Cours> getCoursList(String classId, Context context) {
        Parser P = new Parser(context);
        Gson gson = new Gson();
        List<Cours> coursList = new ArrayList<>();

        try {
            P.execute(P.EqualQuery("class", "classroom_id", classId)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < P.getReturned().length(); i++) {
            try {

                coursList.add(gson.fromJson(String.valueOf(P.getReturned().get(i)), Cours.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return coursList;
    }

    public static Classroom getClassroom(String classroomId, Context context) {
        Gson gson = new Gson();
        Classroom classroom = null;
        Parser np = new Parser(context);
        try {

//            np.execute(np.EqualQuery("class","classroom_id",(String) path.getString(2))).getString();
            np.execute(np.EqualQuery("classroom", "classroom_id", classroomId)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < np.getReturned().length(); i++) {
            try {
                classroom = (gson.fromJson(String.valueOf(np.getReturned().get(i)), Classroom.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return classroom;
    }

    public static List<Staff> getStaff(String staffId, Context context) {
        Gson gson = new Gson();
        ArrayList<Staff> staffList = new ArrayList<>();
        Parser Staffparser = new Parser(context);
        try {
            Staffparser.execute(Staffparser.ListQuery("staff", "user_id", staffId)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (staffList.size() != 0) {
            staffList.clear();
        }


        for (int i = 0; i < Staffparser.getReturned().length(); i++) {
            Staff s = null;
            try {
                s = gson.fromJson(String.valueOf(Staffparser.getReturned().get(i)), Staff.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            staffList.add(s);
        }
        return staffList;
    }

    public void recurNotifier(final ArrayList<ArrayList> PaysE) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(path != null && path.size() <2){
                if (!(lvSelect.getChildCount() == 0)) {


                    for (int u = 0; u < PaysE.get(0).size(); u++) {

                        if (!PaysE.get(0).get(u).equals("0")) {
                            for (int y = 0; y < lvSelect.getChildCount(); y++) {


                                if (itemListAdapter.getItem(y).getStrId().equals(PaysE.get(0).get(u))) {
                                    String test = ((TextView) lvSelect.getChildAt(y).findViewById(R.id.tvItem)).getText().toString();
                                    String newText = test.split(" -")[0] + " - " + PaysE.get(1).get(u);
                                    ((TextView) lvSelect.getChildAt(y).findViewById(R.id.tvItem)).setText(newText);
                                }
                            }
                        }
                    }

                }
                }
            }
        }, 100);
    }

    private void recurApplyColor(final ArrayList array, final ArrayList grey) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!(lvSelect.getChildCount() == 0)) {

                    for (int child = 0; child < lvSelect.getChildCount(); child++) {
                        if (array.contains(itemListAdapter.getItem(child).getStrId())) {
                            TextView te = (TextView) lvSelect.getChildAt(child).findViewById(R.id.tvItem);
                            te.setTextColor(getResources().getColor(R.color.event_color_02));
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
        }, 25);
    }


    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Init ListViews
    //////
    //////////////////////////////////////////////////////////////////////////

    public void initIntensityList() {

        selectedIntensity = new ArrayList<String>();
        intensityPossibleItemList = new ArrayList<>();

        HashMap hm = Cours.getIntensity();

        for (String intensity : listPossibleIntensity) {
            String id = (String) Cours.getKeyByValue(hm, intensity);
            Item item = new Item(intensity, id);
            intensityPossibleItemList.add(item);
        }
        ItemAdapter intensityListAdapter = new ItemAdapter(intensityPossibleItemList, getContext());
        lvIntensity.setAdapter(intensityListAdapter);


    }

    private void initStaffList(String ClassRoomId) {
        if (switchStaff.isChecked()) {
            staffItemList = new ArrayList<>();
            ArrayList<Staff> staffList = (ArrayList<Staff>) getStaffList(ClassRoomId, getContext());
            for (Staff staff : staffList) {
                staffItemList.add(new Item(staff.getNomComplet(), staff.getUser_id()));
            }
            ItemAdapter staffListAdapter = new ItemAdapter(staffItemList, getContext());
            lvStaff.setAdapter(staffListAdapter);
            staffListAdapter.notifyDataSetChanged();
        }
    }
//    public void NotifyError() {
//
//        String Cid = "0";
//        String Lid = "0";
//        String Rcc = "0";
//        String Rid = "0";
//
//        //Liste des cours erronés
//
//        Parser Ptest = new Parser(this.getContext());
//
//        try {
//            Ptest.execute(Ptest.EqualQuery("class", "class_has_issue", "true")).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//        if (Ptest.getReturned()!=null && Ptest.getReturned().length()!=0) {
//            for (int i = 0; i < Ptest.getReturned().length(); i++) {
//                try {
//                    Cours c = gson.fromJson(String.valueOf(Ptest.getReturned().get(i)), Cours.class);
//                    Cid = Cid + "," + c.getClassroom_id(); //ClassroomIdIteration
//                    Lid = Lid + "," + c.getLocation_id(); //LocationId
//                    Rcc = Rcc + "," + c.getClassroom_country_code(); //ClassroomCountryCode
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            //Cid =Erreur par classoom
//
//            Clist = Indicateur.ErreurIteration(Cid);
//            Countrylist = Indicateur.ErreurIteration(Rcc);
//
//            Parser P2 = new Parser(this.getContext());
//            try {
//                P2.execute(P2.ListQuery("region", "region_country_code", Rcc)).get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            for (int i = 0; i < P2.getReturned().length(); i++) {
//                Region R = null;
//                try {
//                    R = gson.fromJson(String.valueOf(P2.getReturned().get(i)), Region.class);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                for (int k = 0; k < Clist.get(0).size(); k++) {
//                    if (R.getClassroom_list().contains(Clist.get(0).get(k))) {
//                        Rid = Rid + "," + R.getRegion_id();
//                    }
//                }
//
//            }
//            Rlist = Indicateur.ErreurIteration(Rid);
//        }
//
//    }
}
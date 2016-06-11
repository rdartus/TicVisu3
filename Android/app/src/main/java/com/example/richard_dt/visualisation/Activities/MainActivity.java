package com.example.richard_dt.visualisation.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwitchCompat.OnCheckedChangeListener {


    private int month_x, day_x, year_x;
    private static final int DIALOG_ID = 0;
    private boolean isDisplayed = false;
    private boolean compIsDisplayed = false;

    private ItemAdapter itemListAdapter;

    private ArrayList<Item> ListLV = new ArrayList<>();


    private ArrayList<String> idClassList = new ArrayList<>();
    private ArrayList<Classroom> ClassList = new ArrayList<>();
    private ArrayList<Cours> coursList = new ArrayList<>();


    private List<String> listPossibleIntensity = new ArrayList<>();


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

    private DatePickerDialog.OnDateSetListener dapickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            day_x = dayOfMonth;
            month_x = monthOfYear + 1;
            String date = day_x + "/" + month_x + "/" + year_x;
            bt2.setText(date);
        }
    };
    private Gson gson;



    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Activity Life Cycle
    //////
    //////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Récupération de nos objets graphique

        lvSelect = (ListView) findViewById(R.id.lLocation);
        lvIntensity = (ListView) findViewById(R.id.lvIntensity);
        lvStaff = (ListView) findViewById(R.id.lTeacher);

        Button bTest = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(popupdial);
        pathUp = (Button) findViewById(R.id.pathUp);

        switchIntensity = (SwitchCompat) findViewById(R.id.switchIntensity);
        switchIntensity.setOnCheckedChangeListener(this);
        switchStaff = (SwitchCompat) findViewById(R.id.switchStaff);
        switchStaff.setOnCheckedChangeListener(this);

        //Support d'une Toolbar en haut de l'activité

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.Titre);


        getSupportActionBar().setIcon(R.mipmap.gslogo);
        supportInvalidateOptionsMenu();

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
                    compIsDisplayed = true;
                    getSupportActionBar().invalidateOptionsMenu();
                    idClassList = new ArrayList();
                }
                if (path.size() == 2 && Click) {
                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    if(lvSelect.getChoiceMode() == AbsListView.CHOICE_MODE_MULTIPLE) {
                        Classroom classroom = getClassroom(idItem,getApplicationContext());
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
                        Testeur.ColorListViewMulti(lvSelect, itemListAdapter, getApplicationContext(), idClassList);
                    }
                    else{
                        Classroom classroom = getClassroom(idItem,getApplicationContext());
                        if (ClassList.size() != 0) {
                            Classroom tmp = ClassList.get(0);
                            ClassList.clear();
                            ClassList.add(tmp);
                        }
                        else {
                            ClassList.add(classroom);
                        }
                        if (idClassList.size()!=0) {
                            String tmp = idClassList.get(0);
                            idClassList.clear();
                            idClassList.add(tmp);
                        }
                        else {
                            idClassList.add(idItem);
                        }
                        Testeur.ColorListViewSolo(position,lvSelect, itemListAdapter, getApplicationContext());
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
                Testeur.ColorListViewMulti(lvIntensity, (ItemAdapter) lvIntensity.getAdapter(), getApplicationContext(), selectedIntensity);
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
                    staffList.addAll(getStaff(idStaff,getApplicationContext()));
                }
                Testeur.ColorListViewMulti(lvStaff, (ItemAdapter) lvStaff.getAdapter(), getApplicationContext(), selectedItemStaff);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (path.size() == 0) {
            getCountryFromDB();
        }
        if (path.size() == 1) {
            getRegionFromDB(path.get(0));
        }
        if (path.size() == 2) {
            try {
                getClassroomList(path.get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initIntensityList();
    }

    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Meni Item Methods:
    //////
    //////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (compIsDisplayed) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }
        return true;
    }

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

                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class)
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
                    overridePendingTransition(R.transition.transin, R.transition.transin);
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.ErrorSelectClassroom), Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case popupdial:
                showDialog(DIALOG_ID);
                break;
            case R.id.pathUp:
                if (path.size() == 1) {
                    getCountryFromDB();
                    path.remove(path.size() - 1);


                } else if (path.size() == 2) {
                    getRegionFromDB(path.get(0));
                    path.remove(path.size() - 1);

                    compIsDisplayed = false;
                    invalidateOptionsMenu();
                } else if (path.size() == 3) {
                    getRegionFromDB(path.get(0));
                    path.remove(path.size() - 1);
                    path.remove(path.size() - 1);

                    //comeback

                    (findViewById(R.id.lvIntensity)).setVisibility(View.INVISIBLE);
                    (findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
                    compIsDisplayed = false;
                    invalidateOptionsMenu();
                }
                break;


        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dapickListener, year_x, month_x, day_x);
        }
        return null;
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
                    (findViewById(R.id.lTeacher)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.profText)).setVisibility(View.VISIBLE);
                    if(idClassList.size()!=0) {
                        initStaffList(idClassList.get(0));
                    }
                } else {
                    (findViewById(R.id.lTeacher)).setVisibility(View.INVISIBLE);
                    (findViewById(R.id.profText)).setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.switchIntensity:
                if (switchIntensity.isChecked()) {
                    (findViewById(R.id.lvIntensity)).setVisibility(View.VISIBLE);
                    (findViewById(R.id.textView)).setVisibility(View.VISIBLE);
                    initIntensityList();
                } else {
                    (findViewById(R.id.lvIntensity)).setVisibility(View.INVISIBLE);
                    (findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
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

        CountryParser = new Parser(getApplicationContext());

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


            itemListAdapter = new ItemAdapter(ListLV, this);
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

           // if (Countrylist != null && Countrylist.size() != 0) {
             //   recurApplyColor(Countrylist.get(0), null);
           // }
           // recurNotifier(Countrylist);
        }
    }

    public void getRegionFromDB(String idPays) {
        Parser newP = new Parser(this.getApplicationContext());
        try {
            newP.execute(newP.EqualQuery("region", "region_country_code", idPays)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
            itemListAdapter = new ItemAdapter(ListLV, this);
//            if (ListLV.size() == 1) {
//                itemListAdapter.add(ListLV.get(0));
//            } else {
//                itemListAdapter.addAll(ListLV);
//            }
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

          //  recurApplyColor(Rlist.get(0), null);
           // recurNotifier(Rlist);


        }
    }


    public void getClassroomList(String iditem) throws JSONException {
        ArrayList<String> GreyedValue = new ArrayList<>();

        Parser newP = new Parser(this.getApplicationContext());
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
        Parser P = new Parser(this.getApplicationContext());
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


                    Parser Par = new Parser(getApplicationContext());
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


            itemListAdapter = new ItemAdapter(ListLV, this);
            lvSelect.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
        }

        //recurApplyColor(Clist.get(0), GreyedValue);
        //recurNotifier(Clist);


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
        ArrayList<Item> intensityPossibleItemList = new ArrayList<>();

        HashMap hm = Cours.getIntensity();

        for (String intensity : listPossibleIntensity) {
            String id = (String) Cours.getKeyByValue(hm, intensity);
            Item item = new Item(intensity, id);
            intensityPossibleItemList.add(item);
        }
        ItemAdapter intensityListAdapter = new ItemAdapter(intensityPossibleItemList, this);
        lvIntensity.setAdapter(intensityListAdapter);


    }

    private void initStaffList(String ClassRoomId) {
        if (switchStaff.isChecked()) {
            ArrayList<Item> staffItemList = new ArrayList<>();
            ArrayList<Staff> staffList = (ArrayList<Staff>) getStaffList(ClassRoomId, getApplicationContext());
            for (Staff staff : staffList) {
                staffItemList.add(new Item(staff.getNomComplet(), staff.getUser_id()));
            }
            ItemAdapter staffListAdapter = new ItemAdapter(staffItemList, this);
            lvStaff.setAdapter(staffListAdapter);
            staffListAdapter.notifyDataSetChanged();
        }
    }

}

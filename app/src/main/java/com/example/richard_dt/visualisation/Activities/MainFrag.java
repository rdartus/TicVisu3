package com.example.richard_dt.visualisation.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.Helper.DividerItemDecoration;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.LocalisationItem;
import com.example.richard_dt.visualisation.Helper.LocalisationItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.PopulateList;
import com.example.richard_dt.visualisation.Helper.Test_swipe.DataAdapter;
import com.example.richard_dt.visualisation.Helper.Test_swipe.RecyclerViewAdapter;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
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
@SuppressWarnings("unchecked")
public class MainFrag extends Fragment implements  View.OnClickListener, SwitchCompat.OnCheckedChangeListener  {

    private View rootView;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog  datePickerDialog;
    private Paint paint = new Paint();
    
    private Calendar cal = Calendar.getInstance();
    private int month_x, day_x, year_x;
    private static final int DIALOG_ID = 0;
    private boolean isDisplayed = false;
    private boolean compIsDisplayed = false;

    private static ClassParameters selectedClassParameter;
    private static ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();

    private static ArrayList<LocalisationItem> localisationItemArrayList = new ArrayList<>();
    private static LocalisationItemAdapter localisationItemAdapter;

    private static ArrayList<Item<String>> intensityPossibleItemList;
    private static ArrayList<Item<String>> staffItemList;


    private ArrayList<String> idClassList = new ArrayList<>();
    private ArrayList<Classroom> ClassList = new ArrayList<>();
    private ArrayList<Cours> coursList = new ArrayList<>();


    private List<String> listPossibleIntensity = new ArrayList<>();

    private ArrayList<ArrayList> Clist = new ArrayList<>();
    private ArrayList<ArrayList> Rlist = new ArrayList<>();
    private ArrayList<ArrayList> Countrylist = new ArrayList<>();

    private ArrayList<String> path = new ArrayList<>();


    private static RecyclerViewAdapter mAdapter;
    private static DataAdapter selectAdapter;

    //VIEWS
    private static Button bt2;
    private static Button pathUp;
    private static SwitchCompat switchStaff;
    private static SwitchCompat switchIntensity;
    private static List<String> selectedIntensity = new ArrayList<>();
    private static List<String> selectedItemStaff = new ArrayList<>();
    private static List<Staff> staffList = new ArrayList<>();
    private static ListView lvSelect = null;
    private static ListView lvIntensity = null;
    private static ListView lvStaff = null;
    private RecyclerView recyclerView;
    Bundle b;

    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener dsl = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            day_x = dayOfMonth;
            month_x = monthOfYear;
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String date = day_x + "/" + (month_x+1) + "/" + year_x;
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


        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        String date = day_x + "/" + month_x + "/" + year_x;
        bt2.setText(date);

        //Création d'un Parser qui nous permet de récuperer un jsonarray depuis une url

        gson = new Gson();



        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        selectAdapter = new DataAdapter(classParametersArrayList,getContext());
        recyclerView.setAdapter(selectAdapter);
        initSwipe();


/*
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Item Decorator:
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
//        recyclerView.setItemAnimator(new FadeInLeftAnimator());

        // Adapter:
//        String[] adapterData = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
//        ArrayList<String> mDataSet = new ArrayList<String>(Arrays.asList(adapterData));
        mAdapter = new RecyclerViewAdapter(getContext(), classParametersArrayList);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setOnScrollListener(onScrollListener);

*/




        //Setters

        if (bTest != null) {
            bTest.setOnClickListener(this);
        }
        bt2.setOnClickListener(this);
        pathUp.setOnClickListener(this);
        localisationItemAdapter = new LocalisationItemAdapter(getContext(),0,localisationItemArrayList);
        // Init Component

        PopulateList.getCountryFromDB(getContext(),
                localisationItemArrayList,
                localisationItemAdapter,
                lvSelect,Countrylist);
//        initStaffList("1000");
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
                    idItem = localisationItemAdapter.getItem(position).getId();
//                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click = false;
                }
                if (path.size() == 1 && !Click) {

                    PopulateList.getRegionFromDB(idItem,
                            getContext(),
                            localisationItemArrayList,
                            localisationItemAdapter,
                            lvSelect,Rlist);


                }
                if (path.size() == 1 && Click) {
                    idItem = localisationItemAdapter.getItem(position).getId();
//                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click = false;
                }
                if (path.size() == 2 && !Click) {
                    try {
                        PopulateList.getClassroomList(idItem,
                                path.get(0),
                                getContext(),
                                localisationItemArrayList,
                                localisationItemAdapter,
                                lvSelect,Clist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setSelectedClasses(localisationItemArrayList,classParametersArrayList);
                    idClassList = new ArrayList<>();
                }
                if (path.size() == 2 && Click) {

                    //recuperer l'index puis la salle
                    int index  = (position );
                    localisationItemArrayList.get(index).setSelected(true);
                    localisationItemAdapter.notifyDataSetChanged();
                    idItem = localisationItemArrayList.get(index).getId();
                    Classroom classroom = getClassroom(idItem,getContext());


                    boolean isInList = false;
                    for (ClassParameters classParameters : classParametersArrayList){
                        if(classParameters.compareTo(classroom)==0){
                            isInList = true;
                            selectedClassParameter = classParameters;
                        }
                    }
                    //s'il existe pas l'ajouter a la liste
                    if(!isInList){
                        ClassParameters classParam = new ClassParameters(classroom);
                        selectedClassParameter = classParam;
                        classParametersArrayList.add(classParametersArrayList.size(),classParam);
                    }
                    path.add(path.size(), localisationItemArrayList.get(index).getId());
                    coursList = (ArrayList<Cours>) getCoursList(path.get(2), parent.getContext());
                    listPossibleIntensity = getListPossibleIntensity(coursList);
                    initIntensityList();
                    initStaffList(""+selectedClassParameter.getClassroom().getClassroom_id());
                    refreshList();
//                    initStaffList(idClassList.get(idClassList.size()-1));

                }

            }


        });
        lvIntensity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList intensityList = selectedClassParameter.getIntensitySelected()==null?
                        new ArrayList():selectedClassParameter.getIntensitySelected();
                String intensityId = (((Item<String>) lvIntensity.getAdapter().getItem(position)).getId());
                HashMap hm = Cours.getIntensity();
                if(!intensityList.contains((intensityId))){
                    intensityList.add((intensityId));
                }
                else
                {
                    intensityList.remove((intensityId));
                }
                if(selectedClassParameter.getIntensitySelected()==null){
                    selectedClassParameter.setIntensitySelected(intensityList);
                }
                if (!selectedIntensity.contains(((Item<String>) lvIntensity.getAdapter().getItem(position)).getId())) {
                    selectedIntensity.add(((Item<String>) lvIntensity.getAdapter().getItem(position)).getId());
                    ((Item<String>) lvIntensity.getAdapter().getItem(position)).setSelected(true);
                } else {
                    selectedIntensity.remove(((Item<String>) lvIntensity.getAdapter().getItem(position)).getId());
                    ((Item<String>) lvIntensity.getAdapter().getItem(position)).setSelected(false);
                }
                refreshList();

//                Testeur.ColorListViewMulti(lvIntensity, (ItemAdapter) lvIntensity.getAdapter(), getContext(), selectedIntensity);
            }
        });
        lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList staffList = selectedClassParameter.getStaffSelected()==null?
                        new ArrayList():selectedClassParameter.getStaffSelected();
                String staffId = (((Item<String>) lvStaff.getAdapter().getItem(position)).getId());
                if(!staffList.contains(staffId)){
                    staffList.add(staffId);
                }
                else
                {
                    staffList.remove(staffId);
                }
                if(selectedClassParameter.getStaffSelected()==null){
                    selectedClassParameter.setStaffSelected(staffList);
                }
                if (!selectedItemStaff.contains(((Item<String>) lvStaff.getAdapter().getItem(position)).getId())) {
                    selectedItemStaff.add(((Item<String>) lvStaff.getAdapter().getItem(position)).getId());
                    ((Item<String>) lvStaff.getAdapter().getItem(position)).setSelected(true);
                } else {
                    selectedItemStaff.remove(((Item<String>) lvStaff.getAdapter().getItem(position)).getId());
                    ((Item<String>) lvStaff.getAdapter().getItem(position)).setSelected(false);
                }
                MainFrag.this.staffList = new ArrayList<>();
                for(String idStaff : selectedItemStaff){
                    MainFrag.this.staffList.addAll(getStaff(idStaff,getContext()));
                }
                refreshList();
//                Testeur.ColorListViewMulti(lvStaff, (ItemAdapter) lvStaff.getAdapter(), getContext(), selectedItemStaff);
            }
        });

//        recyclerView.set

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

        b= getArguments();
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

            PopulateList.getCountryFromDB(getContext(),
                    localisationItemArrayList,
                    localisationItemAdapter,
                    lvSelect,Countrylist);
        }
        if (path.size() == 1) {

            PopulateList.getRegionFromDB(path.get(0),
                    getContext(),
                    localisationItemArrayList,
                    localisationItemAdapter,
                    lvSelect,Rlist);
        }
        if (path.size() >= 2) {
            try {

                PopulateList.getClassroomList(path.get(1),
                        path.get(0),
                        getContext(),
                        localisationItemArrayList,
                        localisationItemAdapter,
                        lvSelect,Clist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setSelectedClasses(localisationItemArrayList,classParametersArrayList);
        }
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initIntensityList();
//    }



    //<test>

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


    //</test>

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
                            .putExtra("millis", cal.getTimeInMillis())
                            .putExtra("classParametersArrayList", classParametersArrayList));
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

                    PopulateList.getCountryFromDB(getContext(),
                            localisationItemArrayList,
                            localisationItemAdapter,
                            lvSelect,Countrylist);
                    path.remove(path.size() - 1);


                } else if (path.size() == 2) {

                    PopulateList.getRegionFromDB(path.get(0),
                            getContext(),
                            localisationItemArrayList,
                            localisationItemAdapter,
                            lvSelect,Rlist);
                    path.remove(path.size() - 1);

                } else if (path.size() == 3) {

                    PopulateList.getRegionFromDB(path.get(0),
                            getContext(),
                            localisationItemArrayList,
                            localisationItemAdapter,
                            lvSelect,Rlist);
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

    ///////////////////////////////////////////////////////////////////////////
    //////
    ////// Init ListViews
    //////
    //////////////////////////////////////////////////////////////////////////

    public void initIntensityList() {

        selectedIntensity = new ArrayList<>();
        intensityPossibleItemList = new ArrayList<>();

        HashMap hm = Cours.getIntensity();

        for (String intensity : listPossibleIntensity) {
            String id = (String) Cours.getKeyByValue(hm, intensity);
            Item<String> item = new Item<>(intensity, id);
            intensityPossibleItemList.add(item);
        }
        ItemAdapter intensityListAdapter = new ItemAdapter<>(intensityPossibleItemList, getContext());
        lvIntensity.setAdapter(intensityListAdapter);


    }

    private void initStaffList(String ClassRoomId) {
        if (switchStaff.isChecked()) {
            staffItemList = new ArrayList<>();
            ArrayList<Staff> staffList = (ArrayList<Staff>) getStaffList(ClassRoomId, getContext());
            for (Staff staff : staffList) {
                staffItemList.add(new Item<>(staff.getNomComplet(), staff.getUser_id()));
            }
            ItemAdapter staffListAdapter = new ItemAdapter<>(staffItemList, getContext());
            lvStaff.setAdapter(staffListAdapter);
            staffListAdapter.notifyDataSetChanged();
        }
    }
    public static Bundle NotifyError(Context context) {

        String Cid = "0";
        String Lid = "0";
        String Rcc = "0";
        String Rid = "0";
        Gson gson = new Gson();
        ArrayList<ArrayList> clist = new ArrayList<>();
        ArrayList<ArrayList> rlist = new ArrayList<>();
        ArrayList<ArrayList> countrylist = new ArrayList<>();
        //Liste des cours erronés

        Parser Ptest = new Parser(context);

        try {
            Ptest.execute(Ptest.EqualQuery("class", "class_has_issue", "true")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (Ptest.getReturned()!=null && Ptest.getReturned().length()!=0) {
            for (int i = 0; i < Ptest.getReturned().length(); i++) {
                try {
                    Cours c = gson.fromJson(String.valueOf(Ptest.getReturned().get(i)), Cours.class);
                    Cid = Cid + "," + c.getClassroom_id(); //ClassroomIdIteration
                    Lid = Lid + "," + c.getLocation_id(); //LocationId
                    Rcc = Rcc + "," + c.getClassroom_country_code(); //ClassroomCountryCode
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Cid =Erreur par classoom

            clist = Indicateur.ErreurIteration(Cid);
            countrylist = Indicateur.ErreurIteration(Rcc);

            Parser P2 = new Parser(context);
            try {
                P2.execute(P2.ListQuery("region", "region_country_code", Rcc)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < P2.getReturned().length(); i++) {
                Region R = null;
                try {
                    R = gson.fromJson(String.valueOf(P2.getReturned().get(i)), Region.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int k = 0; k < clist.get(0).size(); k++) {
                    if (R==null){
                        throw new NullPointerException("region null");
                    }
                    if (R.getClassroom_list().contains(clist.get(0).get(k))) {
                        Rid = Rid + "," + R.getRegion_id();
                    }
                }

            }
            rlist = Indicateur.ErreurIteration(Rid);
        }
        Bundle b = new Bundle();


        serialArrayArray Clistserial=new serialArrayArray(clist);
        serialArrayArray Rlistserial=new serialArrayArray(rlist);
        serialArrayArray Countrylistserial=new serialArrayArray(countrylist);
//
//        b.putSerializable("clist",  Clistserial);
//        b.putSerializable("Rlist", Rlistserial);
//        b.putSerializable("Countrylist",  Countrylistserial);

        return b;

    }
    public static void setSelectedClasses(ArrayList<LocalisationItem> localisationItemArrayList,
                                     ArrayList<ClassParameters> classParametersArrayList){
        if(localisationItemArrayList==null
                &&classParametersArrayList==null){
            return;
        }
        boolean isSelected;
        for (LocalisationItem localisationItem : localisationItemArrayList){
            isSelected = false;
            for (ClassParameters classParameters : classParametersArrayList){
                if(localisationItem.getLocalisation().compareTo(classParameters.getClassroom().getClassroom_name()) == 0
                        && Integer.valueOf(localisationItem.getId()) == classParameters.getClassroom().getClassroom_id()) {
                    localisationItem.setSelected(true);
                    isSelected = true;
                }
            }
            if (!isSelected) {
                localisationItem.setSelected(false);
            }
        }
    }
    public static void refreshList(){
        synchroniseSelection();
        lvStaff.invalidateViews();
        lvIntensity.invalidateViews();
        lvSelect.invalidateViews();
        selectAdapter.notifyDataSetChanged();
//        mAdapter.notifyDataSetChanged();
    }
    //test
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    selectAdapter.removeItem(position);
                    refreshList();
                }
//                else {
//                    ClassParameters classParameters =classParametersArrayList.get(position);
//                    int regionId = classParameters.getClassroom().getRegion_id();
//                    try {
//                        PopulateList.getClassroomList(""+regionId,
//                                path.get(0),
//                                getContext(),
//                                localisationItemArrayList,
//                                localisationItemAdapter,
//                                lvSelect,Clist);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    coursList = (ArrayList<Cours>) getCoursList(path.get(2),getContext());
//                    listPossibleIntensity = getListPossibleIntensity(coursList);
//                    initIntensityList();
//                    initStaffList(""+classParameters.getClassroom().getClassroom_id());
//             }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
//                        paint.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
//                        c.drawRect(background,paint);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,paint);
                    } else {
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    public static void synchroniseSelection(){
        for (int i=0;i< localisationItemArrayList.size();i++){
            boolean exist = false;
            LocalisationItem localisationItem = localisationItemArrayList.get(i);
            for (ClassParameters classParameters : classParametersArrayList){
                if (localisationItem.getLocalisation().compareTo(classParameters.getClassroom().getClassroom_name())==0){
                    exist=true;
                }
            }
            if (!exist){
                localisationItemArrayList.get(i).setSelected(false);
            }
            else {
                localisationItemArrayList.get(i).setSelected(true);
            }
        }

        if(selectedClassParameter.getStaffSelected()!=null
                &&selectedClassParameter.getStaffSelected().size()!=0) {
            for (int i = 0; i < staffItemList.size(); i++) {
                boolean exist = false;
                Item item= staffItemList.get(i);
                for (String string : (ArrayList<String>)selectedClassParameter.getStaffSelected()) {
                    if (((String)item.getId()).compareTo(string) == 0) {
                        exist = true;
                    }
                }
                if (!exist) {
                    staffItemList.get(i).setSelected(false);
                }
                else  {
                    staffItemList.get(i).setSelected(true);
                }
            }
        } if(selectedClassParameter.getIntensitySelected()!=null
                &&selectedClassParameter.getIntensitySelected().size()!=0) {
            for (int i = 0; i < intensityPossibleItemList.size(); i++) {
                boolean exist = false;
                Item item= intensityPossibleItemList.get(i);
                for (String string : (ArrayList<String>)selectedClassParameter.getIntensitySelected()) {
                    if (((String)item.getId()).compareTo(string) == 0) {
                        exist = true;
                    }
                }
                if (!exist) {
                    intensityPossibleItemList.get(i).setSelected(false);
                }
                else  {
                    intensityPossibleItemList.get(i).setSelected(true);
                }
            }
        }
    }
}
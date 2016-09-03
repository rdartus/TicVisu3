package com.example.richard_dt.visualisation.Activities.CoursFragments;

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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Activities.MainFrag;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.LocalisationItem;
import com.example.richard_dt.visualisation.Helper.LocalisationItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.PopulateList;
import com.example.richard_dt.visualisation.Helper.Test_swipe.DataAdapter;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Richard-DT on 24/08/2016.
 */
public class SelectInformationFrag extends Fragment implements View.OnClickListener{


    public final String STAFF = "staff";

    private EditText t;

    private static ArrayList<Item<String>> staffItemArrayList = new ArrayList<>();

    private  static ListView lvStaff;
    private static ArrayList<String> selectedStaffArrayList;
    ItemAdapter<String> itemListAdapter;



    // Select salle
        private static ListView lvClassrooms;
        private Parser CountryParser;

        private static ArrayList<LocalisationItem> localisationItemArrayList = new ArrayList<>();
        private static ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();

        private LocalisationItemAdapter localisationItemAdapter;
        private ArrayList<String> path = new ArrayList<>();
        private Gson gson;

        private Button bt,btback;
        private String Classroom="classroom";

        private Bundle b;

        private ArrayList<ArrayList> Clist = new ArrayList<>();
        private ArrayList<ArrayList> Rlist = new ArrayList<>();
        private ArrayList<ArrayList> Countrylist = new ArrayList<>();

        private RecyclerView recyclerView;
        //    private ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();
        private DataAdapter selectAdapter;
        private Paint paint = new Paint();



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.select_information_frag,null);
            CountryParser = new Parser(getContext());
            gson=new Gson();

            b=getArguments();
            localisationItemAdapter = new LocalisationItemAdapter(getContext(),0,localisationItemArrayList);

            serialArrayArray Clistserial= (serialArrayArray) b.getSerializable("clist");
            serialArrayArray Rlistserial=(serialArrayArray) b.getSerializable("Rlist");
            serialArrayArray Countrylistserial=(serialArrayArray) b.getSerializable("Countrylist");

            if (Rlistserial != null) {
                Rlist = Rlistserial.getList();
            }
            if (Countrylistserial != null) {
                Countrylist = Countrylistserial.getList();
            }
            if (Clistserial != null ) {
                Clist = Clistserial.getList();
            }
            if (Rlistserial != null) {
                Rlist = Rlistserial.getList();
            }
            if (Countrylistserial != null) {
                Countrylist = Countrylistserial.getList();
            }


//        selectedStaffArrayList = new ArrayList<>();
            lvClassrooms =(ListView) rootView.findViewById(R.id.lv);
            bt=(Button) rootView.findViewById(R.id.goToSalles);
            btback=(Button) rootView.findViewById(R.id.btSalle) ;

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_selectFrag);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            selectAdapter = new DataAdapter(classParametersArrayList,getContext());
            recyclerView.setAdapter(selectAdapter);
            initSwipe();


            bt.setVisibility(View.INVISIBLE);
            btback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (path.size() == 1) {
                        PopulateList.getCountryFromDB(getContext(),
                                localisationItemArrayList,
                                localisationItemAdapter,
                                lvClassrooms,Countrylist);
                        if(path.size()!=0)
                            path.remove(path.size() - 1);

                    } else if (path.size() == 2) {
                        PopulateList.getRegionFromDB(path.get(0),
                                getContext(),
                                localisationItemArrayList,
                                localisationItemAdapter,
                                lvClassrooms,Rlist);
                        if(path.size()!=1)
                            path.remove(path.size() - 1);
                    } else if (path.size() == 3) {
                        PopulateList.getRegionFromDB(path.get(0),
                                getContext(),
                                localisationItemArrayList,
                                localisationItemAdapter,
                                lvClassrooms,Rlist);

//                    if(selectedStaffArrayList!=null)
//                    {if(selectedStaffArrayList.size()!=0){selectedStaffArrayList.clear();}}
                        path.remove(path.size() - 1);
                        path.remove(path.size() - 1);
                    }
                }
            });

            try {
                CountryParser.execute("country").get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            PopulateList.getCountryFromDB(getContext(),
                    localisationItemArrayList,
                    localisationItemAdapter,
                    lvClassrooms,Countrylist);


            if(Countrylist.size()!=0) {
                Indicateur.recurNotifier(Countrylist, lvClassrooms, localisationItemAdapter, getContext());
            }
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<com.example.richard_dt.visualisation.gsApiClass.Classroom> classroomArrayList = new ArrayList<>();
                    for (ClassParameters classParameters:classParametersArrayList)
                    {
                        classroomArrayList.add(classParameters.getClassroom());
                    }
                    startActivity(new Intent(getContext(), ClassroomDetail.class)
                            .putExtra("path", path)
                            .putExtra("classroom",Classroom)
                            .putExtra("classroomlist",classroomArrayList));

                }
            });
            lvClassrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Boolean Click=true;
                    String idItem="";
                    if(path.size()==3){path.remove(path.size()-1);}



                    if(path.size()==0) {
                        idItem = localisationItemAdapter.getItem(position).getId();
                        path.add(path.size(), idItem);
                        Click=false;
                    }
                    if (path.size() == 1 && !Click) {

                        PopulateList.getRegionFromDB(idItem,
                                getContext(),
                                localisationItemArrayList,
                                localisationItemAdapter,
                                lvClassrooms,Rlist);


                    }
                    if(path.size()==1 && Click) {
                        idItem = localisationItemAdapter.getItem(position).getId();
                        path.add(path.size(), idItem);
                        Click=false;


                    }
                    if(path.size()==2 && !Click){
                        try {
                            PopulateList.getClassroomList(idItem,
                                    path.get(0),
                                    getContext(),
                                    localisationItemArrayList,
                                    localisationItemAdapter,
                                    lvClassrooms,Clist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
                        bt.setVisibility(View.VISIBLE);

                    }if(path.size()==2 && Click) {


//                        int index  = (position - lvClassrooms.getFirstVisiblePosition());
                        int index  = (position);
                        localisationItemArrayList.get(index).setSelected(true);
                        localisationItemAdapter.notifyDataSetChanged();
                        idItem = localisationItemArrayList.get(index).getId();
                        Classroom classroom = MainFrag.getClassroom(idItem,getContext());

                        ClassParameters selectedClassParameter = new ClassParameters(classroom);

                        boolean isInList = false;
                        int j =-1;
                        for (int i =0;i<classParametersArrayList.size();i++){
                            if(classParametersArrayList.get(i).compareTo(classroom)==0){
                                isInList = true;
                                j=i;
                            }
                        }
                        if (j!= -1) {
                            classParametersArrayList.remove(j);
                        }
                        //s'il existe pas l'ajouter a la liste
                        if(!    isInList){
                            classParametersArrayList.add(classParametersArrayList.size(),selectedClassParameter);
                        }

                        MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
                        lvClassrooms.invalidateViews();
                    }
                }



            });

            Button b = (Button) rootView.findViewById(R.id.Call);
            Button b2 = (Button) rootView.findViewById(R.id.Blisterreur);

            Button bValid = (Button) rootView.findViewById(R.id.LaunchProfFrag);

            t = (EditText) rootView.findViewById(R.id.etResearchBar);

            gson = new Gson();

            b.setOnClickListener(this);
            b2.setOnClickListener(this);
            bValid.setOnClickListener(this);

            selectedStaffArrayList = new ArrayList<>();
            lvStaff =(ListView) rootView.findViewById(R.id.LvprofSelect);

            getTeacherList();

            itemListAdapter = new ItemAdapter<>(staffItemArrayList, this.getActivity());

            lvStaff.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

            lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String idItem = itemListAdapter.getItem(position).getId();

//                    path.add(path.size(), idItem);

                    if(!selectedStaffArrayList.contains((idItem))){
                        selectedStaffArrayList.add((idItem));
                    }
                    else
                    {
                        selectedStaffArrayList.remove((idItem));
                    }
                    synchroniseSelection();
//                    if (!selectedStaffArrayList.contains(((Item<String>) lvStaff.getAdapter().getItem(position)).getId())) {
//                        selectedStaffArrayList.add(((Item<String>) lvStaff.getAdapter().getItem(position)).getId());
//                        ((Item<String>) lvStaff.getAdapter().getItem(position)).setSelected(true);
//                    } else {
//                        selectedStaffArrayList.remove(((Item<String>) lvStaff.getAdapter().getItem(position)).getId());
//                        ((Item<String>) lvStaff.getAdapter().getItem(position)).setSelected(false);
//
//                    }
//                Testeur.ColorListViewMulti(lvStaff, (ItemAdapter) lvStaff.getAdapter(), getContext(), selectedStaffArrayList);
                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (path.size() == 0) {

                PopulateList.getCountryFromDB(getContext(),
                        localisationItemArrayList,
                        localisationItemAdapter,
                        lvClassrooms,Countrylist);
            }
            if (path.size() == 1) {

                PopulateList.getRegionFromDB(path.get(0),
                        getContext(),
                        localisationItemArrayList,
                        localisationItemAdapter,
                        lvClassrooms,Rlist);
            }
            if (path.size() >= 2) {
                try {

                    PopulateList.getClassroomList(path.get(1),
                            path.get(0),
                            getContext(),
                            localisationItemArrayList,
                            localisationItemAdapter,
                            lvClassrooms,Clist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
            }
            if (classParametersArrayList!= null && classParametersArrayList.size()!=0){
                bt.setVisibility(View.VISIBLE);
            }
        }

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
                        synchroniseSelection();
                        lvClassrooms.invalidateViews();
                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    Bitmap icon;
                    if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if(dX > 0){
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
            }
            for (int i=0;i< staffItemArrayList.size();i++){
                boolean exist = false;
                Item<String> item = staffItemArrayList.get(i);
                for (String staff : selectedStaffArrayList){
                    if (item.getId().compareTo(staff)==0){
                        exist=true;
                    }
                }
                if (!exist){
                    staffItemArrayList.get(i).setSelected(false);
                }else {
                    staffItemArrayList.get(i).setSelected(true);
                }
            }
            refreshList();
        }


    @Override
    public void onClick(View v) {
        ArrayList<Staff> stafflist = new ArrayList<>();
        int id = v.getId();
        switch (id) {
            case R.id.LaunchProfFrag:
                String Cstring="0";
                for(int i = 0; i< lvStaff.getChildCount(); i++)
                {
                    for (String s : selectedStaffArrayList){
                        Cstring=Cstring+","+ s;
                    }
                }
                Log.d("test",Cstring);
                if(!Cstring.equals("0")) {
                    Parser P = new Parser(getContext());
                    try {
                        P.execute(P.ListQuery("staff", "user_id", Cstring)).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (P.getReturned() != null && P.getReturned().length()!=0) {
                        for (int k = 0; k < P.getReturned().length(); k++) {
                            Staff s;
                            try {
                                s = gson.fromJson(String.valueOf(P.getReturned().get(k)), Staff.class);

                                stafflist.add(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    startActivity(new Intent(getContext(), ClassroomDetail.class).putExtra("path", path).putExtra("stafflist", stafflist).putExtra("classroom",STAFF));

                }else{
                    Toast.makeText(getContext(),"Veuillez selectionner au moins un professeur",Toast.LENGTH_LONG).show();}


                break;
            case R.id.Call:
                if (!t.getText().toString().equals("")) {
                    if (t.getText().toString().length() != 1) {
                        //Search for last name
                        Parser p = new Parser(getActivity().getApplicationContext());
                        try {
                            p.execute(p.EqualQuery("staff", "user_nom", t.getText().toString())).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        if (p.getReturned() != null && p.getReturned().length()!=0) {
                            try {
                                stafflist.add(gson.fromJson((String.valueOf(p.getReturned().get(0))), Staff.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(getContext(), ClassroomDetail.class).putExtra("path", path).putExtra("stafflist", stafflist).putExtra("classroom", STAFF));
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Personne non trouvée", Toast.LENGTH_LONG).show();
                        }
                    }

                    else if (t.getText().toString().length() == 1) {
                        //Search By First Last Name Letter
                        Parser p = new Parser(getContext());

                        try {
                            p.execute(p.ContainsQuery("staff", "user_nom", t.getText().toString())).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        if (p.getReturned() != null) {

                            if (staffItemArrayList != null) {
                                if (staffItemArrayList.size() != 0) {
                                    staffItemArrayList.clear();
                                }

                                for (int i = 0; i < p.getReturned().length(); i++) {
                                    Staff s;
                                    try {
                                        s = gson.fromJson(String.valueOf(p.getReturned().get(i)), Staff.class);
                                        staffItemArrayList.add(new Item<>(s.getNomComplet(), s.getUser_id()));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                itemListAdapter = new ItemAdapter<>(staffItemArrayList, this.getActivity());

                                lvStaff.setAdapter(itemListAdapter);
                                itemListAdapter.notifyDataSetChanged();
                            }


                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Personne non trouvée", Toast.LENGTH_LONG).show();
                        }
                    } }else {
                    Toast.makeText(getActivity().getBaseContext(), "Veuillez renseignez le champ", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.Blisterreur:
                getTeacherList();
                itemListAdapter = new ItemAdapter<>(staffItemArrayList, this.getActivity());

                lvStaff.setAdapter(itemListAdapter);
                itemListAdapter.notifyDataSetChanged();
                if(selectedStaffArrayList !=null){
                    if(selectedStaffArrayList.size()!=0){
                        selectedStaffArrayList.clear();
                    }
                }
                break;
        }
    }

    public void getTeacherList() {

        Parser p = new Parser(getContext());

        try {
            p.execute(p.EqualQuery("staff", "staff_issue_type", "1")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (p.getReturned() != null) {

            if (staffItemArrayList != null) {
                if (staffItemArrayList.size() != 0) {
                    staffItemArrayList.clear();
                }

                for (int i = 0; i < p.getReturned().length(); i++) {
                    Staff s;
                    try {
                        s = gson.fromJson(String.valueOf(p.getReturned().get(i)), Staff.class);
                        staffItemArrayList.add(new Item<>(s.getNomComplet(), s.getUser_id()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                staffItemArrayList.add(new Item<>("Pas d'erreur de professeur", "0"));
            }


        }

    }

    public static void refreshList(){
        lvClassrooms.invalidateViews();
        lvStaff.invalidateViews();
    }
}

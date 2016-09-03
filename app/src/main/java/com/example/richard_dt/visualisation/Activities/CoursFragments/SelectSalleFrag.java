//package com.example.richard_dt.visualisation.Activities.CoursFragments;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//
//import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
//import com.example.richard_dt.visualisation.Activities.MainFrag;
//import com.example.richard_dt.visualisation.Helper.ClassParameters;
//import com.example.richard_dt.visualisation.Helper.Indicateur;
//import com.example.richard_dt.visualisation.Helper.LocalisationItem;
//import com.example.richard_dt.visualisation.Helper.LocalisationItemAdapter;
//import com.example.richard_dt.visualisation.Helper.Parser;
//import com.example.richard_dt.visualisation.Helper.PopulateList;
//import com.example.richard_dt.visualisation.Helper.Test_swipe.DataAdapter;
//import com.example.richard_dt.visualisation.Helper.serialArrayArray;
//import com.example.richard_dt.visualisation.R;
//import com.example.richard_dt.visualisation.gsApiClass.Classroom;
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
//
//public class SelectSalleFrag extends Fragment {
//
//    ListView lv;
//    Parser CountryParser;
//
//    private static ArrayList<LocalisationItem> localisationItemArrayList = new ArrayList<>();
//    private static ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();
////    private ArrayList<Item> al = new ArrayList<>();
//    private LocalisationItemAdapter localisationItemAdapter;
////    private ArrayList<String> selectList;
//    private ArrayList<String> path = new ArrayList<>();
//    Gson gson;
//
////    ItemAdapter itemListAdapter;
//    Button bt,btback;
//    String Classroom="classroom";
//
//    Bundle b;
//
//    private ArrayList<ArrayList> Clist = new ArrayList<>();
//    private ArrayList<ArrayList> Rlist = new ArrayList<>();
//    private ArrayList<ArrayList> Countrylist = new ArrayList<>();
//
//    private RecyclerView recyclerView;
////    private ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();
//    private DataAdapter selectAdapter;
//    private Paint paint = new Paint();
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.select_information_frag,null);
//        CountryParser = new Parser(getContext());
//        gson=new Gson();
//
//        b=getArguments();
//        localisationItemAdapter = new LocalisationItemAdapter(getContext(),0,localisationItemArrayList);
//
//        serialArrayArray Clistserial= (serialArrayArray) b.getSerializable("clist");
//        serialArrayArray Rlistserial=(serialArrayArray) b.getSerializable("Rlist");
//        serialArrayArray Countrylistserial=(serialArrayArray) b.getSerializable("Countrylist");
//
//        if (Rlistserial != null) {
//            Rlist = Rlistserial.getList();
//        }
//        if (Countrylistserial != null) {
//            Countrylist = Countrylistserial.getList();
//        }
//        if (Clistserial != null ) {
//            Clist = Clistserial.getList();
//        }
//        if (Rlistserial != null) {
//            Rlist = Rlistserial.getList();
//        }
//        if (Countrylistserial != null) {
//            Countrylist = Countrylistserial.getList();
//        }
//
//
////        selectList = new ArrayList<>();
//        lv=(ListView) rootView.findViewById(R.id.lv);
//        bt=(Button) rootView.findViewById(R.id.goToSalles);
//        btback=(Button) rootView.findViewById(R.id.btSalle) ;
//
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_selectFrag);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        selectAdapter = new DataAdapter(classParametersArrayList,getContext());
//        recyclerView.setAdapter(selectAdapter);
//        initSwipe();
//
//
//        bt.setVisibility(View.INVISIBLE);
//        btback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (path.size() == 1) {
//                    PopulateList.getCountryFromDB(getContext(),
//                        localisationItemArrayList,
//                        localisationItemAdapter,
//                        lv,Countrylist);
//                    if(path.size()!=0)
//                    path.remove(path.size() - 1);
//
//                } else if (path.size() == 2) {
//                    PopulateList.getRegionFromDB(path.get(0),
//                            getContext(),
//                            localisationItemArrayList,
//                            localisationItemAdapter,
//                            lv,Rlist);
//                    if(path.size()!=1)
//                    path.remove(path.size() - 1);
//                } else if (path.size() == 3) {
//                    PopulateList.getRegionFromDB(path.get(0),
//                        getContext(),
//                        localisationItemArrayList,
//                        localisationItemAdapter,
//                        lv,Rlist);
//
//
//
//
////                    if(selectList!=null)
////                    {if(selectList.size()!=0){selectList.clear();}}
//                    path.remove(path.size() - 1);
//                    path.remove(path.size() - 1);
//                }
//            }
//        });
//
//        try {
//            CountryParser.execute("country").get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        PopulateList.getCountryFromDB(getContext(),
//                localisationItemArrayList,
//                localisationItemAdapter,
//                lv,Countrylist);
//
//
//        if(Countrylist.size()!=0) {
//            Indicateur.recurNotifier(Countrylist, lv, localisationItemAdapter, getContext());
//        }
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ArrayList<Classroom> classroomArrayList = new ArrayList<>();
//                for (ClassParameters classParameters:classParametersArrayList)
//                {
//                    classroomArrayList.add(classParameters.getClassroom());
//                }
//                startActivity(new Intent(getContext(), ClassroomDetail.class)
//                                .putExtra("path", path)
//                                .putExtra("classroom",Classroom)
//                                .putExtra("classroomlist",classroomArrayList));
//
////                ArrayList<Classroom> ClassroomList=new ArrayList<>();
////                String Cstring="0";
////
////
////                for(int i=0;i<lv.getChildCount();i++)
////                {
////                    for (String s : selectList){
////                        Cstring=Cstring+","+ s;
////                    }
////                }
////                if(!Cstring.equals("0")) {
////                    Parser P = new Parser(getContext());
////                    try {
////                        P.execute(P.AddQuery(P.ListQuery("classroom", "classroom_id", Cstring), P.EqualQuery("classroom", "classroom_country_code", path.get(0)))).get();
////                    } catch (InterruptedException | ExecutionException e) {
////                        e.printStackTrace();
////                    }
////                    if (P.getReturned() != null) {
////                        for (int k = 0; k < P.getReturned().length(); k++) {
////                            Classroom C;
////                            try {
////                                C = gson.fromJson(String.valueOf(P.getReturned().get(k)), Classroom.class);
////                                C.setRegion_id(0);
////                                ClassroomList.add(C);
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////
////                        }
////
////                        startActivity(new Intent(getContext(), ClassroomDetail.class)
////                                .putExtra("path", path)
////                                .putExtra("classroomlist", ClassroomList)
////                                .putExtra("classroom",Classroom));
////
////                    }
////
////                }
//            }
//        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                Boolean Click=true;
//                String idItem="";
//                if(path.size()==3){path.remove(path.size()-1);}
//
//
//
//                if(path.size()==0) {
//                    idItem = localisationItemAdapter.getItem(position).getId();
//                    path.add(path.size(), idItem);
//                    Click=false;
//                }
//                if (path.size() == 1 && !Click) {
//
//                    PopulateList.getRegionFromDB(idItem,
//                            getContext(),
//                            localisationItemArrayList,
//                            localisationItemAdapter,
//                            lv,Rlist);
//
//
//                }
//                if(path.size()==1 && Click) {
//                    idItem = localisationItemAdapter.getItem(position).getId();
//                    path.add(path.size(), idItem);
//                    Click=false;
//
//
//                }
//                if(path.size()==2 && !Click){
//                    try {
//                        PopulateList.getClassroomList(idItem,
//                            path.get(0),
//                            getContext(),
//                            localisationItemArrayList,
//                            localisationItemAdapter,
//                            lv,Clist);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
//                    bt.setVisibility(View.VISIBLE);
//
//                }if(path.size()==2 && Click) {
//
//
//                    int index  = (position - lv.getFirstVisiblePosition());
//                    localisationItemArrayList.get(index).setSelected(true);
//                    localisationItemAdapter.notifyDataSetChanged();
//                    idItem = localisationItemArrayList.get(index).getId();
//                    Classroom classroom = MainFrag.getClassroom(idItem,getContext());
//
//                    ClassParameters selectedClassParameter = new ClassParameters(classroom);
//
//                    boolean isInList = false;
//                    int j =-1;
//                    for (int i =0;i<classParametersArrayList.size();i++){
//                        if(classParametersArrayList.get(i).compareTo(classroom)==0){
//                            isInList = true;
//                            j=i;
//                        }
//                    }
//                    if (j!= -1) {
//                        classParametersArrayList.remove(j);
//                    }
//                    //s'il existe pas l'ajouter a la liste
//                    if(!    isInList){
//                        classParametersArrayList.add(classParametersArrayList.size(),selectedClassParameter);
//                    }
//
//                    MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
//                    lv.invalidateViews();
//                }
//            }
//
//
//
//        });
//
//        return rootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (path.size() == 0) {
//
//            PopulateList.getCountryFromDB(getContext(),
//                    localisationItemArrayList,
//                    localisationItemAdapter,
//                    lv,Countrylist);
//        }
//        if (path.size() == 1) {
//
//            PopulateList.getRegionFromDB(path.get(0),
//                    getContext(),
//                    localisationItemArrayList,
//                    localisationItemAdapter,
//                    lv,Rlist);
//        }
//        if (path.size() >= 2) {
//            try {
//
//                PopulateList.getClassroomList(path.get(1),
//                        path.get(0),
//                        getContext(),
//                        localisationItemArrayList,
//                        localisationItemAdapter,
//                        lv,Clist);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            MainFrag.setSelectedClasses(localisationItemArrayList,classParametersArrayList);
//            }
//        if (classParametersArrayList!= null && classParametersArrayList.size()!=0){
//            bt.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void initSwipe(){
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
////        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//
//                if (direction == ItemTouchHelper.LEFT){
//                    selectAdapter.removeItem(position);
//                    synchroniseSelection();
//                    lv.invalidateViews();
//                }
////                else {
////                    ClassParameters classParameters =classParametersArrayList.get(position);
////                    int regionId = classParameters.getClassroom().getRegion_id();
////                    try {
////                        PopulateList.getClassroomList(""+regionId,
////                                path.get(0),
////                                getContext(),
////                                localisationItemArrayList,
////                                localisationItemAdapter,
////                                lvSelect,Clist);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    coursList = (ArrayList<Cours>) getCoursList(path.get(2),getContext());
////                    listPossibleIntensity = getListPossibleIntensity(coursList);
////                    initIntensityList();
////                    initStaffList(""+classParameters.getClassroom().getClassroom_id());
////             }
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//
//                Bitmap icon;
//                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//
//                    View itemView = viewHolder.itemView;
//                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                    float width = height / 3;
//
//                    if(dX > 0){
////                        paint.setColor(Color.parseColor("#388E3C"));
////                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
////                        c.drawRect(background,paint);
////                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
////                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
////                        c.drawBitmap(icon,null,icon_dest,paint);
//                    } else {
//                        paint.setColor(Color.parseColor("#D32F2F"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,paint);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
//                        c.drawBitmap(icon,null,icon_dest,paint);
//                    }
//                }
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//    }
//    public static void synchroniseSelection(){
//        for (int i=0;i< localisationItemArrayList.size();i++){
//            boolean exist = false;
//            LocalisationItem localisationItem = localisationItemArrayList.get(i);
//            for (ClassParameters classParameters : classParametersArrayList){
//                if (localisationItem.getLocalisation().compareTo(classParameters.getClassroom().getClassroom_name())==0){
//                    exist=true;
//                }
//            }
//            if (!exist){
//                localisationItemArrayList.get(i).setSelected(false);
//            }
//        }
//    }
//}
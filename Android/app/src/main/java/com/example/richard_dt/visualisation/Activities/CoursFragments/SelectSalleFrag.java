package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.Testeur;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Country;
import com.example.richard_dt.visualisation.gsApiClass.Region;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class SelectSalleFrag extends Fragment {

    ListView lv;
    Parser CountryParser;
    private ArrayList<Item> al = new ArrayList<>();
    private ArrayList<String> selectList;
    private ArrayList<String> path = new ArrayList<>();
    Gson gson;

    ItemAdapter itemListAdapter;
    Button bt,btback;
    String Classroom="classroom";

    Bundle b;

    private ArrayList<ArrayList> Clist = new ArrayList<>();
    private ArrayList<ArrayList> Rlist = new ArrayList<>();
    private ArrayList<ArrayList> Countrylist = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.select_salle_frag,null);
        CountryParser = new Parser(getContext());
        gson=new Gson();

        b=getArguments();

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


        selectList = new ArrayList<>();
        lv=(ListView) rootView.findViewById(R.id.lv);
        bt=(Button) rootView.findViewById(R.id.goToSalles);
        btback=(Button) rootView.findViewById(R.id.btSalle) ;

        bt.setVisibility(View.INVISIBLE);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path.size() == 1) {
                    getCountryFromDB();
                    if(path.size()!=0)
                    path.remove(path.size() - 1);

                } else if (path.size() == 2) {
                    getRegionFromDB(path.get(0));
                    if(path.size()!=1)
                    path.remove(path.size() - 1);
                } else if (path.size() == 3) {
                    getRegionFromDB(path.get(0));
                    if(selectList!=null)
                    {if(selectList.size()!=0){selectList.clear();}}
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

        getCountryFromDB();


        if(Countrylist.size()!=0) {
            Indicateur.recurNotifier(Countrylist, lv, itemListAdapter, getContext());

            Indicateur.recurApplyColor(Countrylist.get(0), null, lv, itemListAdapter, getContext());
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Classroom> ClassroomList=new ArrayList<>();
                String Cstring="0";


                for(int i=0;i<lv.getChildCount();i++)
                {
                    for (String s : selectList){
                        Cstring=Cstring+","+ s;
                    }
                }
                if(!Cstring.equals("0")) {
                    Parser P = new Parser(getContext());
                    try {
                        P.execute(P.AddQuery(P.ListQuery("classroom", "classroom_id", Cstring), P.EqualQuery("classroom", "classroom_country_code", path.get(0)))).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (P.getReturned() != null) {
                        for (int k = 0; k < P.getReturned().length(); k++) {
                            Classroom C;
                            try {
                                C = gson.fromJson(String.valueOf(P.getReturned().get(k)), Classroom.class);
                                C.setRegion_id(0);
                                ClassroomList.add(C);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        startActivity(new Intent(getContext(), ClassroomDetail.class).putExtra("path", path).putExtra("classroomlist", ClassroomList).putExtra("classroom",Classroom));

                    }

                }
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Boolean Click=true;
                String idItem="";
                if(path.size()==3){path.remove(path.size()-1);}



                if(path.size()==0) {
                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click=false;
                }
                if (path.size() == 1 && !Click) {

                    getRegionFromDB(idItem);


                }
                if(path.size()==1 && Click) {
                    idItem = itemListAdapter.getItem(position).getStrId();
                    path.add(path.size(), idItem);
                    Click=false;


                }
                if(path.size()==2 && !Click){
                    try {
                        getClassroomList(idItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bt.setVisibility(View.VISIBLE);

                }if(path.size()==2 && Click) {

                    idItem = itemListAdapter.getItem(position).getStrId();

                    path.add(path.size(), idItem);


                    if (!selectList.contains(((Item) lv.getAdapter().getItem(position)).getStrId())) {
                        selectList.add(((Item) lv.getAdapter().getItem(position)).getStrId());
                    } else {
                        selectList.remove(((Item) lv.getAdapter().getItem(position)).getStrId());
                    }
                    Testeur.ColorListViewMulti(lv, (ItemAdapter) lv.getAdapter(), getContext(), selectList);
                }
               // pathUp.setText(path.size()+"");
            }



        });

        return rootView;
    }
    public void getCountryFromDB(){
        if(al!=null) {
            if(al.size()!=0) {al.clear();}
            //REST API
            if(CountryParser.getReturned()!=null){
                for (int i = 0; i < CountryParser.getReturned().length(); i++) {
                    Country C;
                    try {
                        C = gson.fromJson(String.valueOf(CountryParser.getReturned().get(i)), Country.class);
                        al.add(new Item(C.getCountry_name(), C.getCountry_code()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}


            itemListAdapter = new ItemAdapter(al, getContext());

            lv.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();

            if (path!=null)
                if(path.size()!=0)
                    path.clear();

        }}
    public void getRegionFromDB(String idPays){
        Parser newP = new Parser(getContext());
        try {
            newP.execute(newP.EqualQuery("region", "region_country_code", idPays)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(al!=null) {
            if (al.size() != 0) {
                al.clear();
            }
            for (int i = 0; i < newP.getReturned().length(); i++) {
                Region r=null;
                try {
                    r = gson.fromJson(String.valueOf(newP.getReturned().get(i)), Region.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(r!=null)al.add(new Item(r.getRegion_name(), r.getRegion_id()));
            }

            itemListAdapter = new ItemAdapter(al,getContext());
            lv.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
            Indicateur.recurNotifier(Rlist, lv, itemListAdapter, getContext());

            Indicateur.recurApplyColor(Rlist.get(0), null, lv, itemListAdapter, getContext());

        }
    }
    public void getClassroomList(String iditem) throws JSONException {




        Parser newP = new Parser(getContext());
        try {
            newP.execute(newP.AddQuery(newP.EqualQuery("region", "region_id", iditem+""),newP.EqualQuery("region","region_country_code",path.get(0)+""))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Region R=gson.fromJson(String.valueOf(newP.getReturned().get(0)),Region.class);

        String Cstring;


        Cstring=R.getClassroom_list().get(0)+"";

        for(int i=1;i<R.getClassroom_list().size();i++)
        {Cstring=Cstring+","+R.getClassroom_list().get(i);}

        Parser P=new Parser(getContext());
        try {
            P.execute(P.AddQuery(P.ListQuery("classroom","classroom_id",Cstring),P.EqualQuery("classroom","classroom_country_code", path.get(0)))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(al!=null) {
            if (al.size() != 0) {
                al.clear();
            }
            for (int i = 0; i < P.getReturned().length(); i++) {
                Classroom C = null;
                try {
                    C = gson.fromJson(String.valueOf(P.getReturned().get(i)), Classroom.class);
                    C.setRegion_id(0);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (C != null) {
                    al.add(new Item(C.getClassroom_name(),""+C.getClassroom_id()));
                }
            }

            itemListAdapter = new ItemAdapter(al, getContext());
            lv.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
            Indicateur.recurNotifier(Clist, lv, itemListAdapter, getContext());

            Indicateur.recurApplyColor(Clist.get(0), null, lv, itemListAdapter, getContext());
        }

    }
}
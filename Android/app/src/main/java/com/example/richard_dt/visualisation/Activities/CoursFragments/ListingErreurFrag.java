package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Activities.ErreurDetail;
import com.example.richard_dt.visualisation.Activities.ErreurTableActivity;
import com.example.richard_dt.visualisation.Helper.ErreurItem;
import com.example.richard_dt.visualisation.Helper.ErreurItemAdapter;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Country;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Region;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ListingErreurFrag extends Fragment {

    ArrayList<ErreurItem> Erreurs=new ArrayList<>();

    ListView lv;
    Parser CountryParser;
    ItemAdapter itemListAdapter;
    private ArrayList<Item> al = new ArrayList<>();
    private ArrayList<String> path =new ArrayList<>();

    private ArrayList<Item> CurrentClassroom=new ArrayList<>();
    private Gson gson;

    Bundle b;

    private ArrayList<ArrayList> Clist = new ArrayList<>();
    private ArrayList<ArrayList> Rlist = new ArrayList<>();
    private ArrayList<ArrayList> Countrylist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listing_erreur_frag, null);

        lv=(ListView)rootView.findViewById(R.id.ListeErreur);
        Button bt = (Button) rootView.findViewById(R.id.btErreur);

        gson = new Gson();

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

        CountryParser=new Parser(getContext());
        try {
            CountryParser.execute("country").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        getCountryFromDB();

        Indicateur.recurNotifier(Countrylist,lv,itemListAdapter,getContext());

        Indicateur.recurApplyColor(Countrylist.get(0),null,lv,itemListAdapter,getContext());

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (path.size() == 1) {
                    getCountryFromDB();
                    path.remove(path.size() - 1);

                } else if (path.size() == 2) {
                    getRegionFromDB(path.get(0));

                     Indicateur.recurNotifier(Rlist,lv,itemListAdapter,getContext());
                     Indicateur.recurApplyColor(Rlist.get(0),null,lv,itemListAdapter,getContext());
                    path.remove(path.size() - 1);
                } else if (path.size() == 3) {

                    path.remove(path.size() - 1);
                    path.remove(path.size() - 1);

                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                    Indicateur.recurNotifier(Rlist,lv,itemListAdapter,getContext());
                    Indicateur.recurApplyColor(Rlist.get(0),null,lv,itemListAdapter,getContext());


                }
                if(path.size()==2 && !Click){
                    idItem = itemListAdapter.getItem(position).getStrId();
                    Erreurs=getErrorByRegion(idItem);



                    startActivity(new Intent(getContext(), ErreurTableActivity.class).putExtra("erreurlist",Erreurs));

                }
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
            if (Countrylist != null && Countrylist.size() != 0) {
                Indicateur.recurNotifier(Countrylist, lv, itemListAdapter, getContext());

                Indicateur.recurApplyColor(Countrylist.get(0), null, lv, itemListAdapter, getContext());
            }



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
            ItemAdapter itemListAdapter = new ItemAdapter(al, getContext());

            lv.setAdapter(itemListAdapter);
            itemListAdapter.notifyDataSetChanged();
            if (Rlist != null && Rlist.size() != 0) {
                Indicateur.recurNotifier(Rlist, lv, itemListAdapter, getContext());

                Indicateur.recurApplyColor(Rlist.get(0), null, lv, itemListAdapter, getContext());
            }

        }

        }


    @Override
    public void onResume() {
        super.onResume();
        if(path!=null)
        {if( path.size()!=0){

        getRegionFromDB(path.get(0));
            Indicateur.recurNotifier(Rlist,lv,itemListAdapter,getContext());
            Indicateur.recurApplyColor(Rlist.get(0),null,lv,itemListAdapter,getContext());
            path.remove(path.size()-1);

        }}

    }

    public ArrayList<ErreurItem> getErrorByRegion(String Iditem){
    getClassroomList(Iditem);


    ArrayList<ErreurItem> arrayList=new ArrayList<>();

    Parser parser=new Parser(getContext());
    try {
        parser.execute("RetrieveErrorList/"+Iditem).get();
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
    if(parser.getReturned()!=null){
        Log.d("error list size",parser.getReturned().length()+"");
        for(int i=0;i<parser.getReturned().length();i++){
            Cours C;
            try {
                C=(gson.fromJson(String.valueOf(parser.getReturned().get(i)), Cours.class));
                String casted="";
                for(int j=0;j<CurrentClassroom.size();j++)
                {
                    String val = Integer.toString(C.getClassroom_id());
                    if(((int)CurrentClassroom.get(j).getId() +"").equals(val))
                    {casted=CurrentClassroom.get(j).getItemName();
                        arrayList.add(new ErreurItem(C,casted));}
                }
                arrayList.add(new ErreurItem(C,casted));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    ItemAdapter itemListAdapter = new ItemAdapter(al, getContext());

    lv.setAdapter(itemListAdapter);
    itemListAdapter.notifyDataSetChanged();



    Indicateur.recurNotifier(Rlist,lv,itemListAdapter,getContext());
    Indicateur.recurApplyColor(Rlist.get(0),null,lv,itemListAdapter,getContext());

    return arrayList;

}



    public void getClassroomList(String iditem) {




        Parser newP = new Parser(getContext());
        try {
            newP.execute(newP.AddQuery(newP.EqualQuery("region", "region_id", iditem+""),newP.EqualQuery("region","region_country_code",path.get(0)+""))).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Region R= null;
        try {
            R = gson.fromJson(String.valueOf(newP.getReturned().get(0)),Region.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String Cstring="";


        if (R != null) {
            Cstring=R.getClassroom_list().get(0)+"";
        }

        if (R != null) {
            for(int i=1;i<R.getClassroom_list().size();i++)
            {Cstring=Cstring+","+R.getClassroom_list().get(i);}
        }

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
                    CurrentClassroom.add(new Item(C.getClassroom_name(),C.getClassroom_id()));
                }
            }
        }
    }


}

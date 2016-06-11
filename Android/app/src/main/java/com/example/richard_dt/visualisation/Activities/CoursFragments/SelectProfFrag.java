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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.Testeur;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SelectProfFrag extends Fragment implements View.OnClickListener {

    public final String STAFF = "staff";

    private EditText t;
    private Gson gson;

    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<Item> arrayList = new ArrayList<>();

    private ListView lv;
    private ArrayList<String> selectList;
    ItemAdapter itemListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.select_prof_frag, null);

        Button b = (Button) rootView.findViewById(R.id.Call);
        Button b2 = (Button) rootView.findViewById(R.id.Blisterreur);

        Button bValid = (Button) rootView.findViewById(R.id.LaunchProfFrag);

        t = (EditText) rootView.findViewById(R.id.editText);

        gson = new Gson();

        b.setOnClickListener(this);
        b2.setOnClickListener(this);
        bValid.setOnClickListener(this);

        selectList = new ArrayList<>();
        lv=(ListView) rootView.findViewById(R.id.LvprofSelect);

        getTeacherList();

        itemListAdapter = new ItemAdapter(arrayList, this.getActivity());

        lv.setAdapter(itemListAdapter);
        itemListAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idItem = itemListAdapter.getItem(position).getStrId();

                path.add(path.size(), idItem);


                if (!selectList.contains(((Item) lv.getAdapter().getItem(position)).getStrId())) {
                    selectList.add(((Item) lv.getAdapter().getItem(position)).getStrId());
                } else {
                    selectList.remove(((Item) lv.getAdapter().getItem(position)).getStrId());
                }
                Testeur.ColorListViewMulti(lv, (ItemAdapter) lv.getAdapter(), getContext(), selectList);
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onClick(View v) {

        ArrayList<Staff> stafflist = new ArrayList<>();
        int id = v.getId();
        switch (id) {
            case R.id.LaunchProfFrag:
                String Cstring="0";
                for(int i=0;i<lv.getChildCount();i++)
                {
                    for (String s : selectList){
                        Cstring=Cstring+","+ s;
                    }
                }Log.d("test",Cstring);
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

                    }else{Toast.makeText(getContext(),"Veuillez selectionner au moins un professeur",Toast.LENGTH_LONG).show();}


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

                        if (arrayList != null) {
                            if (arrayList.size() != 0) {
                                arrayList.clear();
                            }

                            for (int i = 0; i < p.getReturned().length(); i++) {
                                Staff s;
                                try {
                                    s = gson.fromJson(String.valueOf(p.getReturned().get(i)), Staff.class);
                                    arrayList.add(new Item(s.getNomComplet(), s.getUser_id()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            itemListAdapter = new ItemAdapter(arrayList, this.getActivity());

                            lv.setAdapter(itemListAdapter);
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
                itemListAdapter = new ItemAdapter(arrayList, this.getActivity());

                lv.setAdapter(itemListAdapter);
                itemListAdapter.notifyDataSetChanged();
                if(selectList!=null){
                    if(selectList.size()!=0){
                        selectList.clear();
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

            if (arrayList != null) {
                if (arrayList.size() != 0) {
                    arrayList.clear();
                }

                for (int i = 0; i < p.getReturned().length(); i++) {
                    Staff s;
                    try {
                        s = gson.fromJson(String.valueOf(p.getReturned().get(i)), Staff.class);
                        arrayList.add(new Item(s.getNomComplet(), s.getUser_id()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                arrayList.add(new Item("Pas d'erreur de professeur", "0"));
            }


        }

    }
}
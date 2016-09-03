package com.example.richard_dt.visualisation.Activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.richard_dt.visualisation.Activities.CoursFragments.CoursActivity;
import com.example.richard_dt.visualisation.Helper.ErreurItem;
import com.example.richard_dt.visualisation.Helper.ErreurItemAdapter;
import com.example.richard_dt.visualisation.Helper.ErreurTableDataAdapter;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.sortableErreurTableView;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.codecrafters.tableview.listeners.TableDataClickListener;

public class ErreurTableActivity extends AppCompatActivity {
    private ArrayList<ErreurItem> ErreurList = new ArrayList<>();
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.erreur_table_activity);


        b = getIntent().getExtras();
        ErreurList = b.getParcelableArrayList("erreurlist");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolerreur);
        setSupportActionBar(myToolbar);


        getSupportActionBar().setTitle(R.string.Titre);
        getSupportActionBar().setIcon(R.mipmap.gslogo);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final sortableErreurTableView erreurTableView = (sortableErreurTableView) findViewById(R.id.tableView);
        if (erreurTableView != null) {
            erreurTableView.setDataAdapter(new ErreurTableDataAdapter(this, ErreurList));
            erreurTableView.addDataClickListener(new ErreurClickListener());
        }


    }


    private class ErreurClickListener implements TableDataClickListener<ErreurItem> {

        @Override
        public void onDataClicked(final int rowIndex, final ErreurItem clickedData) {
            Cours cours = null;
            Classroom classroom = null;
            Gson gson = new Gson();
            String idCours = clickedData.getCours_id() + "";
            String idClassroom = clickedData.getClassroomId() + "";
            Parser Pclassroom = new Parser(getApplicationContext());
            try {
                Pclassroom.execute(Pclassroom.EqualQuery("classroom", "classroom_id", idClassroom)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            try {
                classroom = gson.fromJson(String.valueOf(Pclassroom.getReturned().get(0)), Classroom.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Parser Pcours = new Parser(getApplicationContext());
            try {
                Pcours.execute(Pcours.EqualQuery("class", "class_id", idCours)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            try {
                cours = gson.fromJson(String.valueOf(Pcours.getReturned().get(0)), Cours.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ArrayList<Staff> staffListCoursSelected = new ArrayList<>();

            if ((cours.getTeacher_list() != null
                    && cours.getTeacher_list().size() != 0)
                    || (cours.getHost_list() != null
                    && cours.getHost_list().size() != 0)) {
                for (Object sId : cours.getHost_list()) {
                    staffListCoursSelected.addAll((ArrayList<Staff>) MainActivity.getStaff((String) sId, getApplicationContext()));
                }
                for (Object sId : cours.getTeacher_list()) {
                    staffListCoursSelected.addAll((ArrayList<Staff>) MainActivity.getStaff((String) sId, getApplicationContext()));
                }
            }

            startActivity(new Intent(getApplicationContext(), CoursActivity.class)
                    .putExtra("classroom", classroom)
                    .putExtra("staffList", staffListCoursSelected)
                    .putExtra("cours", cours));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

}

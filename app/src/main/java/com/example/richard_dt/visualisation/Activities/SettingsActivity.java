package com.example.richard_dt.visualisation.Activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.pref;
import com.example.richard_dt.visualisation.R;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    pref p;
    EditText editText,editText3;
    Button bValider,bPicker;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener rpl =
            new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                    bPicker.setText(hourOfDay + " " + getString(R.string.hour));
                    p.setInt("startHour",hourOfDay);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar4);
        setSupportActionBar(myToolbar);

        p = new pref(this.getApplicationContext());

        editText = (EditText) findViewById(R.id.etUrl);
        editText3 = (EditText) findViewById(R.id.editText3);
//        edstartHour = (EditText) findViewById(R.id.editText2);
        bPicker = (Button) findViewById(R.id.btimePicker);
        editText.setText(p.getString("urlApi"));
        int hour = p.getInt("startHour");
        bPicker.setText(hour+" "+getString(R.string.hour));
        bPicker.setOnClickListener(this);
        editText3.setText(p.getInt("rayon")+"");
//        edstartHour.setText(hour + "");

        TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENDCALL)) {
                    bValider.performClick();
                    return true;
                }
                return false;
            }
        };

        editText.setOnEditorActionListener(enterListener);
//        edstartHour.setOnEditorActionListener(enterListener);
        bValider = (Button) findViewById(R.id.bValider);
        bValider.setOnClickListener(this);

        getSupportActionBar().setTitle("Gym Suédoise");
        getSupportActionBar().setIcon(R.mipmap.gslogo);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar cal=Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(rpl,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getApplicationContext()));
        timePickerDialog.setCancelable(false);
        timePickerDialog.setTitle(getString(R.string.mdtp_select_hours));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btimePicker:
                timePickerDialog.show(getFragmentManager(), "timePicker");
                return;
            case R.id.bValider:
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                if (editText.getText() != null && !editText.getText().toString().equals("")) {
                    p.setString("urlApi", editText.getText().toString());
                }
//                if (edstartHour.getText() != null && !edstartHour.getText().toString().equals("")) {
//                    p.setInt("startHour", Integer.valueOf(edstartHour.getText().toString()));
//                }
                if (editText3.getText() != null && !editText3.getText().toString().equals("")){
                    p.setInt("rayon",Integer.valueOf(editText3.getText().toString()));
                }
                Parser np = new Parser(this.getApplication().getApplicationContext());
                try {
                    np.execute("country").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (np.getReturned() != null && np.getReturned().length() != 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.urlValid), Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.urlInvalid), Toast.LENGTH_LONG);
                    toast.show();
                }

                break;
        }
    }
}

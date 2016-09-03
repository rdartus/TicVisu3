package com.example.richard_dt.visualisation.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class Parser extends AsyncTask<String, Integer, Void>
{
    private JSONArray returned;

    private JSONObject returned1;

    private String base_Url;
    private pref pref;

    public JSONArray getReturned() {
        return returned;
    }

    public JSONObject getReturned1() {
        return returned1;
    }

    public Parser(Context context){
        pref=new pref(context);
        base_Url = pref.getString("urlApi");
    }



    @Override
    protected Void doInBackground(String... params) {
        try {
            this.getCountryList(base_Url+params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getCountryList(String query) throws JSONException {


        HttpGet.get(query, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                returned1=response;

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

                returned = timeline;


            }
        });
    }


            public String EqualQuery(String table, String queryArg, String testedValue) {
                //This will send back table?QueryArg=testedValue

                String ConcatanedChain = "";
                ConcatanedChain = table + "?";
                ConcatanedChain += queryArg;
                ConcatanedChain += "=";
                ConcatanedChain += testedValue;
                return ConcatanedChain;
            }

            public String ContainsQuery(String table, String queryArg, String testedValue) {
                //This will send back table?QueryArg__regex=/^testedValue/i

                String ConcatanedChain = "";
                ConcatanedChain = table + "?";
                ConcatanedChain += queryArg;
                ConcatanedChain += "__regex=/^";
                ConcatanedChain += testedValue;
                ConcatanedChain += "/i";
                return ConcatanedChain;


            }

            public String ListQuery(String table, String queryArg, String testedValue) {
                //This will send back table?QueryArg__in=X,X,X,X

                String ConcatanedChain = "";
                ConcatanedChain = table + "?";
                ConcatanedChain += queryArg;
                ConcatanedChain += "__in=";
                ConcatanedChain += testedValue;
                return ConcatanedChain;


        }
    public String AddQuery(String Query1, String Query2) {
        //This will send back table?QueryArg__in=X,X,X,X

        String ConcatanedChain = "";
        ConcatanedChain = Query1+"&"+Query2;

        return ConcatanedChain;


    }

    }

package com.example.richard_dt.visualisation.Helper;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class apiParser extends AsyncTask<String, Integer, Void>
{
    private JSONArray returned;
    private JSONObject returned1;


    public JSONArray getReturned() {
        return returned;
    }
    public JSONObject getReturned1() {return returned1;}



    public apiParser(){}

    @Override
    protected Void doInBackground(String... params) {

           // this.getList(params[0]);
        return null;
    }


   /* public void getList(String query) throws JSONException {



        HttpGet.getFromAPI(query, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                returned1=response;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                returned = timeline;
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });}*/


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
                ConcatanedChain += " __in=";
                ConcatanedChain += testedValue;
                return ConcatanedChain;


        }

    }

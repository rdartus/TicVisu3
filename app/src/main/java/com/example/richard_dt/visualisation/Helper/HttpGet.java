package com.example.richard_dt.visualisation.Helper;


import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;


public class HttpGet {

    private static SyncHttpClient client = new SyncHttpClient();



    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("Tested API URL : ",url);
        client.get(url, params, responseHandler);
    }


}


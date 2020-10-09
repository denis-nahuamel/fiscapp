package com.fiscapp.fiscapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiscapp.fiscapp.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

public class Connection {

    private static final String TAG = "Connection";
    private static Connection instance = null;
    static Context mContext=null;

    private static final String prefixURL = "http://some/url/prefix/";

    //for Volley API
    public RequestQueue requestQueue;

    private Connection(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        mContext=context;
        //other stuf if you need
    }

    public static synchronized Connection getInstance(Context context)
    {
        if (null == instance)
            instance = new Connection(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized Connection getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(Connection.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    //public void PostRequestString(Object param1, final CustomListener<String> listener)
    public void PostRequestString(String Url, final Map jsonParams, final CustomListener<String> listener)
    {

        String url = Url;

      /*  Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("param1", param1);*/


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                        if(null != response.toString())
                            listener.getResult(response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (null != error.networkResponse)
                        {
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                            listener.getResult(null);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                return jsonParams;
            }
        };;

        requestQueue.add(request);
    }
    //region================alert dialog (solo alertas de mensaje )==============
    public static void showOKDialog(Context context, String title, String message)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
               // .setIcon(android.R.drawable.checkbox_on_background)
                .show();
    }
    //endregion
}


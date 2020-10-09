package com.fiscapp.fiscapp.Helpers;

public interface  VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response);
}

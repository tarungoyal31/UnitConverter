package com.tarungoyaldev.android.unitconverter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class for getting requestQueue
 */
public class UtilitiesSingleton {

    private static Context context;
    private static UtilitiesSingleton singleton;
    private RequestQueue requestQueue;

    private UtilitiesSingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized UtilitiesSingleton getUtilitiesSingleton(Context context) {
        if (singleton == null) {
            singleton = new UtilitiesSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}

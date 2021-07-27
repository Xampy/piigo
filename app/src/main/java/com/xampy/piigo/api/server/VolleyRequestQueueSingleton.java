package com.xampy.piigo.api.server;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueueSingleton {

    private static VolleyRequestQueueSingleton volleyRequestQueueSingleton;
    private RequestQueue requestQueue;
    private static Context context;

    public VolleyRequestQueueSingleton(Context ctx) {
        context = ctx;
    }

    public static synchronized VolleyRequestQueueSingleton getInstance(Context context) {
        if(volleyRequestQueueSingleton == null){
            volleyRequestQueueSingleton = new VolleyRequestQueueSingleton(context);
        }

        return volleyRequestQueueSingleton;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }
}

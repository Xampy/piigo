package com.xampy.piigo.api.server.requests;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class RegisterJsonRequest extends JsonObjectRequest {

    private static final String REGISTER_URL = "";
    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null indicates no
     *                      parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public RegisterJsonRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {

        //Always a post request
        super(Method.POST, REGISTER_URL, jsonRequest, listener, errorListener);
    }
}

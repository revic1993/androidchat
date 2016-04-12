package com.example.rujul.pushnotifications.services;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by rujul on 4/4/2016.
 */
public class VolleyService {

    private PushApp mApp;

    public VolleyService() {
        mApp = PushApp.getApp();
    }

    public void makePostRequest(String url,Map<String,String> params, final VolleyInterface view){
        params.put(Constants.TOKEN_NAME, Constants.TOKEN);
        JsonObjectRequest jsonObjectRequest =   new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        view.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        view.onError(error);
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mApp.addToRequestQueue(jsonObjectRequest, Constants.TAG);
    }

    public interface VolleyInterface{
        public void onSuccess(JSONObject response);
        public void onError(VolleyError error);
    }
}

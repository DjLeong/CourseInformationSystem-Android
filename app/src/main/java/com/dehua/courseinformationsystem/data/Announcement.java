package com.dehua.courseinformationsystem.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.mainactivity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by dehua on 16/3/8 008.
 */
public class Announcement {

    public ArrayList<AnnouncementBean> getAnnouncement() {
        return announcement;
    }

    private static ArrayList<AnnouncementBean> announcement;

    public Announcement(){
        getJSONVolley();
    }

    private void getJSONVolley(){
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.getInstance());
        String JSONUrl="http://172.16.112.131/CourseInformationSystem-Server/getJSON.html";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, JSONUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dealData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley","error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void dealData(JSONArray response) {
        Gson gson=new Gson();
        Type listType=new TypeToken<ArrayList<AnnouncementBean>>(){}.getType();
        announcement =gson.fromJson(response.toString(),listType);
    }

}

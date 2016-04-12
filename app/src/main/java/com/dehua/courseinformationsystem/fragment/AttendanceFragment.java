package com.dehua.courseinformationsystem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.CourseBean;
import com.dehua.courseinformationsystem.constants.ServerAdderss;
import com.dehua.courseinformationsystem.mainactivity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG="AnnouncementFragment";

    private OnFragmentInteractionListener mListener;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_attendance, container, false);
        Button button= (Button) view.findViewById(R.id.attendance_button);
        ProgressBar progressBar= (ProgressBar) view.findViewById(R.id.attendance_progress);
        TextView textView= (TextView) view.findViewById(R.id.attendance_textView);
        textView.setText("Tips:打开WIFI并连接\n\""+SSID+"\"\n后开始签到");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSONVolley();
            }
        });
        return view;
    }
    final String mac="7C-E9-D3-00-9C-26";
    final String SSID="CourseIS";

    private void getJSONVolley() {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
        SharedPreferences sharedPreferences=MainActivity.getInstance().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String JSONUrl = ServerAdderss.getServerAddress()+"GetJSON?bean=signIn&id="+sharedPreferences.getString("UserID","");
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, JSONUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<CourseBean>(){}.getType();
                        CourseBean course=gson.fromJson(response.toString(), type);
                        Log.i(TAG,course.getCourseName());
                        WifiManager wifiManager = (WifiManager) MainActivity.getInstance().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        String macInfo = wifiInfo.getBSSID();
                        String SSIDINFO = wifiInfo.getSSID();
                        Log.i(TAG, macInfo + " "+SSIDINFO+"");
                        if (macInfo != null && SSIDINFO != null && macInfo.equals(mac) && SSIDINFO.equals("\""+SSID+"\"")) {
                            signIn(course.getSignInCount(),course.getCourseID());
                        } else {
                            Toast.makeText(MainActivity.getInstance().getApplicationContext(), "请打开wifi", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.getInstance().getApplicationContext(), "签到未开始", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonRequest);
    }

    private void signIn(final int count, final int courseID){
        Toast.makeText(MainActivity.getInstance().getApplicationContext(), "开始签到", Toast.LENGTH_SHORT).show();
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
        SharedPreferences sharedPreferences=MainActivity.getInstance().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        final String User_ID=sharedPreferences.getString("UserID","");
        String Url = ServerAdderss.getServerAddress()+"SigninServlet";
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            Toast.makeText(MainActivity.getInstance().getApplicationContext(), "签到成功", Toast.LENGTH_SHORT).show();
                        }else if (response.equals("2")){
                            Toast.makeText(MainActivity.getInstance().getApplicationContext(), "请勿重复签到", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.getInstance().getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.getInstance().getApplicationContext(), "与服务器连接失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("courseID", courseID+"");
                map.put("count", count+"");
                map.put("stuID", User_ID);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

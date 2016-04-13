package com.dehua.courseinformationsystem.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.ScheduleBean;
import com.dehua.courseinformationsystem.constants.ServerAdderss;
import com.dehua.courseinformationsystem.mainactivity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG="ScheduleFragment";

    private int itemHeight;
    private int marTop;
    private int marLeft;
    private LinearLayout weekPanels[]=new LinearLayout[7];
    private ArrayList scheduleData[]=new ArrayList[7];
    private static ArrayList<ScheduleBean> list=new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    private View view=null;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
        for(int i=0;i<7;i++){
            scheduleData[i]=new ArrayList<ScheduleBean>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_schedule, container, false);
        itemHeight=getResources().getDimensionPixelSize(R.dimen.weekItemHeight);
        marTop=getResources().getDimensionPixelSize(R.dimen.weekItemMarTop);
        marLeft=getResources().getDimensionPixelSize(R.dimen.weekItemMarLeft);
        getJSONVolley(view);
        return view;
    }

    public static ArrayList<ScheduleBean> getList() {
        return list;
    }

    protected void getJSONVolley(final View view) {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
        SharedPreferences sharedPreferences=MainActivity.getInstance().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String JSONUrl = ServerAdderss.getServerAddress()+"GetJSON?bean=schedule&id="+sharedPreferences.getString("UserID","");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSONUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<ScheduleBean>>() {
                        }.getType();
                        list = gson.fromJson(response.toString(), listType);
                        for(ScheduleBean scheduleBean:list){
                            scheduleData[scheduleBean.getDay()].add(scheduleBean);
                        }
                        for (int i = 0; i < weekPanels.length; i++) {
                            weekPanels[i]=(LinearLayout) view.findViewById(R.id.weekPanel_1 + i);
                            initWeekPanel(weekPanels[i], scheduleData[i]);
                        }
                        Log.i(TAG,scheduleData[1].toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "volley error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void initWeekPanel(LinearLayout linearLayout,List<ScheduleBean>scheduleBeans){
        if(linearLayout==null || scheduleBeans==null || scheduleBeans.size()<1)return;
        Log.i("Msg", "初始化面板");
        ScheduleBean pre=scheduleBeans.get(0);
        for (int i = 0; i < scheduleBeans.size(); i++) {
            ScheduleBean c =scheduleBeans.get(i);
            TextView tv =new TextView(MainActivity.getInstance().getApplicationContext());
            LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT ,
                    itemHeight*c.getAmount()+marTop*(c.getAmount()-1));
            if(i>0){
                lp.setMargins(marLeft, (c.getTime()-(pre.getTime()+pre.getAmount()))*(itemHeight+marTop)+marTop, 0, 0);
            }else{
                lp.setMargins(marLeft, (c.getTime()-1)*(itemHeight+marTop)+marTop, 0, 0);
            }
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.TOP);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(12);
            tv.setSingleLine(false);
            if(isAdded()) {
                tv.setTextColor(getResources().getColor(R.color.courseTextColor));
            }
            tv.setText(c.getCourseName() + "\n" + c.getClassroom());
                    tv.setBackgroundColor(Color.parseColor("#3F51B5"));
            linearLayout.addView(tv);
            pre=c;
        }
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

package com.dehua.courseinformationsystem.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.AnnouncementBean;
import com.dehua.courseinformationsystem.bean.Course2StuBean;
import com.dehua.courseinformationsystem.constants.ServerAdderss;
import com.dehua.courseinformationsystem.mainactivity.CourseDetail;
import com.dehua.courseinformationsystem.mainactivity.MainActivity;
import com.dehua.courseinformationsystem.utils.HomepageAnnouncementAdapter;
import com.dehua.courseinformationsystem.utils.HomepageCourseAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HomePageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG="HomepageFragment";

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView mRecyclerView;
    private RecyclerView mTitleRecyclerView;
    private RecyclerView.LayoutManager mTitleLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_page, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.homepage_recycler_view);
        mTitleRecyclerView= (RecyclerView) view.findViewById(R.id.homepage_recycler_title_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mTitleLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTitleRecyclerView.setLayoutManager(mTitleLayoutManager);
        getJSONVolley();
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.homepage_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
            }
        });
        return view;
    }

    private void RefreshData() {
        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                getJSONVolley();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                swipeRefreshLayout.setRefreshing(false);
                super.onPostExecute(s);
            }
        }.execute();
    }

    AnnouncementBean announcement=new AnnouncementBean();
    ArrayList<Course2StuBean> courselist=new ArrayList<>();

    private void getJSONVolley() {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
        SharedPreferences sharedPreferences=MainActivity.getInstance().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String JSONUrl1 = ServerAdderss.getServerAddress()+"GetJSON?bean=latestannouncement&id="+sharedPreferences.getString("UserID","");
        final String JSONUrl2 = ServerAdderss.getServerAddress()+"GetJSON?bean=course&id="+sharedPreferences.getString("UserID","");
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSONUrl1, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<AnnouncementBean>>() {
                        }.getType();
                        ArrayList<AnnouncementBean> list = gson.fromJson(response.toString(), listType);
                        announcement=list.get(0);
                        mTitleRecyclerView.setAdapter(new HomepageAnnouncementAdapter(announcement));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "volley error");
                    }
                }
        );
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, JSONUrl2, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Course2StuBean>>() {
                        }.getType();
                        courselist = gson.fromJson(response.toString(), listType);
                        HomepageCourseAdapter adapter=new HomepageCourseAdapter(courselist);
                        mRecyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new HomepageCourseAdapter.OnRecyclerViewItemClickListener(){
                            @Override
                            public void onItemClick(View view , String data){
                                startActivity(new Intent(MainActivity.getInstance(), CourseDetail.class));
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "volley error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest1);
        requestQueue.add(jsonArrayRequest2);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

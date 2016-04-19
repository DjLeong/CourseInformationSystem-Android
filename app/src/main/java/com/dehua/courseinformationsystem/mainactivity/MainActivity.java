package com.dehua.courseinformationsystem.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.Service.NotificationService;
import com.dehua.courseinformationsystem.bean.CourseBean;
import com.dehua.courseinformationsystem.constants.FragmentPosition;
import com.dehua.courseinformationsystem.constants.ServerAdderss;
import com.dehua.courseinformationsystem.fragment.AnnouncementFragment;
import com.dehua.courseinformationsystem.fragment.AttendanceFragment;
import com.dehua.courseinformationsystem.fragment.HomePageFragment;
import com.dehua.courseinformationsystem.fragment.ScheduleFragment;
import com.dehua.courseinformationsystem.settingfragment.SettingsActivity;
import com.dehua.courseinformationsystem.utils.FragmentController;
import com.dehua.courseinformationsystem.utils.PollingUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AnnouncementFragment.OnFragmentInteractionListener, AttendanceFragment.OnFragmentInteractionListener,
        ScheduleFragment.OnFragmentInteractionListener,HomePageFragment.OnFragmentInteractionListener {

    private static final String TAG="MainActivity";

    private static MainActivity instance;

    private static FragmentController controller;

    private SharedPreferences sharedPreferences;

    private static String User_ID=null;
    private static boolean Login_State=false;

    //jPUSH test
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        TextView user_name = (TextView) headView.findViewById(R.id.user_name);
        User_ID = sharedPreferences.getString("UserID", null);
        if (User_ID != null) {
            user_name.setText(User_ID);
        }
        ImageView imageView= (ImageView) headView.findViewById(R.id.user_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UserActivity.class));
            }
        });

       Login_State = sharedPreferences.getBoolean("LoginState", false);

        if (Login_State) {
            Log.i(TAG, "Login");
            controller = FragmentController.getInstance(this, R.id.content);
            controller.showFragment(FragmentPosition.HomePage.ordinal());
            initJpush();
        } else {
            Log.i(TAG, "Not Login");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        PollingUtils.stopPollingService(this, NotificationService.class, NotificationService.ACTION);
        PollingUtils.startPollingService(this, 5, NotificationService.class, NotificationService.ACTION);
    }

    public static void initJpush(){
        new AsyncTask<String, Float, String>() {
            @Override
            protected String doInBackground(String... strings) {
                //test Jpush SDK
                JPushInterface.setDebugMode(true);
                JPushInterface.init(MainActivity.getInstance());
                getScheduleJSONVolley();
                return null;
            }
        }.execute();
    }

    private  static ArrayList<CourseBean> list=new ArrayList<CourseBean>();

    protected static void getScheduleJSONVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
        final SharedPreferences sharedPreferences=MainActivity.getInstance().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        String JSONUrl = ServerAdderss.getServerAddress()+"GetJSON?bean=course&id="+sharedPreferences.getString("UserID","");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSONUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<CourseBean>>() {
                        }.getType();
                        list = gson.fromJson(response.toString(), listType);
                        Set<String> tags=new HashSet<String>();
                        for(CourseBean courseBean:list){
                            tags.add(courseBean.getCourseID()+"");
                        }
                        JPushInterface.setTags(MainActivity.getInstance(),tags,new TagAliasCallback() {
                            @Override
                            public void gotResult(int responseCode, String alias, Set<String> tags) {
                                // TODO
                                if(responseCode==0){
                                    Log.i("tags", tags.toString());
                                }
                            }
                        });
                        JPushInterface.setAlias(MainActivity.getInstance(), sharedPreferences.getString("UserID", ""), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                if(i==0){
                                    Log.i("Alias", s);
                                }
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
        requestQueue.add(jsonArrayRequest);
    }

    public static MainActivity getInstance() {
        return instance;
    }
    public static void setController(FragmentController controller) {
        MainActivity.controller = controller;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentController.clearConrtoller();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        ActionBar toolbar = getSupportActionBar();
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_announcement:
                controller.showFragment(FragmentPosition.Announcement.ordinal());
                if (toolbar != null) {
                    toolbar.setTitle("公告栏");
                }
                break;
            case R.id.nav_homepage:
                controller.showFragment(FragmentPosition.HomePage.ordinal());
                if (toolbar != null) {
                    toolbar.setTitle("课程信息系统");
                }
                break;
            case R.id.nav_attendance:
                controller.showFragment(FragmentPosition.Attendance.ordinal());
                if (toolbar != null) {
                    toolbar.setTitle("签到");
                }
                break;
            case R.id.nav_schedule:
                controller.showFragment(FragmentPosition.Download.ordinal());
                if (toolbar != null) {
                    toolbar.setTitle("课程表");
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

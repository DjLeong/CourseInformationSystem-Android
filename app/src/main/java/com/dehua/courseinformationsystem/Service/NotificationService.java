package com.dehua.courseinformationsystem.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    public static final String ACTION ="com.dehua.courseinformationsystem.Service.NotificationService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Notification", "===========create=======");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(){
        NotificationManager mn= (NotificationManager) NotificationService.this.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(NotificationService.this);
        Intent notificationIntent=new Intent(Intent.ACTION_MAIN);
        notificationIntent.setClass(this, MainActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this,0,notificationIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_info_black_24dp);
        builder.setTicker("title"); //测试通知栏标题
        builder.setContentText("您明天有课程，打开APP查看"); //下拉通知啦内容
        builder.setContentTitle("课程提醒");//下拉通知栏标题
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setFullScreenIntent(contentIntent, true);
        Notification notification = builder.build();
        mn.notify((int)System.currentTimeMillis(),notification);
    }

    private ArrayList<ScheduleBean> list=new ArrayList<>();

    protected void getJSONVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
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
                            if(scheduleBean.getDay()==getDay()){
                                showNotification();
                                break;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Notification", "volley error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public int getDay(){
        Date date =new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int day=cal.get(Calendar.DAY_OF_WEEK);
        if(day-2>=0) {
            return day-2;
        }else{
            return 6;
        }
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        getJSONVolley();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Notification", "===========destroy=======");
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

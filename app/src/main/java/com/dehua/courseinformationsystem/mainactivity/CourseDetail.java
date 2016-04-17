package com.dehua.courseinformationsystem.mainactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.CourseBean;
import com.dehua.courseinformationsystem.bean.ScheduleBean;
import com.dehua.courseinformationsystem.constants.ServerAdderss;
import com.dehua.courseinformationsystem.fragment.ScheduleFragment;
import com.dehua.courseinformationsystem.utils.CharConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent = this.getIntent();
        final CourseBean courseBean = (CourseBean) intent.getSerializableExtra("course");

        Toolbar toolbar = (Toolbar) findViewById(R.id.course_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(courseBean.getCourseName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView course_detail_id = (TextView) findViewById(R.id.course_detail_id);
        TextView course_detail_schedule = (TextView) findViewById(R.id.course_detail_schedule);
        TextView course_detail_intro = (TextView) findViewById(R.id.course_detail_intro);
        String scheduletext = "";
        ArrayList<ScheduleBean> scheduleBeans = ScheduleFragment.getList();
        for (ScheduleBean scheduleBean : scheduleBeans) {
            if (scheduleBean.getCourseid() == courseBean.getCourseID()) {
                scheduletext = scheduletext + CharConvert.ConvertDay(scheduleBean.getDay() + "") + "第" + scheduleBean.getTime()
                        + "节  " + scheduleBean.getClassroom() + "\n";
            }
        }
        String courseid = "课程编号：" + courseBean.getCourseID() + "\n";
        String courseIntro = "" + courseBean.getCourseIntro();
        course_detail_id.setText(courseid);
        course_detail_schedule.setText(scheduletext);
        course_detail_intro.setText(courseIntro);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetail.this);
                    // Get the layout inflater
                    LayoutInflater inflater = CourseDetail.this.getLayoutInflater();

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    View dialogView=inflater.inflate(R.layout.msg_dialog, null);
                    final TextView Msg_Dialog_Text= (TextView) dialogView.findViewById(R.id.Msg_Dialog_Text);
                    builder.setView(dialogView)
                            // Add action buttons
                            .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    leaveMsg(courseBean.getCourseID()+"",Msg_Dialog_Text.getText().toString());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setTitle("留言");
                    builder.create().show();
                }
            });
        }
    }

    protected void leaveMsg(final String courseID,final String msg) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
        final String User_ID = sharedPreferences.getString("UserID", "");
        String Url = ServerAdderss.getServerAddress() + "AddMsgServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("1")){
                            Toast.makeText(CourseDetail.this,"留言成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(CourseDetail.this,"服务器未知错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CourseDetail.this,"与服务器连接失败",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("user_id", User_ID);
                map.put("msg",msg);
                map.put("courseID",courseID);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

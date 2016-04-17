package com.dehua.courseinformationsystem.mainactivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.CourseBean;
import com.dehua.courseinformationsystem.bean.ScheduleBean;
import com.dehua.courseinformationsystem.fragment.ScheduleFragment;
import com.dehua.courseinformationsystem.utils.CharConvert;

import java.util.ArrayList;

public class CourseDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Intent intent=this.getIntent();
        CourseBean courseBean= (CourseBean) intent.getSerializableExtra("course");

        Toolbar toolbar = (Toolbar) findViewById(R.id.course_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(courseBean.getCourseName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView course_detail_id= (TextView) findViewById(R.id.course_detail_id);
        TextView course_detail_schedule= (TextView) findViewById(R.id.course_detail_schedule);
        TextView course_detail_intro= (TextView) findViewById(R.id.course_detail_intro);
        String scheduletext="";
        ArrayList<ScheduleBean> scheduleBeans= ScheduleFragment.getList();
        for(ScheduleBean scheduleBean:scheduleBeans){
            if(scheduleBean.getCourseid()==courseBean.getCourseID()){
                scheduletext=scheduletext + CharConvert.ConvertDay(scheduleBean.getDay()+"")+"第"+scheduleBean.getTime()
                        +"节  "+scheduleBean.getClassroom()+"\n";
            }
        }
        String courseid="课程编号："+courseBean.getCourseID()+"\n";
        String courseIntro=""+courseBean.getCourseIntro();
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
                    builder.setView(inflater.inflate(R.layout.msg_dialog, null))
                            // Add action buttons
                            .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    // sign in the user ...
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

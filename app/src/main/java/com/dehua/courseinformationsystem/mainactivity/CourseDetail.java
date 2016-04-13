package com.dehua.courseinformationsystem.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

package com.dehua.courseinformationsystem.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.AnnouncementBean;

public class AnnouncementDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        AnnouncementBean announcementBean= (AnnouncementBean) intent.getSerializableExtra("announcement");

        getSupportActionBar().setTitle(announcementBean.getTitle());

        TextView courseText= (TextView) findViewById(R.id.announcement_detail_course);
        courseText.setText("课程："+announcementBean.getCourseName());

        TextView timeText= (TextView) findViewById(R.id.announcement_detail_time);
        timeText.setText("发布时间："+announcementBean.getTime());

        TextView contentText= (TextView) findViewById(R.id.announcement_detail_content);
        contentText.setText(announcementBean.getContent());

        final TextView attachText= (TextView) findViewById(R.id.announcement_detail_attach);
        if(!announcementBean.getDownload().equals("")){
            attachText.setText(Html.fromHtml("<u>"+announcementBean.getDownload()+"</u>"));
            //            attachText.setAutoLinkMask(Linkify.ALL);
            attachText.setMovementMethod(LinkMovementMethod.getInstance());
            LinearLayout linearLayout= (LinearLayout) findViewById(R.id.announcement_detail_attach_layout);
            ViewGroup.LayoutParams p=linearLayout.getLayoutParams();
            p.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            linearLayout.setLayoutParams(p);
            attachText.setOnClickListener(new View.OnClickListener() {
                boolean flag=true;
                @Override
                public void onClick(View view) {
                    if(flag) {
                        attachText.setSingleLine(false);
                        flag=false;
                    }else {
                        attachText.setSingleLine(true);
                        flag=true;
                    }
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

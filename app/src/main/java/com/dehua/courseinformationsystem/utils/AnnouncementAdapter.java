package com.dehua.courseinformationsystem.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.data.Announcement;
import com.dehua.courseinformationsystem.data.AnnouncementBean;

import java.util.ArrayList;

/**
 * Created by dehua on 16/3/7 007.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter {

    class AnnouncementItemHolder extends RecyclerView.ViewHolder {

        View view;
        TextView course,title,content;

        public AnnouncementItemHolder(View view) {
            super(view);
            course=(TextView) view.findViewById(R.id.announcement_item_course);
            title= (TextView) view.findViewById(R.id.announcement_item_title);
            content= (TextView) view.findViewById(R.id.announcement_item_content);
        }

        public TextView getCourse() {
            return course;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnnouncementItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AnnouncementItemHolder vh = (AnnouncementItemHolder) holder;
        AnnouncementBean announcement=announcementList.get(position);
        vh.getCourse().setText(announcement.getCourse());
        vh.getTitle().setText(announcement.getTitle());
        vh.getContent().setText(announcement.getContent());
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    private ArrayList<AnnouncementBean> announcementList=new Announcement().getAnnouncement();
}

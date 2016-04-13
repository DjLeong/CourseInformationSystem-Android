package com.dehua.courseinformationsystem.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.AnnouncementBean;

import java.util.ArrayList;

/**
 * Created by dehua on 16/3/7 007.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private ArrayList<AnnouncementBean> announcementList;
    private int arraySize;

    public AnnouncementAdapter(ArrayList<AnnouncementBean> list) {
        announcementList=list;
        arraySize=announcementList.size();
    }

    class AnnouncementItemHolder extends RecyclerView.ViewHolder {

        TextView course, title, content,time;

        public AnnouncementItemHolder(View view) {
            super(view);
            course = (TextView) view.findViewById(R.id.announcement_item_course);
            title = (TextView) view.findViewById(R.id.announcement_item_title);
            content = (TextView) view.findViewById(R.id.announcement_item_content);
            time= (TextView) view.findViewById(R.id.announcement_item_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item_list, parent, false);
        view.setOnClickListener(this);
        return new AnnouncementItemHolder(view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, AnnouncementBean announcementBean);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (AnnouncementBean) view.getTag());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AnnouncementItemHolder vh = (AnnouncementItemHolder) holder;
        AnnouncementBean announcement = announcementList.get(position);
        vh.course.setText(announcement.getCourseName());
        vh.title.setText(announcement.getTitle());
        vh.content.setText(announcement.getContent());
        vh.time.setText(announcement.getTime());
        holder.itemView.setTag(announcement);
    }

    @Override
    public int getItemCount() {
        return arraySize;
    }
}

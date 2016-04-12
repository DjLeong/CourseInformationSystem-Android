package com.dehua.courseinformationsystem.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.AnnouncementBean;

/**
 * Created by dehua on 16/4/12 012.
 */
public class HomepageAnnouncementAdapter extends RecyclerView.Adapter {

    private AnnouncementBean Announcement;

    public HomepageAnnouncementAdapter(AnnouncementBean announcementBean){
        Announcement=announcementBean;
    }

    public class GroupItemHolder extends RecyclerView.ViewHolder {
        TextView groupText;
        TextView groupTitle;
        TextView groupContent;

        public GroupItemHolder(View itemView) {
            super(itemView);
            groupText = (TextView) itemView.findViewById(R.id.homepage_group_item_text);
            groupTitle= (TextView) itemView.findViewById(R.id.homepage_group_item_title);
            groupContent= (TextView) itemView.findViewById(R.id.homepage_group_item_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupItemHolder groupItemHolder= (GroupItemHolder) holder;
        AnnouncementBean announcementBean=Announcement;
        groupItemHolder.groupText.setText("最新公告");
        groupItemHolder.groupTitle.setText(announcementBean.getCourseName());
        groupItemHolder.groupContent.setText(announcementBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

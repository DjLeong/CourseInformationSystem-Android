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
public class HomepageAnnouncementAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private static final int NORMAL_ITEM = 1;
    private static final int NORMAL_ITEM_TITLE= 0;
    private AnnouncementBean Announcement;

    public HomepageAnnouncementAdapter(AnnouncementBean announcementBean){
        Announcement=announcementBean;
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


    public class GroupItemHolder extends RecyclerView.ViewHolder {
        TextView groupTitle;
        TextView groupContent;
        TextView groupTime;

        public GroupItemHolder(View itemView) {
            super(itemView);
            groupTitle= (TextView) itemView.findViewById(R.id.homepage_group_item_title);
            groupContent= (TextView) itemView.findViewById(R.id.homepage_group_item_content);
            groupTime= (TextView) itemView.findViewById(R.id.homepage_group_item_time);
        }
    }
    public class GroupTitleHolder extends RecyclerView.ViewHolder{
        TextView Title;
        public GroupTitleHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.homepage_group_item_text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_group_item, parent, false);
            view.setOnClickListener(this);
            return new GroupItemHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_group_title, parent, false);
            return new GroupTitleHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof GroupItemHolder) {
            GroupItemHolder groupItemHolder = (GroupItemHolder) holder;
            AnnouncementBean announcementBean = Announcement;
            groupItemHolder.groupTitle.setText(announcementBean.getCourseName());
            groupItemHolder.groupContent.setText(announcementBean.getTitle());
            groupItemHolder.groupTime.setText(announcementBean.getTime());
            holder.itemView.setTag(announcementBean);
        }else{
            GroupTitleHolder groupTitleHolder= (GroupTitleHolder) holder;
            if(position==0) {
                groupTitleHolder.Title.setText("最新公告");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return NORMAL_ITEM_TITLE;
        }else
            return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

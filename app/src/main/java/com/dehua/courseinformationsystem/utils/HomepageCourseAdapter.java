package com.dehua.courseinformationsystem.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.bean.AnnouncementBean;
import com.dehua.courseinformationsystem.bean.Course2StuBean;
import com.dehua.courseinformationsystem.bean.CourseBean;

import java.util.ArrayList;

/**
 * Created by dehua on 16/4/11 011.
 */
public class HomepageCourseAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private static final int NORMAL_ITEM = 1;
    private static final int NORMAL_ITEM_TITLE= 0;

    private ArrayList<CourseBean> DataList;
    private int size;

    public HomepageCourseAdapter(ArrayList<CourseBean> courseList){
        DataList=courseList;
        size=DataList.size()+2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == NORMAL_ITEM) {
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepage_item_list, viewGroup, false);
            view.setOnClickListener(this);
            return new NormalItemHolder(view);
        } else{
            View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homepage_item_title, viewGroup, false);
            return new NormalItemTitleHolder(view);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , CourseBean courseBean);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view,(CourseBean) view.getTag());
        }
    }

    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView Title;

        public NormalItemHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.homepage_item_title);
        }
    }

    public class NormalItemTitleHolder extends RecyclerView.ViewHolder{
        TextView Title;

        public NormalItemTitleHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.homepage_item_title_text);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalItemHolder){
            bindNormalItem(holder,position);
        }else{
            bindNormalItemTitle(holder,position);
        }
    }

    private void bindNormalItemTitle(RecyclerView.ViewHolder holder, int position) {
        NormalItemTitleHolder titleHolder= (NormalItemTitleHolder) holder;
        if(position==0) {
            titleHolder.Title.setText("课程");
        }else{
            titleHolder.Title.setText(" ");
        }
    }

    private void bindNormalItem(RecyclerView.ViewHolder holder, int position) {
        NormalItemHolder normalItemHolder= (NormalItemHolder) holder;
        CourseBean coursebean=DataList.get(position-2);
        normalItemHolder.Title.setText(coursebean.getCourseName());
        holder.itemView.setTag(coursebean);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0||position==1) {
            return NORMAL_ITEM_TITLE;
        }else
            return NORMAL_ITEM;
    }

    @Override
    public int getItemCount() {
        return size;
    }
}

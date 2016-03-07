package com.dehua.courseinformationsystem.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dehua.courseinformationsystem.R;
import com.dehua.courseinformationsystem.fragment.AnnouncementFragment;

/**
 * Created by dehua on 16/3/7 007.
 */
public class AnnouncementAdapter extends RecyclerView.Adapter {

    class AnnouncementItemHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title,content;

        public AnnouncementItemHolder(View view) {
            super(view);
            title= (TextView) view.findViewById(R.id.announcement_item_title);
            content= (TextView) view.findViewById(R.id.announcement_item_content);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }
    }

/*           class AnnouncementGroupHolder extends AnnouncementItemHolder{
            TextView time;

            public AnnouncementGroupHolder(View itemView) {
                super(itemView);
                time= (TextView) itemView.findViewById(R.id.announcement_group_item_time);
            }
        }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnnouncementItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item_list,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AnnouncementItemHolder vh = (AnnouncementItemHolder) holder;
        vh.getTitle().setText("Title "+ position);
        vh.getContent().setText("Content "+position);

    }

    @Override
    public int getItemCount() {
        return 30;
    }
}

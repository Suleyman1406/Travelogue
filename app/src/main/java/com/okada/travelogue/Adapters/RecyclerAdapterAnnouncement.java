package com.okada.travelogue.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.okada.travelogue.HelperClasses.AnnouncementClass;
import com.okada.travelogue.R;

import java.util.Map;

public class RecyclerAdapterAnnouncement extends RecyclerView.Adapter<RecyclerAdapterAnnouncement.AnnouncemenetHolder> {
    private Context context;
    private Map<Long, AnnouncementClass> announcementClassMap;

    public RecyclerAdapterAnnouncement(Map<Long, AnnouncementClass> announcementClassMap, Context context) {
        this.announcementClassMap = announcementClassMap;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncemenetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_announcement_item, parent, false);
        return new AnnouncemenetHolder(view);
    }

    private int num = 0;

    @Override
    public void onBindViewHolder(@NonNull AnnouncemenetHolder holder, int position) {
        if (announcementClassMap.size() > 0) {
            if (num == 4) {
                num = 1;
            } else num -= -1;
            switch (num) {
                case 1:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel1card);
                    break;
                case 2:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel2card);
                    break;
                case 3:
                    holder.linearLayout.setBackgroundResource(R.drawable.travel3card);
                    break;
            }
            holder.textView.setText(announcementClassMap.get(Long.parseLong(position+"")).getDesc());
        }
    }


    @Override
    public int getItemCount() {
        return announcementClassMap.size();
    }

    class AnnouncemenetHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView textView;

        public AnnouncemenetHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.announcement_linear_layout);
            textView = itemView.findViewById(R.id.announcement_desc_text);
        }
    }
}

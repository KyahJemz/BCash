package com.sscr.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewNotificationsStaticAdapter extends RecyclerView.Adapter <RecyclerViewNotificationsStaticAdapter.ViewHolder> {
    private ArrayList<ModelNotifications> staticContentArrayList;
    private Context context;
    public RecyclerViewNotificationsStaticAdapter(ArrayList<ModelNotifications> staticContentArrayList, Context context) {
        this.staticContentArrayList = staticContentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewNotificationsStaticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_notifications_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewNotificationsStaticAdapter.ViewHolder holder, int position) {
        ModelNotifications model = staticContentArrayList.get(position);
        holder.tv_Title.setText(model.getTitle());
        holder.tv_Content.setText(model.getContents());
        holder.tv_Date.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return staticContentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Title, tv_Content, tv_Date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tv_Title);
            tv_Content = itemView.findViewById(R.id.tv_Content);
            tv_Date = itemView.findViewById(R.id.tv_Date);
        }
    }
}

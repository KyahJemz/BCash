package com.sscr.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivityLogsStaticAdapter extends RecyclerView.Adapter <RecyclerViewActivityLogsStaticAdapter.ViewHolder> {
    private ArrayList<ModelActivityLogs> activityLogsArrayList;
    private Context context;
    public RecyclerViewActivityLogsStaticAdapter(ArrayList<ModelActivityLogs> staticContentArrayList, Context context) {
        this.activityLogsArrayList = staticContentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewActivityLogsStaticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_activitylogs_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewActivityLogsStaticAdapter.ViewHolder holder, int position) {
        ModelActivityLogs model = activityLogsArrayList.get(position);
        holder.tv_Date.setText(model.getTimestamp());
        holder.tv_Name.setText(model.getAccountAddress());
        holder.tv_Task.setText(model.getTask());
    }

    @Override
    public int getItemCount() {
        return activityLogsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Date, tv_Name, tv_Task;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Date = itemView.findViewById(R.id.tv_Date);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Task = itemView.findViewById(R.id.tv_Task);
        }
    }
}

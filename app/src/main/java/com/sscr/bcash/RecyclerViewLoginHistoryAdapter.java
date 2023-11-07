package com.sscr.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewLoginHistoryAdapter extends RecyclerView.Adapter <RecyclerViewLoginHistoryAdapter.ViewHolder> {
    private ArrayList<ModelLoginHistory> loginHistoryArrayList;
    private Context context;
    private InterfaceLoginHistory interfaceLoginHistory;
    public RecyclerViewLoginHistoryAdapter(ArrayList<ModelLoginHistory> loginHistoryArrayList, Context context, InterfaceLoginHistory interfaceLoginHistory) {
        this.loginHistoryArrayList = loginHistoryArrayList;
        this.context = context;
        this.interfaceLoginHistory = interfaceLoginHistory;
    }

    @NonNull
    @Override
    public RecyclerViewLoginHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_loginhistory_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewLoginHistoryAdapter.ViewHolder holder, int position) {
        ModelLoginHistory model = loginHistoryArrayList.get(position);
        holder.tv_IpAddress.setText(model.getIpAddress());
        holder.tv_Device.setText(model.getDevice());
        holder.tv_Location.setText(model.getLocation());
        holder.tv_LastOnline.setText(model.getLastOnline());

        holder.tv_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceLoginHistory.onLoginHistoryRemove(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return loginHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_IpAddress, tv_Device, tv_Location, tv_LastOnline, tv_Remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_IpAddress = itemView.findViewById(R.id.tv_IpAddress);
            tv_Device = itemView.findViewById(R.id.tv_Device);
            tv_Location = itemView.findViewById(R.id.tv_Location);
            tv_LastOnline = itemView.findViewById(R.id.tv_LastOnline);
            tv_Remove = itemView.findViewById(R.id.tv_Remove);
        }
    }
}

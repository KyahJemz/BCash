package com.sscr.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewWhitelistAdapter extends RecyclerView.Adapter <RecyclerViewWhitelistAdapter.ViewHolder> {
    private ArrayList<ModelWhitelist> whitelistArrayList;
    private Context context;
    private InterfaceWhitelist interfaceWhitelist;
    public RecyclerViewWhitelistAdapter(ArrayList<ModelWhitelist> whitelistArrayList, Context context, InterfaceWhitelist interfaceWhitelist) {
        this.whitelistArrayList = whitelistArrayList;
        this.context = context;
        this.interfaceWhitelist = interfaceWhitelist;
    }

    @NonNull
    @Override
    public RecyclerViewWhitelistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_whitelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWhitelistAdapter.ViewHolder holder, int position) {
        ModelWhitelist model = whitelistArrayList.get(position);
        holder.tv_Date.setText(model.getTimestamp());
        holder.tv_Name.setText(model.getWhitelistedName());

        holder.tv_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceWhitelist.onWhitelistRemove(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return whitelistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Name, tv_Date, tv_Remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Date = itemView.findViewById(R.id.tv_Date);
            tv_Remove = itemView.findViewById(R.id.tv_Remove);
        }
    }
}

package com.sscr.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewItemsStaticAdapter extends RecyclerView.Adapter <RecyclerViewItemsStaticAdapter.ViewHolder> {
    private ArrayList<ModelItems> itemsArrayList;
    private Context context;
    public RecyclerViewItemsStaticAdapter(ArrayList<ModelItems> itemsArrayList, Context context) {
        this.itemsArrayList = itemsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewItemsStaticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemsStaticAdapter.ViewHolder holder, int position) {
        ModelItems model = itemsArrayList.get(position);
        double itemPrice = Double.parseDouble(model.getItemPrice());
        int itemQuantity = Integer.parseInt(model.getItemQuantity());
        double totalItemPrice = itemPrice * itemQuantity;
        holder.tv_ItemName.setText(model.getItemName());
        holder.tv_ItemPrice.setText(Helpers.getBalance(String.valueOf(totalItemPrice)));
        holder.tv_ItemQuantity.setText(String.format("x%s", model.getItemQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ItemName, tv_ItemPrice, tv_ItemQuantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ItemName = itemView.findViewById(R.id.tv_ItemName);
            tv_ItemPrice = itemView.findViewById(R.id.tv_ItemPrice);
            tv_ItemQuantity = itemView.findViewById(R.id.tv_ItemQuantity);
        }
    }
}

package com.example.bcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class transaction_adapter extends RecyclerView.Adapter<transaction_view_holder> {
    private final transaction_interface transactionInterface;

    Context contex;
    List<records> recordsList = new ArrayList<>();;

    public transaction_adapter(Context contex, List<records> recordsList, transaction_interface transactionInterface) {
        this.contex = contex;
        this.recordsList = recordsList;
        this.transactionInterface = transactionInterface;
    }

    @NonNull
    @Override
    public transaction_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new transaction_view_holder(LayoutInflater.from(contex).inflate(R.layout.transaction_history_row,parent,false), transactionInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull transaction_view_holder holder, int position) {
        holder.tv_transactionType.setText(recordsList.get(position).getTransactionType());
        holder.tv_transactionDate.setText(recordsList.get(position).getTransactionDate());
        holder.tv_transactionAmount.setText(recordsList.get(position).getTransactionAmount());
        holder.cl_items.setTag(recordsList.get(position).getTransactionId());
        holder.iv_moreinfo.setImageResource(R.drawable.info);
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }
}

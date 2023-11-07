package com.sscr.bcash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewTransactionStaticAdapter extends RecyclerView.Adapter <RecyclerViewTransactionStaticAdapter.ViewHolder> {
    private ArrayList<TransactionModel> transactionModelArrayList;
    private Context context;
    public RecyclerViewTransactionStaticAdapter(ArrayList<TransactionModel> transactionModelArrayList, Context context) {
        this.transactionModelArrayList = transactionModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewTransactionStaticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_home_transactions_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTransactionStaticAdapter.ViewHolder holder, int position) {
        TransactionModel model = transactionModelArrayList.get(position);
        holder.tv_TransactionAmount.setText(model.getTransactionAmount());
        holder.tv_TransactionDate.setText(model.getTransactionDate());
        holder.tv_TransactionType.setText(model.getTransactionType());

        holder.ll_TransactionRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReceiptActivity.class);
                intent.putExtra("TransactionAddress",model.getTransactionAddress());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_TransactionDate, tv_TransactionType, tv_TransactionAmount;
        LinearLayout ll_TransactionRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_TransactionType = itemView.findViewById(R.id.tv_TransactionType);
            tv_TransactionAmount = itemView.findViewById(R.id.tv_TransactionAmount);
            tv_TransactionDate = itemView.findViewById(R.id.tv_TransactionDate);
            ll_TransactionRow = itemView.findViewById(R.id.ll_TransactionRow);
        }
    }
}

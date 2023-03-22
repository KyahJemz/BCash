package com.example.bcash;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class transaction_view_holder extends RecyclerView.ViewHolder {

    TextView tv_transactionType, tv_transactionDate, tv_transactionAmount;
    ConstraintLayout cl_items;
    ImageView iv_moreinfo;

    public transaction_view_holder(@NonNull View itemView, transaction_interface transactionInterface) {
        super(itemView);
        tv_transactionType = itemView.findViewById(R.id.tv_TransactionType);
        tv_transactionDate = itemView.findViewById(R.id.tv_TransactionDate);
        tv_transactionAmount = itemView.findViewById(R.id.tv_TransactionAmount);
        cl_items = itemView.findViewById(R.id.items);
        iv_moreinfo = itemView.findViewById(R.id.iv_MoreInfo);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transactionInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        transactionInterface.onItemClick(position, view);
                    }
                }
            }
        });
    }
}

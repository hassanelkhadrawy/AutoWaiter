package com.example.autowaiter.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autowaiter.R;

public class OrderAdapter extends RecyclerView.ViewHolder {
    public   TextView TableNamber;
    public  TextView OrderName;
    public ImageView Delete;


    public OrderAdapter(@NonNull View itemView) {
        super(itemView);
        TableNamber=itemView.findViewById(R.id.table_number);
        OrderName=itemView.findViewById(R.id.order_name);
        Delete=itemView.findViewById(R.id.delete_order);


    }
}

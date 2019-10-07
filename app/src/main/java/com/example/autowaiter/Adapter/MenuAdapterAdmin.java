package com.example.autowaiter.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autowaiter.R;

public class MenuAdapterAdmin extends RecyclerView.ViewHolder {

   public ImageView MenuImage,Delete;
   public TextView MenuName;

    public MenuAdapterAdmin(@NonNull View itemView) {
        super(itemView);
        MenuImage=itemView.findViewById(R.id.menu_image);
        MenuName=itemView.findViewById(R.id.menu_name);
        Delete=itemView.findViewById(R.id.delete);

    }
}

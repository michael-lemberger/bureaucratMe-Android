package com.example.bureaucratme;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvFileName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFileName = itemView.findViewById(R.id.tvFileName);
    }
}

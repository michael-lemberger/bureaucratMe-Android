package com.example.bureaucratme;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private Button btnFileName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        btnFileName = itemView.findViewById(R.id.btnFileName);
    }

    public Button getBtnFileName() {
        return btnFileName;
    }
}

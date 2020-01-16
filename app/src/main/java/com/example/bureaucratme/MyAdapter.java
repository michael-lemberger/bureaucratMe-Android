package com.example.bureaucratme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<FilesData> filesDataList;
    private FormChooserActivity formChooserActivity;

    public MyAdapter(FormChooserActivity formChooserActivity, ArrayList<FilesData> filesDataList) {
        this.filesDataList = filesDataList;
        this.formChooserActivity = formChooserActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(formChooserActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.tvFileName.setText(filesDataList.get(position).getFilename());
    }

    @Override
    public int getItemCount() {
        return filesDataList.size();
    }
}

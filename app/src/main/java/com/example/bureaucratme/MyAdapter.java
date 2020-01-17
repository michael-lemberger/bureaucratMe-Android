package com.example.bureaucratme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.xml.transform.Templates;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private int x;
    private ArrayList<?> arrayList;
    private Activity activity;
    private Context context;


    public MyAdapter(Activity activity, ArrayList<?> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = activity;
    }

    public MyAdapter(Activity activity, ArrayList<?> arrayList, int x) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = activity;
        this.x = x;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        if(x == 1) {
            myViewHolder.getBtnFileName().setText(((FilesData) arrayList.get(position)).getFilename());

            myViewHolder.getBtnFileName().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, DocumentActivity.class);
                    i.putExtra("institution", ((FilesData) arrayList.get(position)).getInstitutionName());
                    i.putExtra("filename", ((FilesData) arrayList.get(position)).getNameInStorage());
                    context.startActivity(i);
                }
            });
        }
        else if(x == 2) {
            myViewHolder.getBtnFileName().setText(arrayList.get(position).toString());

            myViewHolder.getBtnFileName().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, FormChooserActivity.class);
                    if (x == 2)
                        i.putExtra("institution", (arrayList.get(position)).toString());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

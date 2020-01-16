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

    private ActivityEnum activityEnum;
    private ArrayList<?> arrayList;
    private Activity activity;
    private Context context;


    public MyAdapter(Activity activity, ArrayList<?> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = activity;
    }

    public MyAdapter(Activity activity, ArrayList<?> arrayList, ActivityEnum activityEnum) {
        this.arrayList = arrayList;
        this.activity = activity;
        this.context = activity;
        this.activityEnum = activityEnum;
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
        if(this.activityEnum == ActivityEnum.FORMCHOOSERACTIVITY) {
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

        else if(this.activityEnum == ActivityEnum.HOMEACTIVITY) {
            myViewHolder.getBtnFileName().setText(arrayList.get(position).toString());

            myViewHolder.getBtnFileName().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = arrayList.get(position)+"";
                    Intent i = new Intent(activity, FormChooserActivity.class);
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

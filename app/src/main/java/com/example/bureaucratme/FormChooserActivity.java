package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FormChooserActivity extends AppCompatActivity {

    private Button btnForm;
    private FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;
    private ArrayList<FilesData> arrayList = new ArrayList<>();
    private MyAdapter mAdapter;
    private DatabaseReference reference;
    private String institution;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_chooser);

        init();

        Intent intent = getIntent();
        institution = intent.getStringExtra("institution");

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("FilesData");

        readFromFirebase();


//        btnForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FormChooserActivity.this, DocumentActivity.class);
//
//                Intent i = getIntent();
//
//                intent.putExtra("institution", i.getStringExtra("institution"));
//                intent.putExtra("file", "maccabi1.pdf");
//
//                startActivity(intent);
//            }
//        });
    }

    private void readFromFirebase() {
//        if(arrayList.size() > 0)
//            arrayList.clear();

//        reference.child("EmptyFiles").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String s1 = ds.child("FileName").getValue(String.class);
//                    String s2 = ds.child("Link").getValue(String.class);
//                    FilesData filesData = new FilesData(s1, s2);
//
//                    arrayList.add(filesData);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("TAGGG", "ERROR");
//            }
//        });

        reference.child("EmptyFiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s1 = ds.child("FileName").getValue(String.class);
                    String s2 = ds.child("Link").getValue(String.class);
                    FilesData filesData = new FilesData(s1, s2);

                    arrayList.add(filesData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAGGG", "ERROR");
            }
        });

        mAdapter = new MyAdapter(FormChooserActivity.this, arrayList);
        recyclerView.setAdapter(mAdapter);
    }

    private void init() {
//        btnForm = findViewById(R.id.btnForm);
        recyclerView = findViewById(R.id.recycler);
    }


}

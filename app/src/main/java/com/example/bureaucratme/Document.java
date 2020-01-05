package com.example.bureaucratme;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Document {
   private ArrayList<String> docTags;
   private ArrayList<String> dbTags;
   private HashMap<String,String> values;
   private FirebaseUser fu;
   private FirebaseAuth mAuth;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference mReference;

    public Document() {
        this.mAuth = FirebaseAuth.getInstance();
        this.fu = mAuth.getCurrentUser();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.mReference = firebaseDatabase.getReference("Users");
    }

    public void fillTags(String docTag, String dbTag){
        docTags.add(docTag);
        dbTags.add(dbTag);
    }

    public void readValues(){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("dbId").getValue().equals(fu.getUid())) {
                        for(String value: dbTags) {
                            for (String tag : docTags) {
                                values.put(tag , ds.child(value).getValue(String.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

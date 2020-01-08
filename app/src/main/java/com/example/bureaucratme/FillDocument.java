package com.example.bureaucratme;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FillDocument {

    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private String src, dest;
    private HashMap<String, String> map;


//    public FillDocument(FirebaseAuth mAuth, FirebaseUser fu) {
//        this.mAuth = FirebaseAuth.getInstance();
//        this.fu = mAuth.getCurrentUser();
//        this.firebaseDatabase = FirebaseDatabase.getInstance();
//        this.mReference = firebaseDatabase.getReference("Users");
//        this.map = new HashMap<>();
//        this.src = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PdfToFill.pdf";
//        this.dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NewPdf.pdf";
//    }

    public FillDocument(FirebaseAuth mAuth, FirebaseUser fu, String src, String dest) {
        this.mAuth = FirebaseAuth.getInstance();
        this.fu = mAuth.getCurrentUser();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.mReference = firebaseDatabase.getReference("Users");
        this.map = new HashMap<>();
        this.src = src;
        this.dest = dest;
    }


    public void readFromDatabase() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("dbId").getValue().equals(fu.getUid())) {
                        map.put("id", ds.child("id").getValue(String.class));
                        map.put("email", ds.child("email").getValue(String.class));
                        map.put("firstName", ds.child("firstName").getValue(String.class));
                        map.put("lastName", ds.child("lastName").getValue(String.class));
                        map.put("phone", ds.child("phoneNumber").getValue(String.class));
                        map.put("birthDate", ds.child("birthdate").getValue(String.class));
                        map.put("street", ds.child("street").getValue(String.class));
                        map.put("city", ds.child("city").getValue(String.class));
                        map.put("zip", ds.child("zip").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillToPdf() {
        String x ;

        try{
            PdfReader reader = new PdfReader(src);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

            AcroFields form = stamper.getAcroFields();

            for(String s : map.keySet()){
                form.setField(s, map.get(s));
            }

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);

            String fullAddress = map.get("street") + " " + map.get("city") + " " + map.get("zip");
            String fullName = map.get("firstName") + " " + map.get("lastName");

            form.setField("fullAddress", fullAddress);
            form.setField("date5", thisDate);
            form.setField("fullname5", fullName);

            stamper.close();
            reader.close();

        } catch (Exception e) {
            Log.d("TAGGGGGGG" , e.getMessage()+"");
        }
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}

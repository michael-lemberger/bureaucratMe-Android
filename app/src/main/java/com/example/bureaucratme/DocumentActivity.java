package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

import java.io.File;

public class DocumentActivity extends AppCompatActivity {

    private Button btnSend, btnView, btnUpdate;
    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private FillDocument fd;
    String dest, src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Intent intn = getIntent();
         src = intn.getStringExtra("src");
         dest = intn.getStringExtra("dest");
        Log.d("TAGG", src);
        Log.d("TAGGGGG", dest);
        init();

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        fd = new FillDocument(mAuth, fu, src, dest);


        fd.readFromDatabase();

        viewDoc();
        sendDoc();
        updateDoc();
    }

    private void updateDoc() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocumentActivity.this, PersonalDetailsActivity.class);
                startActivity(intent);


                fd.fillToPdf();
            }
        });
    }

    private void openDoc(){
//        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NewPdf.pdf");
        File myFile = new File(dest);
        Intent intent = new Intent();
        intent.setPackage("com.adobe.reader");
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        startActivity(intent);
    }

    private void viewDoc() {
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fd.readFromDatabase();
                fd.fillToPdf();

                openDoc();
            }
        });
    }

    private void sendDoc(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fd.readFromDatabase();
                fd.fillToPdf();

                // need to do

            }
        });
    }

    private void init() {
        btnSend = findViewById(R.id.btnSend);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
}

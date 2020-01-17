package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class DocumentActivity extends AppCompatActivity implements Upload, Downloads {

    private Button btnSend, btnView, btnUpdate, btnDownload;
    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private Document doc;
    private FillDocument fd;
    private String dest, src, institutionName, fileName;
    private StorageReference storageRef;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Intent intent = getIntent();
        institutionName = intent.getStringExtra("institution");
        fileName = intent.getStringExtra("filename");
        src = Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/" + fileName;
        dest = Environment.getExternalStorageDirectory() + "/BurecrateMe/" + fileName;

        Log.d("institution: ", institutionName);
        Log.d("filename: ", fileName);

        removeFile(src);
        removeFile(dest);

        init();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();

        fd = new FillDocument(mAuth, fu, src, dest);

//        doc.readValues();

        downloadDoc();
        viewDoc();
        sendDoc();
        updateDoc();
    }

    private void downloadDoc() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFullPdf();
            }
        });
    }

    private void updateDoc() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DocumentActivity.this, PersonalDetailsActivity.class);
//                startActivity(intent);

                upload();
            }
        });
    }

    private void openDoc(){
        File myFile = new File(dest);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage("com.adobe.reader");
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        startActivity(intent);
//        String urlLink="";
//        Intent intent = new Intent(DocumentActivity.this, PdfViewerActivity.class);
//        intent.putExtra("UrlLink", urlLink);
//        startActivity(intent);
    }

    private void viewDoc() {
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doc.readValues();

                fd.fillToPdf();

                openDoc();
            }
        });
    }

    private void sendDoc(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doc.readValues();
                fd.fillToPdf();

                // need to do

            }
        });
    }

    private void init() {
        btnSend = findViewById(R.id.btnSend);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDownload = findViewById(R.id.btnDownload);
    }

    private void uploadPdf(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        String uid = fu.getUid();
        StorageReference reference = storageRef.child(uid + "/" + uri.getLastPathSegment());

        UploadTask uploadTask = reference.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("TAGGGGG", taskSnapshot.getMetadata() + "");
            }
        });
    }

    @Override
    public void upload() {
        uploadPdf(dest);
    }

    @Override
    public void download() {
        StorageReference reference = storageRef.child("Uploads/").child(institutionName).child(fileName);

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
//                downloadPdf(uri, Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/" + fileName);

            }
        });
    }

    private void downloadEmptyPdf() {
        StorageReference reference = storageRef.child("Uploads/").child(institutionName).child(fileName);

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadPdf(uri, Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/" + fileName,
                        DownloadManager.Request.VISIBILITY_HIDDEN);

            }
        });
    }

    private void downloadFullPdf() {
        StorageReference reference = storageRef.child(fu.getUid()).child(fileName);

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadPdf(uri, Environment.DIRECTORY_DOWNLOADS, DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGGG", "DOWNLOAD FAILED!!!!");
            }
        });
    }


    private void downloadPdf(Uri uri, String dest, final int notificationVisibility) {
        File file = new File(dest);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(fileName)
                .setDescription("Downloading: " + fileName)
                .setNotificationVisibility(notificationVisibility)
                .setDestinationUri(Uri.fromFile(file));

        downloadManager.enqueue(request);
    }



    private boolean createFolder(String path) {
        boolean flag = true;
        File folder = new File(path);

        if(!folder.exists()) {
            flag = folder.mkdir() ? true : false;
        }

        return flag;
    }

    private boolean removeFile(String path) {
        File file = new File(path);

        return file.delete();
    }

//    private void download(String s) {
//        StorageReference reference = storageRef.child("Uploads/").child(institutionName).child(fileName);
//
//        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                downloadPdf(uri, Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/" + fileName);
//
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        removeFile(src);
        removeFile(dest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        removeFile(src);
        removeFile(dest);

        createFolder(Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty");
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        downloadEmptyPdf();
        unregisterReceiver(onDownloadComplete);

        fd.fillToPdf();
    }

    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Download ", "Completed");

        }
    };
}

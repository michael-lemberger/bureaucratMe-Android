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

    private Button btnSend, btnView, btnUpdate;
    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private FillDocument fd;
    String dest, src, institutionName, fileName;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private String filePath;
    String str1,str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Intent intn = getIntent();
        src = intn.getStringExtra("src");
        dest = intn.getStringExtra("dest");
        institutionName = intn.getStringExtra("institution");
        fileName = intn.getStringExtra("file");
        str1 = Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/" + fileName;
        str2 = Environment.getExternalStorageDirectory() + "/BurecrateMe/" + fileName;

        Log.d("SRCCCC", src);
        Log.d("STR111", str1);
        Log.d("DESTTT", dest);
        Log.d("STR222", str2);

        removeFile(str1);
        removeFile(str2);

        init();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        fd = new FillDocument(mAuth, fu, str1, str2);

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


                upload();
            }
        });
    }

    private void openDoc(){
//        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NewPdf.pdf");
        File myFile = new File(str2);
        Intent intent = new Intent();
        intent.setPackage("com.adobe.reader");
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        startActivity(intent);
    }

    private void viewDoc() {
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fd.readFromDatabase();
                fd.fillToPdf();

                openDoc();
            }
        });
    }

    private void sendDoc(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // need to do

            }
        });
    }

    private void init() {
        btnSend = findViewById(R.id.btnSend);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void uploadPdf(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        String string = fu.getUid();
        StorageReference reference = storageRef.child(string + "/" + uri.getLastPathSegment());

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
        uploadPdf(str2);
    }

    private void downloadPdf(Uri uri) {
        String path = Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty";// + institutionName;
        createFolder(path);
        File file = new File(path, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(fileName)
                .setDescription("Downloading: " + fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
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

    @Override
    public void download() {
        StorageReference reference = storageRef.child("Uploads/").child(institutionName).child(fileName);

        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                downloadPdf(uri);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        removeFile(str1);
        removeFile(str2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        removeFile(str1);
        removeFile(str2);

        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        download();
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

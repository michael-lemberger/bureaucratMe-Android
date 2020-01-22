package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AdminUploadPDFActivity extends AppCompatActivity {

    TextView txt_pathShow;
    Button btn_filePicker;
    Intent myFileIntent;
    private FirebaseAuth mAuth;
    private String institutionName, fileName;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private DatabaseReference filesReference;
    private FirebaseDatabase firebaseDatabase;
    public int x;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_pdf);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
        }


        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        filesReference = firebaseDatabase.getReference("FilesData");

        txt_pathShow= (TextView) findViewById(R.id.txt_path);
        btn_filePicker=(Button)findViewById(R.id.btn_FilePicker);
        btn_filePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                startActivityForResult(myFileIntent,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK){
            String path=data.getData().getPath();
            String name=data.getData().getLastPathSegment();
            fileName=name;
            institutionName="none";
            path=path.replace("/external_files","");
            String newPath="/storage/emulated/0"+path;
            Log.d("6644TRT", newPath);
            uploadPdf(path,name);
        }

    }


    private void uploadPdf(String path, String name) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        final StorageReference reference = storageRef.child("Uploads/Admins");
        flag=true;
        UploadTask uploadTask = reference.putFile(uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FilesData filesData = new FilesData(uri.toString(),
                                fileName, institutionName, taskSnapshot.getMetadata().getName());
                        filesReference.child(institutionName).child("File" + (x+1)).setValue(filesData);
                        Log.d("URI", uri.toString());
                        Log.d("URI", taskSnapshot.getMetadata().getName());
                    }
                });
            }
        });
        if(flag){
            Intent i = new Intent(AdminUploadPDFActivity.this, AdminPDFValActivity.class);
            startActivity(i);
        }
        else{
            Log.d("6644TRT", "Can't upload PDF file, try Again.");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] premissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,premissions,grantResults);
        switch (requestCode){
            case 1001:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "permission granted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "permission granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}

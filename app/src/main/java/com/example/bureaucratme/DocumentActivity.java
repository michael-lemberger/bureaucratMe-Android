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


public class DocumentActivity extends AppCompatActivity {

    private Button btnSend, btnView, btnUpdate, btnDownload;
    private ProgressBar progressBar;
    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private String dest, src, institutionName, fileName;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private DatabaseReference filesReference, userReference;
    private FirebaseDatabase firebaseDatabase;
    private HashMap<String, String> userInfo, pdfLinks;
    private boolean isUpload, emptyFile, down;
    public int x;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        userInfo = new HashMap<>();
        pdfLinks = new HashMap<>();

        Intent intent = getIntent();
        x = intent.getIntExtra("size", 0);
        institutionName = intent.getStringExtra("institution");
        fileName = intent.getStringExtra("filename");
        src = Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty/";
        dest = Environment.getExternalStorageDirectory() + "/BurecrateMe/Full/";


        boolean f = hasPermissions(this, PERMISSIONS);
        if(!f) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        createFolder(Environment.getExternalStorageDirectory() + "/BurecrateMe/");
        boolean f1 = createFolder(src);
        Log.d("permissions: ", hasPermissions(this, PERMISSIONS)+"");
        Log.d("createfolder: ", createFolder(src)+"");
        Log.d("institution: ", institutionName);
        Log.d("filename: ", fileName);

//        removeFile(src);
//        removeFile(dest);

        init();

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        filesReference = firebaseDatabase.getReference("FilesData");
        userReference = firebaseDatabase.getReference("Users");

        isUpload = true;
        emptyFile = true;
        down = true;

//        readFile(new FirebaseCallback() {
//            @Override
//            public void onCallback(Map<String, String> map) {
//                if(down)
//                    new Downloader().execute(map.get("Link"));
//                else
//                    openPdf(map.get("Link"));
//                    new Downloader().execute("https://firebasestorage.googleapis.com/v0/b/bureaucratme-be01d.appspot.com/o/Uploads%2FMaccabiHMO%2Fmaccabi1.pdf?alt=media&token=305b2b4a-3ee6-4f56-ae54-440d9001bc3b");
//
//            }
//        });

//        downloadDoc();
//        viewDoc();
//        sendDoc();
//        updateDoc();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile(new FirebaseCallback() {
                    @Override
                    public void onCallback(Map<String, String> map) {
                        emptyFile = true;
                        new Downloader().execute(map.get("urlLink"));
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DocumentActivity.this, PersonalDetailsActivity.class);
                i.putExtra("finish", true);
                startActivity(i);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyFile = false;
                DatabaseReference ref2 = filesReference.child(fu.getUid()).child(institutionName);


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String str1 = ds.child("fileName").getValue(String.class);
                            String str2 = ds.child("urlLink").getValue(String.class);
                            String str3 = ds.child("nameInStorage").getValue(String.class);
                            if (ds.child("fileName").getValue().equals(fileName)) {
                                Log.d("URLL", str2);
                                Intent intent = new Intent(DocumentActivity.this, PdfViewerActivity.class);
                                intent.putExtra("UrlLink", str2);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                ref2.addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fillPdf() {
        readUser(new FirebaseCallback() {
            @Override
            public void onCallback(Map<String, String> map) {
                try{
                    PdfReader reader = new PdfReader(src+fileName);
                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest+fileName));

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

                    if(isUpload)
                        uploadPdf2(dest+fileName);

                } catch (Exception e) {
                    Log.d("TAGGGGGGG" , e.getMessage()+"");
                }
            }
        });


    }

    private void openPdf(String url) {
        Intent intent = new Intent(DocumentActivity.this, PdfViewerActivity.class);
        intent.putExtra("UrlLink", url);
        startActivity(intent);
    }

    public void readUser(final FirebaseCallback firebaseCallback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String str = ds.child("dbId").getValue(String.class);
                    String str2 = fu.getUid();
                    if (ds.child("dbId").getValue().equals(fu.getUid())) {
                        userInfo.put("id", ds.child("id").getValue(String.class));
                        userInfo.put("email", ds.child("email").getValue(String.class));
                        userInfo.put("firstName", ds.child("firstName").getValue(String.class));
                        userInfo.put("lastName", ds.child("lastName").getValue(String.class));
                        userInfo.put("phone", ds.child("phoneNumber").getValue(String.class));
                        userInfo.put("birthDate", ds.child("birthdate").getValue(String.class));
                        userInfo.put("street", ds.child("street").getValue(String.class));
                        userInfo.put("city", ds.child("city").getValue(String.class));
                        userInfo.put("zip", ds.child("zip").getValue(String.class));
                    }
                }

                firebaseCallback.onCallback(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        userReference.addValueEventListener(valueEventListener);
    }


    private void uploadPdf2(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        String uid = fu.getUid();
        final StorageReference reference = storageRef.child(uid + "/" + uri.getLastPathSegment());

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
                        filesReference.child(fu.getUid()).child(institutionName).child("File" + (x+1)).setValue(filesData);
                        Log.d("URI", uri.toString());
                        Log.d("URI", taskSnapshot.getMetadata().getName());
                    }
                });
            }
        });
    }


    public void readFile(final FirebaseCallback firebaseCallback) {
        DatabaseReference ref;
        if(emptyFile) {
            ref = filesReference.child("EmptyFiles").child(institutionName);
        } else {
            ref = filesReference.child(fu.getUid()).child(institutionName);
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String str = ds.child("nameInStorage").getValue(String.class);
                    if(ds.child("nameInStorage").getValue().equals(fileName)) {
                        pdfLinks.put("nameInStorage", ds.child("nameInStorage").getValue(String.class));
                        pdfLinks.put("urlLink", ds.child("urlLink").getValue(String.class));
                        pdfLinks.put("fileName", ds.child("fileName").getValue(String.class));
                        pdfLinks.put("institutionName", ds.child("institutionName").getValue(String.class));
                    }
                }

                firebaseCallback.onCallback(pdfLinks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateFileData(final FirebaseCallback firebaseCallback) {
        DatabaseReference ref;
        if(emptyFile) {
            ref = filesReference.child("Empty");
        } else {
            ref = filesReference.child(fu.getUid()).child(institutionName);
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("nameInStorage").getValue().equals(fileName)) {
                        pdfLinks.put("nameInStorage", ds.child("nameInStorage").getValue(String.class));
                        pdfLinks.put("urlLink", ds.child("urlLink").getValue(String.class));
                        pdfLinks.put("fileName", ds.child("fileName").getValue(String.class));
                        pdfLinks.put("institutionName", ds.child("institutionName").getValue(String.class));
                    }
                }

                firebaseCallback.onCallback(pdfLinks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void init() {
        btnSend = findViewById(R.id.btnSend);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDownload = findViewById(R.id.btnDownload);

        progressBar = findViewById(R.id.progressBarDoc);
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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        removeFile(src);
//        removeFile(dest);
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        removeFile(src);
//        removeFile(dest);
//
//        createFolder(Environment.getExternalStorageDirectory() + "/BurecrateMe/Empty");
//        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        downloadEmptyPdf();
//        unregisterReceiver(onDownloadComplete);
//
//        fd.fillToPdf();
//    }

//    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d("Download ", "Completed");
//            fd.fillToPdf();
//
//        }
//    };

final int id = 101;
    private class Downloader extends AsyncTask<String, Integer, String> {
        //before download
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            removeFile(src+fileName);
            removeFile(dest+fileName);
            createFolder(src);
            createFolder(dest);

            Log.d("Download", "Start");
        }

        //download
        @Override
        protected String doInBackground(String... strings) {
            try {
                int count;
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();

                urlConnection.connect();

                int length = urlConnection.getContentLength();

                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                OutputStream outputStream = new FileOutputStream(src+fileName);

                byte[] data = new byte[1024];

                long total = 0;

                while((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                }

                outputStream.flush();

                outputStream.close();
                inputStream.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //after download
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            fillPdf();
            Log.d("Donwload", "Complete");
        }
    }

    private interface FirebaseCallback {
        void onCallback(Map<String, String> map);
    }
}

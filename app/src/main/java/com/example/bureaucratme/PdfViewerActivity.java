package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewerActivity extends AppCompatActivity {


    PDFView pdfView ;
    String url;
    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfView = findViewById(R.id.pdfView);
        btnDownload = findViewById(R.id.btnDown);

        Intent intent = getIntent();
        url = intent.getStringExtra("UrlLink");

        new RetrievePDFStream().execute(url);
//        new RetrievePDFStream().execute("https://firebasestorage.googleapis.com/v0/b/bureaucratme-be01d.appspot.com/o/p5lJZ0h9MAYl0hqVIKG5ZQWHrzs1%2Fmaccabi1.pdf?alt=media&token=657da892-b628-47bf-b180-624144749fcd");

        Log.d("DD", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                        DownloadManager.Request.NETWORK_WIFI);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle("Downloading...");
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
                        uri.getLastPathSegment());
                ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

            }
        });
    }


    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL u = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                if(urlConnection.getResponseCode() == 200)
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                Log.d("TAGGGG", e.getMessage());
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            pdfView.fromStream(inputStream).load();
        }
    }
}

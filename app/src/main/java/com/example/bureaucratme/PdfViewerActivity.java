package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewerActivity extends AppCompatActivity {


    PDFView pdfView ;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        url = intent.getStringExtra("UrlLink");

        new RetrievePDFStream().execute(url);
    }

    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL u = new URL(strings[0]);
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

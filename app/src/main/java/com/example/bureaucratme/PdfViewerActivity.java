package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

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

//        new RetrievePDFStream().execute(url);
        new RetrievePDFStream().execute("https://firebasestorage.googleapis.com/v0/b/bureaucratme-be01d.appspot.cokkkkkkkkkm mm/o/VbHgF2f8XGMe5SaN7h8VU6c4M7B3%2Fmaccabi1.pdf?alt=media&token=a4ccc776-91cd-477b-a552-b07ffa0a9282");

//        String s = "https://firebasestorage.googleapis.com/v0/b/bureaucratme-be01d.appspot.com/o/VbHgF2f8XGMe5SaN7h8VU6c4M7B3%2Fmaccabi1.pdf?alt=media&token=db09f3b7-1d1d-44df-b7bb-93ba3692866b";
//        String doc="<iframe src='"+url+"' width='100%' height='100%' style='border: none;'></iframe>";
//        WebView webView = findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadData(doc, "text/html", "UTF-8");
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

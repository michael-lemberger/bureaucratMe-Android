package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private Button btnSave;
    private Spinner[] dbTags;
    private EditText[] docTags;
    private String tags[];
    private String dbvalues[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                extractText();
            }
        });
    }

    private void init() {

        dbTags = new Spinner[9];
        docTags = new EditText[9];
        tags=new String[docTags.length];
        dbvalues=new String[dbTags.length];
        for(int i = 0; i<dbTags.length; i++) {

            String spinner = "dbTags_spinner"+(i+1);
            int resID = getResources().getIdentifier(spinner, "id", getPackageName());
            dbTags[i] = (Spinner) findViewById(resID);
            ArrayAdapter<CharSequence> sp = ArrayAdapter.createFromResource(this, R.array.dbTags, android.R.layout.simple_spinner_item);
            sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dbTags[i].setAdapter(sp);

            String editext = "docTag"+(i+1);
            resID = getResources().getIdentifier(editext, "id", getPackageName());
            docTags[i] = findViewById(resID);
        }
        btnSave = (Button) findViewById(R.id.btnSave);

    }

    private void extractText() {
        for (int i = 0; i <dbvalues.length; i++) {
            dbvalues[i] = dbTags[i].getSelectedItem().toString();
            tags[i]= docTags[i].getText().toString();
        }

        try {
            prepareText();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareText() throws IOException {
        Document doc= new Document();
        HashMap<String,String> map = doc.getValues(tags,dbvalues);
       Iterator it = map.entrySet().iterator();
       File root = new File (Environment.getExternalStorageDirectory(),"Notes" );
       if(!root.exists()){
           root.mkdirs();
       }
       File myfile= new File(root, "massage");
       FileWriter fw= new FileWriter(myfile);
       fw.append("rrrrrrrrrrrrrrr");
       fw.flush();
       fw.close();
       while (it.hasNext()){
           Map.Entry pair = (Map.Entry)it.next();
       }
    }

}

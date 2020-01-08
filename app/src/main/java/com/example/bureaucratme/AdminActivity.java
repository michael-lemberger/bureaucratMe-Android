package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private Button btnSave;
    FirebaseAuth mAuth;
    FirebaseUser fu;
    private Spinner[] dbTags;
    private EditText[] docTags;
    private String tags[];
    private String dbvalues[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();


        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addUser();
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

    private void addUser() {
        for (int i = 0; i <dbvalues.length; i++) {
            dbvalues[i] = dbTags[i].getSelectedItem().toString();
            tags[i]= docTags[i].getText().toString();
        }
    }
}

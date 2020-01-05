package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    private EditText docTag, dbTag;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private Button btnSave;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();

        Spinner spinner = (Spinner) findViewById(R.id.dbTags_spinner);
        ArrayAdapter<CharSequence> sp = ArrayAdapter.createFromResource(this, R.array.dbTags, android.R.layout.simple_spinner_item);
        sp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp);

        //init();

    }



    /*private void init(){
        docTag = (TextView)findViewById(R.id.docTag);
        dbTag = (EditText)findViewById(R.id.dbTag);
        btnSave = (Button)findViewById(R.id.btnSave);

    }*/
}

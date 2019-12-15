package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalDetailsActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText id, privateName, familyName, phoneNumber, email;
    DatabaseReference usersDataBase;
    String dbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        btnSave = (Button)findViewById(R.id.save);
        id = (EditText)findViewById(R.id.id);
        privateName = (EditText)findViewById(R.id.firstName);
        familyName = (EditText)findViewById(R.id.familyName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        email = (EditText)findViewById(R.id.email);

        usersDataBase = FirebaseDatabase.getInstance().getReference().child("Users");

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addUser();
            }
        });
    }


    private void addUser(){
        String ID = id.getText().toString().trim();
        String praName = privateName.getText().toString().trim();
        String fName = familyName.getText().toString().trim();
        String phNumber = phoneNumber.getText().toString().trim();
        String Email = email.getText().toString().trim();

        dbId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(!TextUtils.isEmpty(ID)){
            Users user = new Users(ID, praName, fName, Email, phNumber, dbId);
            usersDataBase.child(dbId).setValue(user);
            Toast.makeText(this, "details updated",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Enter ID!",Toast.LENGTH_LONG).show();
        }

    }

}

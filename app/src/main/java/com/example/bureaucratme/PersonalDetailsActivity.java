package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PersonalDetailsActivity extends AppCompatActivity {
    //class variables.
    private Button btnSave;
    private EditText id, firstName, familyName, phoneNumber, email;
    DatabaseReference usersDataBase;
    String dbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //integrate between layout and activity
        setContentView(R.layout.activity_personal_details);
        
        ////intitalize objects.
        btnSave = (Button)findViewById(R.id.save);
        id = (EditText)findViewById(R.id.id);
        firstName = (EditText)findViewById(R.id.firstName);
        familyName = (EditText)findViewById(R.id.familyName);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        email = (EditText)findViewById(R.id.email);
        
       // initalize refernce to firebase database.
        usersDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        
        //set click listener, add user to data base on click.
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addUser();
            }
        });
    }

    //build user object and upload to data base
    private void addUser(){
        String ID = id.getText().toString().trim();
        String frstName = firstName.getText().toString().trim();
        String lstName = familyName.getText().toString().trim();
        String phNumber = phoneNumber.getText().toString().trim();
        String eml = email.getText().toString().trim();
        
        //get the auth id of the current user.
        dbId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(!isID(ID))
            Toast.makeText(this, "Error. Wrong ID",Toast.LENGTH_SHORT).show();
        else if(frstName.length() < 2)
            Toast.makeText(this, "Error. Wrong First Name",Toast.LENGTH_SHORT).show();
        else if (lstName.length() < 2)
            Toast.makeText(this, "Error. Wront Family Name",Toast.LENGTH_SHORT).show();
        else if((!eml.contains("@")) && (!eml.contains(".")))
            Toast.makeText(this, "Error. Wrong Email",Toast.LENGTH_SHORT).show();
        else if (phNumber.length() < 9)
            Toast.makeText(this, "Error. Wrong Phone Number",Toast.LENGTH_SHORT).show();
        else {
            Users user = new Users(ID, frstName, lstName, eml, phNumber, dbId);
            //update user object to the adjusted database
            usersDataBase.child(dbId).setValue(user);
            Toast.makeText(this, "details updated",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ID validation
     * @param s the ID
     * @return if real id
     */
    private boolean isID(String s) {
        int x , y = 0;

        // if id less the 9 digits return false
        if(s.length() < 9)
            return false;

        // check if id is real
        //
        for (int i = 0; i < 9; i++) {
            x = (s.charAt(i) - '0') * ((i % 2) + 1);
            y += (x / 10) + (x % 10);
        }

        if (y % 10 == 0)
            return true;

        return  false;
    }



}

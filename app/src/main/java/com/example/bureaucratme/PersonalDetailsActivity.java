package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfReader;

import java.util.Calendar;


public class PersonalDetailsActivity extends AppCompatActivity {
    //class variables.
    private Button btnSave, btnBirthdate;
    private EditText etId, etFirstName, etLastName, etPhoneNumber, etEmail, etCity, etStreet, etZip;
    private DatabaseReference usersDataBase;
    private String dbId;
    private DatePickerDialog.OnDateSetListener dpd;
    private TextView tvBirthDate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private boolean flag;

    FirebaseAuth mAuth;
    FirebaseUser fu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        Intent intent = getIntent();
        flag = intent.getBooleanExtra("finish", false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("Users");

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();

        init();

        readFromDatabase();

       // initalize refernce to firebase database.
        usersDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        
        //set click listener, add user to data base on click.
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addUser();
                if(flag)
                    finish();
            }
        });

        chooseBirthdateButton();
    }

    private void chooseBirthdateButton() {
        btnBirthdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PersonalDetailsActivity.this, dpd, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ++month;
                String date = dayOfMonth +"/" + month + "/" + year;

                tvBirthDate.setText(date);
            }
        };
    }

    public void readFromDatabase() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("dbId").getValue().equals(fu.getUid())) {
                        etId.setText(ds.child("id").getValue(String.class));
                        etFirstName.setText(ds.child("firstName").getValue(String.class));
                        etLastName.setText(ds.child("lastName").getValue(String.class));
                        etPhoneNumber.setText(ds.child("phoneNumber").getValue(String.class));
                        etEmail.setText(ds.child("email").getValue(String.class));
                        etStreet.setText(ds.child("street").getValue(String.class));
                        etCity.setText(ds.child("city").getValue(String.class));
                        etZip.setText(ds.child("zip").getValue(String.class));
                        tvBirthDate.setText(ds.child("birthdate").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //build user object and upload to data base
    private void addUser(){
        String ID = etId.getText().toString().trim();
        String frstName = etFirstName.getText().toString().trim();
        String lstName = etLastName.getText().toString().trim();
        String phNumber = etPhoneNumber.getText().toString().trim();
        String eml = etEmail.getText().toString().trim();
        String strt = etStreet.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String birthdate = tvBirthDate.getText().toString().trim();

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
            Users user = new Users(ID, frstName, lstName, eml, phNumber, birthdate, strt, city, zip, dbId);

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

    //intitalize objects.
    private void init(){
        tvBirthDate = (TextView)findViewById(R.id.tvBirthDate);
        etId = (EditText)findViewById(R.id.etId);
        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etFamilyName);
        etPhoneNumber = (EditText)findViewById(R.id.etPhoneNumber);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etStreet = (EditText)findViewById(R.id.etStreet);
        etCity = (EditText)findViewById(R.id.etCity);
        etZip = (EditText)findViewById(R.id.etZip);
        btnBirthdate = (Button)findViewById(R.id.btnBirthDate);
        btnSave = (Button)findViewById(R.id.btnSave);

    }

}

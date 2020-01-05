package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etEmail;
    private EditText etPass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("Admins");

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmail.getText().toString();
                String Password = etPass.getText().toString();
                // Check if email and password not empty
                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                } else {
                   if(checkByMail(Email)) {
                        // Login with email
                        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If login successfully open home activity and finish current activity
                                        // Else show error massage
                                        if (task.isSuccessful()) {
                                            Intent i = new Intent(AdminLoginActivity.this, HomeActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }


                    else
                        Toast.makeText(AdminLoginActivity.this, "Admin not exist", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void init() {
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
    }

    boolean flag = false;

    private boolean checkByMail(String email){

//        DatabaseReference databaseReference = mReference.child(email);
            mReference.orderByChild("Email").equalTo(email).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        flag = true;
                        Log.d("1111111111", ""+flag);
                    }
                }
                @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
            });
        Log.d("222222222", ""+flag);
        return flag;
    }



}



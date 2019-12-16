package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etEmail, etPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        init();

        //Register OnClickListener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    /**
     * Initialize objects from xml
     */
    private void init() {
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
    }

    private void createNewAccount() {
        //Get user details from xml
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

        //Check if password or email empty
        if(TextUtils.isEmpty(pass) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Password or Email empty", Toast.LENGTH_SHORT).show();
        }
        else {
            // create user and password in firebase authentication
            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If task completed successfully create account and return to main activity
                            // Else show error massage
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,"Account Created Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                String msg = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error " + msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

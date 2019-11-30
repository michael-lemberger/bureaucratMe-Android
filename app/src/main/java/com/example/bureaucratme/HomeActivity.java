package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser fu;
    FirebaseAuth mAuth;
    TextView tv;
    Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.btnSignOut);


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(HomeActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
                signOut();
            }
        });


        tv = findViewById(R.id.textView);
        fu = mAuth.getCurrentUser();
        if(fu != null) {
            tv.setText(fu.getEmail());
        }
    }

    private void signOut() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
        finish();

    }
}

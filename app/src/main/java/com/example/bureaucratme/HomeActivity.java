package com.example.bureaucratme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser fu;
    FirebaseAuth mAuth;
    private Button firstEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        fu = mAuth.getCurrentUser();
//        if(fu != null) { }

        init();

        firstEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, EntityActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalDetails:
                Intent myIntent = new Intent(HomeActivity.this, PersonalDetailsActivity.class);
                startActivity(myIntent);
                break;
            case R.id.logOut:
                signOut();
                break;
        }

        return true;
    }

    // onClickListner for login button

    /**
     * sign out and return to main activity
     */
    private void signOut() {
        mAuth.signOut();
        Toast.makeText(HomeActivity.this, "Sign out", Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
        finish();

    }

    private void init(){
        firstEntity = findViewById(R.id.btnBir1);
    }
}

package com.example.bureaucratme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser fu;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private MyAdapter mAdapter;
    private DatabaseReference institutionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("Users");
        institutionReference = firebaseDatabase.getReference("Institution");
        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
//        if(fu != null) { }

        init();

        readFromFirebase();
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
        recyclerView = findViewById(R.id.homeRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void readFromFirebase() {
        if(arrayList.size() > 0)
            arrayList.clear();

//        DatabaseReference institution = mReference.child("EmptyFiles").child(institution);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    arrayList.add(ds.child("name").getValue(String.class));
                }

                mAdapter = new MyAdapter(HomeActivity.this, arrayList, ActivityEnum.HOMEACTIVITY);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        institutionReference.addListenerForSingleValueEvent(valueEventListener);
    }

}

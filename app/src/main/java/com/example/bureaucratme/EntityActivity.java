package com.example.bureaucratme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EntityActivity extends AppCompatActivity{

    private Button form;
    String formUrl;
    String userID;
    FirebaseUser fu;
    FirebaseAuth mAuth;
    UserDocs userDocs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        fu = mAuth.getCurrentUser();
        userID = fu.getUid();
//        if(fu != null) { }

        init();

        //fill the write form when clicked
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDocs = new UserDocs(formUrl,userID);
               // userDocs.fillForm();
            }
        });
    }

    public void init(){
        form.findViewById(R.id.form1);
    }
}

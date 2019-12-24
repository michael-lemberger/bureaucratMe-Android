package com.example.bureaucratme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

import java.io.InputStream;
import java.util.Set;


public class HomeActivity extends AppCompatActivity {

    FirebaseUser fu;
    FirebaseAuth mAuth;
    TextView txt;
    private Button firstEntity;

    Intent mi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        txt = findViewById(R.id.textView123);

        mAuth = FirebaseAuth.getInstance();

        fu = mAuth.getCurrentUser();
//        if(fu != null) { }

        init();

        firstEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getElementsFromPDF();
            }
        });


    }

    private void getElementsFromPDF(){
        String y = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PdfToFill.pdf";

        try{
            PdfReader reader = new PdfReader( y );

            AcroFields fields = reader.getAcroFields();

            Set<String> fldNames = fields.getFields().keySet();

            for (String fldName : fldNames) {
                Toast.makeText(HomeActivity.this, fldName + ": " + fields.getField( fldName ), Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e) {
            Toast.makeText(HomeActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

        }
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

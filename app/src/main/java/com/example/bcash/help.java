package com.example.bcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class help extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        googleVerification();
    }

    void googleVerification(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String email = acct.getEmail();
            if (!(email.contains("@sscr.edu"))) {
                Toast.makeText(this, "Please use a valid SSCR account.", Toast.LENGTH_LONG).show();
                signOut();
            }
        }
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                Intent intent = new Intent(help.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void moveTo(View view){
        Context cntxt = help.this;
        Intent intent = null;
        Object tag = view.getTag();
        if ("dashboard".equals(tag)) {
            finish();
            intent = new Intent(cntxt, dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else if ("transaction".equals(tag)) {
            finish();
            intent = new Intent(cntxt, transactions.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else if ("help".equals(tag)) {
            finish();
            intent = new Intent(cntxt, help.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else if ("profile".equals(tag)) {
            finish();
            intent = new Intent(cntxt, profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        } else {
            Toast.makeText(cntxt, "("+tag+")", Toast.LENGTH_SHORT).show();
        }
        if (!(intent == null)) startActivity(intent);
    }
}
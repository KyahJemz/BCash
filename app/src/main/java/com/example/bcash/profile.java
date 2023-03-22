package com.example.bcash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;


public class profile extends AppCompatActivity {
    private Session session;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    TextView userId, userFullName;
    ImageView userProfileImage;
    ConstraintLayout btnLogOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new Session(profile.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        userId = findViewById(R.id.UserId);
        userFullName = findViewById(R.id.UserFullName);
        userProfileImage = findViewById(R.id.UserProfileImage);

        userFullName.setText(session.getFirstName()+" "+session.getLastName());
        userId.setText("ID : "+session.getPersonalId());

        Uri personPhoto = Uri.parse(session.getProfileImage());
        Picasso.with(this).load(personPhoto).into(userProfileImage);

        googleVerification();

        btnLogOut = findViewById(R.id.btn_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
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
                Intent intent = new Intent(profile.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void moveTo(View view){
        Context cntxt = profile.this;
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
        }  else if ("parental-controls".equals(tag)) {
            intent = new Intent(cntxt, parental_controls.class);
        } else if ("about".equals(tag)) {
            intent = new Intent(cntxt, about.class);
        } else if ("settings".equals(tag)) {
            intent = new Intent(cntxt, settings.class);
        } else {
            Toast.makeText(cntxt, "("+tag+")", Toast.LENGTH_SHORT).show();
        }
        if (!(intent == null)) startActivity(intent);

    }
}
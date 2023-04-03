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

    public void help(View view){
        String tag = view.getTag().toString();
        String text = "";
        String title = "";
        if (tag.equals("1")){
            title = "How to cash-In?";
            text = "If you want to cash in money, you will need to go to the cashier and provide them with physical cash to complete the transaction.";
        } else if (tag.equals("2")) {
            title = "How to transfer money?";
            text = "In order to transfer money using B-Cash, one must go to the Transfer Cash section and enter the recipients school email address.";
        } else if (tag.equals("3")) {
            title = "How to purchase an item?";
            text = "To make a purchase, you must visit the 'store' section, select the items you wish to buy, and add them to your cart.";
        } else if (tag.equals("4")) {
            title = "Can I cash out my money?";
            text = "According to the terms and conditions of B-Cash, it is not possible to withdraw the funds you have deposited into the B-Cash application.";
        } else if (tag.equals("5")) {
            title = "Lost school Id Card";
            text = "If you lose your ID card and need to freeze your B-Cash account to protect your balance, it is important to submit an affidavit of loss to the DSA as soon as possible";
        } else if (tag.equals("6")) {
            title = "Parental Controls";
            text = "Parental controls are for parents or guardians to guid and limit the BCash user activities, can be found in settings";
        } else if (tag.equals("7")) {
            title = "Freeze Money Transfer";
            text = "To freeze money transfer, click on settings then turn off Transfer Money";
        } else if (tag.equals("8")) {
            title = "Rewards Mechanics";
            text = "The confirmation process for this category is currently ongoing, but it might be published shortly.";
        } else if (tag.equals("9")) {
            title = "Login and Security";
            text = "BCash uses authentications in each process to create a more secure login and process.";
        }
        Intent intent = new Intent(help.this, help_details.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        startActivity(intent);
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
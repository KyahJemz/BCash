package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class login_verification extends AppCompatActivity {

    private Session session;
    private bcash_api bcashApi;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verification);
        session = new Session(login_verification.this);
        bcashApi = new bcash_api();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        googleVerification();
    }

    void googleVerification(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String email = acct.getEmail();

            if (email.contains("@sscr.edu")) {
                email = email.substring(0, email.length() - 9);
                session.setEmail(email);
                session.setFirstName(acct.getGivenName());
                session.setLastName(acct.getFamilyName());
                session.setPersonalId(acct.getId());
                session.setProfileImage(String.valueOf(acct.getPhotoUrl()));
                api();
            } else {
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
                    Intent intent = new Intent(login_verification.this, login.class);
                startActivity(intent);
            }
        });
    }

    boolean api(){

        String urlParam = "/accounts.php";

        RequestQueue queue = Volley.newRequestQueue(login_verification.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bcashApi.url+urlParam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        finish();
                        Intent intent = new Intent(login_verification.this, dashboard.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(login_verification.this, "Network Connection Failed, Try Again", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(login_verification.this, login.class);
                        startActivity(intent);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("registeration-data", "");
                params.put("email", session.getEmail());
                params.put("firstname", session.getFirstName());
                params.put("lastname", session.getLastName());
                params.put("personalid", session.getPersonalId());

                return params;
            }
        };
        queue.add(stringRequest);

        return false;
    }

    public void onBackPressed() {}
}
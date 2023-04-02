package com.example.bcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class dashboard extends AppCompatActivity {

    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    TextView tvuserFullName ,tvuserPersonalId, tvuserBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        session = new Session(dashboard.this);
        bcashapi = new bcash_api();
        queue = Volley.newRequestQueue(dashboard.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        tvuserFullName = findViewById(R.id.tv_userFullName);
        tvuserPersonalId = findViewById(R.id.tv_userPersonalId);
        tvuserBalance = findViewById(R.id.tv_userBalance);

        googleVerification();

        getDataFromApi();

        tvuserFullName.setText("Hello "+session.getFirstName());
        tvuserPersonalId.setText("Id : "+session.getPersonalId());
    }

    boolean getDataFromApi(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, bcashapi.url+"/accounts.php?email="+session.getEmail(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            tvuserBalance.setText("P "+response.getString("user_cash"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
                Toast.makeText(dashboard.this, "Network Connection Failed, Try Again", Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(dashboard.this, login.class);
                startActivity(intent);
            }
        });
        queue.add(getRequest);
        return true;
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
                Intent intent = new Intent(dashboard.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void moveTo(View view){
        Context cntxt = dashboard.this;
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
        } else if ("transfer-form".equals(tag)) {
            intent = new Intent(cntxt, transfer_form.class);
        } else if ("transfer-confirmation".equals(tag)) {
            intent = new Intent(cntxt, transfer_form.class);
        }  else if ("parental-controls".equals(tag)) {
            intent = new Intent(cntxt, parental_controls.class);
        } else if ("about".equals(tag)) {
            intent = new Intent(cntxt, about.class);
        } else if ("settings".equals(tag)) {
            intent = new Intent(cntxt, settings.class);
        } else if ("qrscan".equals(tag)) {
            intent = new Intent(cntxt, qrscanner.class);
        } else {
            Toast.makeText(cntxt, "("+tag+")", Toast.LENGTH_SHORT).show();
        }
        if (!(intent == null)) startActivity(intent);
    }
}
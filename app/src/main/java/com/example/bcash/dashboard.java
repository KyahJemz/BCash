package com.example.bcash;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class dashboard extends AppCompatActivity {

    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    TextView tvuserFullName ,tvuserPersonalId, tvuserBalance;
    ImageView b11, b12,b13,b14, b21,b22,b23,b24;

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

        b11 = findViewById(R.id.banner_1_1);
        b12 = findViewById(R.id.banner_1_2);
        b13 = findViewById(R.id.banner_1_3);
        b14 = findViewById(R.id.banner_1_4);
        b21 = findViewById(R.id.banner_2_1);
        b22 = findViewById(R.id.banner_2_2);
        b23 = findViewById(R.id.banner_2_3);
        b24 = findViewById(R.id.banner_2_4);

        Uri b11x = Uri.parse(bcashapi.url+"/banners/functions-1.png");
        Picasso.with(this).load(b11x).into(b11);
        Uri b12x = Uri.parse(bcashapi.url+"/banners/functions-2.png");
        Picasso.with(this).load(b12x).into(b12);
        Uri b13x = Uri.parse(bcashapi.url+"/banners/functions-3.png");
        Picasso.with(this).load(b13x).into(b13);
        Uri b14x = Uri.parse(bcashapi.url+"/banners/functions-4.png");
        Picasso.with(this).load(b14x).into(b14);
        Uri b21x = Uri.parse(bcashapi.url+"/banners/ans-1.png");
        Picasso.with(this).load(b21x).into(b21);
        Uri b22x = Uri.parse(bcashapi.url+"/banners/ans-2.png");
        Picasso.with(this).load(b22x).into(b22);
        Uri b23x = Uri.parse(bcashapi.url+"/banners/ans-3.png");
        Picasso.with(this).load(b23x).into(b23);
        Uri b24x = Uri.parse(bcashapi.url+"/banners/ans-4.png");
        Picasso.with(this).load(b24x).into(b24);
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
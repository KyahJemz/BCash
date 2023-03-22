package com.example.bcash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class transactions extends AppCompatActivity implements transaction_interface{

    private Session session;
    private bcash_api bcashapi;
    private RequestQueue queue;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    String type,date,amount,id;

    RecyclerView recyclerView;
    List<records> recordsList = new ArrayList<records>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        session = new Session(transactions.this);
        bcashapi = new bcash_api();
        queue = Volley.newRequestQueue(transactions.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        recyclerView = findViewById(R.id.recyclerview);

        googleVerification();

        getDataFromApi();
    }

    void getDataFromApi(){
        String urlParam = "/transaction.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bcashapi.url+urlParam,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        addData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(transactions.this, "Network Connection Failed, Try Again", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(transactions.this, login.class);
                        startActivity(intent);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("transaction-history-data", "");
                params.put("email", session.getEmail());
                return params;
            }
        };
        queue.add(stringRequest);
    }
    void addData(String data){
        data = data.replace("[", "");
        data = data.replace("]", "");
        data = data.replace("{", "");
        data = data.replace("}", "");
        String[] newData = data.split(",");
        int num = 0;
        for (int i = 0; i<newData.length/5; i++) {

            for (int j = 0; j<5; j++) {
                String[] temp = newData[num].split(":");
                String temp2 = temp[1].replace("\"", "");
                switch (j) {
                    case 0:
                        id = temp2;
                        break;
                    case 1 :
                        date = temp2;
                        break;
                    case 2:
                        type=temp2;
                        break;
                    case 3:
                        amount = temp2;
                        break;
                    case 4:
                        break;
                    default:
                }
                num++;
            }
            Log.d("awts", id+" - "+date+" - "+type+" - "+amount);
            recordsList.add(new records(type,date,amount,id));
        }
       refresher ();
    }


    public void refresher (){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new transaction_adapter(getApplicationContext(),recordsList,this) );
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
                Intent intent = new Intent(transactions.this, login.class);
                startActivity(intent);
            }
        });
    }

    public void moveTo(View view){
        Context cntxt = transactions.this;
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

    @Override
    public void onItemClick(int position, View view) {
        Intent intent = new Intent(transactions.this, transaction_details.class);
        intent.putExtra("transactionId", view.getTag().toString());
        startActivity(intent);
    }
}
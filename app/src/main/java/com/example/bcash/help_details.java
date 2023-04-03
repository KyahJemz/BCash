package com.example.bcash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class help_details extends AppCompatActivity {

    Intent i = getIntent();
    TextView title,text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);
        i = getIntent();

        title = findViewById(R.id.tv_Title);
        text = findViewById(R.id.tv_Text);

        title.setText(i.getStringExtra("title").toString());
        text.setText(i.getStringExtra("text").toString());
    }

    public void back(View view){
        onBackPressed();
    }
}
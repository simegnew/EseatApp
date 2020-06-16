package com.app.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView lblEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblEmail = findViewById(R.id.lblEmailAddress);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userdetails",0);

        lblEmail.setText(sharedPreferences.getString("email","0"));
    }
}
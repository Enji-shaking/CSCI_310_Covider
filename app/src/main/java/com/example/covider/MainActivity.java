package com.example.covider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covider.database.ManagerFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        System.out.println(System.currentTimeMillis());
        ManagerFactory.initialize(getApplicationContext());
    }
}
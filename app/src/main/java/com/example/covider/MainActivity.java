package com.example.covider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.covider.database.ManagerFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        System.out.println(System.currentTimeMillis());
        ManagerFactory.initialize(getApplicationContext());
        ImageButton.OnClickListener bottomNavListener = (View view) -> {
            view.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal_selected)));
            String content = view.getContentDescription().toString();
            if (content.equals("Map")) {
                findViewById(R.id.report).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                findViewById(R.id.profile).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
            } else if (content.equals("Report")) {
                findViewById(R.id.map).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                findViewById(R.id.profile).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
            } else if (content.equals("Profile")) {
                findViewById(R.id.report).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
                findViewById(R.id.map).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardinal)));
            }
        };
        findViewById(R.id.map).setOnClickListener(bottomNavListener);
        findViewById(R.id.report).setOnClickListener(bottomNavListener);
        findViewById(R.id.profile).setOnClickListener(bottomNavListener);
    }
}
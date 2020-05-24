package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import beans.SpinnerBean;
import db.DatabaseManagerSede;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        img.animate().alpha(4000).setDuration(0);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dsp = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(dsp);
                finish();
            }
        },500);


    }
}

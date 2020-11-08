package com.example.draw4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DrawingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_drawing);
        setContentView(new MyView(this));
    }
}
package com.remilefaivre.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent tokenI = getIntent();
        String token = tokenI.getStringExtra("token");
        String id = tokenI.getStringExtra("id");
    }
}
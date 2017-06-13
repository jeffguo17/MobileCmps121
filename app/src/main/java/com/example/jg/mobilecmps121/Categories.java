package com.example.jg.mobilecmps121;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Categories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
    }

    public void phoneClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", "mobilephone");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void laptopClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", "laptops");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void camClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", "cameras");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void tabletClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category", "tablets");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

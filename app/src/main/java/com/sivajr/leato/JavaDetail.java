package com.sivajr.leato;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class JavaDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Java");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(JavaDetail.this, R.color.black));

        TextView title,details,period,lang;
        String key = "";

        title = findViewById(R.id.title);
        details = findViewById(R.id.details);
        period = findViewById(R.id.period);

        Bundle bundle = getIntent().getExtras();

        AlertDialog.Builder builder = new AlertDialog.Builder(JavaDetail.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.dismiss();

        if(bundle != null){

            title.setText(bundle.getString("title"));
            details.setText(bundle.getString("details"));
            period.setText(bundle.getString("period"));

            key = bundle.getString("key");

        }

    }

}
package com.devilcat.leato.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.devilcat.leato.R;

import java.util.Objects;

public class PythonDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python_detail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Python");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(PythonDetail.this, R.color.black));

        TextView title,details,period;

        title = findViewById(R.id.title);
        details = findViewById(R.id.details);
        period = findViewById(R.id.period);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            title.setText(bundle.getString("title"));
            details.setText(bundle.getString("details"));
            period.setText(bundle.getString("period"));

        }

    }

}
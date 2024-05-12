package com.devilcat.leato.views;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.devilcat.leato.R;

import java.util.Objects;

public class Detail extends AppCompatActivity {

    TextView title,details,period,lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.title);
        details = findViewById(R.id.details);
        period = findViewById(R.id.period);
        lang = findViewById(R.id.language);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            title.setText(bundle.getString("title"));
            details.setText(bundle.getString("details"));
            period.setText(bundle.getString("period"));
            lang.setText(bundle.getString("lang"));

        }

    }

}
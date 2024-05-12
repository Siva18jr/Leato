package com.devilcat.leato.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.devilcat.leato.MainActivity;
import com.devilcat.leato.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    TextView version;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        version = findViewById(R.id.version);
        auth = FirebaseAuth.getInstance();

        String v = "1.0";

        new Handler().postDelayed(() -> {

            if (auth.getCurrentUser() == null) {

                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();

            }else{

                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();

            }

        },3000);

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            v = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e){

            e.printStackTrace();

        }

        version.setText("Version " + v);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

}
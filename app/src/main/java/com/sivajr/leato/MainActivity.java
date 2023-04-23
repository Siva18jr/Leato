package com.sivajr.leato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Context context;
    ImageView offline;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        offline = findViewById(R.id.offline);
        auth = FirebaseAuth.getInstance();

        showErrorAlertDialog();

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_java:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new JavaFragment()).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_java);
                AlertDialog dialog = builder.create();
                dialog.show();
                Intent intentJava = new Intent(MainActivity.this, Java.class);
                startActivity(intentJava);
                break;

            case R.id.nav_python:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PythonFragment()).commit();
                AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                build.setCancelable(false);
                build.setView(R.layout.progress_python);
                AlertDialog dialogs = build.create();
                dialogs.show();
                Intent intentPy = new Intent(MainActivity.this, Python.class);
                startActivity(intentPy);
                break;

            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                break;

            case R.id.nav_about:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                Intent url = new Intent();
                url.setAction(Intent.ACTION_VIEW);
                url.addCategory(Intent.CATEGORY_BROWSABLE);
                url.setData(Uri.parse("https://sivajr-3.web.app/"));
                startActivity(url);

            case R.id.nav_logout:
                logout();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    private void showErrorAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.error_dialog,(ConstraintLayout)findViewById(R.id.DialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.title)).setText("Error");
        ((TextView) view.findViewById(R.id.textMessage)).setText("Go to menu and select the language");
        ((Button) view.findViewById(R.id.action)).setText("Okay");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.error);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }

        });

        if (alertDialog.getWindow() != null){

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        }

        alertDialog.show();

    }

//    private void showSuccessAlertDialog(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(MainActivity.this).inflate(
//                R.layout.success_dialog,(ConstraintLayout)findViewById(R.id.DialogContainer)
//        );
//        builder.setView(view);
//        ((TextView) view.findViewById(R.id.title)).setText("Success");
//        ((TextView) view.findViewById(R.id.textMessage)).setText("Successfully Logged Out");
//        ((Button) view.findViewById(R.id.action)).setText("Okay");
//        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.done);
//
//        final AlertDialog alertDialog = builder.create();
//
//        view.findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                alertDialog.dismiss();
//                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
//
//            }
//
//        });
//
//        if (alertDialog.getWindow() != null){
//
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//
//        }
//
//        alertDialog.show();
//
//    }

    private void logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                auth.signOut();
//                showSuccessAlertDialog();
                startActivity(new Intent(MainActivity.this, Login.class));
                dialogInterface.dismiss();
                finish();

            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }

        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }

}
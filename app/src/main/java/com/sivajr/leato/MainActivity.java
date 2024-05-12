package com.devilcat.leato;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devilcat.leato.adapter.HomeAdapter;
import com.devilcat.leato.fragments.HomeFragment;
import com.devilcat.leato.fragments.JavaFragment;
import com.devilcat.leato.fragments.PythonFragment;
import com.devilcat.leato.fragments.ShareFragment;
import com.devilcat.leato.models.HomeDataModel;
import com.devilcat.leato.views.Java;
import com.devilcat.leato.views.Login;
import com.devilcat.leato.views.Python;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ImageView offline;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    List<HomeDataModel> dataList;
    DatabaseReference db;
    SearchView searchView;
    HomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.home_recycler);
        searchView = findViewById(R.id.search);
        offline = findViewById(R.id.offline);
        auth = FirebaseAuth.getInstance();

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new HomeAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference("topics");
        dialog.show();

        db.child("Java").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot snap: snapshot.getChildren()){

                    HomeDataModel dataClass = snap.getValue(HomeDataModel.class);
                    assert dataClass != null;
                    dataClass.setKey(snap.getKey());
                    dataList.add(dataClass);

                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();

            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search(newText);
                return true;

            }

        });

        db.child("Python").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap: snapshot.getChildren()){

                    HomeDataModel dataClass = snap.getValue(HomeDataModel.class);
                    assert dataClass != null;
                    dataClass.setKey(snap.getKey());
                    dataList.add(dataClass);

                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();

            }

        });

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

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    private void logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", (dialogInterface, i) -> {

            auth.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            dialogInterface.dismiss();
            finish();

        });

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void search(String text){

        ArrayList<HomeDataModel> search = new ArrayList<>();

        for (HomeDataModel data : dataList){

            if (data.getDetails().toLowerCase().contains(text.toLowerCase()) || data.getLang().toLowerCase().contains(text.toLowerCase()) || data.getTitle().toLowerCase().contains(text.toLowerCase())){

                search.add(data);

            }

            adapter.searchData(search);

        }

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
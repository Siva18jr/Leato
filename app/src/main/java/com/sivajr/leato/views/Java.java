package com.devilcat.leato.views;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devilcat.leato.adapter.JavaAdapter;
import com.devilcat.leato.models.JavaModel;
import com.devilcat.leato.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Java extends AppCompatActivity {

    RecyclerView recyclerView;
    List<JavaModel> dataList;
    DatabaseReference db;
    SearchView searchView;
    JavaAdapter adapter;
    Button logout, home;
    ImageView offline;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Java");
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(Java.this, R.color.black));

        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search);
        dataList = new ArrayList<>();
        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        offline = findViewById(R.id.offline);
        home = findViewById(R.id.home);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Java.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new JavaAdapter(Java.this, dataList);
        recyclerView.setAdapter(adapter);

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        home.setOnClickListener(view -> {

            Intent intent = new Intent(Java.this, Splash.class);
            startActivity(intent);

        });

        logout.setOnClickListener(v -> logout());

        AlertDialog.Builder builder = new AlertDialog.Builder(Java.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        db = FirebaseDatabase.getInstance().getReference("topics");

        dialog.show();

        db.child("Java").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    JavaModel dataClass = snap.getValue(JavaModel.class);
                    dataList.add(dataClass);

                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Java.this, "error fetching data", Toast.LENGTH_SHORT).show();

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

    }

    public void search(String text){

        ArrayList<JavaModel> search = new ArrayList<>();

        for (JavaModel data : dataList) {

            if (data.getDetails().toLowerCase().contains(text.toLowerCase())) {

                search.add(data);

            }

            adapter.searchData(search);

        }

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
            startActivity(new Intent(Java.this, Login.class));
            dialogInterface.dismiss();
            finish();

        });

        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alert = builder.create();
        alert.show();

    }

}
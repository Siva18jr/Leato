package com.sivajr.leato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Python extends AppCompatActivity {

    RecyclerView recyclerView;
    List<PyConstruct> dataList;
    DatabaseReference db;
    ValueEventListener eventListener;
    SearchView searchView;
    PyAdapter adapter;
    Button logout, home;
    ImageView offline;
    FirebaseAuth auth;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python);

        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search);
        dataList = new ArrayList<>();
        logout = findViewById(R.id.logout);
        auth = FirebaseAuth.getInstance();
        offline = findViewById(R.id.offline);
        home = findViewById(R.id.home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Python");
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(Python.this, R.color.black));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Python.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new PyAdapter(Python.this, dataList);
        recyclerView.setAdapter(adapter);

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Python.this, Splash.class);
                startActivity(intent);

            }

        });

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                logout();

            }

        });

        AlertDialog.Builder builder = new AlertDialog.Builder(Python.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        db = FirebaseDatabase.getInstance().getReference("topics");

        dialog.show();

        eventListener = db.child("Python").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    PyConstruct dataClass = snap.getValue(PyConstruct.class);
                    dataList.add(dataClass);

                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Python.this, "error fetching data", Toast.LENGTH_SHORT).show();

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

        ArrayList<PyConstruct> search = new ArrayList<>();

        for (PyConstruct data : dataList) {

            if (data.getDetails().toLowerCase().contains(text.toLowerCase())) {

                search.add(data);

            }

            adapter.searchData(search);

        }

    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    private void logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                auth.signOut();
//                showSuccessAlertDialog();
                startActivity(new Intent(Python.this, Login.class));
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
        alert.show();;

    }

    @Override
    public void onBackPressed() {

        return;

    }

}
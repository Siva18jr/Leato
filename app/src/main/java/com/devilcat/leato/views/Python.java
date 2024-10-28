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

import com.devilcat.leato.MainActivity;
import com.devilcat.leato.models.PythonModel;
import com.devilcat.leato.R;
import com.devilcat.leato.adapter.PythonAdapter;
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
    List<PythonModel> dataList;
    DatabaseReference db;
    ValueEventListener eventListener;
    SearchView searchView;
    PythonAdapter adapter;
    Button home;
    ImageView offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python);

        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.search);
        dataList = new ArrayList<>();
        offline = findViewById(R.id.offline);
        home = findViewById(R.id.home);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Python");
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(Python.this, R.color.black));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Python.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new PythonAdapter(Python.this, dataList);
        recyclerView.setAdapter(adapter);

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        home.setOnClickListener(view -> {

            Intent intent = new Intent(Python.this, MainActivity.class);
            startActivity(intent);

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

                    PythonModel dataClass = snap.getValue(PythonModel.class);
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

        ArrayList<PythonModel> search = new ArrayList<>();

        for (PythonModel data : dataList) {

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

}
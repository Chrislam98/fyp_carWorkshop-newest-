package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchWorkShopActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    OwnerAdapter owneradapter;
    private ProgressBar progressBar;

    private String ownerId = "";

    DatabaseReference reference, reference2;
    ArrayList<Owner> ownersList;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_work_shop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Shop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchWorkShopActivity.this, UserHomeActivity.class));
            }
        });

        //initialize View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchView = (SearchView) findViewById(R.id.searchShop);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //initialize Value
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ownersList = new ArrayList<Owner>();
        reference = FirebaseDatabase.getInstance().getReference().child("Owners");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Owner_service");


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        Owner ownerDataList = dataSnapshot1.getValue(Owner.class);
                        ownersList.add(ownerDataList);
                    }
                    owneradapter = new OwnerAdapter(ownersList);
                    recyclerView.setAdapter(owneradapter);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchWorkShopActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        if (searchView != null) {
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
    }

    private void search(String str) {
        ArrayList<Owner> ownerList = new ArrayList<>();
        for (Owner object : ownersList) {
            if (object.getWorkshopName().toLowerCase().contains(str.toLowerCase()) || object.getWorkshopName().toUpperCase().contains(str.toUpperCase())) {
                ownerList.add(object);
            } else if (object.getWorkshopAddress().toLowerCase().contains(str.toLowerCase()) || object.getWorkshopAddress().toUpperCase().contains(str.toUpperCase())) {
                ownerList.add(object);
            } else if (object.getAppointmentNum().toLowerCase().contains(str.toLowerCase()) || object.getAppointmentNum().toUpperCase().contains(str.toUpperCase())) {
                ownerList.add(object);
            }
        }
        OwnerAdapter adapter1 = new OwnerAdapter(ownerList);
        recyclerView.setAdapter(adapter1);
    }
}


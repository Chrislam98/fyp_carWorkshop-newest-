package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerListActivity extends AppCompatActivity {

    Query reference;
    RecyclerView recyclerView1;
    //List<Owner> olist;
    private List<Owner> olist;
    OwnerStatusAdapter osAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    SearchView searchOwnerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("owner list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerListActivity.this, AdminHomepageActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();


        recyclerView1 = (RecyclerView)findViewById(R.id.myOwnerRecycler);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        olist = new ArrayList<>();
        osAdapter = new OwnerStatusAdapter(OwnerListActivity.this,olist);
        recyclerView1.setAdapter(osAdapter);
        searchOwnerName= findViewById(R.id.searchOwnerName);

        final Button all = findViewById(R.id.all);
        final Button allow = findViewById(R.id.allow);
        final Button block = findViewById(R.id.block);

        reference = FirebaseDatabase.getInstance().getReference().child("Owners");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                olist.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Owner os = postSnapshot.getValue(Owner.class);
                    os.setOwnerId(postSnapshot.getKey());
                    olist.add(os);
                }
                //osAdapter.notifyDataSetChanged();
                OwnerStatusAdapter ownerStatusAdapter = new OwnerStatusAdapter(OwnerListActivity.this, olist);
                recyclerView1.setAdapter(ownerStatusAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OwnerListActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Owners");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        olist.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Owner os = postSnapshot.getValue(Owner.class);
                                os.setOwnerId(postSnapshot.getKey());
                                olist.add(os);
                            }
                           // osAdapter.notifyDataSetChanged();
                            OwnerStatusAdapter ownerStatusAdapter = new OwnerStatusAdapter(OwnerListActivity.this, olist);
                            recyclerView1.setAdapter(ownerStatusAdapter);
                        } else {
                            Toast.makeText(OwnerListActivity.this,"No Owner",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Owners").orderByChild("status").equalTo("allow");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        olist.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Owner os = postSnapshot.getValue(Owner.class);
                                os.setOwnerId(postSnapshot.getKey());
                                olist.add(os);
                            }
                            //osAdapter.notifyDataSetChanged();
                            OwnerStatusAdapter ownerStatusAdapter = new OwnerStatusAdapter(OwnerListActivity.this, olist);
                            recyclerView1.setAdapter(ownerStatusAdapter);
                        } else {
                            Toast.makeText(OwnerListActivity.this,"No Owner Allow",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Owners").orderByChild("status").equalTo("block");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        olist.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Owner os = postSnapshot.getValue(Owner.class);
                                os.setOwnerId(postSnapshot.getKey());
                                olist.add(os);
                            }
                            //osAdapter.notifyDataSetChanged();
                            OwnerStatusAdapter ownerStatusAdapter = new OwnerStatusAdapter(OwnerListActivity.this, olist);
                            recyclerView1.setAdapter(ownerStatusAdapter);
                        } else {
                            Toast.makeText(OwnerListActivity.this,"No Owner Block",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OwnerListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (searchOwnerName != null) {
            searchOwnerName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void search(String newText) {
        ArrayList<Owner> ownerList = new ArrayList<>();
        for (Owner object : olist) {
            if(object.getUsername().toLowerCase().contains(newText.toLowerCase()) ||
                    object.getUsername().toUpperCase().contains(newText.toUpperCase())){
                ownerList.add(object);
            }
        }
        OwnerStatusAdapter ownerStatusAdapter = new OwnerStatusAdapter(OwnerListActivity.this, ownerList);
        recyclerView1.setAdapter(ownerStatusAdapter);
    }
}
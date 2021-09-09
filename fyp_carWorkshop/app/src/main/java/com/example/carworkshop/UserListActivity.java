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

public class UserListActivity extends AppCompatActivity {

    Query reference;
    RecyclerView recyclerView1;
    //ArrayList<UsersData> ulist;
   private List<UsersData> ulist;
    UserAdapter uAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    SearchView searchUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customer list");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserListActivity.this, AdminHomepageActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView1 = (RecyclerView)findViewById(R.id.myUserRecycler);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        ulist = new ArrayList<UsersData>();
        searchUserName = findViewById(R.id.searchUserName);

        final Button all = findViewById(R.id.all);
        final Button allow = findViewById(R.id.allow);
        final Button block = findViewById(R.id.block);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ulist.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    UsersData ud = dataSnapshot1.getValue(UsersData.class);
                    ulist.add(ud);
                }
                uAdapter = new UserAdapter(UserListActivity.this,ulist);
                recyclerView1.setAdapter(uAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserListActivity.this,"error",Toast.LENGTH_SHORT).show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ulist.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                UsersData ud = dataSnapshot1.getValue(UsersData.class);
                                ulist.add(ud);
                            }
                            //uAdapter.notifyDataSetChanged();
                            UserAdapter userAdapter = new UserAdapter(UserListActivity.this, ulist);
                            recyclerView1.setAdapter(userAdapter);
                        } else {
                            Toast.makeText(UserListActivity.this,"No User",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("status").equalTo("allow");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ulist.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                UsersData ud = dataSnapshot1.getValue(UsersData.class);
                                ulist.add(ud);
                            }
                            //uAdapter.notifyDataSetChanged();
                            UserAdapter userAdapter = new UserAdapter(UserListActivity.this, ulist);
                            recyclerView1.setAdapter(userAdapter);
                        } else {
                            Toast.makeText(UserListActivity.this,"No Customer Allow",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("status").equalTo("block");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ulist.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                UsersData ud = dataSnapshot1.getValue(UsersData.class);
                                ulist.add(ud);
                            }
                            //uAdapter.notifyDataSetChanged();
                            UserAdapter userAdapter = new UserAdapter(UserListActivity.this, ulist);
                            recyclerView1.setAdapter(userAdapter);
                        } else {
                            Toast.makeText(UserListActivity.this,"No Customer Block",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UserListActivity.this,"error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (searchUserName != null) {
            searchUserName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<UsersData> usersData = new ArrayList<>();
        for (UsersData object : ulist) {
            if(object.getUsername().toLowerCase().contains(newText.toLowerCase()) ||
                    object.getUsername().toUpperCase().contains(newText.toUpperCase())){
                usersData.add(object);
            }
        }
        UserAdapter userAdapter = new UserAdapter(UserListActivity.this, usersData);
        recyclerView1.setAdapter(userAdapter);
    }
}
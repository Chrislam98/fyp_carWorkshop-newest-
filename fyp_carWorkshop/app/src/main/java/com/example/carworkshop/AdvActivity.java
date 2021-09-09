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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdvActivity extends AppCompatActivity implements IconAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    //private IconAdapter iconAdapter;
    //private AdvAdapter advAdapter;
    private ImgWorkshopadapter imgWorkshopadapter;
    private ProgressBar advProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDblistener;
    private List<Icon> icons;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("advertisement image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvActivity.this, AddNewAdvActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();


        mRecyclerView = findViewById(R.id.iconrecview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        advProgressCircle = findViewById(R.id.progress_circle4);
        icons = new ArrayList<>();
        imgWorkshopadapter = new ImgWorkshopadapter(AdvActivity.this, icons);
        mRecyclerView.setAdapter(imgWorkshopadapter);
        imgWorkshopadapter.setOnItemClickListener(AdvActivity.this);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("advimg");

        mDblistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //no duplicator
                icons.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Icon icon = postSnapshot.getValue(Icon.class);
                    icon.setiKey(postSnapshot.getKey());
                    icons.add(icon);
                }
                imgWorkshopadapter.notifyDataSetChanged();
                advProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdvActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                advProgressCircle.setVisibility(View.INVISIBLE);

            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,"normal" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this,"what" + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Icon selectedItem = icons.get(position);
        final String selectedkey = selectedItem.getiKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getiUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedkey).removeValue();
                Toast.makeText(AdvActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDblistener);
    }
}
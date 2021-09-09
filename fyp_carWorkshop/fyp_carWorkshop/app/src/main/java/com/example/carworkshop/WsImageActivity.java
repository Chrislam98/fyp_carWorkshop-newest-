package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class WsImageActivity extends AppCompatActivity implements IconAdapter.OnItemClickListener{
    private RecyclerView wRecyclerView;
   // private IconAdapter iconAdapter;
    private ImgWorkshopadapter imgWorkshopadapter;
    private ProgressBar wProgressCircle;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDblistener;
    private List<Icon> icons;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView ownerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws_image);

        ownerID = findViewById(R.id.ownerID2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
        wRecyclerView = findViewById(R.id.iconrecview2);
        wRecyclerView.setHasFixedSize(true);
        wRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wProgressCircle = findViewById(R.id.progress_circle3);
        icons = new ArrayList<>();
        imgWorkshopadapter = new ImgWorkshopadapter(WsImageActivity.this, icons);
        wRecyclerView.setAdapter(imgWorkshopadapter);
        imgWorkshopadapter.setOnItemClickListener(WsImageActivity.this);
        mStorage = FirebaseStorage.getInstance();
        //ori
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("Ws_Img");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Ws_Img").child(firebaseUser.getUid());
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
                wProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(WsImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                wProgressCircle.setVisibility(View.INVISIBLE);

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
                Toast.makeText(WsImageActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDblistener);
    }
}
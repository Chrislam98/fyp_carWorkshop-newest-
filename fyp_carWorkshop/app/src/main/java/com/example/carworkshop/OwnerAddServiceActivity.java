package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerAddServiceActivity extends AppCompatActivity {
    private MaterialEditText serviceName, Price, estTime,serdesc;
    private TextView userName,ownerID;
    private Button addServiceBtn;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
    private UsersData usersData;
    private StorageReference storageReference;
    private CircleImageView circleImageView;
    //private List<ImagesList> imagesList;
    private FirebaseDatabase firebaseDatabase;
    private Spinner spinner;
    private ArrayList<String> arrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_service);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("add service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerAddServiceActivity.this, OwnerHomepageActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        showDataSpinner();

        spinner = findViewById(R.id.catdropdown);

        //ori can run
        ownerID = findViewById(R.id.ownerID);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert  usersData != null;
                ownerID.setText(usersData.getOwnerId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OwnerAddServiceActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        serviceName = findViewById(R.id.servicename);
        Price = findViewById(R.id.price);
        estTime = findViewById(R.id.esttime);
        serdesc = findViewById(R.id.desc);
        addServiceBtn = findViewById(R.id.add);

        Button cancelBtn = findViewById(R.id.cancel);

        progressBar = findViewById(R.id.progressBar);
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String service_name = serviceName.getText().toString();
                final String price = Price.getText().toString();
                final String est_time = estTime.getText().toString();
                final String ownerId = ownerID.getText().toString();
                final String ser_desc = serdesc.getText().toString();
                final String cat = spinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(service_name) ||
                        TextUtils.isEmpty(price) ||
                        TextUtils.isEmpty(est_time) ||
                        TextUtils.isEmpty(ownerId) ||
                        TextUtils.isEmpty(ser_desc)) {
                    Toast.makeText(OwnerAddServiceActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    add(service_name,price,est_time,ownerId,cat,ser_desc);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerAddServiceActivity.this, OwnerHomepageActivity.class));
            }
        });
    }

    private void showDataSpinner() {
        databaseReference1.child("ser_icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    arrayList.add(item.child("iName").getValue(String.class));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(OwnerAddServiceActivity.this,R.layout.style_spinner,arrayList);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void add(String service_name, String price, String est_time, String ownerId, String cat, String ser_desc) {
            progressBar.setVisibility(View.VISIBLE);
            databaseReference = FirebaseDatabase.getInstance().getReference("Owner_service").child(ownerId).child(service_name);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("servicename", service_name);
            hashMap.put("ownerId", ownerId);
            hashMap.put("price", price);
            hashMap.put("time",est_time);
            hashMap.put("category", cat);
            hashMap.put("ser_desc", ser_desc);
            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        Intent intent = new Intent(OwnerAddServiceActivity.this,OwnerHomepageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(OwnerAddServiceActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
}
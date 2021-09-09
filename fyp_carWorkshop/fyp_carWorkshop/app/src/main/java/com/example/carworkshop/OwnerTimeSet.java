package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class OwnerTimeSet extends AppCompatActivity {

    private MaterialEditText serviceName, Price, estTime,serdesc;
    private TextView ownerID;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private UsersData usersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_time_set);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("add service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerTimeSet.this, OwnerHomepageActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

       ownerID = findViewById(R.id.ownerID1);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert  usersData != null;
                ownerID.setText(usersData.getOwnerId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OwnerTimeSet.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        final Spinner suM1 = (Spinner) findViewById(R.id.sum1);
        final Spinner suM2 = (Spinner) findViewById(R.id.sum2);
        final Spinner moM1 = (Spinner) findViewById(R.id.mom1);
        final Spinner moM2 = (Spinner) findViewById(R.id.mom2);
        final Spinner tuM1 = (Spinner) findViewById(R.id.tum1);
        final Spinner tuM2 = (Spinner) findViewById(R.id.tum2);
        final Spinner weM1 = (Spinner) findViewById(R.id.wem1);
        final Spinner weM2 = (Spinner) findViewById(R.id.wem2);
        final Spinner thM1 = (Spinner) findViewById(R.id.thm1);
        final Spinner thM2 = (Spinner) findViewById(R.id.thm2);
        final Spinner frM1 = (Spinner) findViewById(R.id.frm1);
        final Spinner frM2 = (Spinner) findViewById(R.id.frm2);
        final Spinner saM1 = (Spinner) findViewById(R.id.sam1);
        final Spinner saM2 = (Spinner) findViewById(R.id.sam2);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(OwnerTimeSet.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.time1));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suM1.setAdapter(myadapter);
        suM2.setAdapter(myadapter);
        moM1.setAdapter(myadapter);
        moM2.setAdapter(myadapter);
        tuM1.setAdapter(myadapter);
        tuM2.setAdapter(myadapter);
        weM1.setAdapter(myadapter);
        weM2.setAdapter(myadapter);
        thM1.setAdapter(myadapter);
        thM2.setAdapter(myadapter);
        frM1.setAdapter(myadapter);
        frM2.setAdapter(myadapter);
        saM1.setAdapter(myadapter);
        saM2.setAdapter(myadapter);

        final Spinner suN1 = (Spinner) findViewById(R.id.sun1);
        final Spinner suN2 = (Spinner) findViewById(R.id.sun2);
        final Spinner moN1 = (Spinner) findViewById(R.id.mon1);
        final Spinner moN2 = (Spinner) findViewById(R.id.mon2);
        final Spinner tuN1 = (Spinner) findViewById(R.id.tun1);
        final Spinner tuN2 = (Spinner) findViewById(R.id.tun2);
        final Spinner weN1 = (Spinner) findViewById(R.id.wen1);
        final Spinner weN2 = (Spinner) findViewById(R.id.wen2);
        final Spinner thN1 = (Spinner) findViewById(R.id.thn1);
        final Spinner thN2 = (Spinner) findViewById(R.id.thn2);
        final Spinner frN1 = (Spinner) findViewById(R.id.frn1);
        final Spinner frN2 = (Spinner) findViewById(R.id.frn2);
        final Spinner saN1 = (Spinner) findViewById(R.id.san1);
        final Spinner saN2 = (Spinner) findViewById(R.id.san2);
        ArrayAdapter<String> myadapter1 = new ArrayAdapter<String>(OwnerTimeSet.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.time2));
        myadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suN1.setAdapter(myadapter1);
        suN2.setAdapter(myadapter1);
        moN1.setAdapter(myadapter1);
        moN2.setAdapter(myadapter1);
        tuN1.setAdapter(myadapter1);
        tuN2.setAdapter(myadapter1);
        weN1.setAdapter(myadapter1);
        weN2.setAdapter(myadapter1);
        thN1.setAdapter(myadapter1);
        thN2.setAdapter(myadapter1);
        frN1.setAdapter(myadapter1);
        frN2.setAdapter(myadapter1);
        saN1.setAdapter(myadapter1);
        saN2.setAdapter(myadapter1);

        Button cancelBtn = findViewById(R.id.cbtn);
        Button updateBtn = findViewById(R.id.ubtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivity(new Intent(OwnerTimeSet.this,OwnerHomepageActivity.class));
         }
     });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sun1 = suM1.getSelectedItem().toString();
                String sun2 = suM2.getSelectedItem().toString();
                String sun3 = suN1.getSelectedItem().toString();
                String sun4 = suN2.getSelectedItem().toString();
                String mon1 = moM1.getSelectedItem().toString();
                String mon2 = moM2.getSelectedItem().toString();
                String mon3 = moN1.getSelectedItem().toString();
                String mon4 = moN2.getSelectedItem().toString();
                String tue1 = tuM1.getSelectedItem().toString();
                String tue2 = tuM2.getSelectedItem().toString();
                String tue3 = tuN1.getSelectedItem().toString();
                String tue4 = tuN2.getSelectedItem().toString();
                String wed1 = weM1.getSelectedItem().toString();
                String wed2 = weM2.getSelectedItem().toString();
                String wed3 = weN1.getSelectedItem().toString();
                String wed4 = weN2.getSelectedItem().toString();
                String thus1 = thM1.getSelectedItem().toString();
                String thus2 = thM2.getSelectedItem().toString();
                String thus3 = thN1.getSelectedItem().toString();
                String thus4 = thN2.getSelectedItem().toString();
                String fri1 = frM1.getSelectedItem().toString();
                String fri2 = frM2.getSelectedItem().toString();
                String fri3 = frN1.getSelectedItem().toString();
                String fri4 = frN2.getSelectedItem().toString();
                String sat1 = saM1.getSelectedItem().toString();
                String sat2 = saM2.getSelectedItem().toString();
                String sat3 = saN1.getSelectedItem().toString();
                String sat4 = saN2.getSelectedItem().toString();
                final String ownerId = ownerID.getText().toString();

                set(sun1,sun2,sun3,sun4,mon1,mon2,mon3,mon4,tue1,tue2,tue3,tue4,wed1,wed2,wed3,wed4,thus1,thus2,thus3,thus4,fri1,fri2,fri3,fri4,sat1,sat2,sat3,sat4,ownerId);
            }

            private void set(String sun1, String sun2, String sun3, String sun4, String mon1, String mon2, String mon3, String mon4, String tue1, String tue2, String tue3, String tue4, String wed1, String wed2, String wed3, String wed4, String thus1, String thus2, String thus3, String thus4, String fri1, String fri2, String fri3, String fri4, String sat1, String sat2, String sat3, String sat4,String ownerId) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Operation_hrs").child(ownerId);
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("ownerId", ownerId);
                hashMap.put("sunS1", sun1);
                hashMap.put("sunS2", sun2);
                hashMap.put("sunS3", sun3);
                hashMap.put("sunS4", sun4);
                hashMap.put("monS1", mon1);
                hashMap.put("monS2", mon2);
                hashMap.put("monS3", mon3);
                hashMap.put("monS4", mon4);
                hashMap.put("tuesS1", tue1);
                hashMap.put("tuesS2", tue2);
                hashMap.put("tuesS3", tue3);
                hashMap.put("tuesS4", tue4);
                hashMap.put("wedS1", wed1);
                hashMap.put("wedS2", wed2);
                hashMap.put("wedS3", wed3);
                hashMap.put("wedS4", wed4);
                hashMap.put("thusS1", thus1);
                hashMap.put("thusS2", thus2);
                hashMap.put("thusS3", thus3);
                hashMap.put("thusS4", thus4);
                hashMap.put("friS1", fri1);
                hashMap.put("friS2", fri2);
                hashMap.put("friS3", fri3);
                hashMap.put("friS4", fri4);
                hashMap.put("satS1", sat1);
                hashMap.put("satS2", sat2);
                hashMap.put("satS3", sat3);
                hashMap.put("satS4", sat4);
                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(OwnerTimeSet.this,OwnerHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(OwnerTimeSet.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
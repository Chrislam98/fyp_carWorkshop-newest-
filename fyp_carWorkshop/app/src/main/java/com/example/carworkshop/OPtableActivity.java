package com.example.carworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OPtableActivity extends AppCompatActivity {

    TextView sunS1, sunS2, sunS3, sunS4;
    TextView monS1, monS2, monS3, monS4;
    TextView tuesS1, tuesS2, tuesS3, tuesS4;
    TextView wedS1, wedS2, wedS3, wedS4;
    TextView thusS1, thusS2, thusS3, thusS4;
    TextView friS1, friS2, friS3, friS4;
    TextView satS1, satS2, satS3, satS4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_ptable);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Operation hours table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OPtableActivity.this, OwnerHomepageActivity.class));
            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
        DatabaseReference refTime = FirebaseDatabase.getInstance().getReference("Operation_hrs").child(firebaseUser.getUid());

        monS1 = findViewById(R.id.monS1);
        monS2 = findViewById(R.id.monS2);
        monS3 = findViewById(R.id.monS3);
        monS4 = findViewById(R.id.monS4);

        tuesS1 = findViewById(R.id.tuesS1);
        tuesS2 = findViewById(R.id.tuesS2);
        tuesS3 = findViewById(R.id.tuesS3);
        tuesS4 = findViewById(R.id.tuesS4);

        wedS1 = findViewById(R.id.wedS1);
        wedS2 = findViewById(R.id.wedS2);
        wedS3 = findViewById(R.id.wedS3);
        wedS4 = findViewById(R.id.wedS4);

        thusS1 = findViewById(R.id.thusS1);
        thusS2 = findViewById(R.id.thusS2);
        thusS3 = findViewById(R.id.thusS3);
        thusS4 = findViewById(R.id.thusS4);

        friS1 = findViewById(R.id.friS1);
        friS2 = findViewById(R.id.friS2);
        friS3 = findViewById(R.id.friS3);
        friS4 = findViewById(R.id.friS4);

        satS1 = findViewById(R.id.satS1);
        satS2 = findViewById(R.id.satS2);
        satS3 = findViewById(R.id.satS3);
        satS4 = findViewById(R.id.satS4);

        sunS1 = findViewById(R.id.sunS1);
        sunS2 = findViewById(R.id.sunS2);
        sunS3 = findViewById(R.id.sunS3);
        sunS4 = findViewById(R.id.sunS4);

        refTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    OwnerTime ownerTime = dataSnapshot.getValue(OwnerTime.class);

                    if(ownerTime.getMonS1().equals("Rest")){
                        monS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    monS1.setText(ownerTime.getMonS1());
                    if(ownerTime.getMonS2().equals("Rest")){
                        monS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    monS2.setText(ownerTime.getMonS2());
                    if(ownerTime.getMonS3().equals("Rest")){
                        monS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    monS3.setText(ownerTime.getMonS3());
                    if(ownerTime.getMonS4().equals("Rest")){
                        monS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    monS4.setText(ownerTime.getMonS4());

                    if(ownerTime.getTuesS1().equals("Rest")){
                        tuesS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    tuesS1.setText(ownerTime.getTuesS1());
                    if(ownerTime.getTuesS2().equals("Rest")){
                        tuesS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    tuesS2.setText(ownerTime.getTuesS2());
                    if(ownerTime.getTuesS3().equals("Rest")){
                        tuesS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    tuesS3.setText(ownerTime.getTuesS3());
                    if(ownerTime.getTuesS4().equals("Rest")){
                        tuesS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    tuesS4.setText(ownerTime.getTuesS4());

                    if(ownerTime.getWedS1().equals("Rest")){
                        wedS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    wedS1.setText(ownerTime.getWedS1());
                    if(ownerTime.getWedS2().equals("Rest")){
                        wedS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    wedS2.setText(ownerTime.getWedS2());
                    if(ownerTime.getWedS3().equals("Rest")){
                        wedS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    wedS3.setText(ownerTime.getWedS3());
                    if(ownerTime.getWedS4().equals("Rest")){
                        wedS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    wedS4.setText(ownerTime.getWedS4());

                    if(ownerTime.getThusS1().equals("Rest")){
                        thusS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    thusS1.setText(ownerTime.getThusS1());
                    if(ownerTime.getThusS2().equals("Rest")){
                        thusS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    thusS2.setText(ownerTime.getThusS2());
                    if(ownerTime.getThusS3().equals("Rest")){
                        thusS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    thusS3.setText(ownerTime.getThusS3());
                    if(ownerTime.getThusS4().equals("Rest")){
                        thusS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    thusS4.setText(ownerTime.getThusS4());

                    if(ownerTime.getFriS1().equals("Rest")){
                        friS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    friS1.setText(ownerTime.getFriS1());
                    if(ownerTime.getFriS2().equals("Rest")){
                        friS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    friS2.setText(ownerTime.getFriS2());
                    if (ownerTime.getFriS3().equals("Rest")){
                        friS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    friS3.setText(ownerTime.getFriS3());
                    if (ownerTime.getFriS4().equals("Rest")){
                        friS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    friS4.setText(ownerTime.getFriS4());

                    if (ownerTime.getSatS1().equals("Rest")){
                        satS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    satS1.setText(ownerTime.getSatS1());
                    if (ownerTime.getSatS2().equals("Rest")){
                        satS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    satS2.setText(ownerTime.getSatS2());
                    if (ownerTime.getSatS3().equals("Rest")){
                        satS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    satS3.setText(ownerTime.getSatS3());
                    if (ownerTime.getSatS4().equals("Rest")){
                        satS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    satS4.setText(ownerTime.getSatS4());

                    if (ownerTime.getSunS1().equals("Rest")){
                        sunS1.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    sunS1.setText(ownerTime.getSunS1());
                    if (ownerTime.getSunS2().equals("Rest")){
                        sunS2.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    sunS2.setText(ownerTime.getSunS2());
                    if (ownerTime.getSunS3().equals("Rest")){
                        sunS3.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    sunS3.setText(ownerTime.getSunS3());
                    if (ownerTime.getSunS4().equals("Rest")){
                        sunS4.setTextColor(ContextCompat.getColor(OPtableActivity.this, R.color.red));
                    }
                    sunS4.setText(ownerTime.getSunS4());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OPtableActivity.this, "Error Operation Time", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
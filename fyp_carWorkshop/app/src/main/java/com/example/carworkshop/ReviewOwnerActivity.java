package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class ReviewOwnerActivity extends AppCompatActivity {

    TextView ownerId1, workshopName1, workshopAddress1, appointmentNum1, rateCount, numRating, review;
    EditText reviewDescription;
    TextView userName, userMobile, userEmail;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ImageView imageUrl;
    RatingBar ratingBar;
    FirebaseAuth firebaseAuth;
    float rateValue; String temp, numRatingS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_owner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review Shop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewOwnerActivity.this, ReviewOwnerListActivity.class));
            }
        });

        final String ownerId = getIntent().getStringExtra("ownerId");
        final String workshopAddress = getIntent().getStringExtra("workshopAddress");
        final String appointmentNum = getIntent().getStringExtra("appointmentNum");
        final String workshopName = getIntent().getStringExtra("workshopName");

        Button confirmBtn = (Button) findViewById(R.id.confirmReview);
        Button cancelBtn = (Button) findViewById(R.id.cancelReview);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;

        progressBar = findViewById(R.id.progressBar);

        ownerId1 = findViewById(R.id.ownerId);
        imageUrl = (ImageView) findViewById(R.id.ownerImage);
        workshopName1 = (TextView) findViewById(R.id.shopName);
        workshopAddress1 = (TextView) findViewById(R.id.shopAddress);
        appointmentNum1 = (TextView) findViewById(R.id.shopPhone);
        ratingBar = findViewById(R.id.ratingBar);
        rateCount = findViewById(R.id.rateCount);
        reviewDescription = findViewById(R.id.reviewDescription);
        review = findViewById(R.id.review);

        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
        userMobile = findViewById(R.id.userMobile);

        getOwnerDetails(ownerId);
        getUserDetails(firebaseUser.getUid());

        String rating1 = String.valueOf(ratingBar.getRating());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

//                if(rateValue <= 1 && rateValue > 0)
//                    rateCount.setText("Bad" + rateValue + "/5");
//                else if(rateValue <= 2 && rateValue > 1)
//                    rateCount.setText("OK" + rateValue + "/5");
//                else if(rateValue <= 3 && rateValue > 2)
//                    rateCount.setText("Good" + rateValue + "/5");
//                else if(rateValue <= 4 && rateValue > 3)
//                    rateCount.setText("Excellent" + rateValue + "/5");
//                else if(rateValue <= 5 && rateValue > 4)
//                    rateCount.setText("Perfect" + rateValue + "/5");

                if(rateValue <= 1 && rateValue > 0) {
                    rateCount.setText("Bad");
                    numRatingS = String.valueOf(rateValue);
                }
                else if(rateValue <= 2 && rateValue > 1) {
                    rateCount.setText("OK");
                    numRatingS = String.valueOf(rateValue);
                }
                else if(rateValue <= 3 && rateValue > 2){
                    rateCount.setText("Good");
                    numRatingS = String.valueOf(rateValue);
                }
                else if(rateValue <= 4 && rateValue > 3){
                    rateCount.setText("Excellent");
                    numRatingS = String.valueOf(rateValue);
                }
                else if(rateValue <= 5 && rateValue > 4){
                    rateCount.setText("Perfect");
                    numRatingS = String.valueOf(rateValue);
                }
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
                final String reviewDescription1 = reviewDescription.getText().toString();
                final String review1 = review.getText().toString();
                final String ownerId2 = ownerId1.getText().toString();
                final String userName1 = userName.getText().toString();
                final String userPhone = userMobile.getText().toString();
                final String userEmail1 = userEmail.getText().toString();

                temp = rateCount.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Owner_review").child(ownerId2).child(currentDate);
                HashMap<String, String> hashMapOwner = new HashMap<>();
                hashMapOwner.put("reviewId", currentDate);
                hashMapOwner.put("ownerId", ownerId2);
                hashMapOwner.put("userId", firebaseUser.getUid());
                hashMapOwner.put("userName", userName1);
                hashMapOwner.put("userPhone", userPhone);
                hashMapOwner.put("userEmail", userEmail1);
                hashMapOwner.put("rating", numRatingS);
                hashMapOwner.put("description", reviewDescription1);
                hashMapOwner.put("review", temp);
                hashMapOwner.put("workshopName", workshopName);
                hashMapOwner.put("workshopAddress", workshopAddress);
                hashMapOwner.put("appointmentNum", appointmentNum);
                databaseReference.setValue(hashMapOwner).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(ReviewOwnerActivity.this, "Your Review Is Completed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ReviewOwnerActivity.this, ReviewOwnerListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ReviewOwnerActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewOwnerActivity.this, ReviewOwnerListActivity.class));
            }
        });

    }

    private void getUserDetails(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.exists();
                UsersData users = dataSnapshot.getValue(UsersData.class);
                assert users != null;
                userEmail.setText(users.getEmail());
                userName.setText(users.getUsername());
                userMobile.setText(users.getMobile());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getOwnerDetails(String ownerId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owners");
        ref.child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Owner owner = snapshot.getValue(Owner.class);
                    assert owner != null;
                    ownerId1.setText(owner.getOwnerId());
//                    ownerName.setText(owner.getUsername());
//                    email.setText(owner.getEmail());
                    workshopAddress1.setText(owner.getWorkshopAddress());
                    workshopName1.setText(owner.getWorkshopName());
                    appointmentNum1.setText(owner.getAppointmentNum());
                    Picasso.get().load(owner.getImageUrl()).into(imageUrl);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewOwnerActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
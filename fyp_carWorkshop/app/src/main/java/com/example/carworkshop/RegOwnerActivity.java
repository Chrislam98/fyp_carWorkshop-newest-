package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegOwnerActivity extends AppCompatActivity {
    private MaterialEditText userName, emailAddress, password, mobile, oRole, workshopname, workshopadds, workshopphone;
    private Button registerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String status = "allow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_owner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //need to change desitination to admin home page
                startActivity(new Intent(RegOwnerActivity.this, AdminHomepageActivity.class));
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.username);
        oRole = findViewById(R.id.owner);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mobile = findViewById(R.id.mobile);
        workshopname = findViewById(R.id.wsname);
        workshopadds = findViewById(R.id.wssdd);
        workshopphone = findViewById(R.id.wsphone);
        registerBtn = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = userName.getText().toString();
                final String owner_role = oRole.getText().toString();
                final String email = emailAddress.getText().toString();
                final String txt_password = password.getText().toString();
                final String txt_mobile = mobile.getText().toString();
                final String workshopName = workshopname.getText().toString();
                final String workshopAddress = workshopadds.getText().toString();
                final String workshopph = workshopphone.getText().toString();

                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(owner_role) || TextUtils.isEmpty(email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_mobile) || TextUtils.isEmpty(workshopName) || TextUtils.isEmpty(workshopAddress) || TextUtils.isEmpty(workshopph)) {
                    Toast.makeText(RegOwnerActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    register(user_name, owner_role, email, txt_password, txt_mobile, workshopName, workshopAddress, workshopph);
                }
            }
        });
    }

    private void register(final String user_name, final String owner_role, final String email, String txt_password, final String txt_mobile, final String workshopName, final String workshopAddress,final String workshopph) {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser rUser = firebaseAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(userId);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("ownerId", userId);
                    hashMap.put("username", user_name);
                    hashMap.put("role", owner_role);
                    hashMap.put("status", status);
                    hashMap.put("email", email);
                    hashMap.put("mobile", txt_mobile);
                    hashMap.put("workshopName", workshopName);
                    hashMap.put("workshopAddress", workshopAddress);
                    hashMap.put("appointmentNum", workshopph);
                    hashMap.put("imageUrl", "default");
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                //need to change the destination to admin home page
                                Intent intent = new Intent(RegOwnerActivity.this, AdminHomepageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegOwnerActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegOwnerActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
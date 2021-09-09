package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHomepageActivity extends AppCompatActivity {

    Button regAdmin, regOwner, userList, ownerList, addCat, changePassword, logOut, adv1;
    UsersData admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        regAdmin = findViewById(R.id.regAdmin);
        regOwner = findViewById(R.id.regOwner);
        userList = findViewById(R.id.userList);
        ownerList = findViewById(R.id.ownerList);
        addCat = findViewById(R.id.addCategory);
        changePassword = findViewById(R.id.changePassword);
        logOut = findViewById(R.id.logOut);
        adv1 = findViewById(R.id.adv);


        regAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,RegAdminActivity.class));
            }
        });

        regOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,RegOwnerActivity.class));
            }
        });
        userList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,UserListActivity.class));
            }
        });
        ownerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,OwnerListActivity.class));
            }
        });
        addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,NewCatActivity.class));
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,ChangeAdminPasswordActivity.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,MainActivity.class));
            }
        });
        adv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomepageActivity.this,AddNewAdvActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.userProfile) {
            startActivity(new Intent(AdminHomepageActivity.this,AdminHomeActivity.class));
        }
        return true;
    };

}
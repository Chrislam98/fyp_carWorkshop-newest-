package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    MaterialEditText username, email, mobile;
    TextView gender;
    RadioGroup radioGroup;
    RadioButton male, female;
    Button update;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, StartActivity.class));
            }
        });

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.radioButton);

        gender = findViewById(R.id.gender);

        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        update = findViewById(R.id.updateProfile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        getUserDetails(username, email, mobile, gender);

//        male.setChecked(true);
//        female.setChecked(true);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selected_gender = radioGroup.findViewById(checkedId);

                if(selected_gender == null){
                    Toast.makeText(EditProfileActivity.this,"Select gender please", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(Objects.requireNonNull(username.getText()).toString())) {
                    Toast.makeText(EditProfileActivity.this,"Please Fill Your User Name", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(Objects.requireNonNull(email.getText()).toString())) {
                    Toast.makeText(EditProfileActivity.this,"Please Fill Your Email", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(Objects.requireNonNull(mobile.getText()).toString())) {
                    Toast.makeText(EditProfileActivity.this,"Please Fill Your Mobile", Toast.LENGTH_SHORT).show();
                } else {
                    final String gender = selected_gender.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", username.getText().toString());
                    map.put("email", email.getText().toString());
                    map.put("mobile", mobile.getText().toString());
                    map.put("gender", gender);

                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).updateChildren(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditProfileActivity.this, "Your Profile Is Updated", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(EditProfileActivity.this, StartActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, "Please Required all field", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }


    private void getUserDetails(final MaterialEditText username, final MaterialEditText email, final MaterialEditText mobile, final TextView gender) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UsersData user = dataSnapshot.getValue(UsersData.class);
                assert user != null;
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                mobile.setText(user.getMobile());
                gender.setText(user.getGender());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
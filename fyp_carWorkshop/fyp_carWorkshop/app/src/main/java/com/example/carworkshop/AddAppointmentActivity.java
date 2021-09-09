package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.DataFormatException;

public class AddAppointmentActivity extends AppCompatActivity {

    TextView ownerId1, workshopName1, workshopAddress1, appointmentNum1, ownerName, email, appointmentDate, appointmentTime;
    TextView userName, userMobile, userEmail;
    TextView carBrand, carPlateNum, orderNumId;
    ImageButton btnMap, btnMapName;
    ProgressBar progressBar;
    ImageView imageUrl;
    RecyclerView ownerServices, carList;
    ArrayList<Owner_service> ownerServiceList;
    ArrayList<Appointment> appointments;
    ArrayList<Vehicle> vehicleArrayList;
    OwnerServiceAdapter ownerServiceAdapter;
    VehicleSelectAdapter vehicleSelectAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAppointmentActivity.this, UserHomeActivity.class));
            }
        });

        final String ownerId = getIntent().getStringExtra("ownerId");
        final String workshopAddress = getIntent().getStringExtra("workshopAddress");
        final String appointmentNum = getIntent().getStringExtra("appointmentNum");
        final String workshopName = getIntent().getStringExtra("workshopName");

        //Button
        Button confirmBtn = (Button) findViewById(R.id.confirm);
        Button cancelBtn = (Button) findViewById(R.id.cancel);
        Button callBtn = findViewById(R.id.call);
        //Owner Details
        ownerId1 = findViewById(R.id.ownerId);
        imageUrl = (ImageView) findViewById(R.id.ownerImage);
        workshopName1 = (TextView) findViewById(R.id.shopName);
        workshopAddress1 = (TextView) findViewById(R.id.shopAddress);
        appointmentNum1 = (TextView) findViewById(R.id.shopPhone);
        ownerName = (TextView) findViewById(R.id.ownerName);
        email = (TextView) findViewById(R.id.shopEmail);
        btnMap = findViewById(R.id.btnMap);
        btnMapName = findViewById(R.id.btnMapName);
        progressBar = findViewById(R.id.progressBar);

        // User Details
        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);
        userMobile = findViewById(R.id.userMobile);

        carBrand = (TextView) findViewById(R.id.carBrand1);
        carPlateNum = (TextView) findViewById(R.id.carPlateNum1);
        orderNumId = (TextView) findViewById(R.id.orderNumId1);

        //Get current User Id
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Appointment Time and Date
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentTime = findViewById(R.id.appointmentTime);

        //Important ownerService
        ownerServices = findViewById(R.id.ownerServices);
        ownerServices.setLayoutManager(new LinearLayoutManager(this));

        ownerServiceList = new ArrayList<Owner_service>();
        appointments = new ArrayList<Appointment>();
        vehicleArrayList = new ArrayList<Vehicle>();

        //Get Details and Service Of Owner
        getOwnerDetails(ownerId);
        getOwnerService(ownerId);
        assert firebaseUser != null;

        getUserDetails(firebaseUser.getUid());
        getVehicleDetails(firebaseUser.getUid());


        btnMapName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopName = workshopName1.getText().toString();
                Uri uri = Uri.parse("geo:0, 0?q=" + shopName);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopAddress = workshopAddress1.getText().toString();
                Uri uri = Uri.parse("geo:0, 0?q=" + shopAddress);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    final String userId = firebaseUser.getUid();
                    final String ownerId2 = ownerId1.getText().toString();
                    final String userName1 = userName.getText().toString();
                    final String userPhone = userMobile.getText().toString();
                    final String appointmentTime1 = appointmentTime.getText().toString();
                    final String appointmentDate1 = appointmentDate.getText().toString();
                    final String ownerName1 = ownerName.getText().toString();

                    final String orderNumId1 = orderNumId.getText().toString();
                    final String carBrand1 = carBrand.getText().toString();
                    final String carPlateNum1 = carPlateNum.getText().toString();

                    if (TextUtils.isEmpty(appointmentTime1) ||
                            TextUtils.isEmpty(appointmentDate1)) {
                            Toast.makeText(AddAppointmentActivity.this, "Please Fill Your Appointment Time and Date", Toast.LENGTH_SHORT).show();
                            } else {
                         databaseReference = FirebaseDatabase.getInstance().getReference("Users_appointment")
                                 .child(userId).child(orderNumId1);
                         HashMap<String, Object> hashMap = new HashMap<>();
                         hashMap.put("orderNumId", orderNumId1);
                         hashMap.put("userId", firebaseUser.getUid());
                         hashMap.put("username", userName1);
                         hashMap.put("userPhone", userPhone);
                         hashMap.put("ownerId", ownerId);
                         hashMap.put("ownerName", ownerName1);
                         hashMap.put("appointmentTime", appointmentTime1);
                         hashMap.put("appointmentDate", appointmentDate1);
                         hashMap.put("workshopName", workshopName);
                         hashMap.put("workshopAddress", workshopAddress);
                         hashMap.put("appointmentNum", appointmentNum);
                         hashMap.put("status","Pending");
                        progressBar.setVisibility(View.GONE);

                        databaseReference.updateChildren(hashMap);

                        databaseReference = FirebaseDatabase.getInstance().getReference("Owner_appointment")
                                .child(ownerId2).child(orderNumId1);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("orderNumId", orderNumId1);
                        map.put("userId", firebaseUser.getUid());
                        map.put("username", userName1);
                        map.put("userPhone", userPhone);
                        map.put("ownerId", ownerId);
                        map.put("ownerName", ownerName1);
                        map.put("carBrand", carBrand1);
                        map.put("carPlateNum", carPlateNum1);
                        map.put("appointmentTime", appointmentTime1);
                        map.put("appointmentDate", appointmentDate1);
                        map.put("workshopName", workshopName);
                        map.put("workshopAddress", workshopAddress);
                        map.put("appointmentNum", appointmentNum);
                        map.put("status","Pending");
                        progressBar.setVisibility(View.GONE);
                        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddAppointmentActivity.this, "Your Appointment Is Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddAppointmentActivity.this, UserHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(AddAppointmentActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                     }
                }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ownerId2 = ownerId1.getText().toString();
                final String orderNumId1 = orderNumId.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Users_appointment")
                    .child(firebaseUser.getUid()).child(orderNumId1);
                databaseReference.removeValue();

                databaseReference = FirebaseDatabase.getInstance().getReference("Owner_appointment")
                        .child(ownerId2).child(orderNumId1);
                databaseReference.removeValue();


                startActivity(new Intent(AddAppointmentActivity.this, UserHomeActivity.class));
            }
        });

        //Add Appointment Date
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int YEAR = calendar.get(Calendar.YEAR);
                int MONTH = calendar.get(Calendar.MONTH);
                int DATE = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month);
                        calendar1.set(Calendar.DATE, date);
                        String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();
                        appointmentDate.setText(dateText);
                    }
                    }, YEAR, MONTH, DATE);
                datePickerDialog.show();
            }
        });

        //Add Appointment Time
        appointmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int HOUR = calendar.get(Calendar.HOUR);
                int MINUTE = calendar.get(Calendar.MINUTE);
                boolean is24HourFormat = DateFormat.is24HourFormat(AddAppointmentActivity.this);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR, hour);
                        calendar1.set(Calendar.MINUTE, minute);
                        String dateText = DateFormat.format("hh:mm aa", calendar1).toString();
                        appointmentTime.setText(dateText);

                    }
                }, HOUR, MINUTE, is24HourFormat);

                timePickerDialog.show();
            }
        });


        //Call Context
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + appointmentNum));
                startActivity(intent);
            }
        });
    }


    private void getVehicleDetails(String uid) {
        final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(uid).child(currentDate);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                carBrand.setText(appointment.getCarBrand());
                carPlateNum.setText(appointment.getCarPlateNum());
                orderNumId.setText(appointment.getOrderNumId());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddAppointmentActivity.this, "error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddAppointmentActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOwnerService(final String ownerId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owner_service");
        ref.child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Owner_service os = dataSnapshot1.getValue(Owner_service.class);
                    ownerServiceList.add(os);
                }
                ownerServiceAdapter = new OwnerServiceAdapter(AddAppointmentActivity.this, ownerServiceList);
                ownerServices.setAdapter(ownerServiceAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddAppointmentActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOwnerDetails(final String ownerId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Owners");
        ref.child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Owner owner = snapshot.getValue(Owner.class);
                    assert owner != null;
                    ownerId1.setText(owner.getOwnerId());
                    ownerName.setText(owner.getUsername());
                    email.setText(owner.getEmail());
                    workshopAddress1.setText(owner.getWorkshopAddress());
                    workshopName1.setText(owner.getWorkshopName());
                    appointmentNum1.setText(owner.getAppointmentNum());
                    Picasso.get().load(owner.getImageUrl()).into(imageUrl);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddAppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
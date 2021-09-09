package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

    TextView sunS1, sunS2, sunS3, sunS4;
    TextView monS1, monS2, monS3, monS4;
    TextView tuesS1, tuesS2, tuesS3, tuesS4;
    TextView wedS1, wedS2, wedS3, wedS4;
    TextView thusS1, thusS2, thusS3, thusS4;
    TextView friS1, friS2, friS3, friS4;
    TextView satS1, satS2, satS3, satS4;

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
        final String orderNumId2 = getIntent().getStringExtra("orderNumId");

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

        // Service Time
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
        getOwnerTime(ownerId);
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
                    final String ownerEmail1 = email.getText().toString();

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
                         hashMap.put("ownerEmail", ownerEmail1);
                         hashMap.put("appointmentTime", appointmentTime1);
                         hashMap.put("appointmentDate", appointmentDate1);
                         hashMap.put("workshopName", workshopName);
                         hashMap.put("workshopAddress", workshopAddress);
                         hashMap.put("appointmentNum", appointmentNum);
                         hashMap.put("status","pending");
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
                        map.put("ownerEmail", ownerEmail1);
                        map.put("carBrand", carBrand1);
                        map.put("carPlateNum", carPlateNum1);
                        map.put("appointmentTime", appointmentTime1);
                        map.put("appointmentDate", appointmentDate1);
                        map.put("workshopName", workshopName);
                        map.put("workshopAddress", workshopAddress);
                        map.put("appointmentNum", appointmentNum);
                        map.put("status","pending");
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
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(AddAppointmentActivity.this, UserHomeActivity.class));
                    }
                });
            AddAppointmentActivity.this.notifyAll();
            ownerServiceAdapter.notifyDataSetChanged();
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

    private void getOwnerTime(String ownerId1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Operation_hrs");
        ref.child(ownerId1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    OwnerTime ownerTime = dataSnapshot.getValue(OwnerTime.class);

                    if(ownerTime.getMonS1().equals("Rest")){
                        monS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    monS1.setText(ownerTime.getMonS1());
                    if(ownerTime.getMonS2().equals("Rest")){
                        monS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    monS2.setText(ownerTime.getMonS2());
                    if(ownerTime.getMonS3().equals("Rest")){
                        monS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    monS3.setText(ownerTime.getMonS3());
                    if(ownerTime.getMonS4().equals("Rest")){
                        monS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    monS4.setText(ownerTime.getMonS4());

                    if(ownerTime.getTuesS1().equals("Rest")){
                        tuesS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    tuesS1.setText(ownerTime.getTuesS1());
                    if(ownerTime.getTuesS2().equals("Rest")){
                        tuesS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    tuesS2.setText(ownerTime.getTuesS2());
                    if(ownerTime.getTuesS3().equals("Rest")){
                        tuesS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    tuesS3.setText(ownerTime.getTuesS3());
                    if(ownerTime.getTuesS4().equals("Rest")){
                        tuesS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    tuesS4.setText(ownerTime.getTuesS4());

                    if(ownerTime.getWedS1().equals("Rest")){
                        wedS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    wedS1.setText(ownerTime.getWedS1());
                    if(ownerTime.getWedS2().equals("Rest")){
                        wedS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    wedS2.setText(ownerTime.getWedS2());
                    if(ownerTime.getWedS3().equals("Rest")){
                        wedS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    wedS3.setText(ownerTime.getWedS3());
                    if(ownerTime.getWedS4().equals("Rest")){
                        wedS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    wedS4.setText(ownerTime.getWedS4());

                    if(ownerTime.getThusS1().equals("Rest")){
                        thusS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    thusS1.setText(ownerTime.getThusS1());
                    if(ownerTime.getThusS2().equals("Rest")){
                        thusS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    thusS2.setText(ownerTime.getThusS2());
                    if(ownerTime.getThusS3().equals("Rest")){
                        thusS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    thusS3.setText(ownerTime.getThusS3());
                    if(ownerTime.getThusS4().equals("Rest")){
                        thusS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    thusS4.setText(ownerTime.getThusS4());

                    if(ownerTime.getFriS1().equals("Rest")){
                        friS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    friS1.setText(ownerTime.getFriS1());
                    if(ownerTime.getFriS2().equals("Rest")){
                        friS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    friS2.setText(ownerTime.getFriS2());
                    if (ownerTime.getFriS3().equals("Rest")){
                        friS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    friS3.setText(ownerTime.getFriS3());
                    if (ownerTime.getFriS4().equals("Rest")){
                        friS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    friS4.setText(ownerTime.getFriS4());

                    if (ownerTime.getSatS1().equals("Rest")){
                        satS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    satS1.setText(ownerTime.getSatS1());
                    if (ownerTime.getSatS2().equals("Rest")){
                        satS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    satS2.setText(ownerTime.getSatS2());
                    if (ownerTime.getSatS3().equals("Rest")){
                        satS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    satS3.setText(ownerTime.getSatS3());
                    if (ownerTime.getSatS4().equals("Rest")){
                        satS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    satS4.setText(ownerTime.getSatS4());

                    if (ownerTime.getSunS1().equals("Rest")){
                        sunS1.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    sunS1.setText(ownerTime.getSunS1());
                    if (ownerTime.getSunS2().equals("Rest")){
                        sunS2.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    sunS2.setText(ownerTime.getSunS2());
                    if (ownerTime.getSunS3().equals("Rest")){
                        sunS3.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    sunS3.setText(ownerTime.getSunS3());
                    if (ownerTime.getSunS4().equals("Rest")){
                        sunS4.setTextColor(ContextCompat.getColor(AddAppointmentActivity.this, R.color.red));
                    }
                    sunS4.setText(ownerTime.getSunS4());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddAppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getVehicleDetails(String uid) {
//        final String currentDate = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(new Date());
        final String orderNumId2 = getIntent().getStringExtra("orderNumId");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users_appointment").child(uid).child(orderNumId2);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                assert appointment != null;
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
                ownerServiceAdapter = new OwnerServiceAdapter(AddAppointmentActivity.this, ownerServiceList, getIntent().getStringExtra("orderNumId"));
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
package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnerHomeActivity extends AppCompatActivity {
    private TextView userName;
    private CircleImageView circleImageView;
    private FirebaseUser firebaseUser;
    private OwnerimgrecAdapter ownerimgrecAdapter;
    private DatabaseReference databaseReference;
    private List<ImagesList> imagesList;
    private static final int IMAGE_REQUEST = 1;
    private StorageTask storageTask;
    private Uri imageUri;
    private StorageReference storageReference;
    private UsersData usersData;
    private Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_home);

        final MaterialEditText ownerName = findViewById(R.id.ownerName);
        final MaterialEditText ownerMobile = findViewById(R.id.ownerMobile);
        final MaterialEditText ownerEmail = findViewById(R.id.ownerEmail);
        final MaterialEditText workshopAddress = findViewById(R.id.workshopAddress);
        final MaterialEditText workshopName = findViewById(R.id.workshopName);

        final Button update = findViewById(R.id.update);
        final Button cancel = findViewById(R.id.cancel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomeActivity.this, OwnerHomepageActivity.class));
            }
        });
        imagesList = new ArrayList<>();
        userName = findViewById(R.id.username);

        circleImageView = findViewById(R.id.profileImage);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //which file
        databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Owners_profile_images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersData = dataSnapshot.getValue(UsersData.class);
                assert usersData != null;
                userName.setText(usersData.getUsername());

                //Certification();

                if (usersData.getImageURL().equals("default")) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background);
                } else {
                    Glide.with(getApplicationContext()).load(usersData.getImageURL()).into(circleImageView);
                }

                owner = dataSnapshot.getValue(Owner.class);
                assert owner != null;
                ownerName.setText(owner.getUsername());
                ownerMobile.setText(owner.getMobile());
                ownerEmail.setText(owner.getEmail());
                workshopAddress.setText(owner.getWorkshopAddress());
                workshopName.setText(owner.getWorkshopName());

            }
//                addservice.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OwnerHomeActivity.this, OwnerAddServiceActivity.class));
//                    }
//                });
//                servicelist.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OwnerHomeActivity.this, ListServiceActivity.class));
//                    }
//                });
//                timeSet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OwnerHomeActivity.this, OwnerTimeSet.class));
//                    }
//                });
//                uploadWsI.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OwnerHomeActivity.this, WorkshopImgActivity.class));
//                    }
//                });
//                  editProfile.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(OwnerHomeActivity.this, EditOwnerProfileActivity.class));
//                    }
//                });

//            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OwnerHomeActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ownerName1 = ownerName.getText().toString();
                final String ownerEmail1 = ownerEmail.getText().toString();
                final String ownerMobile1 = ownerMobile.getText().toString();
                final String workshopAddress1 = workshopAddress.getText().toString();
                final String workshopName1 = workshopName.getText().toString();

               if (TextUtils.isEmpty(ownerName1)) {
                   Toast.makeText(OwnerHomeActivity.this, "Please Fill Your Owner Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(ownerEmail1)) {
                   Toast.makeText(OwnerHomeActivity.this, "Please Fill Owner Email", Toast.LENGTH_LONG).show();
               } else if (TextUtils.isEmpty(ownerMobile1)) {
                   Toast.makeText(OwnerHomeActivity.this, "Please Fill Owner Mobile", Toast.LENGTH_LONG).show();
               } else if (TextUtils.isEmpty(workshopAddress1)) {
                   Toast.makeText(OwnerHomeActivity.this, "Please Fill WorkShop Address", Toast.LENGTH_LONG).show();
               } else if (TextUtils.isEmpty(workshopName1)) {
                   Toast.makeText(OwnerHomeActivity.this, "Please Fill WorkShop Name", Toast.LENGTH_LONG).show();
               } else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Owners").child(firebaseUser.getUid());
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", ownerName1);
                    map.put("email", ownerEmail1);
                    map.put("mobile", ownerMobile1);
                    map.put("appointmentNum", ownerMobile1);
                    map.put("workshopAddress", workshopAddress1);
                    map.put("workshopName", workshopName1);

                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(OwnerHomeActivity.this, "Your Shop Profile Is Updated", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(OwnerHomeActivity.this, OwnerHomepageActivity.class));
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerHomeActivity.this, OwnerHomepageActivity.class));
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OwnerHomeActivity.this);
                builder.setCancelable(true);
                View mView = LayoutInflater.from(OwnerHomeActivity.this).inflate(R.layout.select_image_layout,null);
                RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
                collectOldImages();
                recyclerView.setLayoutManager(new GridLayoutManager(OwnerHomeActivity.this,3));
                recyclerView.setHasFixedSize(true);
                ownerimgrecAdapter = new OwnerimgrecAdapter(imagesList,OwnerHomeActivity.this);
                recyclerView.setAdapter(ownerimgrecAdapter);
                ownerimgrecAdapter.notifyDataSetChanged();
                //has some different
                final Button openImage = mView.findViewById(R.id.openImages);
                openImage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        openImage();
                    }
                });
                builder.setView(mView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (storageTask != null && storageTask.isInProgress()) {
                Toast.makeText(this, "Uploading is in process", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
    //importtant
    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image");
        progressDialog.show();
        if (imageUri != null){
            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
            byte[] imageFileToByte = byteArrayOutputStream.toByteArray();
            final StorageReference imageReference = storageReference.child(usersData.getUsername()+System.currentTimeMillis()+".jpg");
            storageTask = imageReference.putBytes(imageFileToByte);
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downLoadUri = task.getResult();
                        String sDownloadUri = downLoadUri.toString();
                        Map<String,Object>hashMap = new HashMap<>();
                        hashMap.put("imageUrl",sDownloadUri);
                        databaseReference.updateChildren(hashMap);
                        final DatabaseReference profileImagesReference = FirebaseDatabase.getInstance().getReference("Owners_profile_images").child(firebaseUser.getUid());
                        profileImagesReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(OwnerHomeActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OwnerHomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
    //menu start
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.profile_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.changePsw){
//            startActivity(new Intent(OwnerHomeActivity.this,ChangePasswordActivity.class));
//        }
//        else if(id == R.id.logout){
//            firebaseAuth.signOut();
//            startActivity(new Intent(OwnerHomeActivity.this,MainActivity.class));
//            finish();
//        }
//        return true;
//    }
    //menu end

    private void collectOldImages() {
        final DatabaseReference imageListReference = FirebaseDatabase.getInstance().getReference("Owners_profile_images").child(firebaseUser.getUid());
        imageListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imagesList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    imagesList.add(snapshot.getValue(ImagesList.class));
                }
                ownerimgrecAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OwnerHomeActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
package com.example.carworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class WorkshopImgActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button wBtnChooseImg;
    private Button wBtnUpload;
    private TextView wShowUpload;
    private ImageView wImageView;
    private EditText wName;
    private ProgressBar wProBar;
    private TextView ownerID;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private UsersData usersData;
    private Uri wImageUri;

    private StorageReference wStorageRef;
    private DatabaseReference wDatabaseRef;

    private StorageTask wUploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_img);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("upload workshop image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkshopImgActivity.this,OwnerHomepageActivity.class));
            }
        });

        ownerID = findViewById(R.id.ownerID2);
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
                Toast.makeText(WorkshopImgActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        wBtnChooseImg = findViewById(R.id.btn_ws_img);
        wBtnUpload = findViewById(R.id.btn_upload_ws);
        wShowUpload = findViewById(R.id.show_upload_ws);
        wName = findViewById(R.id.wsImageName);
        wImageView = findViewById(R.id.image_view_icon_ws);
        wProBar = findViewById(R.id.h_progress_bar_ws);

        //storage
        wStorageRef = FirebaseStorage.getInstance().getReference("Ws_Img");
        //realtimedatabse
        wDatabaseRef = FirebaseDatabase.getInstance().getReference("Ws_Img");
        //wDatabaseRef = FirebaseDatabase.getInstance().getReference("Ws_Img").child(ownerID.toString());
        wBtnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        wBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wUploadtask != null && wUploadtask.isInProgress()){
                    Toast.makeText(WorkshopImgActivity.this, "image Uploading", Toast.LENGTH_SHORT).show();
                }else {
                    iconUpload();
                }
            }
        });

        wShowUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconView();
            }
        });
    }
    private void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        //NewCatActivityForResult(intent, PICK_IMAGE_REQUEST);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            wImageUri = data.getData();

            Picasso.get().load(wImageUri).into(wImageView);

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void iconUpload(){
        //need check the text has also no null
        if(wImageUri != null){
            StorageReference fileReference = wStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(wImageUri));


            wUploadtask = fileReference.putFile(wImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wProBar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(WorkshopImgActivity.this, "Upload Success", Toast.LENGTH_LONG).show();

                    //icon class
                    //line 127 may has error 'getUploadSessionUri'
                    //Icon icon  = new Icon(mName.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString());
                    Icon icon  = new Icon(wName.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                    String uploadId = wDatabaseRef.push().getKey();
                    wDatabaseRef.child(firebaseUser.getUid()).child(uploadId).setValue(icon);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(WorkshopImgActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            wProBar.setProgress((int)progress);
                        }
                    });
        }else{
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void iconView(){
        Intent intent = new Intent(this,WsImageActivity.class);
        startActivity(intent);
    }
}
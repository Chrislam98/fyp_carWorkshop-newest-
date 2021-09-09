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

public class AddNewAdvActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button advBtnChooseImg;
    private Button advBtnUpload;
    private TextView advShowUpload;
    private ImageView advImageView;
    private EditText advName;
    private ProgressBar advProBar;
    private TextView ownerID;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private UsersData usersData;
    private Uri advImageUri;

    private StorageReference advStorageRef;
    private DatabaseReference advDatabaseRef;

    private StorageTask advUploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_adv);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("upload Advertisement image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddNewAdvActivity.this,AdminHomepageActivity.class));
            }
        });

        ownerID = findViewById(R.id.ownerID2);
        firebaseAuth = FirebaseAuth.getInstance();


        advBtnChooseImg = findViewById(R.id.btn_adv_img);
        advBtnUpload = findViewById(R.id.btn_upload_adv);
        advShowUpload = findViewById(R.id.show_upload_adv);
        advName = findViewById(R.id.advImageName);
        advImageView = findViewById(R.id.image_view_icon_adv);
        advProBar = findViewById(R.id.h_progress_bar_adv);

        //storage
        advStorageRef = FirebaseStorage.getInstance().getReference("advimg");
        //realtimedatabse
        advDatabaseRef = FirebaseDatabase.getInstance().getReference("advimg");

        advBtnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        advBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advUploadtask != null && advUploadtask.isInProgress()){
                    Toast.makeText(AddNewAdvActivity.this, "icon Uploading", Toast.LENGTH_SHORT).show();
                }else {
                    iconUpload();
                }
            }
        });

        advShowUpload.setOnClickListener(new View.OnClickListener() {
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
            advImageUri = data.getData();

            Picasso.get().load(advImageUri).into(advImageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void iconUpload(){
        //need check the text has also no null
        if(advImageUri != null){
            StorageReference fileReference = advStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(advImageUri));


            advUploadtask = fileReference.putFile(advImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            advProBar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(AddNewAdvActivity.this, "Upload Success", Toast.LENGTH_LONG).show();

                    //icon class
                    //line 127 may has error 'getUploadSessionUri'
                    //Icon icon  = new Icon(mName.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString());
                    Icon icon  = new Icon(advName.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                    String uploadId = advDatabaseRef.push().getKey();
                    advDatabaseRef.child(uploadId).setValue(icon);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewAdvActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            advProBar.setProgress((int)progress);
                        }
                    });
        }else{
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void iconView(){
        Intent intent = new Intent(this,AdvActivity.class);
        startActivity(intent);
    }
}
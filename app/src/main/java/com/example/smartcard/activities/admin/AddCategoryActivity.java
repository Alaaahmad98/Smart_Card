package com.example.smartcard.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smartcard.R;
import com.example.smartcard.activities.CategoryActivity;
import com.example.smartcard.helper.AddCardHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button bnChooseFile, bnUpload;
    private EditText edName;
    private ImageView imageViewCategory;
    private ProgressBar progressBar, progressBarC;

    private Uri mImageUri;

    private StorageReference storageReference;
    private DatabaseReference reference;

    private Uri uri;

    private Intent intent;
    private String nameParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        getSupportActionBar().setTitle("Add Card 2/4");

        initiateView();
        clickView();

        storageReference = FirebaseStorage.getInstance().getReference("Category");
        reference = FirebaseDatabase.getInstance().getReference("Card");


    }


    private void initiateView() {
        bnChooseFile = findViewById(R.id.bn_choose_file);
        bnUpload = findViewById(R.id.bn_upload);
        edName = findViewById(R.id.ed_name_category);
        imageViewCategory = findViewById(R.id.imageView_category);
        progressBar = findViewById(R.id.progress_bar);
        progressBarC = findViewById(R.id.progress_bar_c);
        intent = getIntent();
        nameParent = intent.getStringExtra("NAME_CARD");
    }

    private void clickView() {
        bnChooseFile.setOnClickListener(this);
        bnUpload.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_choose_file:
                openChooseFile();
                break;
            case R.id.bn_upload:
                uploadFile();
                break;

        }
    }

    private void openChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(imageViewCategory);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(uri));
    }


    private void uploadFile() {
        if (mImageUri != null) {
            bnUpload.setVisibility(View.INVISIBLE);
            progressBarC.setVisibility(View.VISIBLE);
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);
                    bnUpload.setVisibility(View.VISIBLE);
                    progressBarC.setVisibility(View.INVISIBLE);
                    Toast.makeText(AddCategoryActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            uri = task.getResult();
                            AddCardHelper addCardHelper = new AddCardHelper(uri.toString());

                            reference.child(nameParent).child(edName.getText().toString()).setValue(addCardHelper);


                            Intent intent = new Intent(AddCategoryActivity.this, CategoryActivity.class);
                            intent.putExtra("NAME_CARD", nameParent);
                            startActivity(intent);
                            finish();


                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("NAME_CARD", nameParent);
        overridePendingTransition(R.anim.left, R.anim.right);
        startActivity(intent);
        finish();
        return true;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("NAME_CARD", nameParent);
        overridePendingTransition(R.anim.left, R.anim.right);
        startActivity(intent);
        finish();


    }
}
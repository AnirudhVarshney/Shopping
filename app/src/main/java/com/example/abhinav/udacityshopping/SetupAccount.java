package com.example.abhinav.udacityshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupAccount extends AppCompatActivity {
EditText inputusername,inputphonenumber;
    StorageReference mStorage;
    Uri prfileimageurl;
    DatabaseReference db,db1;
    private FirebaseAuth auth;
    ImageView profilepic;
    Button finishsetup;
    private ProgressDialog mProgress;
    Boolean flag=false;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        db= FirebaseDatabase.getInstance().getReference();
        Firebase.setAndroidContext(this);
        finishsetup= (Button) findViewById(R.id.finishsetup);
        profilepic= (ImageView) findViewById(R.id.profilpic);
        mStorage= FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        mProgress=new ProgressDialog(this);
        final String uid=auth.getCurrentUser().getUid();
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setprfilepic();
            }
        });
        finishsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveindatabase(uid);
            }
        });
        inputusername = (EditText) findViewById(R.id.username);
        inputphonenumber = (EditText) findViewById(R.id.userphone);

    }
    public void saveindatabase(final String uid) {
        mProgress.setMessage("Saving in database..");
        if (!TextUtils.isEmpty(inputphonenumber.getText().toString()) && !TextUtils.isEmpty(inputusername.getText().toString()) && prfileimageurl != null) {
            mProgress.show();
            StorageReference filestorage = mStorage.child("Profile Images").child(uid).child(prfileimageurl.getLastPathSegment());
            filestorage.putFile(prfileimageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloaduri = taskSnapshot.getDownloadUrl();
                    Log.d("ani", downloaduri + "dwnlduri");
                    db1 = db.child("users").child(uid);
                    db1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                Users newUser = new Users(inputusername.getText().toString(), inputphonenumber.getText().toString(), downloaduri.toString(),auth.getCurrentUser().getEmail());
                                db1.setValue(newUser);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("ani", databaseError.getMessage());
                        }
                    });
                    if (flag == true) {
                        Intent intent = new Intent(SetupAccount.this, BlogMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("uid", uid);
                        mProgress.dismiss();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SetupAccount.this, "Some error occured in profile pic", Toast.LENGTH_SHORT).show();
                    }
                }

            });


        }
        else{
            Toast.makeText(SetupAccount.this,"Fields are missing ",Toast.LENGTH_SHORT).show();
        }
    }
    private void setprfilepic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_PICTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedUri = data.getData();
                CropImage.activity(selectedUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1).setAutoZoomEnabled(true)
                        .start(this);}}
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                prfileimageurl = result.getUri();
                Log.d("ani",prfileimageurl+"uri in acc");
                Glide.with(this).load(prfileimageurl).centerCrop().into(profilepic);
                flag=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("ani",error+"errr");
            }
        }

        //Log.d("ani",selectedUri+"uri");
    }






}

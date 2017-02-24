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

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {
    ImageView selectimage;
    EditText mPosttitle;
    EditText mPostDescription;
    Button mSubmit;
    Uri selectedUri;
    private FirebaseAuth auth;
    FirebaseUser CurrentUser;
    DatabaseReference databaseReference,databaseReferencenew,databaseReferenceusers,db1;
    private ProgressDialog mProgress;
StorageReference mStorage;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mPosttitle= (EditText) findViewById(R.id.posttitle);
        mStorage= FirebaseStorage.getInstance().getReference();
        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        CurrentUser=auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReferenceusers=databaseReference.child("users").child(CurrentUser.getUid());
        mPostDescription= (EditText) findViewById(R.id.postDescription);
mSubmit= (Button) findViewById(R.id.submitpost);
        mProgress=new ProgressDialog(this);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
        selectimage = (ImageView) findViewById(R.id.postimage);
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_SELECT_PICTURE);

            }


        });
    }

    private void startPosting() {
        mProgress.setMessage("Posting to blog..");
    final String title=mPosttitle.getText().toString();
        final String desc=mPostDescription.getText().toString();
        if(!TextUtils.isEmpty(title) &&!TextUtils.isEmpty(desc) &&selectedUri!=null){
            mProgress.show();
            StorageReference filestorage=mStorage.child("Blog Images").child(selectedUri.getLastPathSegment());
            filestorage.putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloaduri=taskSnapshot.getDownloadUrl();
                    Log.d("ani","inside"+title+desc);
                    databaseReferencenew=databaseReference.child("Blog").push();
                    db1=databaseReferencenew.child("timestampLastChanged");
                    databaseReferenceusers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                String name=dataSnapshot.child("name").getValue().toString();
                            String userdp=dataSnapshot.child("imageurl").getValue().toString();
                                Log.d("ani",name+"username");

                            Blogmodel blogmodel=new Blogmodel(title,desc,downloaduri.toString(),dataSnapshot.child("name").getValue().toString(),CurrentUser.getUid().toString(),userdp,dataSnapshot.child("email").getValue().toString());
                            databaseReferencenew.setValue(blogmodel);
                            Log.d("ani",blogmodel.getTimestampLastChanged().get(constants.FIREBASE_PROPERTY_TIMESTAMP).toString());
                    }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

;
                    mProgress.dismiss();
                    Intent i=new Intent(PostActivity.this,BlogMainActivity.class);
                    startActivity(i);
                }

            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                 selectedUri = data.getData();
                Glide.with(this).load(selectedUri).centerCrop().into(selectimage);
                Log.d("ani",selectedUri+"uri");
            }

        }
    }

}

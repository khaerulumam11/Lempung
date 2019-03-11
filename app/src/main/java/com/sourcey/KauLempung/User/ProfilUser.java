package com.sourcey.KauLempung.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilUser extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth firebaseAuth;

    private CircleImageView mDisplayImage;
    private TextView mName, mEmail, mNoHp,mAlamat,k_username,m_alamat;
    private Uri UrlGambar;

    private StorageReference mImageStorage;
    private ProgressDialog mProgressDalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);

        mDisplayImage = (CircleImageView) findViewById(R.id.imageView);
        mName = (TextView) findViewById(R.id.namapengguna);
        mAlamat = (TextView) findViewById(R.id.alamat);
        mNoHp = (TextView) findViewById(R.id.nohp);
        mEmail = (TextView) findViewById(R.id.email);
        k_username = findViewById(R.id.username);
        m_alamat = findViewById(R.id.k_alamat);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String alamat = dataSnapshot.child("alamat").getValue().toString();
                String nohp = dataSnapshot.child("nohp").getValue().toString();
//                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mEmail.setText(email);
                mAlamat.setText(alamat);
                mNoHp.setText(nohp);
                k_username.setText(name);
                m_alamat.setText(alamat);
                // mEmail.setText(user.getEmail());

                Picasso.with(ProfilUser.this).load(image).into(mDisplayImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editData(View view) {
        Intent hh = new Intent(ProfilUser.this,EditProfile.class);
        startActivity(hh);
    }
}

package com.sourcey.KauLempung.User.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.User.DaftarKatalog;
import com.sourcey.KauLempung.User.EditProfile;
import com.sourcey.KauLempung.User.ProfilUser;
import com.sourcey.KauLempung.User.RiwayatPembelianActivity;
import com.sourcey.KauLempung.utils.NetworkConnection;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    Context mContext;

    FirebaseAuth mAuth;
    LinearLayout linearLogout, suntingLayout, lihatRiwayat;
    View view;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth firebaseAuth;

    private CircleImageView mDisplayImage;
    private TextView mName, mEmail, mNoHp,mAlamat,k_username,m_alamat;
    private Uri UrlGambar;

    private StorageReference mImageStorage;
    private ProgressDialog mProgressDalog;

    public ProfileFragment() {
        // Required empty public constructor
    }



    public boolean isNetworkConnected() {
        return NetworkConnection.isNetworkConnected(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.mContext = (FragmentActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDisplayImage = (CircleImageView) view.findViewById(R.id.imageView);
        mName = (TextView) view.findViewById(R.id.username);
        mAlamat = (TextView) view.findViewById(R.id.k_alamat);

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
                mAlamat.setText(alamat);
                // mEmail.setText(user.getEmail());

                Picasso.with(mContext).load(image).into(mDisplayImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        linearLogout = view.findViewById(R.id.logout);
        mAuth =FirebaseAuth.getInstance();
        linearLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
            }
        });

        suntingLayout = view.findViewById(R.id.suntingLy);
        suntingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(mContext, EditProfile.class);
                startActivity(pindah);
            }
        });

        lihatRiwayat = view.findViewById(R.id.riwayatLy);
        lihatRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(mContext, RiwayatPembelianActivity.class);
                startActivity(pindah);
            }
        });

        return view;
    }

}

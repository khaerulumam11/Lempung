package com.sourcey.KauLempung.Admin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.Model.User;
import com.sourcey.KauLempung.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailUser extends AppCompatActivity {

    TextView aa,bb,cc,dd,ee;
    CircleImageView ff;
    String a,b,c,d,e;
    ProgressDialog dlg;

    String idCurrentUser;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        aa = findViewById(R.id.namapengguna);
        bb = findViewById(R.id.email);
        cc = findViewById(R.id.alamat);
        dd = findViewById(R.id.nohp);

        ff = findViewById(R.id.imageView);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dlg = new ProgressDialog(this);

        a = getIntent().getStringExtra("name");
        b = getIntent().getStringExtra("alamat");
        c = getIntent().getStringExtra("email");
        d = getIntent().getStringExtra("nohp");
        e = getIntent().getStringExtra("image");

        aa.setText(a);
        bb.setText(c);
        cc.setText(b);
        dd.setText(d);

        Glide.with(getApplication()).load(e).into(ff);

    }

}

package com.sourcey.KauLempung.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.User.KonfirmasiPembayaran;

public class KonfirmasiPembayaranAdmin extends AppCompatActivity {

    String aa,ss,dd,ff,gg,hh,jj,kk,mm,nn,uu;
    TextView a,b,c,d;


    DatabaseReference databaseFood;

    FirebaseAuth mAuth;

    String stringUri;


    private Uri imageUri;


    private StorageReference mStorage;

    Query databaseUser;

    ProgressDialog dlg;

    String idCurrentUser;

    String id;

    String idUser;

    private String postKey = "";

    Button mChooseImage;

    ImageView hg,gh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran_admin);

        aa = getIntent().getStringExtra("namaproduk");
        ss = getIntent().getStringExtra("jumlahpesanan");
        dd = getIntent().getStringExtra("tanggalpesanan");
        ff = getIntent().getStringExtra("user");
        gg = getIntent().getStringExtra("hargaproduk");
        hh = getIntent().getStringExtra("total");
        jj = getIntent().getStringExtra("image");
        kk = getIntent().getStringExtra("status");
        mm = getIntent().getStringExtra("key");
        nn = getIntent().getStringExtra("id");
        uu = getIntent().getStringExtra("bukti");

        a = findViewById(R.id.email_pembeli);
        b = findViewById(R.id.nama_produk);
        c = findViewById(R.id.jmlhpsnan);
        d = findViewById(R.id.totalharga);
        hg = findViewById(R.id.gmbr_prod);
        gh = findViewById(R.id.gmbr_bukti);

        a.setText(String.valueOf(ff));
        b.setText(String.valueOf(aa));
        c.setText(String.valueOf(ss));
        d.setText(String.valueOf(hh));

        Glide.with(getApplication()).load(jj).into(hg);
        Glide.with(getApplication()).load(uu).into(gh);
    }

    public void setuju(View view) {

        Pesanan user = new Pesanan(jj,aa,ss,dd,gg,hh,mm,nn,ff,"Approved",uu);

        databaseFood = FirebaseDatabase.getInstance().getReference();

        databaseFood.child("LempungProduk").child("Pemesanan").child(mm).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(KonfirmasiPembayaranAdmin.this, "Status berhasil dirubah", Toast.LENGTH_SHORT).show();
                Intent nm = new Intent(KonfirmasiPembayaranAdmin.this,ListPesanan.class);
                startActivity(nm);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KonfirmasiPembayaranAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void cancel(View view) {
        Pesanan user = new Pesanan(jj,aa,ss,dd,gg,hh,mm,nn,ff,"Decline",uu);

        databaseFood = FirebaseDatabase.getInstance().getReference();

        databaseFood.child("LempungProduk").child("Pemesanan").child(mm).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(KonfirmasiPembayaranAdmin.this, "Status berhasil dirubah", Toast.LENGTH_SHORT).show();
                Intent mn = new Intent(KonfirmasiPembayaranAdmin.this,ListPesanan.class);
                startActivity(mn);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KonfirmasiPembayaranAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

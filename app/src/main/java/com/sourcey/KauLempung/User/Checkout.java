package com.sourcey.KauLempung.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.Model.UserProfile;
import com.sourcey.KauLempung.R;

import java.text.NumberFormat;
import java.util.Locale;

public class Checkout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView a,s,d,f,g,h,j,k;
    Spinner spinner;
    ImageView imageView;
    String aa,ss,dd,ff,gg,hh,jj,kk,mm,nn,uu;
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

    int pil1,pil2,pil3,tot;

    private String postKey = "";

    NumberFormat formatRupiah;

    private static final String[] paths = {"Paket A", "Paket B", "Paket C"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        a = findViewById(R.id.namaproduk);
        s = findViewById(R.id.jmlhpesanan_tv);
        d = findViewById(R.id.tv_tglpesan);
        f = findViewById(R.id.tv_jmlhproduk);
        g = findViewById(R.id.tv_hargaproduk);
        h = findViewById(R.id.tv_tothargaproduk);
        j = findViewById(R.id.tv_ongkoskirim);
        k = findViewById(R.id.hrgatot);
        spinner = findViewById(R.id.pilpengiriman);
        imageView = findViewById(R.id.gmbr_produk);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dlg = new ProgressDialog(this);

        Locale localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        mStorage = FirebaseStorage.getInstance().getReference();
        databaseFood = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Pemesanan");


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
        uu = "Rp. ";

        postKey = mm;

        a.setText(String.valueOf(aa));
        s.setText(ss);
        d.setText(String.valueOf(dd));
        f.setText(String.valueOf(ss));
        g.setText(String.valueOf(gg));
        h.setText(uu + hh);

        Glide.with(getApplication()).load(jj).into(imageView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Checkout.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cek(View view) {
        if (spinner.getSelectedItemPosition() == 0){
            int vv = Integer.parseInt(ss);
            if (vv <10) {
                pil1 = Integer.parseInt(ss) * 2000;
                j.setText(formatRupiah.format((double) pil1));
                tot = Integer.parseInt(hh) + pil1;
                k.setText(String.valueOf(formatRupiah.format((double) tot)));
            } else if (vv > 10 || vv <30){
                pil1 = Integer.parseInt(ss) * 4000;
                j.setText(formatRupiah.format((double) pil1));
                tot = Integer.parseInt(hh) + pil1;
                k.setText(String.valueOf(formatRupiah.format((double) tot)));
            } else if (vv > 30){
                pil1 = Integer.parseInt(ss) * 5000;
                j.setText(formatRupiah.format((double) pil1));
                tot = Integer.parseInt(hh) + pil1;
                k.setText(String.valueOf(formatRupiah.format((double) tot)));
            }
        } else if (spinner.getSelectedItemPosition()==1){
            pil2 = Integer.parseInt(ss) *50000;
            j.setText(formatRupiah.format((double)pil2));
            tot = Integer.parseInt(hh) + pil2;
            k.setText(String.valueOf(formatRupiah.format((double)tot)));
        } else if (spinner.getSelectedItemPosition() == 2){
            pil3 = Integer.parseInt(ss) *100000;
            j.setText(formatRupiah.format((double)pil3));
            tot = Integer.parseInt(hh) + pil3;
            k.setText(String.valueOf(formatRupiah.format((double)tot)));
        }
    }

    public void bayar(View view) {

        dlg.setMessage("Memesann....");
        Pesanan user = new Pesanan(jj,aa,ss,dd,gg,k.getText().toString(),mm,nn,ff,"Belum Upload Bukti","default");

        databaseFood = FirebaseDatabase.getInstance().getReference();

        databaseFood.child("LempungProduk").child("Pemesanan").child(postKey).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Checkout.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Checkout.this,KonfirmasiPembayaran.class);
                intent.putExtra("user", ff);
                intent.putExtra("key", mm );
                intent.putExtra("namaproduk", aa);
                intent.putExtra("tanggalpesanan", dd);
                intent.putExtra("jumlahpesanan", ss);
                intent.putExtra("hargaproduk", gg);
                intent.putExtra("id", nn);
                intent.putExtra("image", jj);
                intent.putExtra("total", k.getText().toString());
                intent.putExtra("status", "Belum Upload Bukti");
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Checkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dlg.dismiss();
    }
}

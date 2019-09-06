package com.sourcey.KauLempung.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.Model.UserProfile;
import com.sourcey.KauLempung.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class Checkout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView a,s,d,f,g,h,j,k,z,u,p;
    Spinner spinner;
    ImageView imageView;
    String aa,ss,dd,ff,gg,hh,jj,kk,mm,nn,uu,zz,xx;
    DatabaseReference databaseFood,databaseUser;
    Toolbar toolbar;

    FirebaseAuth mAuth;

    String stringUri;


    private Uri imageUri;


    private StorageReference mStorage;

//    Query databaseUser;

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


        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_primary_24dp);
        toolbar.setTitle("Beli Produk > Keranjang > Checkout");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        a = findViewById(R.id.namaproduk);
        z = findViewById(R.id.namaPemesan);
        u = findViewById(R.id.alamatPesan);
        p = findViewById(R.id.tlpPemesan);
//        s = findViewById(R.id.jmlhpesanan_tv);
        d = findViewById(R.id.tv_tglpesan);
        g = findViewById(R.id.tv_hargaproduk);
        h = findViewById(R.id.tv_tothargaproduk);
        j = findViewById(R.id.tv_ongkoskirim);
        k = findViewById(R.id.hrgatot);
        spinner = findViewById(R.id.pilpengiriman);
//        imageView = findViewById(R.id.gmbr_produk);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dlg = new ProgressDialog(this);

        Locale localeID = new Locale("in", "ID");
        formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        mStorage = FirebaseStorage.getInstance().getReference();
        databaseFood = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Pemesanan");
//        databaseUser = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user");

        databaseUser = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user").child(id);

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String alamat = dataSnapshot.child("alamat").getValue().toString();
                String nohp = dataSnapshot.child("nohp").getValue().toString();
//                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                z.setText(name);
                u.setText(alamat);
                p.setText(nohp);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        zz = getIntent().getStringExtra("jumlahPesanan");
        xx = getIntent().getStringExtra("totalHarga");
        uu = "Rp. ";

        postKey = mm;

//        a.setText(String.valueOf(aa));
//        s.setText(ss);
//        d.setText(String.valueOf(dd));
//        g.setText(String.valueOf(gg));
        h.setText(addRp(String.valueOf(xx)));
        k.setText(addRp(String.valueOf(xx)));

//        Glide.with(getApplication()).load(jj).into(imageView);

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
            int vv = Integer.parseInt(zz);
            if (vv <10) {
                pil1 = Integer.parseInt(zz) * 2000;
                j.setText(addRp(String.valueOf(pil1)));
                tot = Integer.parseInt(xx) + pil1;
                k.setText(addRp(String.valueOf(tot)));
            } else if (vv > 10 || vv <30){
                pil1 = Integer.parseInt(zz) * 4000;
                j.setText(addRp(String.valueOf(pil1)));
                tot = Integer.parseInt(xx) + pil1;
                k.setText(addRp(String.valueOf(tot)));
            } else if (vv > 30){
                pil1 = Integer.parseInt(zz) * 5000;
                j.setText(addRp(String.valueOf(pil1)));
                tot = Integer.parseInt(xx) + pil1;
                k.setText(addRp(String.valueOf(tot)));
            }
        } else if (spinner.getSelectedItemPosition()==1){
            pil2 = Integer.parseInt(zz) *50000;
            j.setText(addRp(String.valueOf(pil2)));
            tot = Integer.parseInt(xx) + pil2;
            k.setText(addRp(String.valueOf(tot)));
        } else if (spinner.getSelectedItemPosition() == 2){
            pil3 = Integer.parseInt(zz) *100000;
            j.setText(addRp(String.valueOf(pil3)));
            tot = Integer.parseInt(xx) + pil3;
            k.setText(addRp(String.valueOf(tot)));
        }
    }

    public void bayar(View view) {

        dlg.setMessage("Memesann....");
//        Pesanan user = new Pesanan(jj,aa,ss,dd,gg,k.getText().toString(),mm,nn,ff,"Belum Upload Bukti","default");
//
//        databaseFood = FirebaseDatabase.getInstance().getReference();
//
//        databaseFood.child("LempungProduk").child("Pemesanan").child(postKey).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(Checkout.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Checkout.this,KonfirmasiPembayaran.class);
//                intent.putExtra("user", ff);
//                intent.putExtra("key", mm );
//                intent.putExtra("namaproduk", aa);
//                intent.putExtra("tanggalpesanan", dd);
//                intent.putExtra("jumlahpesanan", ss);
//                intent.putExtra("hargaproduk", gg);
//                intent.putExtra("id", nn);
//                intent.putExtra("image", jj);
//                intent.putExtra("total", k.getText().toString());
//                intent.putExtra("status", "Belum Upload Bukti");
//                startActivity(intent);
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Checkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        Intent pindah = new Intent(Checkout.this,KonfirmasiPembayaran.class);
        pindah.putExtra("status","Belum Upload Bukti");
        startActivity(pindah);

        dlg.dismiss();
    }

    public static String addRp(String currency) {
//        String[] toConvert = currency.split(".");
        StringBuilder str = new StringBuilder(currency);
        int i = str.length() - 3;
        while (i > 0) {
            str.insert(i, ".");
            i -= 3;
        }
        return "Rp. " + str.toString();
    }
}

package com.sourcey.KauLempung.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sourcey.KauLempung.Adapter.CommentAdapter;
import com.sourcey.KauLempung.Admin.DetailProduk;
import com.sourcey.KauLempung.Admin.KatalogProduk;
import com.sourcey.KauLempung.Model.Comment;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailProdukUser extends AppCompatActivity {

    TextView q,w,e,r,t;

    ImageView y;

    String z,x,c,v,b,n,u,i;

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

    EditText sourcekomentar,jumlahpes,alamat;
    RecyclerView rc;
    CommentAdapter adapter;
    ArrayList<Comment> list;
    DatabaseReference dref,mDatabase;
    ProgressDialog pd;
    String usernya,usernya1, idfoto,namamakanan,harga,user1,images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk_user);

         q = findViewById(R.id.judul_title);
         w = findViewById(R.id.harga);
         e = findViewById(R.id.namatoko);
         r = findViewById(R.id.tlpntoko);
         t = findViewById(R.id.almttoko);
         y = findViewById(R.id.gambar);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dlg = new ProgressDialog(this);

        String usersaatini = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usernya = usersaatini;

        String usersaatini1 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usernya1 = usersaatini1;
        idfoto = getIntent().getStringExtra("key");

        z = getIntent().getStringExtra("judulproduk");
        x = getIntent().getStringExtra("harga");
        c = getIntent().getStringExtra("namatoko");
        v = getIntent().getStringExtra("tlpntoko");
        b = getIntent().getStringExtra("alamattoko");
        n = getIntent().getStringExtra("gambarproduk");
        u = getIntent().getStringExtra("id");
        i = getIntent().getStringExtra("key");

        q.setText(z);
        w.setText("Rp." + x);
        e.setText(c);
        r.setText(v);
        t.setText(b);

        Glide.with(getApplication()).load(n).into(y);

        sourcekomentar = findViewById(R.id.komentar);
        pd = new ProgressDialog(this);
        dref = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Comment");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Pemesanan");

        list = new ArrayList<>();
        adapter = new CommentAdapter(this, list);
        rc = findViewById(R.id.rv_comment);
        rc.setHasFixedSize(true);
//        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(adapter);

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment cur = dataSnapshot.getValue(Comment.class);
                if (cur.getmFoto().equals(idfoto)) {
                    list.add(cur);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "/" + (month+1) + "/" + year;
    }

    public void beli(View view) {
// cara pemanggilannya seperti ini
        final String tanggal_sekarang = getCurrentDate();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetailProdukUser.this);
        builder.setTitle("Data Pembelian");
        builder.setMessage("Pembeli dapat memasukkan jumlah pembelian pada laman ini");

        LayoutInflater inflater= getLayoutInflater();
        View dialog = inflater.inflate(R.layout.isi_jumlah,null);
        builder.setView(dialog);

        final EditText jumlahpes = dialog.findViewById(R.id.jmlhpsnan);

        builder.setPositiveButton("Pesan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String jumlah = jumlahpes.getText().toString();

                int a = Integer.parseInt(jumlah) * Integer.parseInt(x);

                final String total = String.valueOf(a);

                Pesanan user = new Pesanan(n,q.getText().toString(),jumlah,tanggal_sekarang,w.getText().toString(),total,i,u,usernya1,"Belum Pesan","default");

                mDatabase.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailProdukUser.this,"Pesanan Berhasil",Toast.LENGTH_LONG).show();
                        Intent ii = new Intent(DetailProdukUser.this, Keranjang.class);
                        ii.putExtra("user", usernya1);
                        ii.putExtra("key", i );
                        ii.putExtra("namaproduk", z);
                        ii.putExtra("tanggalpesanan",tanggal_sekarang);
                        ii.putExtra("jumlahpesanan", jumlah);
                        ii.putExtra("hargaproduk", w.getText().toString());
                        ii.putExtra("id", u);
                        ii.putExtra("image", n);
                        ii.putExtra("total", total);
                        ii.putExtra("status", "Belum Memesan");
                        startActivity(ii);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailProdukUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog asd = builder.create();
        asd.show();
    }

    public void submit(View view) {

        String komentar = sourcekomentar.getText().toString();

        //Inisialisasi objek
        Comment com = new Comment(usernya, sourcekomentar.getText().toString(), idfoto);

        //Input data ke Firebase
        if (komentar.isEmpty()){
            Toast.makeText(this, "Isi Komentar Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            pd.setMessage("Memberikan komentar... ");
            pd.show();
            dref.push().setValue(com).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DetailProdukUser.this, "Komentar berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        sourcekomentar.setText(null);
                    } else {
                        Toast.makeText(DetailProdukUser.this, "Gagal memberikan komentar", Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }
            });
        }

    }
}

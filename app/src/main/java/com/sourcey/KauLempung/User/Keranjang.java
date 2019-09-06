package com.sourcey.KauLempung.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Adapter.KeranjangAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.utils.SwipeUtilDelete;

import java.util.ArrayList;

public class Keranjang extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editText;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    DatabaseReference ref;

    ArrayList<Pesanan> list;

    Button checkout;

    KeranjangAdapter katalogAdapter;

    String idUser;

    int total_harga=0, jumlahpesanan;

    TextView harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            idUser = user.getEmail();
            ref = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Pemesanan");

            list = new ArrayList<>();
            katalogAdapter = new KeranjangAdapter(this,list);

            recyclerView = findViewById(R.id.rv_keranjang);
            harga = findViewById(R.id.tv_total_pricecourse);
            checkout = findViewById(R.id.btn_buynow);
            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(katalogAdapter);

            setSwipeForRecyclerView();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    int harga_course=0;
                    int jmlhpesanan=0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Pesanan cur = data.getValue(Pesanan.class);
                        if (cur.getUser().equals(idUser) && cur.getStatus().equals("Belum Pesan")) {
                            harga_course =Integer.parseInt(cur.getTotal());
                            jmlhpesanan = Integer.parseInt(cur.getJumlahpesanan());
                            total_harga+=harga_course;
                            jumlahpesanan+=jmlhpesanan;
                            cur.key = data.getKey();
                            list.add(cur);
                            katalogAdapter.notifyDataSetChanged();
                        }
                    }
                    harga.setText(addRp(String.valueOf(total_harga)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(Keranjang.this,Checkout.class);
                    pindah.putExtra("jumlahPesanan",String.valueOf(jumlahpesanan));
                    pindah.putExtra("totalHarga", String.valueOf(total_harga));
                    startActivity(pindah);
                }
            });

        }
    }

    private void setSwipeForRecyclerView() {

        SwipeUtilDelete swipeHelper = new SwipeUtilDelete(0, ItemTouchHelper.START, this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                KeranjangAdapter adapter = (KeranjangAdapter) recyclerView.getAdapter();
                final String key ="";

                //delete dari database nya
                list.clear();
                final CharSequence[] charSequences = {"Ya","Tidak"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Keranjang.this);
                builder.setTitle("Apakah kamu yakin ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMakanan();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                adapter.notifyDataSetChanged();
            }
        };

//agar muncul ikon delete saaat di swipe
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        swipeHelper.setLeftcolorCode(ContextCompat.getColor(this, R.color.colorRed));


    }

    private void deleteMakanan() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getKey(); // ini disimpan jg ke modelnya
                    Pesanan cur = data.getValue(Pesanan.class);
                    if (cur.getUser().equals(idUser)) {
                        cur.setKey(data.getKey());
                        ref.child(data.getKey()).removeValue();
                        katalogAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

package com.sourcey.KauLempung.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Adapter.KeranjangAdapter;
import com.sourcey.KauLempung.Adapter.KeranjangAdapter1;
import com.sourcey.KauLempung.Adapter.ListPesananAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.utils.SwipeUtilDelete;

import java.util.ArrayList;

public class ListPesanan extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editText;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    DatabaseReference ref;

    ArrayList<Pesanan> list;

    ListPesananAdapter katalogAdapter;

    String idUser;


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
            katalogAdapter = new ListPesananAdapter(this,list);

            recyclerView = findViewById(R.id.rv_keranjang);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(katalogAdapter);

//            setSwipeForRecyclerView();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Pesanan cur = data.getValue(Pesanan.class);
                            cur.key = data.getKey();
                            list.add(cur);
                            katalogAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void setSwipeForRecyclerView() {

        SwipeUtilDelete swipeHelper = new SwipeUtilDelete(0, ItemTouchHelper.START, this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ListPesananAdapter adapter = (ListPesananAdapter) recyclerView.getAdapter();
                final String key ="";

                //delete dari database nya
                list.clear();
                final CharSequence[] charSequences = {"Ya","Tidak"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ListPesanan.this);
                builder.setTitle("Apakah kamu yakin ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMakanan();
                        finish();
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
                        cur.setKey(data.getKey());
                        ref.child(data.getKey()).removeValue();
                        katalogAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

package com.sourcey.KauLempung.User.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sourcey.KauLempung.User.Checkout;
import com.sourcey.KauLempung.User.Keranjang;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    Context mContext;
    View view;
    RecyclerView recyclerView;
    KeranjangAdapter adapter;
    ArrayList<Pesanan> list = new ArrayList<>();
    private FirebaseAuth mAuth;

    private EditText editText;

    DatabaseReference ref;

    Button checkout;

    KeranjangAdapter katalogAdapter;

    ImageView imageView;
    LinearLayout linearLayout;
    String idUser;

    int total_harga=0, jumlahpesanan;

    TextView harga;

    public CartFragment() {
        // Required empty public constructor
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
        view= inflater.inflate(R.layout.fragment_cart, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        } else {
            idUser = user.getEmail();
            ref = FirebaseDatabase.getInstance().getReference().child("LempungProduk").child("Pemesanan");

            imageView = view.findViewById(R.id.cartEmpty);
            linearLayout = view.findViewById(R.id.cartLy);
            adapter = new KeranjangAdapter(mContext, list);
            recyclerView = view.findViewById(R.id.rvList);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(adapter);

            checkout = view.findViewById(R.id.btn_buynow);

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
                            adapter.notifyDataSetChanged();
                            if (list.isEmpty()){
                                imageView.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            } else {
                                checkout.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pindah = new Intent(mContext, Checkout.class);
                    pindah.putExtra("jumlahPesanan",String.valueOf(jumlahpesanan));
                    pindah.putExtra("totalHarga", String.valueOf(total_harga));
                    startActivity(pindah);
                }
            });

        }


        return view;
    }

}

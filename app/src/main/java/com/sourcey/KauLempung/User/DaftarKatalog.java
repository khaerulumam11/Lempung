package com.sourcey.KauLempung.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Adapter.KatalogAdapter;
import com.sourcey.KauLempung.Adapter.ProdukAdapter;
import com.sourcey.KauLempung.Adapter.ProdukViewHolder;
import com.sourcey.KauLempung.Adapter.SearchAdapter;
import com.sourcey.KauLempung.Adapter.SearchProdukAdapter;
import com.sourcey.KauLempung.Admin.DaftarUser;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Item2;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.Model.Produk2;
import com.sourcey.KauLempung.Model.UserSearch;
import com.sourcey.KauLempung.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class DaftarKatalog extends AppCompatActivity {

    CarouselView carouselView;
    int[] sampleImages = { R.drawable.image_3, R.drawable.image_2, R.drawable.image_4, R.drawable.image_1, R.drawable.image_5};
    private FirebaseAuth mAuth;

    private EditText editText;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    DatabaseReference ref;

    ArrayList<Produk> list;

    ProdukAdapter katalogAdapter;

    String idUser;

    ArrayList<Produk2> produks;


    FirebaseRecyclerOptions<Produk2> firebaseRecyclerAdapter;

    FirebaseRecyclerAdapter<Produk2, ProdukViewHolder> oo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_katalog);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            carouselView = findViewById(R.id.carouselView);
            carouselView.setPageCount(sampleImages.length);
            carouselView.setImageListener(imageListener);
            idUser = user.getEmail();
            ref = FirebaseDatabase.getInstance().getReference().child("katalogproduk");

            list = new ArrayList<>();
            produks = new ArrayList<>();
            katalogAdapter = new ProdukAdapter(this,list);

            recyclerView = findViewById(R.id.list_recycler);

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new GridLayoutManager(this,2));

            recyclerView.setAdapter(katalogAdapter);

            editText = findViewById(R.id.searchproduk);

//            showData();

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!s.toString().isEmpty()) {
                        setAdapter(s.toString());
                    }

                }
            });

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Produk cur = data.getValue(Produk.class);
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

    private void showData() {
        firebaseRecyclerAdapter = new FirebaseRecyclerOptions.Builder<Produk2>(
        ).setQuery(ref, Produk2.class).build();

        oo = new FirebaseRecyclerAdapter<Produk2, ProdukViewHolder>(firebaseRecyclerAdapter) {
            //
            @NonNull
            @Override
            public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_katalog, parent, false);
                return new ProdukViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ProdukViewHolder holder, int position, final Produk2 model) {

                holder.aa.setText(model.getTitle());
                holder.bb.setText("Rp. " + model.getHarga());
                holder.bb.setTag(model.getImage());

                Glide.with(DaftarKatalog.this).load(model.getImage()).into(holder.dd);


                holder.cc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent kk = new Intent(DaftarKatalog.this, DetailProdukUser.class);
                        kk.putExtra("namatoko", model.getNamaprod());
                        kk.putExtra("key", model.getKey() );
                        kk.putExtra("judulproduk", model.getTitle());
                        kk.putExtra("alamattoko", model.getAlamat());
                        kk.putExtra("tlpntoko", model.getNohp());
                        kk.putExtra("id", model.getId());
                        kk.putExtra("gambarproduk", model.getImage());
                        kk.putExtra("harga", model.getHarga());
                        startActivity(kk);
                    }
                });

            }
        };

        oo.startListening();
        recyclerView.setAdapter(oo);

    }

    private void setAdapter(String s) {
        Query firebaseSearchQuery = ref.orderByChild("title").startAt(s).endAt(s + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    produks.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final Produk2 userSearch = dataSnapshot1.getValue(Produk2.class);
                        produks.add(userSearch);
                    }

                    SearchProdukAdapter searchAdapter = new SearchProdukAdapter(DaftarKatalog.this, produks);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(DaftarKatalog.this, LoginActivity.class));
            finish();
        } else if (id == R.id.action_profile){
            startActivity(new Intent(DaftarKatalog.this,ProfilUser.class));
            finish();
        } else if (id == R.id.action_pemesanan){
            startActivity(new Intent(DaftarKatalog.this,Keranjang1.class));
//            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.sourcey.KauLempung.User.fragment;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

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
import com.smarteist.autoimageslider.SliderView;
import com.sourcey.KauLempung.Adapter.ProdukAdapter;
import com.sourcey.KauLempung.Adapter.ProdukViewHolder;
import com.sourcey.KauLempung.Adapter.SearchProdukAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.Model.Produk2;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.User.DaftarKatalog;
import com.sourcey.KauLempung.User.DetailProdukUser;
import com.sourcey.KauLempung.utils.NetworkConnection;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4, R.drawable.slider5};
    private FirebaseAuth mAuth;

    private EditText editText;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    DatabaseReference ref;

    ArrayList<Produk> list;

    ProdukAdapter katalogAdapter;

    SliderView sliderView;

    String idUser;

    ArrayList<Produk2> produks;

    Context mContext;
    View view;

    FirebaseRecyclerOptions<Produk2> firebaseRecyclerAdapter;

    FirebaseRecyclerAdapter<Produk2, ProdukViewHolder> oo;

    FrameLayout frameLayout;
    ScrollView scrollView;
    SwipeRefreshLayout swipeRefreshLayout;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(getContext(), LoginActivity.class);
//            startActivity(intent);
//        } else {
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        if (isNetworkConnected()){
            carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
//            idUser = user.getEmail();
        ref = FirebaseDatabase.getInstance().getReference().child("katalogproduk");

        list = new ArrayList<>();
        produks = new ArrayList<>();
        katalogAdapter = new ProdukAdapter(getContext(), list);

        recyclerView = view.findViewById(R.id.list_recycler);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(katalogAdapter);


        editText = view.findViewById(R.id.searchproduk);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isNetworkConnected();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

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
                    showData();
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
    } else {
         scrollView = view.findViewById(R.id.scrollLayout);
         frameLayout = view.findViewById(R.id.mainLayout);
         scrollView.setVisibility(View.GONE);
         frameLayout.setBackgroundResource(R.drawable.notconnection);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isNetworkConnected();
                }
            });
        }


//    }
        return view;

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

                Glide.with(getContext()).load(model.getImage()).into(holder.dd);


                holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent kk = new Intent(getContext(), DetailProdukUser.class);
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

                    SearchProdukAdapter searchAdapter = new SearchProdukAdapter(getContext(), produks);
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


}

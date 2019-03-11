package com.sourcey.KauLempung.Admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Adapter.KatalogAdapter;
import com.sourcey.KauLempung.Adapter.SearchAdapter;
import com.sourcey.KauLempung.Adapter.UserAdapter;
import com.sourcey.KauLempung.Adapter.UsersViewHolder;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.Model.User;
import com.sourcey.KauLempung.Model.UserSearch;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;

public class DaftarUser extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;



    DatabaseReference ref;

    ArrayList<User> list;

    UserAdapter katalogAdapter;

    String idUser;

    EditText editText;
    ImageButton imageButton;

    ArrayList<UserSearch> userSearches;
    SearchAdapter searchAdapter;

    FirebaseRecyclerOptions<UserSearch> firebaseRecyclerAdapter;

    FirebaseRecyclerAdapter<UserSearch, UsersViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_user);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getEmail();
        ref = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user");


        list = new ArrayList<>();

        userSearches = new ArrayList<>();
        katalogAdapter = new UserAdapter(this, list);

        recyclerView = findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(katalogAdapter);


        editText = findViewById(R.id.searchuser);

        showData();

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
                    // ini disimpan jg ke modelnya
                    User cur = data.getValue(User.class);
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

    private void showData() {
        firebaseRecyclerAdapter = new FirebaseRecyclerOptions.Builder<UserSearch>(
        ).setQuery(ref, UserSearch.class).build();

        adapter = new FirebaseRecyclerAdapter<UserSearch, UsersViewHolder>(firebaseRecyclerAdapter) {
            //
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsersViewHolder holder, int position, final UserSearch model) {

                holder.aa.setText(model.getName());
                holder.bb.setText(model.getEmail());

                holder.cardViewPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent kk = new Intent(DaftarUser.this, DetailUser.class);
                        kk.putExtra("user", model.getUser());
                        kk.putExtra("key", model.getKey() );
                        kk.putExtra("name", model.getName());
                        kk.putExtra("alamat", model.getAlamat());
                        kk.putExtra("nohp", model.getNohp());
                        kk.putExtra("id", model.getId());
                        kk.putExtra("image", model.getImage());
                        kk.putExtra("email", model.getEmail());
                        startActivity(kk);
                    }
                });

            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void setAdapter(final String s) {
        Query firebaseSearchQuery = ref.orderByChild("name").startAt(s).endAt(s + "\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    userSearches.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final UserSearch userSearch = dataSnapshot1.getValue(UserSearch.class);
                        userSearches.add(userSearch);
                    }

                    SearchAdapter searchAdapter = new SearchAdapter(DaftarUser.this, userSearches);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

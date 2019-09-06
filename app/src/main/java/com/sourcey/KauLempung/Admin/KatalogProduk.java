package com.sourcey.KauLempung.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Adapter.KatalogAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.R;
import com.sourcey.KauLempung.utils.SwipeUtilDelete;
import com.sourcey.KauLempung.utils.SwipeUtilEdit;

import java.util.ArrayList;


public class KatalogProduk extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;

    DatabaseReference ref;

    ArrayList<Produk> list;

    KatalogAdapter katalogAdapter;

    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_katalogpro);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Katalog Produk");
//        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getEmail();
        ref = FirebaseDatabase.getInstance().getReference().child("katalogproduk");

        list = new ArrayList<>();
        katalogAdapter = new KatalogAdapter(this,list);

        recyclerView = findViewById(R.id.recview);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(katalogAdapter);

        setSwipeForRecyclerView();


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getKey(); // ini disimpan jg ke modelnya
                    Produk cur = data.getValue(Produk.class);
                    if (cur.getUser().equals(idUser)) {
                        cur.setKey(data.getKey());
                        list.add(cur);
                        katalogAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent a = new Intent(KatalogProduk.this,TambahKatalog.class);
               startActivity(a);
            }
        });
    }

    private void setSwipeForRecyclerView() {

        SwipeUtilDelete swipeHelper = new SwipeUtilDelete(0, ItemTouchHelper.START, this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                KatalogAdapter adapter = (KatalogAdapter) recyclerView.getAdapter();
                final String key ="";

                //delete dari database nya
                list.clear();
                final CharSequence[] charSequences = {"Ya","Tidak"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(KatalogProduk.this);
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
                        Intent aa = new Intent(KatalogProduk.this,KatalogProduk.class);
                        startActivity(aa);
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


        SwipeUtilEdit swipeHelpers = new SwipeUtilEdit(0, ItemTouchHelper.RIGHT, this) {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                KatalogAdapter adapter = (KatalogAdapter) recyclerView.getAdapter();

                Intent ag = new Intent(KatalogProduk.this, EditKatalog.class);
                startActivity(ag);

                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper mItemTouchHelpers = new ItemTouchHelper(swipeHelpers);
        mItemTouchHelpers.attachToRecyclerView(recyclerView);
        swipeHelpers.setLeftcolorCode(ContextCompat.getColor(this, R.color.colorGreen));


    }

    private void deleteMakanan() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    data.getKey(); // ini disimpan jg ke modelnya
                    Produk cur = data.getValue(Produk.class);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(KatalogProduk.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

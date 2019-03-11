package com.sourcey.KauLempung.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sourcey.KauLempung.Adapter.ItemListAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Item2;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;

public class HomeAdmin extends AppCompatActivity {

    ArrayList<Item2> exampleItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    FirebaseAuth.AuthStateListener listener;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            exampleItems = new ArrayList<>();
            exampleItems.add(new Item2(R.drawable.catalog, "Katalog Produk",R.color.red));
            exampleItems.add(new Item2(R.drawable.info, "Daftar Pengguna",R.color.orange));
            exampleItems.add(new Item2(R.drawable.order, "Pemesanan",R.color.yellow));
            buildRecyclerView();
        }

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
            startActivity(new Intent(HomeAdmin.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.recycler);

        mAdapter = new ItemListAdapter(this,exampleItems);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }
}

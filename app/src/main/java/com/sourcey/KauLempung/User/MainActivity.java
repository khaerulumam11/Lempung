package com.sourcey.KauLempung.User;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sourcey.KauLempung.Adapter.ItemAdapter;
import com.sourcey.KauLempung.Adapter.ItemListAdapter;
import com.sourcey.KauLempung.Adapter.ItemUserListAdapter;
import com.sourcey.KauLempung.LoginActivity;
import com.sourcey.KauLempung.Model.Item;
import com.sourcey.KauLempung.Model.Item2;
import com.sourcey.KauLempung.R;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item2> exampleItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    FirebaseAuth.AuthStateListener listener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            exampleItems = new ArrayList<>();
            exampleItems.add(new Item2(R.drawable.dandang, "Katalog Produk",R.color.red));
            exampleItems.add(new Item2(R.drawable.gerabah, "Lihat Pemesanan", R.color.orange));
            exampleItems.add(new Item2(R.drawable.guci, "Profil", R.color.yellow));

            buildRecyclerView();
        }
    }

//
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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.rv);

        mAdapter = new ItemUserListAdapter(this,exampleItems);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}

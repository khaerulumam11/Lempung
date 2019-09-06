package com.sourcey.KauLempung.User;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.sourcey.KauLempung.User.fragment.CartFragment;
import com.sourcey.KauLempung.User.fragment.HomeFragment;
import com.sourcey.KauLempung.User.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item2> exampleItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    FirebaseAuth.AuthStateListener listener;
    private FirebaseAuth mAuth;

    private BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    CartFragment cartFragment;

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
            homeFragment = new HomeFragment();
            profileFragment = new ProfileFragment();
            cartFragment = new CartFragment();
            bottomNavigationView = findViewById(R.id.bottomnav_menu);
            LoadHomeFragment();
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.navigation_home_learning:
                            onchangeSegmentFragment(homeFragment, "Home Fragment");
                            return true;
                        case R.id.navigation_cart:
                            onchangeSegmentFragment(cartFragment, "Cart Fragment");
                            return true;
                        case R.id.navigation_profile:
                            onchangeSegmentFragment(profileFragment, "Profile Fragment");
                            return true;
                    }
                    return false;
                }
            });

        }
    }

    private void LoadHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.main_content, homeFragment, "Home Fragment");
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void onchangeSegmentFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        fragmentManager2.beginTransaction().
                disallowAddToBackStack().
                replace(R.id.main_content, fragment).
                commit();
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

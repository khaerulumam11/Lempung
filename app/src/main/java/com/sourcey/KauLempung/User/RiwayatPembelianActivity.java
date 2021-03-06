package com.sourcey.KauLempung.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sourcey.KauLempung.R;

public class RiwayatPembelianActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembelian);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Riwayat Pembelian");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

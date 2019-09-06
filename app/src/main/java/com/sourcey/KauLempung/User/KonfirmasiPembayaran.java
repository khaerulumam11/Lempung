package com.sourcey.KauLempung.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sourcey.KauLempung.Model.Pesanan;
import com.sourcey.KauLempung.Model.UserProfile;
import com.sourcey.KauLempung.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class KonfirmasiPembayaran extends AppCompatActivity {

    String aa,ss,dd,ff,gg,hh,jj,kk,mm,nn,uu;
    TextView ii;


    DatabaseReference databaseFood;

    FirebaseAuth mAuth;

    String stringUri;


    private Uri imageUri;


    private StorageReference mStorage;

    Query databaseUser;

    ProgressDialog dlg;

    String idCurrentUser;

    String id;

    String idUser;

    private String postKey = "";

    Button mChooseImage;

    ImageView hg;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aa = getIntent().getStringExtra("namaproduk");
        ss = getIntent().getStringExtra("jumlahpesanan");
        dd = getIntent().getStringExtra("tanggalpesanan");
        ff = getIntent().getStringExtra("user");
        gg = getIntent().getStringExtra("hargaproduk");
        hh = getIntent().getStringExtra("total");
        jj = getIntent().getStringExtra("image");
        kk = getIntent().getStringExtra("status");
        mm = getIntent().getStringExtra("key");
        nn = getIntent().getStringExtra("id");
        if (kk.equals("Sudah Upload & Menunggu Konfirmasi Admin")) {
            setContentView(R.layout.menunggu_konfirmasi);
        } else if (kk.equals("Approved") || kk.equals("Decline")){
            setContentView(R.layout.sudah_dikonfirmasi);
        }
        else if (kk.equals("Belum Upload Bukti")) {
            setContentView(R.layout.activity_konfirmasi_pembayaran);

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Beli Produk > Keranjang > Checkout > Bayar");
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_primary_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            dlg = new ProgressDialog(this);

//            mStorage = FirebaseStorage.getInstance().getReference();

            hg = findViewById(R.id.image_konfirm);

            ii.setText(String.valueOf(hh));
        }
    }

    public void choose(View view) {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");

        //Mulai intent untuk memilih foto dan mendapatkan hasil
        startActivityForResult(pickImage, 1);
    }

    public void unggah(View view) {
       simpanperubahan();
       finish();
    }

    private void simpanperubahan() {
        dlg.setMessage("Uploading!");

        Intent pindah = new Intent(KonfirmasiPembayaran.this,DaftarKatalog.class);
        startActivity(pindah);
        //Menentukan nama untuk file di Firebase
//        StorageReference filepath = mStorage.child("LempungProduk").child("Pemesanan").child(mm);
//
//        //Mendapatkan gambar dari Imageview untuk diupload
//        hg.setDrawingCacheEnabled(true);
//        hg.buildDrawingCache();
//        Bitmap bitmap = hg.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        final UploadTask task = filepath.putBytes(data);
//
//        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            //Method ketika upload gambar berhasil
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                //Inisialisasi post untuk disimpan di FirebaseDatabase
////                Task<Uri> aa = task.getSnapshot().getMetadata().getReference().getDownloadUrl();
////                stringUri = aa.toString();
//                task.getSnapshot().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        stringUri = uri.toString();
//                        Pesanan user = new Pesanan(jj,aa,ss,dd,gg,hh,mm,nn,ff,"Sudah Upload & Menunggu Konfirmasi Admin",stringUri);
//
//                        databaseFood = FirebaseDatabase.getInstance().getReference();
//
//                        databaseFood.child("LempungProduk").child("Pemesanan").child(mm).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(KonfirmasiPembayaran.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(KonfirmasiPembayaran.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        //Tutup dialog ketika berhasil atau pun gagal
//                        dlg.dismiss();
//                    }
//
//                    //Ketika upload gambar gagal
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(KonfirmasiPembayaran.this, "Gagal Upload!", Toast.LENGTH_SHORT).show();
//                        dlg.dismiss();
//                    }
//                });
//            }
//        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                //Mendapatkan data dari intent
                imageUri = data.getData();
                try {
                    //Merubah data menjadi inputstream yang diolah menjadi bitmap dan ditempatkan pada imageview
                    InputStream stream = getContentResolver().openInputStream(imageUri);
                    Bitmap gambar = BitmapFactory.decodeStream(stream);
                    hg.setImageBitmap(gambar);
                } catch (FileNotFoundException e) {
                    Log.w("FileNotFoundException", e.getMessage());
                    Toast.makeText(this, "Tidak dapat mengupload gambar", Toast.LENGTH_SHORT).show();
                }
            }

            //Ketika user tidak memilih foto
        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show();
        }

    }


}

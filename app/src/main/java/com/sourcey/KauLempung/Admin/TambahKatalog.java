package com.sourcey.KauLempung.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TambahKatalog extends AppCompatActivity {

    EditText mTitle, mNamaProd, mHarga, mAlamat,mNoHp;

    ImageView imageView;

    Button mChooseImage;

    //our database reference object

    DatabaseReference databaseFood;

    FirebaseAuth mAuth;

    String stringUri;


    private Uri imageUri;


    private StorageReference mStorage;

    Query databaseUser;

    ProgressDialog dlg;

    String idCurrentUser;

    String id;

    Produk user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_katalog);

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dlg = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        databaseFood = FirebaseDatabase.getInstance().getReference().child("katalogproduk");


        mTitle = findViewById(R.id.namaproduk);
        mHarga = findViewById(R.id.harga);
        mNamaProd = findViewById(R.id.namaprod);
        mAlamat = findViewById(R.id.alamatprod);
        mNoHp = findViewById(R.id.nohpprod);

        mChooseImage = findViewById(R.id.btn_choose_image);
        imageView = findViewById(R.id.img_post);

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImage = new Intent(Intent.ACTION_PICK);
                pickImage.setType("image/*");

                //Mulai intent untuk memilih foto dan mendapatkan hasil
                startActivityForResult(pickImage, 1);

            }
        });
    }

    public void tambahproduk(View view) {

        dlg.setMessage("Uploading!");
        dlg.show();

        //Menentukan nama untuk file di Firebase
        StorageReference filepath = mStorage.child("katalogproduk").child(mTitle.getText().toString());

        //Mendapatkan gambar dari Imageview untuk diupload
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        final UploadTask task = filepath.putBytes(data);

        //Upload gambar ke FirebaseStorage
        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            //Method ketika upload gambar berhasil
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Inisialisasi post untuk disimpan di FirebaseDatabase
//                Task<Uri> aa = task.getSnapshot().getMetadata().getReference().getDownloadUrl();
//                stringUri = aa.toString();
                task.getSnapshot().getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        stringUri = uri.toString();
                        Produk user = new Produk(mTitle.getText().toString(),stringUri,mHarga.getText().toString(), mNamaProd.getText().toString(),mNoHp.getText().toString(), mAlamat.getText().toString(),id,idCurrentUser);
                        databaseFood.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            //Ketika menyimpan data berhasil
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TambahKatalog.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                                Intent pindah = new Intent(TambahKatalog.this,KatalogProduk.class);
                                startActivity(pindah);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            //Ketika menyimpan data gagal
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TambahKatalog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Tutup dialog ketika berhasil atau pun gagal
                        dlg.dismiss();
                    }

                    //Ketika upload gambar gagal
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TambahKatalog.this, "Gagal Upload!", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                });
                    }
                });

                //Menyimpan objek di database

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
                    imageView.setImageBitmap(gambar);
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

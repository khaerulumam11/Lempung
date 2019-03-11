package com.sourcey.KauLempung.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sourcey.KauLempung.Model.Produk;
import com.sourcey.KauLempung.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditKatalog extends AppCompatActivity {


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

    String idUser;

    private String postKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_katalog);

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

        getInfoProduk();
    }

    private void getInfoProduk() {
        databaseFood = FirebaseDatabase.getInstance().getReference();
        databaseFood.child("katalogproduk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Produk cur = data.getValue(Produk.class);
                    if (cur.getUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        cur.setKey(data.getKey());
                        postKey = data.getKey();
                        mTitle.setText(cur.getTitle());
                        mHarga.setText(cur.getHarga());
                        mNamaProd.setText(cur.getNamaprod());
                        mNoHp.setText(cur.getNohp());
                        mHarga.setText(cur.getHarga());
                        mAlamat.setText(cur.getAlamat());
                        Glide.with(getApplication()).load(cur.getImage()).into(imageView);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateproduk(View view) {
        simpanperubahan();
        finish();
    }

    private void simpanperubahan() {

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String iduser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        dlg.setMessage("Uploading!");

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

                        databaseFood = FirebaseDatabase.getInstance().getReference();

                        databaseFood.child("katalogproduk").child(postKey).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditKatalog.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditKatalog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Tutup dialog ketika berhasil atau pun gagal
                        dlg.dismiss();
                    }

                    //Ketika upload gambar gagal
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditKatalog.this, "Gagal Upload!", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                });
            }
        });
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

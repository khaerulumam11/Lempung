package com.sourcey.KauLempung;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sourcey.KauLempung.Admin.HomeAdmin;
import com.sourcey.KauLempung.Admin.KatalogProduk;
import com.sourcey.KauLempung.User.DaftarKatalog;
import com.sourcey.KauLempung.User.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener listener;
    ProgressDialog progressDialog;
    DatabaseReference ref;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {

                    ref = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user").child(user.getUid()).child("role");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);

                            if (value.equals("user") ){
                                Intent move = new Intent(LoginActivity.this, MainActivity.class);
                                move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(move);
                                finish();
                            } else if (value.equals("admin")){
                                Intent move = new Intent(LoginActivity.this, HomeAdmin.class);
                                move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(move);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        };
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    //Method untuk menghentikan state
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(listener);
    }

    //Method untuk memulai menjalankan aplikasi
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog.setMessage("Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //cek kondisi inputan apakah ada atau tidak
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            progressDialog.show();

            //Login sesuai dengan username dan password user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Ketika login berhasil
                    if (task.isSuccessful()) {
//                       onAuthSuccess(task.getResult().getUser());
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {

                            ref = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user").child(user.getUid()).child("role");
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);

                                    if (value.equals("user")){
                                        Intent move = new Intent(LoginActivity.this, DaftarKatalog.class);
                                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(move);
                                        finish();
                                    } else if (value.equals("admin")){
                                        Intent move = new Intent(LoginActivity.this, HomeAdmin.class);
                                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(move);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        //Ketika login gagal
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Login, Periksa username dan password anda!", Toast.LENGTH_SHORT).show();
                        _loginButton.setEnabled(true);
                    }

                    //Tutup dialog ketika login berhasil atau gagal
                    progressDialog.dismiss();
                }
            });

            //Kalau inputan kosong
        } else {
//            Snackbar.make(findViewById(R.id.rootlogin), "Data yang anda masukkan kosong!", Snackbar.LENGTH_LONG).show();
        }

        // TODO: Implement your own authentication logic here.

//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    private void onAuthSuccess(FirebaseUser user) {

        if (user != null) {

            ref = FirebaseDatabase.getInstance().getReference().child("KauLempung").child(user.getUid()).child("role");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);

                    if (value == "admin"){
                        Intent move = new Intent(LoginActivity.this, KatalogProduk.class);
                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(move);
                        finish();
                    } else {
                        Intent move1 = new Intent(LoginActivity.this, MainActivity.class);
                        move1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(move1);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Gagal", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Masukan alamat email yang valid!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _passwordText.setError("Password minimal image_4 karakter!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

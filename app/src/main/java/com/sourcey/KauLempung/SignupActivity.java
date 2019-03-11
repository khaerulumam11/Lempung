package com.sourcey.KauLempung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    ProgressDialog mProgressDialog;

    FirebaseAuth.AuthStateListener listener;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
//
        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Intent pindah = new Intent(SignupActivity.this, LoginActivity.class);
                    pindah.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    startActivity(pindah);
                }
            }
        };

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    //Method untuk activity ketika dimulai
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    //Method ketika activity berakhir
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Creating Account...");
        mProgressDialog.show();

        final String name = _nameText.getText().toString();
        final String address = _addressText.getText().toString();
        final String email = _emailText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        //Jika tidak :

        //Membuat user baru berdasarkan input user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Kondisi jika pembuatan user berhasil
                    if (task.isSuccessful()) {

                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                        String uid = current_user.getUid();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("KauLempung").child("user").child(uid);
                        HashMap<String, String> userMap = new HashMap<String, String>();

                        userMap.put("name",name);
                        userMap.put("email",email);
                        userMap.put("password", password);
                        userMap.put("alamat", address);
                        userMap.put("nohp", mobile);
                        userMap.put("role", "user");
                        userMap.put("image", "default");

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    mProgressDialog.dismiss();
                                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        });

//                        Toast.makeText(SignupActivity.this, "Akun sudah dibuat", Toast.LENGTH_SHORT).show();
//                        Intent movehome = new Intent(SignupActivity.this, LoginActivity.class);
//                        movehome.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
//                        finish();

                        //Kondisi ketika pembuatan user baru gagal
                    } else {
                        Log.w("Firebase", task.getException());
                        Toast.makeText(SignupActivity.this, "Gagal membuat akun baru!", Toast.LENGTH_SHORT).show();
                        _emailText.setText(null);
                        _passwordText.setText(null);
                    }
                    //Tutup dialog ketika kondisi berhasil atau pun tidak
                    mProgressDialog.dismiss();
                }
            });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        mProgressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(listener);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Minimal image_3 karakter");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Masukan alamat valid");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("Masukan alamat email valid");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty()) {
            _mobileText.setError("Masukan nomor telepon valid");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Masukan password antara image_4 hingga 10 karakter");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password tidak valid");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}
package com.sivajr.leato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    EditText phone,otp;
    Button send, verify;
    FirebaseAuth mAuth;
    String verificationID;
    ProgressBar bar;
    ImageView offline;
    Context context;
    String[] items = {"Python","Java"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getWindow().setStatusBarColor(ContextCompat.getColor(Login.this, R.color.black));

        phone = findViewById(R.id.mobile);
        otp = findViewById(R.id.otp);
        send = findViewById(R.id.send);
        verify = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.bar);
        offline = findViewById(R.id.offline);
        autoCompleteTxt = findViewById(R.id.auto_complete_language);

        adapterItems = new ArrayAdapter<String>(this,R.layout.item_lang,items);

        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

            }

        });

        if(!isConnected()){

            offline.setVisibility(View.VISIBLE);
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

        }

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(phone.getText().toString())) {

                    Toast.makeText(Login.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();

                }else {

                    String number = phone.getText().toString();
                    bar.setVisibility(View.VISIBLE);
                    sendverificationcode(number);

                }

            }

        });

        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(otp.getText().toString())){

                    Toast.makeText(Login.this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();

                } else {

                    verifycode(otp.getText().toString());

                }

            }

        });

    }

    private void sendverificationcode(String phoneNumber) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91"+phoneNumber)  // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            final String code = credential.getSmsCode();

            if(code!=null) {

                verifycode(code);

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Login.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {

            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(Login.this, "Code sent", Toast.LENGTH_SHORT).show();
            verify.setEnabled(true);
            bar.setVisibility(View.INVISIBLE);

        }};

    private void verifycode(String Code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,Code);
        signinbyCredentials(credential);

    }

    private void signinbyCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                String language = autoCompleteTxt.getText().toString();

                if (task.isSuccessful() && language.equals("Java")) {

                    Toast.makeText(Login.this, "verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Java.class));
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                } else if (task.isSuccessful() && language.equals("Python")) {

                    Toast.makeText(Login.this, "verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Python.class));
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                } else if (task.isSuccessful()) {

                    Toast.makeText(Login.this, "verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null) {

            startActivity(new Intent(Login.this, Splash.class));
            finish();

        }

    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }

    @Override
    public void onBackPressed() {

        finishAffinity();

    }

}
package com.example.autowaiter.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autowaiter.CheckInternet;
import com.example.autowaiter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Action();
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void Action(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() == 0) {
                    email.setError("name required");
                    email.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (CheckInternet.isConnectToInternet(getBaseContext())) {


                    SignUser(email.getText().toString(), password.getText().toString());

                } else {
                    Toast.makeText(Login.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void SignUser(String Email, String Password) {


        firebaseAuth.signInWithEmailAndPassword(Email,Password ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                startActivity(new Intent(Login.this,Dashboard.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Faild", Toast.LENGTH_SHORT).show();

            }
        });

    }


}

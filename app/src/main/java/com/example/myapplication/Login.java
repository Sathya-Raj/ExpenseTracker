package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText l_email,l_pass;
    Button sign_In;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        l_email=(EditText)findViewById(R.id.l_email);
        l_pass=(EditText) findViewById(R.id.l_pass);
        sign_In=(Button) findViewById(R.id.sign_In);

        sign_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=l_email.getText().toString().trim();
                String password=l_pass.getText().toString().trim();
                if (email.length()<=0||password.length()<=0){
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this,"login Succesful",Toast.LENGTH_LONG).show();
                        try {
                            startActivity(new Intent(Login.this,Mainpage.class));
                            l_email.setText("");
                            l_pass.setText("");
                            finish();
                        }
                        catch (Exception e){
                            Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
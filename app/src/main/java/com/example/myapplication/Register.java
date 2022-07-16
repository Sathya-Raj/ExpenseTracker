package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    EditText username,password,email;
    TextView acnt_exist;
    Button signIn;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText) findViewById(R.id.uname);
        password=(EditText) findViewById(R.id.pass);
        email=(EditText) findViewById(R.id.email);
        signIn=(Button) findViewById(R.id.create_acnt);
        signIn.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        acnt_exist=(TextView) findViewById(R.id.acnt_exists);
        acnt_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this,Login.class);
                try {
                    startActivity(intent);
                }catch (Exception e){

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        String uname=username.getText().toString();
        String u_email=email.getText().toString();
        String u_pass=password.getText().toString();
        if (u_email.trim().length()<=0||u_pass.trim().length()<=0){
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(u_email,u_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Register.this,"User Created!!",Toast.LENGTH_SHORT).show();
                Map<String,Object> userDetailes=new HashMap<>();
                userDetailes.put("username",uname);
                userDetailes.put("useremail",u_email);

                fStore.collection("UserDetails").document(firebaseAuth.getUid()).set(userDetailes).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Register.this,"Added",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
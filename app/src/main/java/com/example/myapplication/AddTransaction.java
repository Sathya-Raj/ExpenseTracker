package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransaction extends AppCompatActivity {
    EditText u_amount,u_note;
    Button add_trans;
    RadioGroup radio_FlagG,radio_TypeG;
    RadioButton radio_flag,radio_type;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_transaction);
        u_amount=(EditText)findViewById(R.id.u_amount);
        u_note=(EditText) findViewById(R.id.u_note);
        add_trans=(Button) findViewById(R.id.add_trans);
        radio_FlagG=(RadioGroup)findViewById(R.id.radio_flag);
        radio_TypeG=(RadioGroup)findViewById(R.id.radio_TypeG);
        fStore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        add_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=u_amount.getText().toString();
                String note=u_note.getText().toString();
                int selected_flag= radio_FlagG.getCheckedRadioButtonId();
                int selected_type= radio_TypeG.getCheckedRadioButtonId();
                if (amount.length()<=0){
                    Toast.makeText(AddTransaction.this,"Enter the transaction amount",Toast.LENGTH_LONG).show();
                    return;
                }
                String rtype;
                if (selected_type<0){
                    Toast.makeText(AddTransaction.this,"Select transaction type",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    radio_type=(RadioButton) findViewById(selected_type) ;
                     rtype=radio_type.getText().toString();
                }

                String rflag;
                if (selected_flag<0){
                    Toast.makeText(AddTransaction.this,"Flag the transaction",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    radio_flag=(RadioButton) findViewById(selected_flag) ;
                     rflag=radio_flag.getText().toString();
                }
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());
                String currentdatetime=simpleDateFormat.format(new Date());

                String id= UUID.randomUUID().toString();
                Map<String,Object>transaction=new HashMap<>();
                transaction.put("id",id);
                transaction.put("amount",amount);
                transaction.put("note",note);
                transaction.put("type",rtype);
                transaction.put("flag",rflag);
                transaction.put("datetime",currentdatetime);
                fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddTransaction.this,"Added",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTransaction.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });




            }
        });
    }
}
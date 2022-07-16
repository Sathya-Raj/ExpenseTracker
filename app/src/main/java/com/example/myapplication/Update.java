package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Update extends AppCompatActivity {
    EditText u_amount,u_note;
    Button update,delete;
    RadioButton income,expense,shopping,food,education,fuel,health,lend,party;
    RadioGroup radio_FlagG,radio_TypeG;
    RadioButton radio_flag,radio_type;
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        firebaseAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        income=findViewById(R.id.income);
        expense=findViewById(R.id.expense);
        food=findViewById(R.id.food);
        education=findViewById(R.id.education);
        fuel=findViewById(R.id.fuel);
        health=findViewById(R.id.health);
        lend=findViewById(R.id.moneyLent);
        party=findViewById(R.id.party);
        shopping=findViewById(R.id.shopping);
        u_amount=(EditText)findViewById(R.id.u_amount);
        u_note=(EditText) findViewById(R.id.u_note);
        update=(Button) findViewById(R.id.update);
        delete=(Button) findViewById(R.id.Delete);
        radio_FlagG=(RadioGroup)findViewById(R.id.radio_flag);
        radio_TypeG=(RadioGroup)findViewById(R.id.radio_TypeG);
        String id=getIntent().getStringExtra("id");
        String amount=getIntent().getStringExtra("amount");
        String note=getIntent().getStringExtra("note");
        String type=getIntent().getStringExtra("type");
        String flag=getIntent().getStringExtra("flag");
        u_amount.setText(amount);
        u_note.setText(note);
        String newType;
        switch (type){
            case "EXPENSE":

                expense.setChecked(true);
                break;
            case "INCOME":
                income.setChecked(true);
                break;

        }

        switch (flag){
            case "Shopping":

                shopping.setChecked(true);
                break;
            case "Food":

                food.setChecked(true);
                break;
            case "Fuel":

                fuel.setChecked(true);
                break;
            case "Education":

                education.setChecked(true);
                break;
            case "Health":

                health.setChecked(true);
                break;
            case "Lend":

                lend.setChecked(true);
                break;
            case "Party":

                party.setChecked(true);
                break;
       }


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=u_amount.getText().toString();
                String note=u_note.getText().toString();
                int selected_flag= radio_FlagG.getCheckedRadioButtonId();
                int selected_type= radio_TypeG.getCheckedRadioButtonId();
                if (amount.length()<=0){
                    Toast.makeText(Update.this,"Enter the transaction amount",Toast.LENGTH_LONG).show();
                    return;
                }
                String rtype;
                if (selected_type<0){
                    Toast.makeText(Update.this,"Select transaction type",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    radio_type=(RadioButton) findViewById(selected_type) ;
                    rtype=radio_type.getText().toString();
                }

                String rflag;
                if (selected_flag<0){
                    Toast.makeText(Update.this,"Flag the transaction",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    radio_flag=(RadioButton) findViewById(selected_flag) ;
                    rflag=radio_flag.getText().toString();
                }

                fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).
                        update("amount",amount,"note",note,"type",rtype,"flag",rflag).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(Update.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Update.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialogue();
            }

            private void deleteDialogue() {
                AlertDialog.Builder builder= new AlertDialog.Builder(Update.this);
                builder.setTitle("Delete Transaction").setMessage("Are you sure you want to delete this transaction ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).
                                        delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                onBackPressed();
                                                Toast.makeText(Update.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Update.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create().show();
            }
        });


    }
}




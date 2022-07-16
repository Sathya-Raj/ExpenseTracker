package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;


public class Mainpage extends AppCompatActivity  {
    HashMap<String,Integer> flagIncamount=new HashMap<>();
    HashMap<String,Integer> flagExpamount=new HashMap<>();
    Button Add_btn;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;
    RecyclerView recyclerView;
    TextView netbalance,netExp,netInc,userGreeting;
    SwipeRefreshLayout swipeRefreshLayout;
   ImageButton logout;
   PieChart pieChart;
    int sumInc,sumExp,netBal;
    RadioButton income,expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        pieChart=findViewById(R.id.piechart);
        income=findViewById(R.id.incomeAnalysis);
        expense=findViewById(R.id.expenseAnalysis);
        expense.setChecked(true);
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChart(flagExpamount,"EXPENSE");
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChart(flagIncamount,"INCOME");
            }
        });
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(Mainpage.this,Mainpage.class));
                finish();
                transactionAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        netbalance=findViewById(R.id.netbalance_amt);
        netInc=findViewById(R.id.netincome_amt);
        netExp=findViewById(R.id.netexpense_amt);
        userGreeting=findViewById(R.id.usergreeting);
        Add_btn= (Button) findViewById(R.id.fab);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        transactionModelArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.history_recycler_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logoutDialogue();
            }


        });
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(Mainpage.this,MainActivity.class));
                    finish();
                }
            }
        });
        Add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Mainpage.this, AddTransaction.class));
            }
        });
        loadData();
        System.out.println("HEllooooooo");
        for (String name: flagExpamount.keySet()) {
            String key = name;
            int value = flagExpamount.get(name);
            System.out.println(key+value);

        }







    }

    private void setChart(HashMap<String,Integer> amtDetails,String type) {

        ArrayList<PieEntry>Flagtype=new ArrayList<>();
        for (String name: amtDetails.keySet()) {
            String key = name;
            int value = amtDetails.get(name);
            Flagtype.add(new PieEntry(value,key));

        }
        PieDataSet pieDataSet=new PieDataSet(Flagtype,"Transaction Flags");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);
        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(type);
        pieChart.setNoDataTextColor(Color.WHITE);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(0);
        pieChart.clearAnimation();
        pieChart.setCenterTextColor(Color.WHITE);
        Legend legend=pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(10f);
        pieChart.invalidate();
        pieChart.refreshDrawableState();




    }

    private void logoutDialogue() {

        AlertDialog.Builder builder= new AlertDialog.Builder(Mainpage.this);
        builder.setTitle("Log Out").setMessage("Are you sure you want to logout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        builder.create().show();
    }

    private void loadData() {
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot ds:task.getResult()){


                    TransactionModel model =new TransactionModel(
                            ds.getString("id"),
                            ds.getString("note"),
                            ds.getString("type"),
                            ds.getString("flag"),
                            ds.getString("amount"),
                            ds.getString("datetime")
                    );
                    transactionModelArrayList.add(model);
                    int amount=Integer.parseInt(ds.getString("amount"));
                    String flag=ds.getString("flag");
                    if(ds.getString("type").equals("EXPENSE")){
                        sumExp+=amount;
                        if(flagExpamount.containsKey(flag)){
                            flagExpamount.put(flag,(flagExpamount.get(flag)+amount));
                        }
                        else{
                            flagExpamount.put(flag,amount);
                        }
                    }
                    else{
                        sumInc+=amount;
                        if(flagIncamount.containsKey(flag)){
                            flagIncamount.put(flag,(flagIncamount.get(flag)+amount));
                        }
                        else{
                            flagIncamount.put(flag,amount);
                        }
                    }



                }
                setChart(flagExpamount,"EXPENSE");
                netBal=sumInc-sumExp;
                netbalance.setText(String.valueOf(netBal));
                netInc.setText(String.valueOf(sumInc));
                netExp.setText(String.valueOf(sumExp));
                transactionAdapter=new TransactionAdapter(Mainpage.this,transactionModelArrayList);
                recyclerView.setAdapter(transactionAdapter);
            }
        });

        firebaseFirestore.collection("UserDetails").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               DocumentSnapshot ds= task.getResult();
               String user=ds.getString("username");
               userGreeting.setText(new StringBuilder().append("Hi,").append(user).toString());
            }
        });

    }


}
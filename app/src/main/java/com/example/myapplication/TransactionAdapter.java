package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;

import io.grpc.internal.SharedResourceHolder;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    Context context;
    ArrayList <TransactionModel> transactionModelArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TransactionModel model =transactionModelArrayList.get(position);
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDatetime());
        holder.note.setText(model.getNote());
        String type= model.getType();
        if (type.equals("EXPENSE")){
            holder.trType.setBackgroundResource(R.drawable.type_exp);
        }
        else{
            holder.trType.setBackgroundResource(R.drawable.type_inc);
        }
        String flag=model.getFlag();
        if(flag.equals("Shopping"))holder.flag.setBackgroundResource(R.drawable.ic_shoping);
        if(flag.equals("Food"))holder.flag.setBackgroundResource(R.drawable.ic_food);
        if(flag.equals("Fuel"))holder.flag.setBackgroundResource(R.drawable.ic_fuel);
        if(flag.equals("Education"))holder.flag.setBackgroundResource(R.drawable.ic_education);
        if(flag.equals("Health"))holder.flag.setBackgroundResource(R.drawable.ic_health);
        if(flag.equals("Lend"))holder.flag.setBackgroundResource(R.drawable.ic_lend);
        if(flag.equals("Party"))holder.flag.setBackgroundResource(R.drawable.ic_baseline_celebration_24);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent= new Intent(context,Update.class);
                intent.putExtra("id",transactionModelArrayList.get(position).getId());
                intent.putExtra("amount",transactionModelArrayList.get(position).getAmount());
                intent.putExtra("note",transactionModelArrayList.get(position).getNote());
                intent.putExtra("type",transactionModelArrayList.get(position).getType());
                intent.putExtra("flag",transactionModelArrayList.get(position).getFlag());
                context.startActivity(intent);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note,amount,date;
        View trType,flag;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.note);
            amount=itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.datetime);
            flag=itemView.findViewById(R.id.flag);

            trType=itemView.findViewById(R.id.trtype);

        }
    }
}
;
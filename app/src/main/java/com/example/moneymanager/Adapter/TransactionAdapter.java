package com.example.moneymanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Model.TransactionModel;
import com.example.moneymanager.R;
import com.example.moneymanager.TransactionPage;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.transactionviewholder> {


    List<TransactionModel> transactionModelList;
    Context context;

    int Amount=0;

    public TransactionAdapter(List<TransactionModel> transactionModelList, Context context) {
        this.transactionModelList = transactionModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionAdapter.transactionviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new transactionviewholder(LayoutInflater.from(context).inflate(R.layout.transactionitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull transactionviewholder holder, int position) {
        TransactionModel TM = transactionModelList.get(position);
        holder.amountdisplay.setText("₹"+TM.getAmount());
        holder.tvdatedisplay.setText(TM.getTransactiondate());
        if(!(TextUtils.isEmpty(TM.getNotes()))) {
            holder.transactiondescription.setText("For : " + TM.getNotes());
        }


        try {

            holder.categorydisplay.setText("Category : " + TM.getCategory());

            holder.transactionitemid.setCardBackgroundColor(Color.parseColor(TM.getCategoryColor()));
        }
        catch (Exception e){}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args1 = new Bundle();
                args1.putBoolean("UpdateTransaction",true);
                args1.putString("TransactionAmount",TM.getAmount());
                args1.putString("CategoryColor",TM.getCategoryColor());
                args1.putString("CategoryName",TM.getCategory());
                args1.putString("TransactionDescription",TM.getNotes());
                args1.putString("TransactionID",TM.getKey());
                args1.putString("TransactionDate",TM.getTransactiondate());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, TransactionPage.class,args1)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }



    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class transactionviewholder extends RecyclerView.ViewHolder
    {
        TextView amountdisplay,tvdatedisplay,categorydisplay,transactiondescription;

        CardView transactionitemid;

        public transactionviewholder(@NonNull View itemView) {
            super(itemView);

            amountdisplay = itemView.findViewById(R.id.amountdisplay);
            tvdatedisplay = itemView.findViewById(R.id.tvdatedisplay);
            categorydisplay = itemView.findViewById(R.id.categorydisplay);
            transactionitemid = itemView.findViewById(R.id.transactionitemid);
            transactiondescription = itemView.findViewById(R.id.transactiondescription);
        }
    }
}

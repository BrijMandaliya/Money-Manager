package com.example.moneymanager.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Model.TransactionModel;
import com.example.moneymanager.R;
import com.example.moneymanager.TransactionPage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class firebasetransactionadapater extends FirebaseRecyclerAdapter<TransactionModel,firebasetransactionadapater.transactionviewholder> {

    String key;

    FirebaseRecyclerOptions<TransactionModel> options;
    public firebasetransactionadapater(@NonNull FirebaseRecyclerOptions<TransactionModel> options) {
        super(options);
        this.options = options;

        Log.d("Optionreachchcek",options.toString());
    }

    @Override
    protected void onBindViewHolder(@NonNull transactionviewholder holder, int position, @NonNull TransactionModel model) {
        TransactionModel TM = model;
        AppCompatActivity activity1 = (AppCompatActivity) holder.itemView.getContext();
        if(TM==null)
        {
            Toast.makeText(activity1, " " + TM.getCategory(), Toast.LENGTH_SHORT).show();
        }
        holder.amountdisplay.setText("₹"+model.getAmount());
        holder.tvdatedisplay.setText(model.getTransactiondate());
        if(!(TextUtils.isEmpty(TM.getNotes()))) {
            holder.transactiondescription.setText("For : " + model.getNotes());
        }
        holder.categorydisplay.setText("Category : "+model.getCategory());

        holder.transactionitemid.setCardBackgroundColor(Color.parseColor(model.getCategoryColor()));







        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args1 = new Bundle();
                args1.putBoolean("UpdateTransaction",true);
                args1.putString("TransactionAmount",TM.getAmount());
                args1.putString("CategoryColor",TM.getCategoryColor());
                args1.putString("CategoryName",TM.getCategory());
                args1.putString("TransactionDescription",TM.getNotes());
                args1.putString("TransactionID",String.valueOf(options.getSnapshots().getSnapshot(position).getKey()));
                args1.putString("TransactionDate",TM.getTransactiondate());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, TransactionPage.class,args1)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @NonNull
    @Override
    public transactionviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionitem,parent,false);
        return new transactionviewholder(v);
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

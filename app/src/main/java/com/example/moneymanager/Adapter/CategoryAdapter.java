package com.example.moneymanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.R;
import com.example.moneymanager.TransactionPage;
import com.example.moneymanager.addcategory;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryviewholder> {

    List<HashMap> categoryitem;
    Context context;

    Boolean pricecheck,updatecategory;

    TextView Tcategoryname;




    public CategoryAdapter(List<HashMap> categoryitem, Context context,boolean pricecheck,boolean updatecategory) {
        this.categoryitem = categoryitem;
        this.context = context;
        this.pricecheck = pricecheck;
        this.updatecategory = updatecategory;
    }

    @NonNull
    @Override
    public CategoryAdapter.categoryviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new categoryviewholder(LayoutInflater.from(context).inflate(R.layout.categoryitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.categoryviewholder holder, int position) {
        HashMap hashMap = categoryitem.get(position);
        holder.CategoryName.setText(hashMap.get("CategoryName").toString());
        holder.CategoryColor.setBackgroundColor(Color.parseColor(hashMap.get("CategoryColor").toString()));



        if(updatecategory==true)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args1 = new Bundle();
                    args1.putString("CategoryName",hashMap.get("CategoryName").toString());
                    args1.putString("CategoryColor",hashMap.get("CategoryColor").toString());
                    args1.putString("CategoryDescription",hashMap.get("CategoryDescription").toString());
                    args1.putString("CategoryID",hashMap.get("CategoryID").toString());
                    args1.putBoolean("UpdateCategory",true);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.homepagefragment, addcategory.class,args1)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args1 = new Bundle();
                    args1.putString("CategoryName",hashMap.get("CategoryName").toString());
                    args1.putString("CategoryColor",hashMap.get("CategoryColor").toString());
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().popBackStackImmediate("FromTransaction", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    TransactionPage transactionPage = new TransactionPage();
                    transactionPage.selectedcategory = activity.findViewById(R.id.selectedcategory);
                    transactionPage.selectedcategory.setText(hashMap.get("CategoryName").toString());
                    transactionPage.categoryimg = activity.findViewById(R.id.categoryimg);
                    transactionPage.categoryimg.setBackgroundColor(Color.parseColor(hashMap.get("CategoryColor").toString()));
                    transactionPage.categorycolor =  hashMap.get("CategoryColor").toString();

                }
            });
        }


            if(pricecheck)
            {
                holder.categoyrcarditem.setClickable(false);
                holder.categoryamountdisplay.setText(hashMap.get("CategoryPrice").toString());
            }



    }

    @Override
    public int getItemCount() {
        return categoryitem.size();
    }

    public class categoryviewholder extends RecyclerView.ViewHolder
    {
        TextView CategoryName,categoryamountdisplay;

        ImageView CategoryColor;

        CardView categoyrcarditem;
        public categoryviewholder(@NonNull View itemView) {
            super(itemView);

            CategoryColor = itemView.findViewById(R.id.categorylistpagecategorycolor);
            categoryamountdisplay = itemView.findViewById(R.id.categoryamountdisplay);
            CategoryName = itemView.findViewById(R.id.categorylistpagecategoryname);
            categoyrcarditem = itemView.findViewById(R.id.categoyrcarditem);
        }
    }
}

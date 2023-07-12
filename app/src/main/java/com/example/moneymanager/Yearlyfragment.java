package com.example.moneymanager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Adapter.CategoryAdapter;
import com.example.moneymanager.Model.TransactionModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Yearlyfragment extends Fragment {

    TextView showcurrentyear,yearlyamount;

    PieChart categoryyearpiechart;

    int categoryamount,yearlyamounttotal;

    List<PieEntry> pieEntryList = new ArrayList<>();

    RelativeLayout yearlynocontent,yearlycontent,yearlycategorycontent;

    RecyclerView categorywiseitemsyearly;

    CategoryAdapter categoryAdapter;

    List<HashMap> categorylist = new ArrayList<>();

    List<Integer> piechartcolor;
    ImageButton rightbtn,leftbtn;

    FirebaseUser fuser;
    DatabaseReference fdb,fdb1;

    String currentyear;

    ShimmerFrameLayout yearlycategoryshimmer,yearlypriceshimmer;

    int i =0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yearlyfragment, container, false);
        categorywiseitemsyearly = v.findViewById(R.id.categorywiseitemsyearly);
        categorywiseitemsyearly.setLayoutManager(new LinearLayoutManager(v.getContext()));
        yearlynocontent = v.findViewById(R.id.yearlynocontent);
        yearlycontent = v.findViewById(R.id.yearlycontent);
        rightbtn = v.findViewById(R.id.rightbtnforyear);
        leftbtn = v.findViewById(R.id.leftbtnforyear);
        showcurrentyear = v.findViewById(R.id.specificyear);
        categoryyearpiechart = v.findViewById(R.id.categoryyearpiechart);
        yearlyamount = v.findViewById(R.id.yearlyamount);
        yearlycategoryshimmer = v.findViewById(R.id.yearlycategoryshimmer);
        yearlypriceshimmer = v.findViewById(R.id.yearlypriceshimmer);
        yearlycategorycontent = v.findViewById(R.id.yearlycategorycontent);

        yearlypriceshimmer.setVisibility(View.VISIBLE);
        yearlypriceshimmer.startShimmerAnimation();

        yearlycategoryshimmer.setVisibility(View.VISIBLE);
        yearlycategoryshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            yearlypriceshimmer.stopShimmerAnimation();
            yearlypriceshimmer.setVisibility(View.GONE);
            yearlycategoryshimmer.stopShimmerAnimation();
            yearlycategoryshimmer.setVisibility(View.GONE);

            yearlyamount.setVisibility(View.VISIBLE);
            yearlycategorycontent.setVisibility(View.VISIBLE);
        },2500);

        return v;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DateFormat dateFormat = new SimpleDateFormat("YYYY");
        Date date = new Date();
        showcurrentyear.setText(dateFormat.format(date));
        currentyear = dateFormat.format(date);
        showyeardata(currentyear);





        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                Calendar cal = Calendar.getInstance();
                Date date = new Date();
                cal.setTime(date);
                cal.add(Calendar.YEAR,i);
                showcurrentyear.setText(dateFormat.format(cal.getTime()));
                currentyear = dateFormat.format(cal.getTime());
                showyeardata(currentyear);
            }
        });

        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                Calendar cal = Calendar.getInstance();
                Date date = new Date();
                cal.setTime(date);
                cal.add(Calendar.YEAR,i);
                showcurrentyear.setText(dateFormat.format(cal.getTime()));
                currentyear = dateFormat.format(cal.getTime());
                showyeardata(currentyear);
            }
        });
    }

    private void showyeardata(String showcurrentyear) {
        piechartcolor = new ArrayList<Integer>();
        yearlyamounttotal=0;
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fdb = FirebaseDatabase.getInstance().getReference("Category").child(fuser.getUid());

        fdb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                pieEntryList.clear();
                categoryyearpiechart.clear();
                categorylist.clear();
                for(DataSnapshot sp : task.getResult().getChildren())
                {

                    HashMap categorydata = (HashMap)sp.getValue();
                    if(categorydata!=null)
                    {
                        getcategorysdata(categorydata,showcurrentyear);
                    }
                }
            }
        });
    }

    private void getcategorysdata(HashMap categorydata, String showcurrentyear) {

        fdb1 = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
        fdb1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot sp : task.getResult().getChildren())
                {
                    TransactionModel transactionModel = sp.getValue(TransactionModel.class);
                    if(transactionModel!=null)
                    {
                        if(TextUtils.equals(showcurrentyear,transactionModel.getTransactionyear())) {
                            yearlycontent.setVisibility(View.VISIBLE);
                            yearlynocontent.setVisibility(View.GONE);
                            if (TextUtils.equals(transactionModel.getCategory(), categorydata.get("CategoryName").toString())) {
                                yearlyamounttotal += Integer.parseInt(transactionModel.getAmount());
                                categoryamount += Integer.parseInt(transactionModel.getAmount());
                            }
                        }
                        else if (categorylist.size()==0) {
                            yearlycontent.setVisibility(View.GONE);
                            yearlynocontent.setVisibility(View.VISIBLE);

                        }
                    }
                }
                yearlyamount.setText("₹"+String.valueOf(yearlyamounttotal));
            }
        }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(categoryamount != 0)
                    {
                        String categorynamepieentry = categorydata.get("CategoryName").toString();
                        String categorycolorpieentry = categorydata.get("CategoryColor").toString();
                        pieEntryList.add(new PieEntry(categoryamount,categorynamepieentry));
                        piechartcolor.add(Color.parseColor(categorycolorpieentry));
                        PieDataSet pieDataSet = new PieDataSet(pieEntryList,null);
                        pieDataSet.setColors(piechartcolor);
                        pieDataSet.setValueTextSize(getResources().getColor(R.color.black));
                        PieData pieData = new PieData(pieDataSet);
                        pieData.setValueTextSize(10f);
                        categoryyearpiechart.setEntryLabelColor(Color.BLACK);
                        categoryyearpiechart.animateY(1200, Easing.EaseInOutQuad);
                        categoryyearpiechart.setEntryLabelTextSize(10f);
                        categoryyearpiechart.setDescription(null);
                        categoryyearpiechart.setData(pieData);
                        categoryyearpiechart.invalidate();

                        HashMap hashMap = new HashMap();
                        hashMap.put("CategoryName",categorynamepieentry);
                        hashMap.put("CategoryColor",categorycolorpieentry);
                        hashMap.put("CategoryPrice",String.valueOf(categoryamount));

                        categorylist.add(hashMap);
                        categoryAdapter = new CategoryAdapter(categorylist,getActivity(),true,false);
                        categorywiseitemsyearly.setAdapter(categoryAdapter);

                    }
                    categoryamount = 0;
                }
            }
        });
    }
}
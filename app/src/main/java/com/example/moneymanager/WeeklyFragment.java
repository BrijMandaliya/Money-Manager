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


public class WeeklyFragment extends Fragment {

    TextView showcurrentweek,weeklymaount;


    RecyclerView categorywiseitemsweekly;

    CategoryAdapter categoryAdapter;

    List<HashMap> categorylist = new ArrayList<>();

    ImageButton rightbtn,leftbtn;


    List<TransactionModel> transactionModelList;

    List<PieEntry> pieEntryList = new ArrayList<>();


    RelativeLayout weeklynocontentlayout,weeklycontentlayout,weeklycategorycontent;

    PieChart categoryweekpiechart;

    String currentweek;


    List<Integer> piechartcolor;

    int weeklytotalamount=0,categoryamount,i=0;

    FirebaseUser fuser;
    DatabaseReference fdb,fdb1;

    ShimmerFrameLayout weeklypriceshimmer,weeklycategoryshimmer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weekly, container, false);
        categorywiseitemsweekly = v.findViewById(R.id.categorywiseitemsweekly);
        categorywiseitemsweekly.setLayoutManager(new LinearLayoutManager(v.getContext()));
        showcurrentweek = v.findViewById(R.id.specificweek);
        weeklynocontentlayout = v.findViewById(R.id.weeklynocontentlayout);
        weeklycontentlayout = v.findViewById(R.id.weeklycontentlayout);
        rightbtn = v.findViewById(R.id.rightbtnforweek);
        leftbtn = v.findViewById(R.id.leftbtnforweek);
        categoryweekpiechart = v.findViewById(R.id.categoryweekpiechart);
        weeklymaount = v.findViewById(R.id.weeklyamount);
        weeklycategoryshimmer = v.findViewById(R.id.weeklycategoryshimmer);
        weeklypriceshimmer = v.findViewById(R.id.weeklypriceshimmer);
        weeklycategorycontent = v.findViewById(R.id.weeklycategorycontent);

        weeklypriceshimmer.setVisibility(View.VISIBLE);
        weeklypriceshimmer.startShimmerAnimation();

        weeklycategoryshimmer.setVisibility(View.VISIBLE);
        weeklycategoryshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            weeklypriceshimmer.stopShimmerAnimation();
            weeklypriceshimmer.setVisibility(View.GONE);
            weeklycategoryshimmer.stopShimmerAnimation();
            weeklycategoryshimmer.setVisibility(View.GONE);

            weeklymaount.setVisibility(View.VISIBLE);
            weeklycategorycontent.setVisibility(View.VISIBLE);
        },2500);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryamount = 0;
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM");
        Date date = new Date();
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal1.set(Calendar.DAY_OF_WEEK, 8);
        cal1.add(Calendar.DAY_OF_WEEK,7);
        showcurrentweek.setText(dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime()));
        currentweek = dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime());
        showdata(currentweek);




        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=i+7;
                cal.add(Calendar.DAY_OF_WEEK,7);
                cal1.add(Calendar.DAY_OF_WEEK,7);
                showcurrentweek.setText(dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime()));
                currentweek = dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime());
                showdata(currentweek);
            }
        });

        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=i-7;
                cal.add(Calendar.DAY_OF_WEEK,-7);
                cal1.add(Calendar.DAY_OF_WEEK,-7);
                showcurrentweek.setText(dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime()));
                currentweek = dateFormat.format(cal.getTime()) + " - " + dateFormat.format(cal1.getTime());
                showdata(currentweek);
            }
        });

    }

    public void showdata(String week)
    {
        transactionModelList = new ArrayList<>();
        piechartcolor = new ArrayList<Integer>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fdb = FirebaseDatabase.getInstance().getReference("Category").child(fuser.getUid());

        fdb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                pieEntryList.clear();
                categoryweekpiechart.clear();
                categorylist.clear();
                weeklytotalamount=0;
                for(DataSnapshot sp : task.getResult().getChildren())
                {

                    HashMap categorydata = (HashMap)sp.getValue();
                    if(categorydata!=null)
                    {
                        getcategorysdata(categorydata,week);
                    }
                }
            }
        });
    }

    private void getcategorysdata(HashMap categorydata, String week) {
        fdb1 = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
        fdb1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for(DataSnapshot sp : task.getResult().getChildren())
                    {
                        TransactionModel transactionModel = sp.getValue(TransactionModel.class);
                        if(transactionModel!=null)
                        {

                            if(TextUtils.equals(week,transactionModel.getTransactionweek())) {

                                weeklycontentlayout.setVisibility(View.VISIBLE);
                                weeklynocontentlayout.setVisibility(View.GONE);
                                if (TextUtils.equals(transactionModel.getCategory(), categorydata.get("CategoryName").toString())) {
                                    weeklytotalamount += Integer.parseInt(transactionModel.getAmount());
                                    categoryamount += Integer.parseInt(transactionModel.getAmount());
                                }
                            }
                            else if (categorylist.size()==0)
                            {
                                weeklycontentlayout.setVisibility(View.GONE);
                                weeklynocontentlayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }


            }
        }).addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                weeklymaount.setText("₹"+String.valueOf(weeklytotalamount));


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
                        pieDataSet.setValueTextColor(getResources().getColor(R.color.black));
                        PieData pieData = new PieData(pieDataSet);
                        pieData.setValueTextSize(10f);
                        categoryweekpiechart.setEntryLabelColor(Color.BLACK);
                        categoryweekpiechart.animateY(1200, Easing.EaseInOutQuad);
                        categoryweekpiechart.setDescription(null);
                        categoryweekpiechart.setData(pieData);
                        categoryweekpiechart.invalidate();

                        HashMap hashMap = new HashMap();
                        hashMap.put("CategoryName",categorydata.get("CategoryName").toString());
                        hashMap.put("CategoryColor",categorydata.get("CategoryColor").toString());
                        hashMap.put("CategoryPrice",String.valueOf(categoryamount));
                        categorylist.add(hashMap);

                        categoryAdapter = new CategoryAdapter(categorylist,getActivity(),true,false);
                        categorywiseitemsweekly.setAdapter(categoryAdapter);
                    }
                    categoryamount = 0;
                }
            }
        });
    }
}
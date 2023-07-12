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

public class MonthlyFragment extends Fragment {

    PieChart categorymonthpiechart;

    int categoryamount,monthlytotalamount;

    List<HashMap> categorylist = new ArrayList<>();

    List<PieEntry> pieEntryList = new ArrayList<>();

    RelativeLayout monthlynocontent,monthlycontent,categorycontent;

    List<Integer> piechartcolor;

    CategoryAdapter categoryAdapter;

    RecyclerView categorywiseitems;

    FirebaseUser fuser;
    DatabaseReference fdb,fdb1;

    TextView showcurrentmonth,monthlyamount;

    ImageButton rightbtn,leftbtn;

    String currentmonth;

    ShimmerFrameLayout monthlypriceshimmer,monthlycategoryshimmer;

    int i=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthly, container, false);
        categorywiseitems = v.findViewById(R.id.categorywiseitems);
        categorywiseitems.setLayoutManager(new LinearLayoutManager(v.getContext()));
        monthlynocontent = v.findViewById(R.id.monthlynocontent);
        monthlycontent = v.findViewById(R.id.monthlycontent);
        showcurrentmonth = v.findViewById(R.id.specificmonth);
        rightbtn = v.findViewById(R.id.rightbtnformonth);
        leftbtn = v.findViewById(R.id.leftbtnformonth);
        categorymonthpiechart = v.findViewById(R.id.categorymonthpiechart);
        monthlyamount = v.findViewById(R.id.monthlyamount);
        monthlypriceshimmer = v.findViewById(R.id.monthlypriceshimmer);
        monthlycategoryshimmer = v.findViewById(R.id.monthlycategoryshimmer);
        categorycontent = v.findViewById(R.id.categorycontent);



        monthlypriceshimmer.setVisibility(View.VISIBLE);
        monthlypriceshimmer.startShimmerAnimation();

        monthlycategoryshimmer.setVisibility(View.VISIBLE);
        monthlycategoryshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            monthlypriceshimmer.stopShimmerAnimation();
            monthlypriceshimmer.setVisibility(View.GONE);
            monthlycategoryshimmer.stopShimmerAnimation();
            monthlycategoryshimmer.setVisibility(View.GONE);

            monthlyamount.setVisibility(View.VISIBLE);
            categorycontent.setVisibility(View.VISIBLE);
        },2500);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DateFormat dateFormat = new SimpleDateFormat("LLLL YYYY");
        Date date = new Date();
        showcurrentmonth.setText(dateFormat.format(date));
        currentmonth = dateFormat.format(date);
        showmonthdata(currentmonth);

        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                Calendar cal = Calendar.getInstance();
                Date date = new Date();
                cal.setTime(date);
                cal.add(Calendar.MONTH,i);
                showcurrentmonth.setText(dateFormat.format(cal.getTime()));
                currentmonth = dateFormat.format(cal.getTime());
                showmonthdata(currentmonth);
            }
        });

        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                Calendar cal = Calendar.getInstance();
                Date date = new Date();
                cal.setTime(date);
                cal.add(Calendar.MONTH,i);
                showcurrentmonth.setText(dateFormat.format(cal.getTime()));
                currentmonth = dateFormat.format(cal.getTime());
                showmonthdata(currentmonth);
            }
        });
    }

    private void showmonthdata(String currentmonth) {
        piechartcolor = new ArrayList<Integer>();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fdb = FirebaseDatabase.getInstance().getReference("Category").child(fuser.getUid());

        fdb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                pieEntryList.clear();
                categorymonthpiechart.clear();
                categorylist.clear();
                monthlytotalamount=0;;
                piechartcolor.clear();

                for(DataSnapshot sp : task.getResult().getChildren())
                {
                    HashMap categorydata = (HashMap)sp.getValue();
                    if(categorydata!=null)
                    {
                        getcategorysdata(categorydata,currentmonth);
                    }
                }

            }
        });
    }

    private void getcategorysdata(HashMap categorydata, String currentmonth) {

        fdb1 = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
        fdb1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot sp : task.getResult().getChildren())
                {
                    TransactionModel transactionModel = sp.getValue(TransactionModel.class);
                    if(transactionModel!=null)
                    {
                        if(TextUtils.equals(currentmonth,transactionModel.getTransactionmonth()) ) {
                            monthlycontent.setVisibility(View.VISIBLE);
                            monthlynocontent.setVisibility(View.GONE);
                            if (TextUtils.equals(transactionModel.getCategory(), categorydata.get("CategoryName").toString())) {
                                monthlytotalamount += Integer.parseInt(transactionModel.getAmount());
                                categoryamount += Integer.parseInt(transactionModel.getAmount());
                            }
                        } else if (categorylist.size()==0)
                        {
                            monthlycontent.setVisibility(View.GONE);
                            monthlynocontent.setVisibility(View.VISIBLE);
                        }
                    }
                }
                monthlyamount.setText("₹"+String.valueOf(monthlytotalamount));
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
                        categorymonthpiechart.setEntryLabelColor(Color.BLACK);
                        categorymonthpiechart.animateY(1200, Easing.EaseInOutQuad);
                        categorymonthpiechart.setDescription(null);
                        categorymonthpiechart.setData(pieData);
                        categorymonthpiechart.invalidate();

                        HashMap hashMap = new HashMap();
                        hashMap.put("CategoryName",categorynamepieentry);
                        hashMap.put("CategoryColor",categorycolorpieentry);
                        hashMap.put("CategoryPrice",String.valueOf(categoryamount));

                        categorylist.add(hashMap);
                        categoryAdapter = new CategoryAdapter(categorylist,getActivity(),true,false);
                        categorywiseitems.setAdapter(categoryAdapter);
                    }
                }
                categoryamount = 0;
            }
        });
    }
}
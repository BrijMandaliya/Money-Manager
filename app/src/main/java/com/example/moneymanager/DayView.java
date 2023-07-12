package com.example.moneymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Adapter.TransactionAdapter;
import com.example.moneymanager.Model.TransactionModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DayView extends Fragment {

        RelativeLayout daynocontent,daycontent;
        RecyclerView rcvdayviewtransactionitems;

        TextView datevalueindayview,dayviewtransactionamount;

        ImageButton leftbtnfordayview,rightbtnfordayview;

        TransactionAdapter transactionAdapter;

        List<TransactionModel> transactionModelList = new ArrayList<>();

        String currentday;

        int i=0,dayamounttotal;

        FirebaseUser fuser;
        DatabaseReference fdb;

        ShimmerFrameLayout dayviewshimmer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day_view, container, false);
        datevalueindayview = v.findViewById(R.id.datevalueindayview);
        leftbtnfordayview = v.findViewById(R.id.leftbtnfordayview);
        rightbtnfordayview = v.findViewById(R.id.rightbtnfordayview);
        daynocontent =v.findViewById(R.id.nocontentlayout);
        daycontent = v.findViewById(R.id.dayviewcontentlayout);
        dayviewtransactionamount = v.findViewById(R.id.dayviewtransactionamount);
        rcvdayviewtransactionitems = v.findViewById(R.id.rcvdayviewtransactionitems);
        dayviewshimmer = v.findViewById(R.id.dayviewshimmer);


        dayviewshimmer.setVisibility(View.VISIBLE);
        dayviewshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayviewshimmer.stopShimmerAnimation();
                dayviewshimmer.setVisibility(View.GONE);

                if(transactionModelList.size()==0)
                {
                    daynocontent.setVisibility(View.VISIBLE);
                }
                else {
                    daycontent.setVisibility(View.GONE);
                }
            }
        },2500);


        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL");
        SimpleDateFormat dateFormatdate = new SimpleDateFormat("dd LLLL YYYY");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        datevalueindayview.setText(dateFormat.format(date));
        currentday = dateFormatdate.format(date).toString();
        showdata(currentday);

        cal.setTime(date);

        datevalueindayview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                int d = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar selecteddate = Calendar.getInstance();
                        selecteddate.set(Calendar.DAY_OF_MONTH,i2);
                        selecteddate.set(Calendar.MONTH,i1);
                        selecteddate.set(Calendar.YEAR,i);
                        datevalueindayview.setText(dateFormat.format(selecteddate.getTime()));
                        currentday = dateFormatdate.format(selecteddate.getTime()).toString();
                        cal.setTime(selecteddate.getTime());
                        showdata(currentday);
                    }
                },y,m,d);
                datePickerDialog.show();
            }
        });



        leftbtnfordayview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                cal.add(Calendar.DATE,-1);
                datevalueindayview.setText(dateFormat.format(cal.getTime()));
                currentday = dateFormatdate.format(cal.getTime()).toString();
                showdata(currentday);
            }
        });




        rightbtnfordayview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                cal.add(Calendar.DATE,1);
                datevalueindayview.setText(dateFormat.format(cal.getTime()));
                currentday = dateFormatdate.format(cal.getTime()).toString();

                showdata(currentday);
            }
        });

    }

    private void showdata(String day) {
            int visibility = dayviewshimmer.getVisibility();
        if (visibility==0) {
            daycontent.setVisibility(View.GONE);
            daynocontent.setVisibility(View.GONE);
        } else {

            rcvdayviewtransactionitems.setLayoutManager(new LinearLayoutManager(getActivity()));

            dayamounttotal = 0;

            fuser = FirebaseAuth.getInstance().getCurrentUser();
            fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
            fdb.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task != null) {
                        transactionModelList.clear();
                        for (DataSnapshot sp : task.getResult().getChildren()) {
                            TransactionModel transactionModel = sp.getValue(TransactionModel.class);
                            if (transactionModel != null) {
                                if (transactionModel.getTransactiondate().compareTo(day) == 0) {
                                    daynocontent.setVisibility(View.GONE);
                                    daycontent.setVisibility(View.VISIBLE);
                                    transactionModel.setKey(sp.getKey());
                                    dayamounttotal += Integer.parseInt(transactionModel.getAmount());
                                    transactionModelList.add(transactionModel);
                                } else if (transactionModelList.size() == 0) {
                                    daynocontent.setVisibility(View.VISIBLE);
                                    daycontent.setVisibility(View.GONE);
                                }
                            }
                        }


                        transactionAdapter = new TransactionAdapter(transactionModelList, getContext());
                        rcvdayviewtransactionitems.setAdapter(transactionAdapter);
                        dayviewtransactionamount.setText("₹" + String.valueOf(dayamounttotal));
                    }
                }
            });
        }
    }
}

package com.example.moneymanager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.moneymanager.Adapter.TransactionAdapter;
import com.example.moneymanager.Model.TransactionModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomePpage extends Fragment {

    TextView homebtn,analysisbtn,settingbtn;

    LottieAnimationView plusbtn;
    DatabaseReference fdb;
    FirebaseUser firebaseUser;
    FirebaseAuth userauth;

    TransactionAdapter transactionAdapter;
    RecyclerView rcvhomepage;

    List<TransactionModel> transactionModelList;

    public TextView hpusername,hpmonthspend,seeall,hpwish;

    int monthamount;

    View bottommenu;

    public RelativeLayout homepagedata;
    public ShimmerFrameLayout homepageshimmer,homepageshimmer2,homepageshimmer3;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_ppage, container, false);
        hpusername = v.findViewById(R.id.hpusername);
        hpwish = v.findViewById(R.id.hpwish);
        seeall = v.findViewById(R.id.seeall);
        hpmonthspend = v.findViewById(R.id.hpmonthspend);
        rcvhomepage = v.findViewById(R.id.rcvhomepage);
        homepagedata = v.findViewById(R.id.homepagedata);
        homepageshimmer = v.findViewById(R.id.homepageshimmer);
        homepageshimmer2 = v.findViewById(R.id.homepageshimmer2);
        homepageshimmer3 = v.findViewById(R.id.homepageshimmer3);

        homepageshimmer.setVisibility(View.VISIBLE);
        homepageshimmer.startShimmerAnimation();

        homepageshimmer2.setVisibility(View.VISIBLE);
        homepageshimmer2.startShimmerAnimation();

        homepageshimmer3.setVisibility(View.VISIBLE);
        homepageshimmer3.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            homepageshimmer.stopShimmerAnimation();
            homepageshimmer.setVisibility(View.GONE);

            homepageshimmer2.stopShimmerAnimation();
            homepageshimmer2.setVisibility(View.GONE);

            homepageshimmer3.stopShimmerAnimation();
            homepageshimmer3.setVisibility(View.GONE);

            rcvhomepage.setVisibility(View.VISIBLE);
            hpmonthspend.setVisibility(View.VISIBLE);
            hpusername.setVisibility(View.VISIBLE);
        },2500);

        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finishActivity(100);
            }
        });


        homebtn = getActivity().findViewById(R.id.homebtn);
        analysisbtn = getActivity().findViewById(R.id.analysisbtn);
        settingbtn = getActivity().findViewById(R.id.settingbtn);
        plusbtn = getActivity().findViewById(R.id.plusbtn);

        homebtn.setBackground(getActivity().getDrawable(R.drawable.round1));
        homebtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.bottommenuitemselectcolor)));

        analysisbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        analysisbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        settingbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        settingbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        plusbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        plusbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));
        

        bottommenu = getActivity().findViewById(R.id.bottommenu);
        bottommenu.setVisibility(View.VISIBLE);


        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment,TransactionList.class,null)
                        .addToBackStack(null)
                        .commit();

                bottommenu.setVisibility(View.GONE);
            }
        });

        monthamount = 0;
        rcvhomepage.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionModelList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        if(hours>=5 && hours<12)
        {
            hpwish.setText("Good Moring");
        } else if (hours>=12 && hours<17) {
            hpwish.setText("Good Afternoon");
        } else if (hours>=17 && hours<21) {
            hpwish.setText("Good Evening");
        }
        else {
            hpwish.setText("Good Night");
        }


        SimpleDateFormat dateFormatmonth = new SimpleDateFormat("LLLL YYYY");
        Date date = new Date();
        String currentmonth = dateFormatmonth.format(date);

        userauth = FirebaseAuth.getInstance();
        firebaseUser = userauth.getCurrentUser();

        if(firebaseUser==null)
        {
            startActivity(new Intent(getActivity(),LoginPage.class));
        }

        else {
            fdb = FirebaseDatabase.getInstance().getReference("Users");
            fdb.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap hashMap =(HashMap) snapshot.getValue();
                    hpusername.setText(hashMap.get("username").toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Fetch Username Error",error.toString());
                }
            });


            fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(firebaseUser.getUid());
            fdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    transactionModelList.clear();
                    for(DataSnapshot sp : snapshot.getChildren())
                    {
                        TransactionModel transactionModel = sp.getValue(TransactionModel.class);
                        if(transactionModel==null)
                        {
                            Log.d("Transaction Data","no Data");
                        }
                        else {
                            if(transactionModel.getTransactionmonth().equals(currentmonth))
                            {
                                transactionModel.setKey(sp.getKey());
                                monthamount += Integer.parseInt(transactionModel.getAmount());
                                transactionModelList.add(transactionModel);
                            }
                        }
                        transactionAdapter = new TransactionAdapter(transactionModelList,getActivity());
                        rcvhomepage.setAdapter(transactionAdapter);
                    }
                    hpmonthspend.setText("₹" + String.valueOf(monthamount));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Transaction Data Error",error.toString());
                }
            });
        }
    }
}
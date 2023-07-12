package com.example.moneymanager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Settingfragment extends Fragment {

    TextView homebtn,analysisbtn,settingbtn;

    LottieAnimationView plusbtn;

    RelativeLayout categorybox,transactionbox,themebox;


    Button logoutbtn;

    FirebaseAuth userauth;

    DatabaseReference fdb;

    TextView profilename,profileemail,dayviewopt,selectedthemeinfo;

    RadioButton RDlightmode,RDdarkmode;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    View bottomlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settingfragment, container, false);
        profilename= v.findViewById(R.id.profilename);
        profileemail= v.findViewById(R.id.profileemail);
        transactionbox = v.findViewById(R.id.transactionbox);
        categorybox = v.findViewById(R.id.categorybox);
        logoutbtn = v.findViewById(R.id.logoutbtn);
        dayviewopt = v.findViewById(R.id.dayviewopt);
        themebox = v.findViewById(R.id.themebox);
        selectedthemeinfo = v.findViewById(R.id.selectedthemeinfo);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences Theme = getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        selectedthemeinfo.setText(Theme.getString("ThemeMode",""));


        homebtn = getActivity().findViewById(R.id.homebtn);
        analysisbtn = getActivity().findViewById(R.id.analysisbtn);
        settingbtn = getActivity().findViewById(R.id.settingbtn);
        plusbtn = getActivity().findViewById(R.id.plusbtn);


        settingbtn.setBackground(getActivity().getDrawable(R.drawable.round1));
        settingbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.bottommenuitemselectcolor)));

        analysisbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        analysisbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        homebtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        homebtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        plusbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        plusbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));


        bottomlayout = getActivity().findViewById(R.id.bottommenu);
        bottomlayout.setVisibility(View.VISIBLE);

        userauth = FirebaseAuth.getInstance();
        FirebaseUser user = userauth.getCurrentUser();



        fdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap hashMap =(HashMap) snapshot.getValue();
                if(hashMap!=null)
                {
                    profilename.setText(hashMap.get("username").toString());
                    profileemail.setText(hashMap.get("Email").toString());
                }
                else {
                    Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("SettingException",error.toString());
            }
        });


        transactionbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment,TransactionList.class,null)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();

                bottomlayout.setVisibility(View.GONE);
            }
        });


        categorybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putBoolean("FromTransaction",false);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, CategoryList.class, args)
                        .addToBackStack(null)
                        .commit();
                bottomlayout.setVisibility(View.GONE);

            }
        });


        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userauth.signOut();
                getActivity().startActivity(new Intent(getActivity(),LoginPage.class));
            }
        });




        dayviewopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, DayView.class,null)
                        .addToBackStack("name")
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

        SharedPreferences.Editor themeselected =Theme.edit();


        themebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.themeopt);

                RDdarkmode = dialog.findViewById(R.id.DarkMode);
                RDlightmode = dialog.findViewById(R.id.LightMode);

                RDlightmode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        themeselected.putBoolean("Darkmode",false);
                        themeselected.putString("ThemeMode","Light Mode");
                        themeselected.apply();
                        dialog.dismiss();
                    }
                });
                RDdarkmode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        themeselected.putBoolean("Darkmode",true);
                        themeselected.putString("ThemeMode","Dark Mode");
                        themeselected.apply();
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,700);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.ThemeSelectDialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

            }
        });


    }
}
;
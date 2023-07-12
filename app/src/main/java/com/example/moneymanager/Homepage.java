package com.example.moneymanager;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Homepage extends AppCompatActivity {

    TextView homebtn,analysisbtn,settingbtn;

    LottieAnimationView plusbtn;

    View bottomlayout;

    FirebaseUser firebaseUser;
    FirebaseAuth userauth;

    View bottomnavigationmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        homebtn = findViewById(R.id.homebtn);
        analysisbtn = findViewById(R.id.analysisbtn);
        settingbtn = findViewById(R.id.settingbtn);
        plusbtn = findViewById(R.id.plusbtn);

        bottomnavigationmenu = findViewById(R.id.bottommenu);

        FragmentManager fragmentManager = getSupportFragmentManager();


        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homebtn.setBackground(getDrawable(R.drawable.round1));
                homebtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.bottommenuitemselectcolor)));

                analysisbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                analysisbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                settingbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                settingbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                plusbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                plusbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));


                fragmentManager.beginTransaction()
                        .replace(R.id.homepagefragment, HomePpage.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        analysisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analysisbtn.setBackground(getDrawable(R.drawable.round1));
                analysisbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.bottommenuitemselectcolor)));

                homebtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                homebtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                settingbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                settingbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                plusbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                plusbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                fragmentManager.beginTransaction()
                        .replace(R.id.homepagefragment, analysis.class,null)
                        .addToBackStack(null)
                        .commit();

            }
        });
        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingbtn.setBackground(getDrawable(R.drawable.round1));
                settingbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.bottommenuitemselectcolor)));

                homebtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                homebtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                analysisbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                analysisbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                plusbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                plusbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                fragmentManager.beginTransaction()
                        .replace(R.id.homepagefragment, Settingfragment.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plusbtn.setBackground(getDrawable(R.drawable.round1));
                plusbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.bottommenuitemselectcolor)));

                homebtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                homebtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                analysisbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                analysisbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                settingbtn.setBackgroundColor(getColor(R.color.bottommenubackgroundcolor));
                settingbtn.setForegroundTintList(ColorStateList.valueOf(getColor(R.color.white)));

                fragmentManager.beginTransaction()
                        .replace(R.id.homepagefragment, TransactionPage.class, null)
                        .addToBackStack(null)
                        .commit();

                bottomnavigationmenu.setVisibility(View.GONE);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences Theme = getSharedPreferences("theme", Context.MODE_PRIVATE);
        Boolean darkmodecheck = Theme.getBoolean("Darkmode",false);
        if(darkmodecheck)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}
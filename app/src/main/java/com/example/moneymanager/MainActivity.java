package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    LottieAnimationView startpagegetstartbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startpagegetstartbtn = findViewById(R.id.getstartbtn);

        startpagegetstartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginPage.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null)
        {
            startActivity(new Intent(MainActivity.this, Homepage.class));
        }
    }
}
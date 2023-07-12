package com.example.moneymanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registeration extends AppCompatActivity {

    EditText registereditTextEmail,registereditTextPassword,registereditTextusername;

    LottieAnimationView registerbtn;

    String username,password,email;

    int flag;

    FirebaseAuth userauth;
    FirebaseUser fuser;
    DatabaseReference fdb;

    Boolean userexisting=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        registerbtn = findViewById(R.id.registerbtn);
        registereditTextEmail = findViewById(R.id.registereditTextEmail);
        registereditTextusername = findViewById(R.id.registereditTextusername);
        registereditTextPassword = findViewById(R.id.registereditTextPassword);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                email = registereditTextEmail.getText().toString();
                password = registereditTextPassword.getText().toString();
                username = registereditTextusername.getText().toString();


                if(TextUtils.isEmpty(email))
                {
                    registereditTextEmail.setError("Please Enter Email");
                    registereditTextEmail.setHintTextColor(Color.parseColor("#f72111"));
                }
                else if(TextUtils.isEmpty(password))
                {
                    registereditTextPassword.setError("Please Enter Password");
                    registereditTextPassword.setHintTextColor(Color.parseColor("#f72111"));
                }
                else if(TextUtils.isEmpty((username)))
                {
                    registereditTextusername.setError("Please Enter Username");
                    registereditTextusername.setHintTextColor(Color.parseColor("#f72111"));
                }
                else
                {
                    signupverify(email,password,username);
                    registerbtn.playAnimation();
                }
            }
        });
    }

    private void signupverify(String email, String password, String username) {
        userauth = FirebaseAuth.getInstance();



        fdb = FirebaseDatabase.getInstance().getReference("Users");
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap hashMap =(HashMap) snapshot.getValue();
                if(hashMap!=null)
                {
                    if(email==hashMap.get("Email"))
                    {
                        userexisting = true;
                    }
                }
                else {
                    Log.d("DATABASE NO DATA","DATA NO DATA");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DATABASE NO DATA",error.toString());
            }
        });

        if(userexisting.equals(false)) {
            userauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Registeration.this, "Registration SuccessFully", Toast.LENGTH_SHORT).show();
                        addtodatabase(email, password, username, userauth.getCurrentUser());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Signinwithemail", e.toString());
                }
            });
        }

    }

    private void addtodatabase(String email, String password, String username, FirebaseUser userauth) {

        HashMap hashMap = new HashMap();
        hashMap.put("Userid",userauth.getUid());
        hashMap.put("username",username);
        hashMap.put("Email",email);
        hashMap.put("Password",password);

        fdb = FirebaseDatabase.getInstance().getReference("Users");
        fdb.child(username).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                startActivity(new Intent(Registeration.this,Homepage.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RealtimeDatabase",e.toString());
            }
        });


    }
}
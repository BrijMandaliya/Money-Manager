package com.example.moneymanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    TextView gotoregisterpage;

    EditText editTextEmail,editTextPassword;

    FirebaseAuth userauth;

    String email,password;
    LottieAnimationView loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        gotoregisterpage = findViewById(R.id.gotoregisterpage);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        gotoregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this,Registeration.class));
            }
        });

        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    editTextEmail.setError("Enter Email");
                    editTextEmail.setFocusable(true);
                }
                if(TextUtils.isEmpty(password))
                {
                    editTextPassword.setError("Enter Password");
                    editTextPassword.setFocusable(true);
                }
                else {

                    loginverify(email,password);
                    loginbtn.playAnimation();
                }

            }
        });
    }

    private void loginverify(String email, String password) {

        userauth = FirebaseAuth.getInstance();
        userauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginPage.this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginPage.this,Homepage.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SigninLoginError",e.toString());
            }
        });
    }
}
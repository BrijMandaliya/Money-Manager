package com.example.moneymanager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class TransactionPage extends Fragment {

    View bottomlayout;

    RelativeLayout selectcategory;


    Boolean updatetransactioncheck=false;

    FloatingActionButton savebtn;

    FirebaseUser fuser;
    DatabaseReference fdb,fdb1;

    EditText currentday,amount,Description;

    public TextView selectedcategory;

    public ImageView categoryimg,selectdate,deletebtn,backbuttonontransactionpage;


    SimpleDateFormat dateFormatdate,dateFormatweekly;

    Calendar selecteddate;

    String transactionamount;
    String category;
    String checkcategory;
    public String categorycolor;

    String week;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_page, container, false);
        amount = v.findViewById(R.id.amount);
        Description = v.findViewById(R.id.Description);
        selectedcategory = v.findViewById(R.id.selectedcategory);
        categoryimg = v.findViewById(R.id.categoryimg);
        deletebtn = v.findViewById(R.id.transactiondeletebtn);
        currentday = v.findViewById(R.id.dateview);
        selectdate = v.findViewById(R.id.dayviewimg);
        selectcategory = v.findViewById(R.id.selectcategory);
        savebtn = v.findViewById(R.id.savebutton);
        backbuttonontransactionpage = v.findViewById(R.id.backbuttonontransactionpage);
        return v;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        bottomlayout = getActivity().findViewById(R.id.bottommenu);
        bottomlayout.setVisibility(View.GONE);

        Toolbar toolbar = getView().findViewById(R.id.transactiontoolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        backbuttonontransactionpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        dateFormatdate = new SimpleDateFormat("dd LLLL YYYY");
        dateFormatweekly = new SimpleDateFormat("dd/MM");
        Date date = new Date();

        selecteddate = Calendar.getInstance();


        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        SimpleDateFormat getdate = new SimpleDateFormat("dd LLLL yyyy");

                        selecteddate.set(Calendar.DAY_OF_MONTH,i2);
                        selecteddate.set(Calendar.MONTH,i1);
                        selecteddate.set(Calendar.YEAR,i);

                        currentday.setText(getdate.format(selecteddate.getTime()));

                        Calendar cal2 = selecteddate;
                        Calendar cal3 = selecteddate;
                        cal2.set(selecteddate.DAY_OF_WEEK,2);

                        week = dateFormatweekly.format(cal2.getTime());

                        cal3.set(selecteddate.DAY_OF_WEEK,8);
                        cal3.add(selecteddate.DAY_OF_WEEK, 7);

                        week += " - " + dateFormatweekly.format(cal3.getTime());

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        currentday.setText(dateFormatdate.format(date));
        selectcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putBoolean("FromTransaction", true);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, CategoryList.class, args)
                        .setReorderingAllowed(true)
                        .addToBackStack("FromTransaction")
                        .commit();
            }
        });

        try {

            if(getArguments().getBoolean("UpdateTransaction"))
            {

                updatetransactioncheck = true;
                amount.setText(getArguments().getString("TransactionAmount"));
                selectedcategory.setText(getArguments().getString("CategoryName"));
                categoryimg.setBackgroundColor(Color.parseColor(getArguments().getString("CategoryColor")));
                categorycolor = getArguments().getString("CategoryColor");
                currentday.setText(getArguments().getString("TransactionDate"));
                Description.setText(getArguments().getString("TransactionDescription"));
                String removetransactionid = getArguments().getString("TransactionID");
                deletebtn.setVisibility(getView().VISIBLE);
                deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
                        fdb.child(removetransactionid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();
                                Toast.makeText(getActivity(), "Remove Transaction SuccessFull", Toast.LENGTH_SHORT).show();
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("Delete Error",error.toString());
                            }
                        });
                    }
                });
            }

        } catch (Exception e) {

        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionamount = amount.getText().toString();
                category = selectedcategory.getText().toString();


                SimpleDateFormat dateFormatmonth = new SimpleDateFormat("LLLL YYYY");
                SimpleDateFormat dateFormatyear = new SimpleDateFormat("YYYY");

                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();

                Date date = new Date();
                cal.set(Calendar.DAY_OF_WEEK, 2);
                cal1.set(Calendar.DAY_OF_WEEK, 8);
                cal1.add(Calendar.DAY_OF_WEEK, 7);


                if (TextUtils.isEmpty(transactionamount)) {
                    amount.setHint("Enter Amount");
                    amount.setHintTextColor(Color.parseColor("#f72111"));
                } else if (TextUtils.isEmpty(category) || TextUtils.equals(category,"Select Category"))
                {
                    selectedcategory.setTextColor(Color.parseColor("#f72111"));
                }
                else {
                    if(updatetransactioncheck)
                    {
                        if(week==null)
                        {
                            week = dateFormatweekly.format(cal.getTime()) + " - " + dateFormatweekly.format(cal1.getTime());
                        }
                        HashMap TMhashmap = new HashMap();
                        TMhashmap.put("Amount",transactionamount);
                        TMhashmap.put("Category",category);
                        TMhashmap.put("CategoryColor",categorycolor);
                        TMhashmap.put("Notes",Description.getText().toString());
                        TMhashmap.put("transactionweek", week);
                        TMhashmap.put("transactiondate", currentday.getText().toString());
                        TMhashmap.put("transactionmonth", dateFormatmonth.format(selecteddate.getTime()));
                        TMhashmap.put("transactionyear", dateFormatyear.format(selecteddate.getTime()));
                        fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
                        fdb.child(getArguments().getString("TransactionID")).updateChildren(TMhashmap)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Update Transaction SuccessFully", Toast.LENGTH_SHORT).show();
                                            HomePpage homePpage = new HomePpage();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.homepagefragment,homePpage,null)
                                                    .commit();
                                        }
                                    }
                                });


                    }
                    else if (!updatetransactioncheck) {
                        week = dateFormatweekly.format(cal.getTime()) + " - " + dateFormatweekly.format(cal1.getTime());
                        if (fuser == null) {
                            startActivity(new Intent(getActivity(), LoginPage.class));
                        } else {
                            ColorDrawable colorDrawable = (ColorDrawable) categoryimg.getBackground();
                            categorycolor = Integer.toHexString(colorDrawable.getColor()).substring(2);
                            fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());

                            Toast.makeText(getActivity(), " " + category, Toast.LENGTH_SHORT).show();

                            HashMap hashMap = new HashMap();
                            hashMap.put("Amount", transactionamount);
                            hashMap.put("Category", category);
                            hashMap.put("CategoryColor","#"+categorycolor);
                            hashMap.put("Notes", Description.getText().toString());
                            hashMap.put("transactionweek", week);
                            hashMap.put("transactiondate", dateFormatdate.format(date));
                            hashMap.put("transactionmonth", dateFormatmonth.format(date));
                            hashMap.put("transactionyear", dateFormatyear.format(date));


                            fdb.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Transaction Add SuccessFully", Toast.LENGTH_SHORT).show();

                                        fdb1 = FirebaseDatabase.getInstance().getReference("Category").child(fuser.getUid());
                                        fdb1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot sp : snapshot.getChildren()) {
                                                    HashMap categorydata = (HashMap) sp.getValue();
                                                    if (categorydata != null) {
                                                        checkcategory = categorydata.get("CategoryName").toString();
                                                        if (checkcategory == category) {
                                                            HashMap updatecategory = new HashMap();
                                                            updatecategory.put("CategoryName", categorydata.get("CategoryName").toString());
                                                            updatecategory.put("CategoryColor", categorydata.get("CategoryColor").toString());
                                                            updatecategory.put("CategoryDescription", categorydata.get("CategoryDescription").toString());

                                                            fdb1.child(checkcategory).updateChildren(updatecategory).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getActivity(), "Done Dona Done", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {

                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("CategoryAmountaddError", e.toString());
                                                                }
                                                            });

                                                        }
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("CategoryData Error", error.toString());
                                            }
                                        });

                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.homepagefragment, HomePpage.class, null)
                                                .addToBackStack(null)
                                                .commit();

                                        amount.setText(" ");
                                        Description.setText(" ");
                                        bottomlayout.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }
}
package com.example.moneymanager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.moneymanager.Model.TransactionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import yuku.ambilwarna.AmbilWarnaDialog;


public class addcategory extends Fragment {

    EditText categoryname,categorydescription;
    Button selectcolorbtn;

    TextView categorycolor,addcategoryheader;

    ImageView categorypagedeletebtn,backbuttononaddcategorypage;

    FloatingActionButton categoryaddbtn;

    String CategoryColor;

    FirebaseUser fuser;
    DatabaseReference fdb,fdb1;

    Boolean checkupdatecategory=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addcategory, container, false);
        categoryname = v.findViewById(R.id.CategoyName);
        categorydescription = v.findViewById(R.id.categorydescription);
        selectcolorbtn = v.findViewById(R.id.selectcolorbtn);
        categorycolor = v.findViewById(R.id.categorycolor);
        categoryaddbtn = v.findViewById(R.id.categoryaddbtn);
        addcategoryheader = v.findViewById(R.id.addcategoryheader);
        categorypagedeletebtn = v.findViewById(R.id.categorypagedeletebtn);
        backbuttononaddcategorypage = v.findViewById(R.id.backbuttononaddcategorypage);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = getView().findViewById(R.id.addcategorypagetoolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        backbuttononaddcategorypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });



        int defaultcolor = Color.parseColor("#ffffff");

        selectcolorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getActivity(), defaultcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        CategoryColor = Integer.toHexString(color).substring(2);
                        categorycolor.setBackgroundColor(color);
                    }
                });
                ambilWarnaDialog.show();
            }
        });

        try {
            if(getArguments().getBoolean("UpdateCategory")) {
                checkupdatecategory=true;
                selectcolorbtn.setText(getArguments().getString("CategoryColor"));
                categorycolor.setBackgroundColor(Color.parseColor(getArguments().getString("CategoryColor")));
                categoryname.setText(getArguments().getString("CategoryName"));
                categorydescription.setText(getArguments().getString("CategoryDescription"));
                addcategoryheader.setText("Update Category");
                String Categoryid = getArguments().getString("CategoryID");
                categoryname.setEnabled(false);
            }
        }
        catch (Exception e){}


        categoryaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cname = categoryname.getText().toString();
                String cdescription = categorydescription.getText().toString();
                if(TextUtils.isEmpty(cname))
                {
                    categoryname.setHintTextColor(Color.RED);
                }
                else if(TextUtils.isEmpty(cdescription))
                {
                    categorydescription.setHintTextColor(Color.RED);
                }
                else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    fdb = FirebaseDatabase.getInstance().getReference("Category").child(user.getUid()).child(cname);
                    if(checkupdatecategory)
                    {
                        HashMap hashMap = new HashMap();
                        hashMap.put("CategoryColor","#"+selectcolorbtn.getText().toString());
                        hashMap.put("CategoryDescription",categorydescription.getText().toString());
                        hashMap.put("CategoryName",categoryname.getText().toString());


                        fdb.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful())
                                {
                                    fdb1 = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
                                    fdb1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task!=null)
                                            {
                                                for(DataSnapshot sp : task.getResult().getChildren())
                                                {

                                                    TransactionModel TM = sp.getValue(TransactionModel.class);
                                                    if(TM!=null)
                                                    {

                                                        if(TextUtils.equals(categoryname.getText().toString(),TM.getCategory()))
                                                        {
                                                            HashMap hashMap1 = new HashMap();
                                                            hashMap1.put("CategoryColor","#"+selectcolorbtn.getText().toString());
                                                            fdb1.child(sp.getKey()).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    Toast.makeText(getActivity(), "Update SuccessFully", Toast.LENGTH_SHORT).show();
                                                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                                            .replace(R.id.homepagefragment, CategoryList.class,null)
                                                                            .addToBackStack(null)
                                                                            .commit();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Update Category Failure",e.toString());
                            }
                        });
                    }
                    else {

                        HashMap hashMap = new HashMap();
                        hashMap.put("CategoryName",cname);
                        hashMap.put("CategoryDescription",cdescription);
                        hashMap.put("CategoryColor","#"+CategoryColor);


                        fdb.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getActivity(), "Category Added", Toast.LENGTH_SHORT).show();
                                    Bundle args = new Bundle();
                                    args.putBoolean("FromTransaction",false);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.homepagefragment,CategoryList.class,args)
                                            .commit();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Category add Error",e.toString());
                            }
                        });
                    }
                }
            }
        });
    }


}
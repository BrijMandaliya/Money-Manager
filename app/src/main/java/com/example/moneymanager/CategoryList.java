package com.example.moneymanager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Adapter.CategoryAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryList extends Fragment {

    Boolean check;

    FloatingActionButton addcategorybutton;

    RecyclerView rcvcategory;

    CategoryAdapter categoryAdapter;

    List<HashMap> categorylist;

    DatabaseReference fdb;

    View Bottomlayout;

    boolean updatecategory;
    Toolbar toolbar;

    ImageView backbuttononcategorypage;

    ShimmerFrameLayout categorylistshimmer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            check = getArguments().getBoolean("FromTransaction");
        }
        catch (Exception e){}

        View v = inflater.inflate(R.layout.fragment_category_list, container, false);
        rcvcategory = v.findViewById(R.id.rcvcategory);
        addcategorybutton = v.findViewById(R.id.addcategorybutton);
        backbuttononcategorypage = v.findViewById(R.id.backbuttononcategorypage);
        toolbar = v.findViewById(R.id.categorypagetoolbar);
        categorylistshimmer = v.findViewById(R.id.categorylistshimmer);

        categorylistshimmer.setVisibility(View.VISIBLE);
        categorylistshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                categorylistshimmer.stopShimmerAnimation();
                categorylistshimmer.setVisibility(View.GONE);

                rcvcategory.setVisibility(View.VISIBLE);
            }
        },2500);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bottomlayout = getActivity().findViewById(R.id.bottommenu);


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        backbuttononcategorypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


        if(check==null)
        {
            check = false;
        }
        if(check)
        {
            backbuttononcategorypage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.homepagefragment,TransactionPage.class,null)
                            .addToBackStack(null)
                            .commit();
                }
            });
            updatecategory = false;
            addcategorybutton.setVisibility(View.GONE);
        }
        else{
            updatecategory=true;
        }

        addcategorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.homepagefragment, addcategory.class,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        fetchcategory();
    }

    private void fetchcategory() {

        rcvcategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        categorylist = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fdb = FirebaseDatabase.getInstance().getReference("Category").child(user.getUid());

        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sp : snapshot.getChildren())
                {
                    HashMap hashMap = (HashMap)sp.getValue();
                    if(hashMap!=null)
                    {
                        hashMap.put("CategoryID",sp.getKey());
                        categorylist.add(hashMap);
                    }
                }
                categoryAdapter = new CategoryAdapter(categorylist,getActivity(),false,updatecategory);
                rcvcategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FetchCategoryError",error.toString());
            }
        });
    }
}
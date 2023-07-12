package com.example.moneymanager;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.Adapter.TransactionAdapter;
import com.example.moneymanager.Adapter.firebasetransactionadapater;
import com.example.moneymanager.Model.TransactionModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class TransactionList extends Fragment {

    RecyclerView rcvtransactionlist;

    TransactionAdapter transactionAdapter;
    public FirebaseRecyclerOptions<TransactionModel> options;

    firebasetransactionadapater FirebaseAdapter;

    List<TransactionModel> transactionModelList = new ArrayList<>();

    FirebaseUser fuser;
    DatabaseReference fdb;

    SearchView etsearch;

    View Bottomlayout;

    ImageView backbuttonontransactionlistpage;

    ShimmerFrameLayout transactionlistshimmer;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        etsearch = v.findViewById(R.id.transactionsearchview);
        rcvtransactionlist = v.findViewById(R.id.rcvtransactionlist);
        backbuttonontransactionlistpage = v.findViewById(R.id.backbuttonontransactionlistpage);
        transactionlistshimmer = v.findViewById(R.id.transactionlistshimmer);

        transactionlistshimmer.setVisibility(View.VISIBLE);
        transactionlistshimmer.startShimmerAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transactionlistshimmer.stopShimmerAnimation();
                transactionlistshimmer.setVisibility(View.GONE);

                rcvtransactionlist.setVisibility(View.VISIBLE);
            }
        },2500);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bottomlayout = getActivity().findViewById(R.id.bottommenu);

        Toolbar toolbar = getView().findViewById(R.id.transactionlisttoolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        backbuttonontransactionlistpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


        rcvtransactionlist.setLayoutManager(new LinearLayoutManager(getActivity()));


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        fdb = FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid());
        FirebaseRecyclerOptions<TransactionModel> options = new FirebaseRecyclerOptions.Builder<TransactionModel>()
                .setQuery(fdb,TransactionModel.class)
                .build();


        FirebaseAdapter = new firebasetransactionadapater(options);
        rcvtransactionlist.setAdapter(FirebaseAdapter);

        etsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchdata(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchdata(s);
                return false;
            }
        });
    }

    private void searchdata(String s) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        options = new FirebaseRecyclerOptions.Builder<TransactionModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Transaction").child(fuser.getUid()).orderByChild("Category").startAt(s).endAt(s+String.valueOf("\uf8ff")),TransactionModel.class)
                .build();

        FirebaseAdapter = new firebasetransactionadapater(options);
        FirebaseAdapter.startListening();
        rcvtransactionlist.setAdapter(FirebaseAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAdapter.startListening();
    }

    public void onSop() {
        super.onStop();
        FirebaseAdapter.stopListening();
    }

}
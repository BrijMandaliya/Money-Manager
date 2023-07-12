package com.example.moneymanager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.moneymanager.analysisviewadapter.viewadapter;
import com.google.android.material.tabs.TabLayout;


public class analysis extends Fragment {

    TextView homebtn,analysisbtn,settingbtn;

    LottieAnimationView plusbtn;

    TabLayout tabLayout;
    ViewPager viewPager;

    View Bottommenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void setupviewpager(ViewPager viewPager) {
        viewadapter adapter = new viewadapter(getChildFragmentManager());
        adapter.addfragment(new WeeklyFragment(),"Weekly");
        adapter.addfragment(new MonthlyFragment(),"Monthly");
        adapter.addfragment(new Yearlyfragment(),"Yearly");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homebtn = getActivity().findViewById(R.id.homebtn);
        analysisbtn = getActivity().findViewById(R.id.analysisbtn);
        settingbtn = getActivity().findViewById(R.id.settingbtn);
        plusbtn = getActivity().findViewById(R.id.plusbtn);


        analysisbtn.setBackground(getActivity().getDrawable(R.drawable.round1));
        analysisbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.bottommenuitemselectcolor)));

        homebtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        homebtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        settingbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        settingbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        plusbtn.setBackgroundColor(getActivity().getColor(R.color.bottommenubackgroundcolor));
        plusbtn.setForegroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.white)));

        Bottommenu = getActivity().findViewById(R.id.bottommenu);
        Bottommenu.setVisibility(View.VISIBLE);

        tabLayout = getView().findViewById(R.id.analysistablayout);
        viewPager = getView().findViewById(R.id.analysisviewpager);

        setupviewpager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
}
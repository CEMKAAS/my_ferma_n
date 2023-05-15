package com.hfad.myferma.incubator;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.CustomAdapterSale;
import com.hfad.myferma.SalePackage.SaleActivity;
import com.hfad.myferma.WriteOff.WriteOffActivity;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;

public class IncubatorMenuFragment extends Fragment{
    private MyFermaDatabaseHelper myDB;
    private RecyclerView recyclerView;
    private ArrayList<String> id, name, type, data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_incubator_menu, container, false);
//убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        //настройка верхнего меню фаб кнопку
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Инкубатор");
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.getMenu().findItem(R.id.delete).setVisible(true);
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        TabLayout tabLayout = layout.findViewById(R.id.tab);
        ViewPager2 viewPager2 = layout.findViewById(R.id.view_pager);
        MenuAdapter menuAdapter = new MenuAdapter(getActivity());
        viewPager2.setAdapter(menuAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return layout;
    }

}
package com.hfad.myferma.AddPackage;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.ListAdapterIncubator;
import com.hfad.myferma.incubator.editDayIncubatorFragment;

import java.util.ArrayList;

public class AddManagerFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView empty_imageview;
    private TextView no_data;
    private MyFermaDatabaseHelper myDB;
    private ArrayList<String> id, title, disc, day, mount, year;
    private CustomAdapterAdd customAdapterAdd;

    private int mount1 = 0;

    private ArrayAdapter<String> arrayAdapterAnimals;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_manager, container, false);
        myDB = new MyFermaDatabaseHelper(getActivity());

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.filler).setVisible(true);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        recyclerView = layout.findViewById(R.id.recyclerView);
        empty_imageview = layout.findViewById(R.id.empty_imageview);
        no_data = layout.findViewById(R.id.no_data);

        id = new ArrayList<>();
        title = new ArrayList<>();
        disc = new ArrayList<>();
        day = new ArrayList<>();
        mount = new ArrayList<>();
        year = new ArrayList<>();

        no_data = layout.findViewById(R.id.no_data);

        storeDataInArrays();
        customAdapterAdd = new CustomAdapterAdd(id, title, disc,
                day, mount, year);

        recyclerView.setAdapter(customAdapterAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        customAdapterAdd.setListener(new CustomAdapterAdd.Listener() {
            @Override
            public void onClick(int position) {
                
            }
        });
        return layout;
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            cursor.moveToLast();
            id.add(cursor.getString(0));
            title.add(cursor.getString(1));
            disc.add(cursor.getString(2));
            day.add(cursor.getString(3));
            mount.add(cursor.getString(4));
            year.add(cursor.getString(5));

            while (cursor.moveToPrevious()){
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                disc.add(cursor.getString(2));
                day.add(cursor.getString(3));
                mount.add(cursor.getString(4));
                year.add(cursor.getString(5));
            }

            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }
}
package com.hfad.myferma.incubator;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;

public class ArchiveIncubatorFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private RecyclerView recyclerView;
    private ArrayList<String> id, name, type, data, arhive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_archive_incubator, container, false);
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setOnClickListener(this);

        //настройка верхнего меню фаб кнопку
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Инкубатор");

        fab.show();
        fab.setText("Добавить");
        fab.setIconResource(R.drawable.baseline_add_24);
        fab.getIcon();

        myDB = new MyFermaDatabaseHelper(getActivity());

        id = new ArrayList<>();
        name = new ArrayList<>();
        type = new ArrayList<>();
        data = new ArrayList<>();
        arhive = new ArrayList<>();
        recyclerView = layout.findViewById(R.id.recyclerView);

        storeDataInArrays();

        ArchiveAdapterIncubator archiveAdapterIncubator = new ArchiveAdapterIncubator(id, name, type, data, arhive);
        recyclerView.setAdapter(archiveAdapterIncubator);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);


        archiveAdapterIncubator .setListener(new ArchiveAdapterIncubator.Listener() {
            @Override
            public void onClick(int position, String name, String type, String data, String id) {
                addChart(id);
            }
        });

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.extended_fab:
                onClickButton(v, new AddIncubatorFragment());
                break;

        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataIncubator();
        if (cursor.getCount() == 0) {
            cursor.close();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(8).equals("1")) {
                    id.add(cursor.getString(0));
                    name.add(cursor.getString(1));
                    type.add(cursor.getString(2));
                    data.add(cursor.getString(3));
                    arhive.add(cursor.getString(8));
                }
            }
            cursor.close();
        }
    }

    public void onClickButton(View view, Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void addChart(String id) {
        //todo
        NowArhiveFragment incubatorMenuFragment = new NowArhiveFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        incubatorMenuFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();

    }
}
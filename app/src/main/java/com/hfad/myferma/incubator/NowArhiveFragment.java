package com.hfad.myferma.incubator;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

public class NowArhiveFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private TextView type, date, egg, littleBirds;
    private String[] massId, massTempDamp, massOver, massAiring;
    private RecyclerView recyclerView;
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_now_arhive, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
        }
         //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);
        myDB = new MyFermaDatabaseHelper(getActivity());

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.delete).setVisible(false);

        //Загружаем информацию с массивы
        massId = setCursor(myDB.idIncubator(id), 0, 13);
        massTempDamp = setCursor(myDB.idIncubatorTempDamp(id), 1, 61);
        massOver = setCursor(myDB.idIncubatorOver(id), 1, 31);
        massAiring = setCursor(myDB.idIncubatorOverAiring(id), 1, 31);

        type = layout.findViewById(R.id.type);
        date = layout.findViewById(R.id.date);
        egg = layout.findViewById(R.id.egg);
        littleBirds = layout.findViewById(R.id.little_birds);
        appBar.setTitle(massId[1]);
        type.setText(massId[2]);
        date.setText(massId[3] + " - " + massId[9]);
        egg.setText("Было заложено: " + massId[5] + " яйц");
        littleBirds.setText("Вышло птенцов: " + massId[6]);

        recyclerView = layout.findViewById(R.id.recyclerView);

        ListAdapterIncubator listAdapterIncubator = new ListAdapterIncubator(massId, massTempDamp, massOver, massAiring);
        recyclerView.setAdapter(listAdapterIncubator);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button add = (Button) layout.findViewById(R.id.begin);
        add.setOnClickListener(this);


        return layout;
    }

    public String[] setCursor(Cursor cursor, int sizeBegin, int size) {
        cursor.moveToNext();

        String[] mass = new String[size];

        for (int i = sizeBegin; i < size - 1; i++) {
            mass[i] = String.valueOf(cursor.getString(i));
        }

        cursor.close();
        return mass;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin:
                beginEndIncubator();
                break;
        }
    }

    public void beginEndIncubator() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Удалить " + massId[1] + " ?");
        builder.setMessage("Вы уверены, что хотите удалить " + massId[1] + " из архива ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteOneRowIncubator(massId[0]);
                IncubatorMenuFragment incubatorMenuFragment = new IncubatorMenuFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}
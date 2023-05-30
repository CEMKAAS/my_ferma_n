package com.hfad.myferma;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.myferma.WriteOff.WriteOffFragment;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;
import com.hfad.myferma.incubator.AddIncubatorFragment;
import com.hfad.myferma.incubator.IncubatorMenuFragment;
import com.hfad.myferma.incubator.ListAdapterIncubator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WarehouseFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private RecyclerView recyclerView;
    private List<String> productList;
    private String unit = null;
    private DecimalFormat f, eggFormat;

    //Создание фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Открываем БД
        myDB = new MyFermaDatabaseHelper(getActivity());

        View layout = inflater.inflate(R.layout.fragment_warehouse, container, false);

        // Назначаем кнопки
        Button addSale = (Button) layout.findViewById(R.id.writeOff_button);
        addSale.setOnClickListener(this);

        Button incubator = (Button) layout.findViewById(R.id.incubator_button);
        incubator.setOnClickListener(this);

        //Настройка листа
        productList = new ArrayList();
        add();

        // Настраиваем адаптер
        recyclerView = layout.findViewById(R.id.recyclerView);

        ProductAdapter productAdapter = new ProductAdapter(add1(),"", " Шт.");
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        f = new DecimalFormat("0.00");
        eggFormat = new DecimalFormat("0");

        return layout;
    }

    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();
    }


    public Map add1() {
        Map<String, Double> tempList = new HashMap<>();

        for (String product : productList) {

            Cursor cursor = myDB.idProduct1(MyConstanta.TABLE_NAME, MyConstanta.TITLE, product);

            if (cursor != null && cursor.getCount() != 0) {

                while (cursor.moveToNext()) {
                    Double productUnit = cursor.getDouble(2);
                    if (tempList.get(product) == null) {
                        tempList.put(product, productUnit);
                    } else {
                        double sum = tempList.get(product) + productUnit;
                        tempList.put(product, sum);
                    }
                }
                cursor.close();

                Cursor cursorSale = myDB.idProduct1(MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, product);

                while (cursorSale.moveToNext()) {
                    Double productUnit = cursorSale.getDouble(2);
                    double minus = tempList.get(product) - productUnit;
                    tempList.put(product, minus);
                }
                cursorSale.close();

                Cursor cursorWriteOff = myDB.idProduct1(MyConstanta.TABLE_NAMEWRITEOFF, MyConstanta.TITLEWRITEOFF, product);

                while (cursorWriteOff.moveToNext()) {
                    Double productUnit = cursorWriteOff.getDouble(2);
                    double minusWriteOff = tempList.get(product) - productUnit;
                    tempList.put(product, minusWriteOff);
                }
                cursorWriteOff.close();
            } else {
                tempList.put(product, 0.0);
            }
        }

        return tempList;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.writeOff_button:
                onClickButtonOff(v, new WriteOffFragment());
                break;
            case R.id.incubator_button:
                onClickButtonOff(v, new IncubatorMenuFragment());
                break;
        }
    }

    public void onClickButtonOff(View view, Fragment fragment) {

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();

//        ft.replace(R.id.conteiner, fragment, "visible_fragment");
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
    }

}
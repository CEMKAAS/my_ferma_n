package com.hfad.myferma.Finance;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hfad.myferma.ProductAdapter;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FinanceFragment extends Fragment implements View.OnClickListener{

    private MyFermaDatabaseHelper myDB;
    private RecyclerView recyclerView;
    private List<String> productList;
    private DecimalFormat f;
    // Прогрузка фрагмента и его активных частей
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Открываем БД
        myDB = new MyFermaDatabaseHelper(getActivity());

        View layout = inflater.inflate(R.layout.fragment_finance, container, false);
        f = new DecimalFormat("0.00");

        //Установка кнопки во фрагменте
        Button priceButton = (Button) layout.findViewById(R.id.financeChart_button);
        priceButton.setOnClickListener(this);

        Button price2Button = (Button) layout.findViewById(R.id.financeChart2_button);
        price2Button.setOnClickListener(this);

        //Настройка листа
        productList = new ArrayList();
        add();

        // Настраиваем адаптер
        recyclerView = layout.findViewById(R.id.recyclerView);

        ProductAdapter productAdapter = new ProductAdapter(addProduct(),"Продали ", " ₽" );
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Общая сумма и Расходы
        double totalAmount = totalAmount();
        double totalExpenses = totalExpenses();
        double clearFinance = totalAmount - totalExpenses;

        TextView totalAmountText = (TextView) layout.findViewById(R.id.totalAmount_text);
        TextView totalExpensesText = (TextView) layout.findViewById(R.id.totalExpenses_text);
        TextView clearFinanceText = (TextView) layout.findViewById(R.id.clearFinance_text);

        totalAmountText.setText(" " +String.valueOf(f.format(totalAmount()))+ " ₽");
        totalExpensesText.setText(" " +String.valueOf(f.format(totalExpenses()))+ " ₽");
        clearFinanceText.setText(" " +String.valueOf(f.format(clearFinance))+ " ₽");

        //Съэкономлено пока отложим
//            TextView savedOnEggs_text = (TextView) view.findViewById(R.id.savedOnEggs_text);
//            TextView savedOnMilk_text = (TextView) view.findViewById(R.id.savedOnMilk_text);
//            TextView savedOnMeat_text = (TextView) view.findViewById(R.id.savedOnMeat_text);
//
//            savedOnEggs_text.setText(String.valueOf((mydbManager.sumSaleEggPrice())));
//            savedOnMilk_text.setText(String.valueOf((mydbManager.sumSaleMilkPrice())));
//            savedOnMeat_text.setText(String.valueOf((mydbManager.sumSaleMeatPrice())));
        return layout;
    }

    // Добавляем продукцию
    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();
    }

    //Считаем сумму по каждому продукту
    public Map addProduct() {
        Map<String, Double> tempList = new HashMap<>();
        for (String product : productList) {

            Cursor cursor = myDB.idProduct1(MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, product);

            if (cursor != null && cursor.getCount() != 0) {

                while (cursor.moveToNext()) {
                    Double productUnit = cursor.getDouble(6);
                    if (tempList.get(product) == null) {
                        tempList.put(product, productUnit);
                    } else {
                        double sum = tempList.get(product) + productUnit;
                        tempList.put(product, sum);
                    }
                }
                cursor.close();
            } else {
                tempList.put(product, 0.0);
            }
        }
        return tempList;
    }

    //расчеты
    // Общая прибыль
    public double totalAmount() {
            Cursor cursor = myDB.readAllDataSale();
            double sum = 0;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    double productUnit = cursor.getDouble(6);
                    sum += productUnit;
                    }
                cursor.close();
            } else {
               return 0;
            }
        return sum;
    }

    // Общие расходы
    public double totalExpenses() {
        Cursor cursor = myDB.readAllDataExpenses();
        double sum = 0;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                double productUnit = cursor.getDouble(2);
                sum += productUnit;
            }
            cursor.close();
        } else {
            return 0;
        }

        return sum;
    }



    //Кнопка
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.financeChart_button:
                onClickFinanceChart(v, new FinanceChartFragment());
                break;
            case R.id.financeChart2_button:
                onClickFinanceChart(v, new FinanceChart2Fragment());
                break;
        }
    }

    //Функция кнопки
    public void onClickFinanceChart(View view, Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }
}
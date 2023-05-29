package com.hfad.myferma.Finance;

import android.database.Cursor;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PriceFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private TextInputLayout til;
    private Map<String, Double> tempList;
    private List<String> productList;
    private View layout;

    // Прогрузка фрагмента и его активных частей
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_price, container, false);
        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList();

        // Настройка аппбара настройка стелочки назад
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        add();
        addMap();
        onBackPressed();
        editTxt();
        return layout;
    }

    //Формируем массив из БД
    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();
    }
    // Формируем мапу из БД с ценой
    public void addMap() {
        tempList = new HashMap<>();
        Cursor cursor = myDB.readAllDataPrice();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String product = cursor.getString(1);
                Double price = cursor.getDouble(2);
                tempList.put(product, price);
            }
            for (String product : productList) {
                if (!tempList.containsKey(product)) {
                    tempList.put(product, 0.0);
                }
            }
        } else {
            for (String product : productList) {
                tempList.put(product, 0.0);
            }
        }
        cursor.close();
    }

    // Настраиваем программно EditText
    public void onBackPressed() {
        for (String product : productList) {
            LinearLayout container = (LinearLayout) layout.findViewById(R.id.mlayout);
            container.setOrientation(LinearLayout.VERTICAL);
            til = new TextInputLayout(getActivity());

            // Установка наименования
            til.setHint("Товар " + product);
            til.setHelperText("Укажите цену за одну единицу товара");
            til.setSuffixText("₽");
            til.setErrorEnabled(true);
            til.setTag(product);

            //Установка задней иконки
            til.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
            til.setEndIconDrawable(R.drawable.baseline_east_24);
            til.getEndIconDrawable();

            til.setStartIconDrawable(R.drawable.baseline_shopping_bag_24);
            til.getStartIconDrawable();

            TextInputEditText et = new TextInputEditText(til.getContext());
            et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            et.setImeOptions(EditorInfo.IME_ACTION_GO);
            til.addView(et);
            container.addView(til);

        }
    }

    // Назначаем EditText
    public void editTxt() {
        for (Map.Entry<String, Double> entry :
                tempList.entrySet()) {
            TextInputLayout p = layout.findViewWithTag(entry.getKey());
            startIcon(p,entry.getKey());
            p.getEditText().setText(String.valueOf(entry.getValue()));
            p.setEndIconOnClickListener(endIconClick(p, entry.getKey()));
            p.getEditText().setOnEditorActionListener(keyboardClick(p, entry.getKey()));

        }
    }

    // Запуск со значка
    public View.OnClickListener endIconClick(TextInputLayout p, String product) {
        View.OnClickListener editorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInsertPrise(p,product);
            }
        };
        return editorListener;
    }

    // Запуск с клавиатуры
    public TextView.OnEditorActionListener keyboardClick(TextInputLayout p, String product) {
        TextView.OnEditorActionListener editorListenerEgg = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    checkInsertPrise(p,product);
                }
                return false;
            }
        };
        return  editorListenerEgg;
    }

    //Проверка EditText
    public void checkInsertPrise (TextInputLayout p, String product){
        if (!p.getEditText().getText().toString().equals("")) {
            p.setErrorEnabled(false);
            double price = Double.parseDouble(p.getEditText().getText().toString());
            inserPrice(product, price);
            p.setEndIconDrawable(R.drawable.baseline_done_24);
            p.getEndIconDrawable();
        } else {
            p.setError("Введите цену!");
            p.getError();
        }
    }

    //Логика заполнение таблицы
    public void inserPrice(String animalsType, double price) {

        //Проверка была ли первый раз внесена цена или нет
        if (myDB.dataPrice(animalsType).getCount() == 0) {
            myDB.insertToDbPrice(animalsType, price);
            Toast.makeText(getActivity(), "Добавили цену за " + animalsType + " " + price + " руб.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Обновили цену за " + animalsType + " " + price + " руб.", Toast.LENGTH_SHORT).show();
            myDB.updateDataPrice(animalsType, price);
        }

    }


    //Настройка значков на продукции (в будущем можно дополнить)
    public void startIcon(TextInputLayout p, String product) {

        if (product.equals("Яйца")) {
            p.setStartIconDrawable(R.drawable.ic_action_egg);
            p.getStartIconDrawable();
        } else if (product.equals("Молоко")) {
            p.setStartIconDrawable(R.drawable.ic_action_milk);
            p.getStartIconDrawable();
        } else if (product.equals("Мясо")) {
            p.setStartIconDrawable(R.drawable.ic_action_meat);
            p.getStartIconDrawable();
        }
    }
    @Override
    public void onClick(View v) {

    }
}
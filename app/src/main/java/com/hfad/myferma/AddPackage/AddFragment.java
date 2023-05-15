package com.hfad.myferma.AddPackage;

import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private TextInputLayout addEdit;
    private TextView totalAdd_text;
    private AutoCompleteTextView animals_spiner;
    private Map<String, Double> tempList;
    private List<String> productList;
    private DecimalFormat f;
    private String unit = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add, container, false);

        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList<>();
        add();
        add1();

        //Установка EditText
        addEdit = layout.findViewById(R.id.add_edit);
        addEdit.getEditText().setOnEditorActionListener(editorListenerAdd);

        // Установка текста
        totalAdd_text = (TextView) layout.findViewById(R.id.totalAdd_text);
        totalAdd_text.setText(f.format(String.valueOf(tempList.get(animals_spiner.getText()))));
        // Установка спинера
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner.setText("Яйца", false);

        f = new DecimalFormat("0");


        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unitString(animals_spiner.getText().toString());

                totalAdd_text.setText(f.format(String.valueOf(tempList.get(animals_spiner.getText()))) + unit); //Todo suffix+ f = new DecimalFormat("0.00");


//                    switch (position) {
//                        case 0:
//                            f = new DecimalFormat("0");
//                            addEdit.setSuffixText("шт.");
//                            totalAdd_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
//                            break;
//                        case 1:
//                            f = new DecimalFormat("0.00");
//                            addEdit.setSuffixText("л.");
//                            totalAdd_text.setText(f.format(mydbManager.sumSaleMilk()) + " л.");
//                            break;
//                        case 2:
//                            f = new DecimalFormat("0.00");
//                            addEdit.setSuffixText("кг.");
//                            totalAdd_text.setText(f.format(mydbManager.sumSaleMeat()) + " кг.");
//                            break;
//                    }
            }
        });

        addEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24);

        Button add = (Button) layout.findViewById(R.id.add_button);
        add.setOnClickListener(this);

        Button addChart = (Button) layout.findViewById(R.id.addChart_button);
        addChart.setOnClickListener(this);

        if (savedInstanceState != null) {
            animals_spiner.setText("Яйца", false);
        }
        return layout;
    }

    //Добавляем продукцию в список
    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();
    }


    //Формируем список из БД
    public void add1() {
        tempList = new HashMap<>();

        for (String product : productList) {

            Cursor cursor = myDB.idProduct1(MyConstanta.TABLE_NAME, MyConstanta.TITLE, product);

            if (cursor != null) {

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
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_edit:
                addEdit.getEditText().setOnEditorActionListener(editorListenerAdd);
                break;
            case R.id.add_button:
                onClickAdd(v);
                break;
            case R.id.addChart_button:
                addChart(v);
                break;
        }
    }

    //Добавление продкции в таблицу через клавиатуру
    private TextView.OnEditorActionListener editorListenerAdd = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                addInTable();
            }
            return false;
        }
    };

    //Добавление продкции в таблицу через кнопку
    public void onClickAdd(View view) {
        MainActivity activity = new MainActivity();
        addInTable();
        activity.closeKeyboard();
    }

    // Логика добавление продукции в таблицу
    public void addInTable() {
        if (!addEdit.getEditText().getText().toString().equals("") && !animals_spiner.getText().toString().equals("")) {

            String animalsType = animals_spiner.getText().toString();
            String inputUnitString = addEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", "");

            // Для ввода целых чисел или дробных

            if (animalsType.equals("Яйца")) {
                if (inputUnitString.contains(".")) {
                    addEdit.setError("Яйца не могут быть дробными...");
                    addEdit.getError();
                    return;
                }
            }

            double inputUnit = Double.parseDouble(inputUnitString);

            //убираем ошибку
            addEdit.setErrorEnabled(false);

            unitString(animalsType);

            myDB.insertToDb(animalsType, inputUnit);

            tempList.put(animalsType, tempList.get(animalsType) + inputUnit);
            totalAdd_text.setText(f.format(tempList.get(animalsType)) + unit);

            addEdit.getEditText().getText().clear();
            addEdit.setEndIconDrawable(R.drawable.baseline_done_24);
            addEdit.getEndIconDrawable();

            Toast.makeText(getActivity(), "Добавлено " + inputUnit + unit, Toast.LENGTH_SHORT).show();
        } else {
            addEdit.setError("Введите кол-во!");
            addEdit.getError();
        }
    }


    public void unitString(String animals) {
        if (animals.equals("Яйца")) {
            f = new DecimalFormat("0");
            unit = " шт.";
        } else if (animals.equals("Молоко")) {
            f = new DecimalFormat("0.00");
            unit = " л.";
        } else if (animals.equals("Мясо")) {
            f = new DecimalFormat("0.00");
            unit = " кг.";
        } else {
            f = new DecimalFormat("0.00");
            unit = " ед.";
        }
    }

    public void addChart(View view) {
        AddChartFragment addChartFragment = new AddChartFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, addChartFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

//    public void onClickAdHistory(View view) {
//        Intent intent = new Intent(getActivity(), AddActivity.class);
//        startActivity(intent);
//    }


//    private void fillCountryList() {
//        countryList = new ArrayList<>();
//        countryList.add(new CountryItem("Яйца2", R.drawable.baseline_egg_24));
//        countryList.add(new CountryItem("Молоко", R.drawable.baseline_backpack_24));
//        countryList.add(new CountryItem("Яйца", R.drawable.baseline_catching_pokemon_24));
//    }


}
package com.hfad.myferma.SalePackage;


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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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


public class SaleFragment extends Fragment implements View.OnClickListener {
    private TextView result_text, priceSale, error;
    private MyFermaDatabaseHelper myDB;

    private TextInputLayout addSaleEdit, addPrice;
    private AutoCompleteTextView animals_spiner;
    private String unit = null;
    private CheckBox checkPrice;
    private Map<String, Double> tempList;
    private Map<String, Double> tempListPrice;
    private ArrayAdapter<String> arrayAdapterProduct;
    private List<String> productList;
    private DecimalFormat f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList<>();
        add();
        add1();
        addMapPrice();

        View layout = inflater.inflate(R.layout.fragment_sale, container, false);

        // Установка EditText
        addSaleEdit = layout.findViewById(R.id.addSale_edit);
        addSaleEdit.getEditText().setOnEditorActionListener(editorListenerSale);

        // Установка EditText Price
        addPrice = (TextInputLayout) layout.findViewById(R.id.addPrice_edit);
        // Установка текста
        priceSale = (TextView) layout.findViewById(R.id.priceSale_text);
        result_text = (TextView) layout.findViewById(R.id.totalSale_text);
        error = (TextView) layout.findViewById(R.id.errorText);

        // Установка CheckBox
        checkPrice = layout.findViewById(R.id.check_price);
        // Установка Spinner
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner.setText(productList.get(0), false);
        addPrice.setVisibility(View.GONE);

        f = new DecimalFormat("0");
        String product = animals_spiner.getText().toString();
        unitString(product);
        result_text.setText(f.format(tempList.get(product)) + unit); //Todo suffix+ f = new DecimalFormat("0.00");
        priceSale.setText(unit + " Товара " + product + " " + tempListPrice.get(product) + " ₽");

        //При выборе спинера происходят следующие изменения
        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productClick = productList.get(position);
                unitString(productClick);
                result_text.setText(f.format(tempList.get(productClick)) + unit);
                priceSale.setText(unit + " Товара " + productClick + " " + tempListPrice.get(productClick) + " ₽");
                addSaleEdit.setSuffixText(unit);
                addSaleEdit.setEndIconDrawable(null);
                addSaleEdit.getEndIconDrawable();
                addPrice.setEndIconDrawable(null);
                addPrice.getEndIconDrawable();

            }
        });

        // установка иконки
        addSaleEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24);

        // устнановка кнопок
        Button addSale = (Button) layout.findViewById(R.id.addSale_button);
        addSale.setOnClickListener(this);

        Button saleChart = (Button) layout.findViewById(R.id.saleChart_button);
        saleChart.setOnClickListener(this);

        //Настройка чека
        checkPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPrice.isChecked()) {
                    addPrice.setVisibility(View.VISIBLE);
                    addSaleEdit.setEndIconDrawable(null);
                    addSaleEdit.getEndIconDrawable();
                    addPrice.setEndIconDrawable(null);
                    addPrice.getEndIconDrawable();
                } else {
                    addPrice.setVisibility(View.GONE);
                    addSaleEdit.setEndIconDrawable(null);
                    addSaleEdit.getEndIconDrawable();
                    addPrice.setEndIconDrawable(null);
                    addPrice.getEndIconDrawable();
                }
            }
        });

        if (checkPrice.isChecked()) {
            addPrice.setVisibility(View.VISIBLE);
        }

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, productList);
            animals_spiner.setAdapter(arrayAdapterProduct);
        }
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
    }

    public void addMapPrice() {
        tempListPrice = new HashMap<>();
        Cursor cursor = myDB.readAllDataPrice();
        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String product = cursor.getString(1);
                Double price = cursor.getDouble(2);
                tempListPrice.put(product, price);
            }
        }
        cursor.close();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSale_edit:
                addSaleEdit.getEditText().setOnEditorActionListener(editorListenerSale);
                break;
            case R.id.addSale_button:
                onClickAddSale(v);
                break;
            case R.id.saleChart_button:
                saleChart(v);
                break;
        }
    }

    private TextView.OnEditorActionListener editorListenerSale = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                saleInTable();
            }
            return false;
        }
    };

    public void onClickAddSale(View view) {
        MainActivity activity = new MainActivity();
        saleInTable();
        activity.closeKeyboard();
    }

//    public void onClickSaleHistori(View view) {
//        Intent intent = new Intent(getActivity(), SaleActivity.class);
//        startActivity(intent);
//    }

    //Основная логика добавления в журнал
    public void saleInTable() {
        if (!addSaleEdit.getEditText().getText().toString().equals("")) {

            String animalsType = animals_spiner.getText().toString();
            String inputUnitString = addSaleEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", "");

            // Для ввода целых чисел или дробных
            if (animalsType.equals("Яйца")) {
                if (inputUnitString.contains(".")) {
                    addSaleEdit.setError("Яйца не могут быть дробными...");
                    addSaleEdit.getError();
                    return;
                }
            }
            // Ошибка если отсутсвует цифра
            if (checkPrice.isChecked() && addPrice.getEditText().getText().toString().equals("")) {
                addPrice.setError("Укажите цену!");
                addPrice.getError();
                return;
            }

            double inputUnit = Double.parseDouble(inputUnitString);

            //убираем ошибку
            addSaleEdit.setErrorEnabled(false);
            addPrice.setErrorEnabled(false);

            //проверка, что введены цены на товар
            if (tempList.containsKey(animalsType)) {
                if (comparison(animalsType, inputUnit)) {
                    double priceSale = inputUnit * tempListPrice.get(animalsType);
                    tempList.put(animalsType, tempList.get(animalsType) - inputUnit);
                    // проверка чекера
                    if (checkPrice.isChecked()) {
                        myDB.insertToDbSale(animalsType, inputUnit, Double.parseDouble(addPrice.getEditText().getText().toString()));//todo ценообразование
                        result_text.setText(f.format(tempList.get(animalsType)) + unit);
                        Toast.makeText(getActivity(), "Вы заработали " + addPrice.getEditText().getText().toString() + " ₽", Toast.LENGTH_SHORT).show();
                    } else {
                        myDB.insertToDbSale(animalsType, inputUnit, priceSale);
                        result_text.setText(f.format(tempList.get(animalsType)) + unit);
                        Toast.makeText(getActivity(), "Вы заработали " + priceSale + " ₽", Toast.LENGTH_SHORT).show();
                    }
                    // установка значков после выполнения
                    addSaleEdit.getEditText().getText().clear();
                    error.setText("");
                    addSaleEdit.setEndIconDrawable(R.drawable.baseline_done_24);
                    addSaleEdit.getEndIconDrawable();

                    addPrice.getEditText().getText().clear();
                    addPrice.setEndIconDrawable(R.drawable.baseline_done_24);
                    addPrice.getEndIconDrawable();
                }


            } else {
                error.setText("Пожалуйста, введите цену за 1 ед. товара в разеделе Мои Финансы, чтобы продать");
                Toast.makeText(getActivity(), "Пожалуйста, укажите цену!", Toast.LENGTH_SHORT).show();
            }
        } else {
            addSaleEdit.setError("Введите кол-во!");
            addSaleEdit.getError();
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

    //проверка что не уйдем в минус
    boolean comparison(String animalsType, double inputUnit) {
        if (tempList.get(animalsType) - inputUnit < 0) {
            addSaleEdit.setError("Нет столько товара на складе");
            addSaleEdit.getError();
            return false;
        }
        return true;
    }

    public void saleChart(View view) {
        SaleChartFragment saleChartFragment = new SaleChartFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, saleChartFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

}
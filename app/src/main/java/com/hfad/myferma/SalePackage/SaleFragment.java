package com.hfad.myferma.SalePackage;


import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;


public class SaleFragment extends Fragment implements View.OnClickListener {
    private TextView result_text, priceSale, error;

    private TextInputLayout addSaleEdit, addPrice;
    private AutoCompleteTextView animals_spiner;
    private MydbManagerMetod mydbManager;
    private String unit = null;
    private CheckBox checkPrice;
    private DecimalFormat f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mydbManager = new MydbManagerMetod(inflater.getContext());
        View layout = inflater.inflate(R.layout.fragment_sale, container, false);

        addSaleEdit = layout.findViewById(R.id.addSale_edit);
        addSaleEdit.getEditText().setOnEditorActionListener(editorListenerSale);
        addPrice = (TextInputLayout) layout.findViewById(R.id.addPrice_edit);
        result_text = (TextView) layout.findViewById(R.id.totalSale_text);
        error = (TextView) layout.findViewById(R.id.errorText);
        priceSale = (TextView) layout.findViewById(R.id.priceSale_text);
        checkPrice = layout.findViewById(R.id.check_price);
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner.setText("Яйца", false);
        addPrice.setVisibility(View.GONE);

        f = new DecimalFormat("0");

        //При выборе спинера происходят следующие изменения
        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addSaleEdit.setSuffixText("шт.");
                        f = new DecimalFormat("0");
                        result_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
                        priceSale.setText("Яйцо " + mydbManager.price("Яйца") + " ₽");
                        break;
                    case 1:
                        addSaleEdit.setSuffixText("л.");
                        f = new DecimalFormat("0.00");
                        result_text.setText(f.format(mydbManager.sumSaleMilk()) + " л.");
                        priceSale.setText("литр Молока " + mydbManager.price("Молоко") + " ₽");
                        break;
                    case 2:
                        addSaleEdit.setSuffixText("кг.");
                        f = new DecimalFormat("0.00");
                        result_text.setText(f.format(mydbManager.sumSaleMeat()) + " кг.");
                        priceSale.setText("кг. Мяса " + mydbManager.price("Мясо") + " ₽");
                        break;
                }
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
                } else {
                    addPrice.setVisibility(View.GONE);
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
            mydbManager.open();
            result_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
            priceSale.setText("Яйцо " + mydbManager.price("Яйца") + " ₽");
        }
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

            unitString(animalsType);

            //убираем ошибку
            addSaleEdit.setErrorEnabled(false);
            addPrice.setErrorEnabled(false);


                //проверка, что введены цены на товар
                if (!mydbManager.getFromBooleanPrice(animalsType)) {
                    if (comparison(animalsType, inputUnit)) {
                    double priceSale = inputUnit * mydbManager.price(animalsType);
                    // проверка чекера
                    if (checkPrice.isChecked()) {
                        mydbManager.insertToDbSale(animalsType, inputUnit, Double.parseDouble(addPrice.getEditText().getText().toString()));//todo ценообразование
                        result_text.setText(f.format(mydbManager.sumSale(animalsType)) + unit);
                        Toast.makeText(getActivity(), "Вы заработали " + addPrice.getEditText().getText().toString() + " ₽", Toast.LENGTH_SHORT).show();
                    } else {
                        mydbManager.insertToDbSale(animalsType, inputUnit, priceSale);//todo ценообразование
                        result_text.setText(f.format(mydbManager.sumSale(animalsType)) + unit);
                        Toast.makeText(getActivity(), "Вы заработали " + priceSale + " ₽", Toast.LENGTH_SHORT).show();
                    }
                }
                // установка значков после выполнения
                addSaleEdit.getEditText().getText().clear();
                error.setText("");
                addSaleEdit.setEndIconDrawable(R.drawable.baseline_done_24);
                addSaleEdit.getEndIconDrawable();

                addPrice.getEditText().getText().clear();
                addPrice.setEndIconDrawable(R.drawable.baseline_done_24);
                addPrice.getEndIconDrawable();

            } else {
                error.setText("Пожалуйста, введите цену за 1 ед. товара в разеделе Мои Финансы, чтобы продать");
                Toast.makeText(getActivity(), "Пожалуйста, укажите цену!", Toast.LENGTH_SHORT).show();
            }
        } else {
            addSaleEdit.setError("Введите кол-во!");
            addSaleEdit.getError();
        }

    }

    public String unitString(String animals) {
        if (animals.equals("Яйца")) {
            unit = " шт.";
        }
        if (animals.equals("Молоко")) {
            unit = " л.";
        }
        if (animals.equals("Мясо")) {
            unit = " кг.";
        }
        return unit;
    }

    //проверка что не уйдем в минус
    boolean comparison(String animalsType, double inputUnit) {
        if (mydbManager.sumSale(animalsType) - inputUnit < 0) {
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
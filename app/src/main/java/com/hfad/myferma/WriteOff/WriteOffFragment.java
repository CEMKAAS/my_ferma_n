package com.hfad.myferma.WriteOff;


import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.SaleChartFragment;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManager;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WriteOffFragment extends Fragment implements View.OnClickListener {
    private TextView result_text, error;
    private MyFermaDatabaseHelper myDB;
    private RadioButton radioButton1, radioButton2;
    private RadioGroup radioGroup;
    private TextInputLayout addWriteOffEdit;
    private AutoCompleteTextView animals_spiner;
    private Map<String, Double> tempList;
    private List<String> productList;
    private ArrayAdapter<String> arrayAdapterProduct;
    private String unit = null;

    private DecimalFormat f;
    private int status = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList<>();
        add();
        add1();

        View layout = inflater.inflate(R.layout.fragment_write_off, container, false);

        // Установка EditText
        addWriteOffEdit = layout.findViewById(R.id.add_edit);
        addWriteOffEdit.getEditText().setOnEditorActionListener(editorListenerWriteOff);
        //Установка Radio
        radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) layout.findViewById(R.id.radio_button_1);
        radioButton2 = (RadioButton) layout.findViewById(R.id.radio_button_2);

        // Установка Текста
        result_text = (TextView) layout.findViewById(R.id.result_text);

        error = (TextView) layout.findViewById(R.id.errorText);

        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner.setText("Яйца", false);

        f = new DecimalFormat("0");

        String product = animals_spiner.getText().toString();
        unitString(product);
        result_text.setText(f.format(tempList.get(product)) + unit);

        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productClick = productList.get(position);
                unitString(productClick);
                result_text.setText(f.format(tempList.get(productClick)) + unit);
                addWriteOffEdit.setSuffixText(unit);
                addWriteOffEdit.setError(null);
                addWriteOffEdit.setEndIconDrawable(null);
                addWriteOffEdit.getEndIconDrawable();

            }
        });
        addWriteOffEdit.setStartIconDrawable(R.drawable.baseline_shopping_bag_24);

        Button addWriteOff = (Button) layout.findViewById(R.id.addWriteOff_button);
        addWriteOff.setOnClickListener(this);

        Button addWriteOffChar = (Button) layout.findViewById(R.id.writeOffChart_button);
        addWriteOffChar.setOnClickListener(this);

        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setOnClickListener(this);

        //настройка верхнего меню фаб кнопку
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Списания");

        fab.show();
        fab.setText("Журнал");
        fab.setIconResource(R.drawable.ic_action_book);
        fab.getIcon();

        //Радио переключатель
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button_1:
                    status = 0;
                    break;

                case R.id.radio_button_2:
                    status = 1;
                    break;
            }
        });

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


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_edit:
                addWriteOffEdit.getEditText().setOnEditorActionListener(editorListenerWriteOff);
                break;

            case R.id.addWriteOff_button:
                onClickAddSale(v);
                break;

            case R.id.extended_fab:
                Intent intent = new Intent(getActivity(), WriteOffActivity.class);
                startActivity(intent);
                break;
            case R.id.writeOffChart_button:
                writeOffChart(v);
                break;
        }
    }

    private TextView.OnEditorActionListener editorListenerWriteOff = new TextView.OnEditorActionListener() {
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

    public void saleInTable() {
        if (!addWriteOffEdit.getEditText().getText().toString().equals("")) {
            String animalsType = animals_spiner.getText().toString();
            String inputUnitString = addWriteOffEdit.getEditText().getText().toString().replaceAll(",", ".").replaceAll("[^\\d.]", "");;

            // Для ввода целых чисел или дробных
            if (animalsType.equals("Яйца")) {
                if (inputUnitString.contains(".")){
                    addWriteOffEdit.setError("Яйца не могут быть дробными...");
                    addWriteOffEdit.getError();
                    return;
                }
            }

            double inputUnit = Double.parseDouble(inputUnitString);

            //убираем ошибку
            addWriteOffEdit.setErrorEnabled(false);

            if (comparison(animalsType, inputUnit)) {
                //проверка, что введены цены на товар
                if (status == 0) {
                    myDB.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_cottage_24);
                } else {
                    myDB.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_delete_24);
                }
                tempList.put(animalsType, tempList.get(animalsType) - inputUnit);
                result_text.setText(f.format(tempList.get(animalsType)) + unit);
                addWriteOffEdit.getEditText().getText().clear();
                error.setText("");
                addWriteOffEdit.setEndIconDrawable(R.drawable.baseline_done_24);
                addWriteOffEdit.getEndIconDrawable();
            }
        } else {
            addWriteOffEdit.setError("Введите кол-во!");
            addWriteOffEdit.getError();
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
            addWriteOffEdit.setError("Нет столько товара на складе");
            addWriteOffEdit.getError();
            return false;
        }
        return true;
    }

    public void writeOffChart(View view){
        WriteOffChartFragment writeOffChartFragment = new WriteOffChartFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, writeOffChartFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }


}
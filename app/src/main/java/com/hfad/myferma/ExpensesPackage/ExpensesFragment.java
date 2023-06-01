package com.hfad.myferma.ExpensesPackage;


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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.SaleChartFragment;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManager;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ExpensesFragment extends Fragment implements View.OnClickListener {

    private TextView totalExpenses_text, unit_text;
    private TextInputLayout expenses_editText, menu;
    private AutoCompleteTextView expensesName_editText;
    private List<String> arrayListAnaimals;
    private ArrayAdapter<String> arrayAdapterAnimals;
    private DecimalFormat f;
    private MyFermaDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myDB = new MyFermaDatabaseHelper(getActivity());
        View layout = inflater.inflate(R.layout.fragment_expenses, container, false);

        expenses_editText = layout.findViewById(R.id.expenses_editText);
        expensesName_editText = layout.findViewById(R.id.expensesName_editText);
        menu = (TextInputLayout) layout.findViewById(R.id.menu);

        expenses_editText.getEditText().setOnEditorActionListener(editorListenerExpenses);
        expensesName_editText.setOnEditorActionListener(editorListenerExpenses);

        Button addExpenses = (Button) layout.findViewById(R.id.addExpenses_button);
        addExpenses.setOnClickListener(this);

        Button expensesChart = (Button) layout.findViewById(R.id.expensesChart_button);
        expensesChart.setOnClickListener(this);
        arrayListAnaimals = new ArrayList<>();

        allExpenses();

        f = new DecimalFormat("0.00");
        totalExpenses_text = (TextView) layout.findViewById(R.id.totalExpenses_text);
        totalExpenses_text.setText(String.valueOf(f.format(getClearFinance())) + " ₽");

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            arrayAdapterAnimals = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListAnaimals);
            expensesName_editText.setAdapter(arrayAdapterAnimals);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expenses_editText:
                expenses_editText.getEditText().setOnEditorActionListener(editorListenerExpenses);
                break;

            case R.id.addExpenses_button:
                onClickAddExpenses(v);
                break;

            case R.id.expensesChart_button:
                expensesChart(v);
                break;

        }
    }

    private TextView.OnEditorActionListener editorListenerExpenses = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    Toast.makeText(getActivity(), "Добавлено!", Toast.LENGTH_SHORT);
                    menu.setErrorEnabled(false);
                    break;
                case EditorInfo.IME_ACTION_GO:
                    expensesInTable();
                    break;
            }
            return false;
        }
    };

    public void onClickAddExpenses(View view) {
        MainActivity activity = new MainActivity();
        expensesInTable();
        activity.closeKeyboard();
    }

    public void expensesInTable() {
        menu.setErrorEnabled(false);
        if (!expenses_editText.getEditText().getText().toString().equals("") && !expensesName_editText.getText().toString().equals("")) {

            Toast.makeText(getActivity(), "Добавлено!", Toast.LENGTH_SHORT).show();

            String expensesName = expensesName_editText.getText().toString();
            String inputExpensesString = expenses_editText.getEditText().getText().toString();

            //убираем ошибку
            expenses_editText.setErrorEnabled(false);

            if (!arrayListAnaimals.contains(expensesName)) {
                arrayListAnaimals.add(expensesName);
                arrayAdapterAnimals = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListAnaimals);
                expensesName_editText.setAdapter(arrayAdapterAnimals);
            }

            double inputExpenses = Double.parseDouble(inputExpensesString.replaceAll(",", ".").replaceAll("[^\\d.]", ""));

            myDB.insertToDbExpenses(expensesName, inputExpenses);

            expenses_editText.setEndIconDrawable(R.drawable.baseline_done_24);
            expenses_editText.getEndIconDrawable();
            totalExpenses_text.setText(String.valueOf(f.format(getClearFinance())) + " ₽");
            expensesName_editText.getText().clear();
            expenses_editText.getEditText().getText().clear();

        } else {
            if (expenses_editText.getEditText().getText().toString().equals("")) {
                expenses_editText.setError("Введите цену!");
                expenses_editText.getError();
            }
            if (expensesName_editText.getText().toString().equals("")) {
                menu.setError("Введите товар");
                menu.getError();
            }
        }
    }


    public  void allExpenses() {
        Set<String> expensesName = new HashSet<>();
        Cursor cursor = myDB.readAllDataExpenses();

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                expensesName.add(cursor.getString(1));
            }
            cursor.close();
        }

        for (String name: expensesName
             ) {
            arrayListAnaimals.add(name);
        }

    }






    public void expensesChart(View view){
        ExpensesChartFragment expensesChartFragment = new ExpensesChartFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, expensesChartFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();

    }

    //расчеты
    // Общая прибыль
    public double totalAmount() {

        Cursor cursor = myDB.readAllDataSale();
        double sum = 0;

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                double productUnit = cursor.getDouble(6);
                sum += productUnit;
            }
            cursor.close();
        }

        return sum;
    }

    // Общие расходы
    public double totalExpenses() {

        Cursor cursor = myDB.readAllDataExpenses();
        double sum = 0;

        if (cursor != null && cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                double productUnit = cursor.getDouble(2);
                sum += productUnit;
            }
            cursor.close();
        }

        return sum;
    }

    //Считает чистую прибыль(тяжелый способо, но пока так)
    public double getClearFinance() {

        double totalAmount = totalAmount();
        double totalExpenses = totalExpenses();
        double clearFinance = totalAmount - totalExpenses;
        return clearFinance;
    }


//    public void onClickExpensesHistori(View view) {
//        Intent intent = new Intent(getActivity(), ExpensesActivity.class);
//        startActivity(intent);
//    }

}
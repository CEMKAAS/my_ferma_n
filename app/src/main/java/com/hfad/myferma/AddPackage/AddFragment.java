package com.hfad.myferma.AddPackage;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;


public class AddFragment extends Fragment implements View.OnClickListener {

    private MydbManagerMetod mydbManager;
    private TextInputLayout addEdit, menu;
    private TextView totalAdd_text;
    private AutoCompleteTextView animals_spiner;

    private DecimalFormat f;

    private String unit = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add, container, false);

            mydbManager = new MydbManagerMetod(inflater.getContext());

//        fillCountryList();
            addEdit = layout.findViewById(R.id.add_edit);
            addEdit.getEditText().setOnEditorActionListener(editorListenerAdd);
            menu = (TextInputLayout) layout.findViewById(R.id.menu);
            totalAdd_text = (TextView) layout.findViewById(R.id.totalAdd_text);
            animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
            animals_spiner.setText("Яйца", false);

            f = new DecimalFormat("0");
            animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            f = new DecimalFormat("0");
                            addEdit.setSuffixText("шт.");
                            totalAdd_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
                            break;
                        case 1:
                            f = new DecimalFormat("0.00");
                            addEdit.setSuffixText("л.");
                            totalAdd_text.setText(f.format(mydbManager.sumSaleMilk()) + " л.");
                            break;
                        case 2:
                            f = new DecimalFormat("0.00");
                            addEdit.setSuffixText("кг.");
                            totalAdd_text.setText(f.format(mydbManager.sumSaleMeat()) + " кг.");
                            break;
                    }
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

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            mydbManager.open();
            totalAdd_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
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
                if (inputUnitString.contains(".")){
                    addEdit.setError("Яйца не могут быть дробными...");
                    addEdit.getError();
                    return;
                }
            }

            double inputUnit = Double.parseDouble(inputUnitString);

            //убираем ошибку
            addEdit.setErrorEnabled(false);

            unitString(animalsType);
            mydbManager.insertToDb(animalsType, inputUnit);
            totalAdd_text.setText(f.format(mydbManager.sumSale(animalsType)) + unit);

            addEdit.getEditText().getText().clear();
            addEdit.setEndIconDrawable(R.drawable.baseline_done_24);
            addEdit.getEndIconDrawable();
            Toast.makeText(getActivity(), "Добавлено " + inputUnit + unit, Toast.LENGTH_SHORT).show();
        } else {
            addEdit.setError("Введите кол-во!");
            addEdit.getError();
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
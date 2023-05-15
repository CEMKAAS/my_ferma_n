package com.hfad.myferma.WriteOff;


import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
import com.hfad.myferma.db.MydbManager;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;


public class WriteOffFragment extends Fragment implements View.OnClickListener {
    private TextView result_text, error;

    private RadioButton radioButton1, radioButton2;
    private RadioGroup radioGroup;
    private TextInputLayout addWriteOffEdit, menu;
    private AutoCompleteTextView animals_spiner;
    private MydbManagerMetod mydbManager;
    private String unit = null;

    private DecimalFormat f;
    private int status = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mydbManager = new MydbManagerMetod(inflater.getContext());
        View layout = inflater.inflate(R.layout.fragment_write_off, container, false);

        addWriteOffEdit = layout.findViewById(R.id.add_edit);
        addWriteOffEdit.getEditText().setOnEditorActionListener(editorListenerWriteOff);

        radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) layout.findViewById(R.id.radio_button_1);
        radioButton2 = (RadioButton) layout.findViewById(R.id.radio_button_2);


        menu = (TextInputLayout) layout.findViewById(R.id.menu);
        result_text = (TextView) layout.findViewById(R.id.result_text);

        error = (TextView) layout.findViewById(R.id.errorText);

        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner.setText("Яйца", false);

        f = new DecimalFormat("0");
        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addWriteOffEdit.setSuffixText("шт.");
                        f = new DecimalFormat("0");
                        result_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
                        break;
                    case 1:
                        addWriteOffEdit.setSuffixText("л.");
                        f = new DecimalFormat("0.00");
                        result_text.setText(f.format(mydbManager.sumSaleMilk()) + " л.");
                        break;
                    case 2:
                        addWriteOffEdit.setSuffixText("кг.");
                        f = new DecimalFormat("0.00");
                        result_text.setText(f.format(mydbManager.sumSaleMeat()) + " кг.");
                        break;
                }
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
            mydbManager.open();
            result_text.setText(f.format(mydbManager.sumSaleEgg()) + " шт.");
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

            unitString(animalsType);

            //убираем ошибку
            addWriteOffEdit.setErrorEnabled(false);

            if (comparison(animalsType, inputUnit)) {
                //проверка, что введены цены на товар
                if (status == 0) {
                    mydbManager.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_cottage_24);
                } else {
                    mydbManager.insertToDbWriteOff(animalsType, inputUnit, R.drawable.baseline_delete_24);
                }
                result_text.setText(f.format(mydbManager.sumSale(animalsType)) + unit);
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
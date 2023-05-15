package com.hfad.myferma.WriteOff;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteOffChartFragment extends Fragment {
    private MyFermaDatabaseHelper myDB;
    private AutoCompleteTextView animals_spiner, mount_spiner, year_spiner;
    private ArrayList<BarEntry> visitors;
    private ArrayAdapter<String> arrayAdapterAnimals;

    private RadioButton radioButton1, radioButton2;

    private RadioGroup radioGroup;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;
    private int mount = 0;

    private int status = R.drawable.baseline_cottage_24;

    private View layout;

    private String infoChart = "График списанной продукции на собсвенные нужды";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_write_off_chart, container, false);

        // установка радио
        radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) layout.findViewById(R.id.radio_button_1);
        radioButton2 = (RadioButton) layout.findViewById(R.id.radio_button_2);

        // установка спинеров
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.mount_spiner);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.year_spiner);

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());

        //Создание списка с данными для графиков
        visitors = new ArrayList<>();

        // настройка спинеров
        animals_spiner.setText("Яйца", false);
        mount_spiner.setText("За весь год", false);
        year_spiner.setText("2023", false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        //настройка верхнего меню фаб кнопку
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Списания - График");

        //Логика просчета
        storeDataInArrays();

        // настройка графиков
        bar(labes,  infoChart);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button_1:
                    visitors.clear();
                    status = R.drawable.baseline_cottage_24;
                    storeDataInArrays();
                    infoChart = "График списанной продукции на собсвенные нужды";
                    if (mount != 13) {
                        bar(mountMass,  infoChart);
                    } else {
                        bar(labes,  infoChart);
                    }
                    break;

                case R.id.radio_button_2:
                    visitors.clear();
                    status = R.drawable.baseline_delete_24;
                    storeDataInArrays();
                    infoChart = "График списанной продукции на утилизацию";
                    if (mount != 13) {
                        bar(mountMass,  infoChart);
                    } else {
                        bar(labes,  infoChart);
                    }
                    break;
            }
        });
        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass,  infoChart);
                } else {
                    bar(labes,  infoChart);
                }
            }
        });
        mount_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass,  infoChart);
                } else {
                    bar(labes,  infoChart);
                }
            }
        });
        year_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass,  infoChart);
                } else {
                    bar(labes,  infoChart);
                }
            }
        });
        return layout;
    }

    public void bar(String[] xAsis, String info) {
        BarChart barChart = layout.findViewById(R.id.barChart);
        BarDataSet barDataSet = new BarDataSet(visitors, animals_spiner.getText().toString());
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.invalidate();
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText(info);
        barChart.animateY(2000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6); //сколько отображается
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAsis));
    }
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            arrayAdapterAnimals = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, add());
            year_spiner.setAdapter(arrayAdapterAnimals);
        }
    }

    public ArrayList<String> add() {
        Set<String> tempList = new HashSet<>();
        Cursor cursor = myDB.readAllDataSale();

        while (cursor.moveToNext()) {
            String string1 = cursor.getString(5);
            tempList.add(string1);
        }
        cursor.close();

        ArrayList<String> tempList1 = new ArrayList<>();
        for (String nameExpenses : tempList) {
            tempList1.add(nameExpenses);
        }

        return tempList1;
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataWriteOff();

        Map<Float, Float> sumCategory = new HashMap<>();

        String animalsType = animals_spiner.getText().toString();
        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();

        setMount(mountString);

        if (mount <= 12 && mount > 0) {
            allProductsMount(cursor, animalsType, year2);

        } else if (cursor.getCount() == 0) {
            visitors.add(new BarEntry(0, 0));
        }

        //проверка за весь год
        else if (mount == 13) {
            allProductsYear(cursor, sumCategory, year2, animalsType);
        } else {
            visitors.add(new BarEntry(0, 0));
        }
    }


    public void allProductsMount(Cursor cursor, String animalsType, String year2) {
        float x, y;
        while (cursor.moveToNext()) {
            // проверка статуса
            if (animalsType.equals(cursor.getString(1))) {
                //проверка месяца
                if (mount == Integer.parseInt(cursor.getString(4))) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {
                        //проверка статуса
                        if (status == Integer.parseInt(cursor.getString(6))) {

                            x = Float.parseFloat(cursor.getString(2));
                            y = Float.parseFloat(cursor.getString(3));

                            visitors.add(new BarEntry(y, x));
                        }
                    }
                }
            }
        }
        cursor.close();
    }

    public void allProductsYear(Cursor cursor, Map<Float, Float> sumCategory, String year2, String animalsType) {
        sumCategory.put(1F, (float) 0);
        sumCategory.put(2F, (float) 0);
        sumCategory.put(3F, (float) 0);
        sumCategory.put(4F, (float) 0);
        sumCategory.put(5F, (float) 0);
        sumCategory.put(6F, (float) 0);
        sumCategory.put(7F, (float) 0);
        sumCategory.put(8F, (float) 0);
        sumCategory.put(9F, (float) 0);
        sumCategory.put(10F, (float) 0);
        sumCategory.put(11F, (float) 0);
        sumCategory.put(12F, (float) 0);

        while (cursor.moveToNext()) {
            if (animalsType.equals(cursor.getString(1))) {
                if (year2.equals(cursor.getString(5))) {
                    if (status == Integer.parseInt(cursor.getString(6))) {
                        float sum = sumCategory.get(Float.parseFloat(cursor.getString(4))) + Float.parseFloat(cursor.getString(2));
                        sumCategory.put(Float.parseFloat(cursor.getString(4)), sum);
                    }
                }
            }
        }
        cursor.close();

        for (Map.Entry<Float, Float> entry : sumCategory.entrySet()) {
            Float name = entry.getKey();
            Float sum = entry.getValue();

            visitors.add(new BarEntry(name, sum));
        }
    }

    public void setMount(String mountString) {
        switch (mountString) {
            case "Январь":
                mount = 1;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Февраль":
                mount = 2;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", ""};
                break;
            case "Март":
                mount = 3;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Апрель":
                mount = 4;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", ""};
                break;
            case "Май":
                mount = 5;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Июнь":
                mount = 6;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", ""};
                break;
            case "Июль":
                mount = 7;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Август":
                mount = 8;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Сентябрь":
                mount = 9;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", ""};
                break;
            case "Октябрь":
                mount = 10;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "Ноябрь":
                mount = 11;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", ""};
                break;
            case "Декабрь":
                mount = 12;
                mountMass = new String[]{"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", ""};
                break;
            case "За весь год":
                mount = 13;
                break;
        }
    }
}
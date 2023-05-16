package com.hfad.myferma.AddPackage;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddChartFragment extends Fragment {

    private MyFermaDatabaseHelper myDB;
    private AutoCompleteTextView animals_spiner, animals_spiner2, animals_spiner3;
    private ArrayList<BarEntry> visitors;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;
    private int mount = 0;
    private View layout;
    private List<String> yearList, productList;
    private ArrayAdapter<String> arrayAdapterProduct, arrayAdapterYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());
        add();

        layout = inflater.inflate(R.layout.fragment_add_chart, container, false);
        // установка спинеров
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        animals_spiner2 = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner2);
        animals_spiner3 = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner3);

        //Создание списка с данными для графиков
        visitors = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        // настройка спинеров
        animals_spiner.setText("Яйца", false);
        animals_spiner2.setText("За весь год", false);
        animals_spiner3.setText(String.valueOf(calendar.get(Calendar.YEAR)), false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои товар - График");
        // Todo кнопка назад

        //Логика просчета
        storeDataInArrays();
        bar(labes);

        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass);
                } else {
                    bar(labes);
                }
            }
        });

        animals_spiner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass);
                } else {
                    bar(labes);
                }
            }
        });

        animals_spiner3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    bar(mountMass);
                } else {
                    bar(labes);
                }
            }
        });
        return layout;
    }

    public void bar(String[] xAsis) {
        //установка графиков
        BarChart barChart = layout.findViewById(R.id.barChart);
        // настройка графиков
        BarDataSet barDataSet = new BarDataSet(visitors, animals_spiner.getText().toString());
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.invalidate();
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("График добавленной продукции на склад\"");
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
            //Настройка спинера с продуктами
            arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, productList);
            animals_spiner.setAdapter(arrayAdapterProduct);

            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            arrayAdapterYear = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, yearList);
            animals_spiner3.setAdapter(arrayAdapterYear);
        }
    }

    public void add() {
        Set<String> yearSet = new HashSet<>();
        Set<String> productSet = new HashSet<>();
        Cursor cursor = myDB.readAllData();

        while (cursor.moveToNext()) {
            String year = cursor.getString(5);
            String product = cursor.getString(1);

            yearSet.add(year);
            productSet.add(product);
        }
        cursor.close();

        yearList = new ArrayList<>();
        productList = new ArrayList<>();
        for (String yearColum : yearSet) {
            yearList.add(yearColum);
        }
        for (String productColum : productSet) {
            productList.add(productColum);
        }
    }


    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        float x, y;
        String animalsType = animals_spiner.getText().toString();
        String mountString = animals_spiner2.getText().toString();
        String year2 = animals_spiner3.getText().toString();
        float jan = 0f;
        float feb = 0f;
        float mar = 0f;
        float apr = 0f;
        float mai = 0f;
        float jun = 0f;
        float jul = 0f;
        float aug = 0f;
        float sep = 0f;
        float oct = 0f;
        float nov = 0f;
        float dec = 0f;

        setMount(mountString);

        cursor.moveToNext();

        if (mount <= 12 && mount > 0) {

            if (animalsType.equals(cursor.getString(1))) {
                //проверка месяца
                if (mount == Integer.parseInt(cursor.getString(4))) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {

                        x = Float.parseFloat(cursor.getString(2));
                        y = Float.parseFloat(cursor.getString(3));

                        visitors.add(new BarEntry(y, x));
                    }
                }
            }

            while (cursor.moveToNext()) {
                //проверка товара
                if (animalsType.equals(cursor.getString(1))) {
                    //проверка месяца
                    if (mount == Integer.parseInt(cursor.getString(4))) {
                        //проверка года
                        if (year2.equals(cursor.getString(5))) {

                            x = Float.parseFloat(cursor.getString(2));
                            y = Float.parseFloat(cursor.getString(3));

                            visitors.add(new BarEntry(y, x));
                        }
                    }
                }
            }
            cursor.close();
        } else if (cursor.getCount() == 0) {

            x = 0;
            y = 0;

            visitors.add(new BarEntry(y, x));
        }
        //проверка за весь год
        else if (mount == 13) {
            if (animalsType.equals(cursor.getString(1))) {
                if (mount == 13) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {

                        switch (Integer.parseInt(cursor.getString(4))) {
                            case 1:
                                jan += Float.parseFloat(cursor.getString(2));
                                break;
                            case 2:
                                feb += Float.parseFloat(cursor.getString(2));
                                break;
                            case 3:
                                mar += Float.parseFloat(cursor.getString(2));
                                break;
                            case 4:
                                apr += Float.parseFloat(cursor.getString(2));
                                break;
                            case 5:
                                mai += Float.parseFloat(cursor.getString(2));
                                break;
                            case 6:
                                jun += Float.parseFloat(cursor.getString(2));
                                break;
                            case 7:
                                jul += Float.parseFloat(cursor.getString(2));
                                break;
                            case 8:
                                aug += Float.parseFloat(cursor.getString(2));
                                break;
                            case 9:
                                sep += Float.parseFloat(cursor.getString(2));
                                break;
                            case 10:
                                oct += Float.parseFloat(cursor.getString(2));
                                break;
                            case 11:
                                nov += Float.parseFloat(cursor.getString(2));
                                break;
                            case 12:
                                dec += Float.parseFloat(cursor.getString(2));
                                break;

                        }
                    }
                }
            }

            while (cursor.moveToNext()) {
                if (animalsType.equals(cursor.getString(1))) {
                    if (mount == 13) {
                        //проверка года
                        if (year2.equals(cursor.getString(5))) {

                            switch (Integer.parseInt(cursor.getString(4))) {
                                case 1:
                                    jan += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 2:
                                    feb += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 3:
                                    mar += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 4:
                                    apr += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 5:
                                    mai += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 6:
                                    jun += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 7:
                                    jul += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 8:
                                    aug += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 9:
                                    sep += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 10:
                                    oct += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 11:
                                    nov += Float.parseFloat(cursor.getString(2));
                                    break;
                                case 12:
                                    dec += Float.parseFloat(cursor.getString(2));
                                    break;
                            }
                        }
                    }
                }
            }
            cursor.close();
            visitors.add(new BarEntry(1, jan));
            visitors.add(new BarEntry(2, feb));
            visitors.add(new BarEntry(3, mar));
            visitors.add(new BarEntry(4, apr));
            visitors.add(new BarEntry(5, mai));
            visitors.add(new BarEntry(6, jun));
            visitors.add(new BarEntry(7, jul));
            visitors.add(new BarEntry(8, aug));
            visitors.add(new BarEntry(9, sep));
            visitors.add(new BarEntry(10, oct));
            visitors.add(new BarEntry(11, nov));
            visitors.add(new BarEntry(12, dec));
            // если месяц пустой
        } else {

            x = 0;
            y = 0;

            visitors.add(new BarEntry(y, x));
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
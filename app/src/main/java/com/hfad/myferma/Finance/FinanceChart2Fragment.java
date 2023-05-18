package com.hfad.myferma.Finance;


import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FinanceChart2Fragment extends Fragment {
    private ArrayAdapter<String> arrayAdapterAnimals;
    private MyFermaDatabaseHelper myDB;
    private ArrayList<Entry> entriesFirst, entriesSecond, entriesThird;
    private AutoCompleteTextView mount_spiner, year_spiner;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;
    private View layout;
    private int mount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());
        Calendar calendar = Calendar.getInstance();
        layout = inflater.inflate(R.layout.fragment_finance_chart2, container, false);

        // установка спинеров
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.mount_spiner);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.year_spiner);

        // настройка спинеров
        mount_spiner.setText("За весь год", false);
        year_spiner.setText(String.valueOf(calendar.get(Calendar.YEAR)), false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Финансы - Общее");

        //Массивы
        entriesFirst = new ArrayList<>();
        entriesSecond = new ArrayList<>();
        entriesThird = new ArrayList<>();

        spiner();

        mount_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner();
            }
        });
        year_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner();
            }
        });

        return layout;
    }

    public void spiner() {
        entriesFirst.clear();
        entriesSecond.clear();
        entriesThird.clear();

        allProducts();

        LineChart lineChart = layout.findViewById(R.id.lineChart);
        lineChart.getDescription().setText("График финансов");
        LineDataSet datasetFirst = new LineDataSet(entriesFirst, "Прибыль");

        // График будет зеленого цвета
        datasetFirst.setColor(Color.GRAY);
        // График будет плавным
        datasetFirst.setMode(LineDataSet.Mode.LINEAR);

        LineDataSet datasetSecond = new LineDataSet(entriesSecond, "Чистая прибыль");
        // График будет зеленого цвета
        datasetSecond.setColor(Color.GREEN);
        // График будет плавным
        datasetSecond.setMode(LineDataSet.Mode.LINEAR);

        LineDataSet datasetThird = new LineDataSet(entriesThird, "Расходы");
        // График будет зеленого цвета
        datasetThird.setColor(Color.RED);
        // График будет плавным
        datasetThird.setMode(LineDataSet.Mode.LINEAR);

        ArrayList<ILineDataSet> dataSets = new ArrayList();
        dataSets.add(datasetFirst);
        dataSets.add(datasetSecond);
        dataSets.add(datasetThird);

        LineData data = new LineData(dataSets);
        lineChart.invalidate();
        lineChart.setData(data);
        lineChart.animateY(500);

        if (mount != 13) {
            xaxis(lineChart, mountMass);
        } else {
            xaxis(lineChart, labes);
        }
    }

    public void xaxis(LineChart lineChart, String[] valueX) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valueX));
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

    public void allProducts() {
        Cursor cursor = myDB.readAllDataSale();
        Cursor cursorExpenses = myDB.readAllDataExpenses();

        Map<Float, Float> sumCategory = new HashMap<>();
        Map<Float, Float> sumCategoryExpenses = new HashMap<>();
        Map<Float, Float> sumCategoryClear = new HashMap<>();

        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();

        setMount(mountString);

        if (mount <= 12 && mount > 0) {
            allProductsMount(cursor, entriesFirst, 6, 1, year2);
            allProductsMount(cursorExpenses, entriesThird, 2, -1, year2);
        }

        //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже
        else if (mount == 13) {

            allProductsYear(cursor, year2, sumCategory, entriesFirst, 6, 1);
            allProductsYear(cursorExpenses, year2, sumCategoryExpenses, entriesThird, 2, -1);

            for (Map.Entry<Float, Float> entry : sumCategory.entrySet()) {
                Float name = entry.getKey();
                Float sum = entry.getValue();
                sumCategoryClear.put(name, sum);
            }

            for (Map.Entry<Float, Float> entry2 : sumCategoryExpenses.entrySet()) {
                Float nameExpenses = entry2.getKey();
                Float sumExpenses = entry2.getValue();

                if (sumCategoryClear.get(nameExpenses) == null) {
                    sumCategoryClear.put(nameExpenses, sumExpenses);
                } else {
                    float sum = sumCategoryClear.get(nameExpenses) + sumExpenses;
                    sumCategoryClear.put(nameExpenses, sum);
                }
            }

            for (Map.Entry<Float, Float> entry : sumCategoryClear.entrySet()) {
                Float name = entry.getKey();
                Float sum = entry.getValue();

                entriesSecond.add(new Entry(name, sum));
            }

        } else {
            entriesFirst.add(new Entry(0F, 0F));
            entriesSecond.add(new Entry(0F, 0F));
            entriesThird.add(new Entry(0F, 0F));
        }
        cursorExpenses.close();
        cursor.close();
    }

    public void allProductsMount(Cursor cursor, ArrayList<Entry> entries, int price, int kof, String year2) {
        float x, y;
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                //проверка месяца
                if (mount == Integer.parseInt(cursor.getString(4))) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {
                        y = Float.parseFloat(cursor.getString(price)) * kof;//Для вычитания
                        x = Float.parseFloat(cursor.getString(3));
                        entries.add(new Entry(x, y));
                    }
                }
            }
        } else {
            entries.add(new Entry(0, 0));
        }
        cursor.close();
    }


    public void allProductsYear(Cursor cursor, String year2, Map<Float, Float> sumCategory, ArrayList<Entry> entries, int price, int kof) {

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {
                    if (sumCategory.get(Float.parseFloat(cursor.getString(4))) == null) {
                        sumCategory.put(Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(price)) * kof);
                    } else {
                        float sum = sumCategory.get(Float.parseFloat(cursor.getString(4))) + Float.parseFloat(cursor.getString(price)) * kof;
                        sumCategory.put(Float.parseFloat(cursor.getString(4)), sum);
                    }
                }
            }
        }
        cursor.close();
        for (float i = 1; i < 12; i++) {
            if (sumCategory.get(i) == null) {
                entries.add(new Entry(i, 0));

            } else {
                for (Map.Entry<Float, Float> entry : sumCategory.entrySet()) {
                    Float name = entry.getKey();
                    Float sum = entry.getValue();

                    entries.add(new Entry(name, sum));
                }
            }
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



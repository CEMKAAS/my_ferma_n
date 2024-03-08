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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class FinanceChartFragment extends Fragment {
    private MyFermaDatabaseHelper myDB;
    private ArrayList<Entry> visitors;
    private AutoCompleteTextView animals_spiner, mount_spiner, year_spiner;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;
    private View layout;
    private int mount = 0, idColor =0;
    private String year;
    private ArrayList<String> yearList, productList, productListAll;
    private ArrayAdapter<String> arrayAdapterProduct, arrayAdapterYear;
    private Map<String, ArrayList<Entry>> sumCategory, sumProductYan, sumProductFeb, sumProductMar, sumProductApr, sumProductMay, sumProductJun, sumProductJar, sumProductAvg, sumProductSep, sumProductOkt, sumProductNov, sumProductDec, sumProductAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Подключение к базе данных и к календарю
        myDB = new MyFermaDatabaseHelper(getActivity());
        add();
        Calendar calendar = Calendar.getInstance();

        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false);

        // установка спинеров
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner2);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner3);

        // настройка спинеров
        animals_spiner.setText("Все", false);
        mount_spiner.setText("За весь год", false);
        year_spiner.setText(String.valueOf(calendar.get(Calendar.YEAR)), false);
        year = year_spiner.getText().toString();

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Финансы - Продукция");
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        //Массивы
        visitors = new ArrayList<>();
        sumCategory = new HashMap<>();
        sumProductYan = new HashMap<>();
        sumProductFeb = new HashMap<>();
        sumProductMar = new HashMap<>();
        sumProductApr = new HashMap<>();
        sumProductMay = new HashMap<>();
        sumProductJun = new HashMap<>();
        sumProductJar = new HashMap<>();
        sumProductAvg = new HashMap<>();
        sumProductSep = new HashMap<>();
        sumProductOkt = new HashMap<>();
        sumProductNov = new HashMap<>();
        sumProductDec = new HashMap<>();
        sumProductAll = new HashMap<>();

        //Логика просчета
        allProducts();
        //Формируем списки
        all();
        // Формируем график
        spiner();

        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner();
            }
        });
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
        visitors.clear();
        String animalsType = animals_spiner.getText().toString();
        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();
        setMount(mountString);

        LineChart lineChart = layout.findViewById(R.id.lineChart);
        lineChart.getDescription().setText("График продукции");

        if (animalsType.equals("Все")) {

            ArrayList<ILineDataSet> dataSets = new ArrayList();

            if (mount <= 12 && mount > 0) {
                allProductsMount(animalsType, year2);
                for (String product : productList) {
                    greatChar(product, dataSets,sumCategory);
                }
            } else {
                if (year.equals(year2)) {
                    for (String product : productList) {
                        greatChar(product, dataSets, sumProductAll);
                    }
                } else {
                    year = year2;
                    //Логика просчета
                    allProducts();
                    //Формируем списки
                    all();
                    for (String product : productList) {
                        greatChar(product, dataSets, sumProductAll);
                    }
                }
            }

            LineData data = new LineData(dataSets);
            lineChart.invalidate();
            lineChart.setData(data);

        } else {
            storeDataInArrays(animalsType, mountString, year2);

            LineDataSet dataset = new LineDataSet(visitors, animalsType);

            LineData data = new LineData(dataset);
            lineChart.invalidate();
            lineChart.setData(data);

        }
        lineChart.animateY(500);
        if (mount != 13) {
            xaxis(lineChart, mountMass);
        } else {
            xaxis(lineChart, labes);
        }
    }

    // Добавление графиков
    public void greatChar(String product, ArrayList<ILineDataSet> dataSets, Map<String, ArrayList<Entry>> mapProduct) {
        LineDataSet datasetFirst = new LineDataSet(mapProduct.get(product), product);
        idColor++;
        //График будет зеленого цвета
        datasetFirst.setColor(setColors()); // Todo Логика просчета
        //График будет плавным
        datasetFirst.setMode(LineDataSet.Mode.LINEAR);
        dataSets.add(datasetFirst);
    }

    public int setColors (){
        switch (idColor){
            case 0:
                return Color.GRAY;
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.YELLOW;
            case 7:
                return Color.WHITE;
            case 8:
                return Color.BLACK;
            case 9:
                return Color.GRAY;
        }
        return Color.GRAY;
    }


    // Добавление значений в мапу
    public void allProducts() {
        sumProductYan.clear();
        sumProductFeb.clear();
        sumProductMar.clear();
        sumProductApr.clear();
        sumProductMay.clear();
        sumProductJun.clear();
        sumProductJar.clear();
        sumProductAvg.clear();
        sumProductSep.clear();
        sumProductOkt.clear();
        sumProductNov.clear();
        sumProductDec.clear();
        sumProductAll.clear();

        Cursor cursor = myDB.readAllDataSale();
        String year2 = year_spiner.getText().toString();

        if (cursor.getCount() != 0) {
            //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже, будущий Семён я сделал все что мог
            while (cursor.moveToNext()) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {
                    switch (Integer.parseInt(cursor.getString(4))) {
                        case 1:
                            productMount(cursor, sumProductYan, 1);
                            break;
                        case 2:
                            productMount(cursor, sumProductFeb, 2);
                            break;
                        case 3:
                            productMount(cursor, sumProductMar, 3);
                            break;
                        case 4:
                            productMount(cursor, sumProductApr, 4);
                            break;
                        case 5:
                            productMount(cursor, sumProductMay, 5);
                            break;
                        case 6:
                            productMount(cursor, sumProductJun, 6);
                            break;
                        case 7:
                            productMount(cursor, sumProductJar, 7);
                            break;
                        case 8:
                            productMount(cursor, sumProductAvg, 8);
                            break;
                        case 9:
                            productMount(cursor, sumProductSep, 9);
                            break;
                        case 10:
                            productMount(cursor, sumProductOkt, 10);
                            break;
                        case 11:
                            productMount(cursor, sumProductNov, 11);
                            break;
                        case 12:
                            productMount(cursor, sumProductDec, 12);
                            break;
                    }
                }
            }
        }
        cursor.close();
    }

    // Добавление значений по месячно
    public void productMount(Cursor cursor, Map<String, ArrayList<Entry>> sumProductMount, float x) {

        if (sumProductMount.get(cursor.getString(1)) == null) {
            ArrayList<Entry> sd = new ArrayList<>();
            float y = Float.parseFloat(cursor.getString(6));
            sd.add(new Entry(x, y));
            sumProductMount.put(cursor.getString(1), sd);
        } else {
            float y = Float.parseFloat(cursor.getString(6));
            for (Entry ds : sumProductMount.get(cursor.getString(1))) {
                y += ds.getY();
            }
            sumProductMount.get(cursor.getString(1)).clear();
            sumProductMount.get(cursor.getString(1)).add(new Entry(x, y));
            sumProductMount.put(cursor.getString(1), sumProductMount.get(cursor.getString(1)));
        }
    }

    // Соединение значений в единую мапу
    public void all() {
        for (String product : productList) {

            ArrayList<Entry> entries = new ArrayList<>();
            entries.addAll(addAll22(sumProductYan, product, 1));
            entries.addAll(addAll22(sumProductFeb, product, 2));
            entries.addAll(addAll22(sumProductMar, product, 3));
            entries.addAll(addAll22(sumProductApr, product, 4));
            entries.addAll(addAll22(sumProductMay, product, 5));
            entries.addAll(addAll22(sumProductJun, product, 6));
            entries.addAll(addAll22(sumProductJar, product, 7));
            entries.addAll(addAll22(sumProductAvg, product, 8));
            entries.addAll(addAll22(sumProductSep, product, 9));
            entries.addAll(addAll22(sumProductOkt, product, 10));
            entries.addAll(addAll22(sumProductNov, product, 11));
            entries.addAll(addAll22(sumProductDec, product, 12));

            sumProductAll.put(product, entries);
        }
    }

    // Находим пустые мапы и добавляем минимальные значения
    public ArrayList<Entry> addAll22(Map<String, ArrayList<Entry>> sumProductYansdad, String product1, float x) {
        ArrayList<Entry> entries = new ArrayList<>();
        if (sumProductYansdad.get(product1) == null) {
            entries.add(new Entry(x, 0));
            sumProductYansdad.put(product1, entries);
        } else {
            entries.addAll(sumProductYansdad.get(product1));
        }
        return entries;
    }

    //Если пользователь выбрал все товары по месяцу за год
    public void allProductsMount(String animalsType, String year2) {
        float x, y;
        sumCategory.clear();
        Cursor cursor = myDB.readAllDataSale();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                if (animalsType.equals("Все")) {
                    //проверка месяца
                    if (mount == Integer.parseInt(cursor.getString(4))) {
                        //проверка года
                        if (year2.equals(cursor.getString(5))) {
                            ArrayList<Entry> sd = new ArrayList<>();
                            y = Float.parseFloat(cursor.getString(6));
                            x = Float.parseFloat(cursor.getString(3));
                            if (sumCategory.get(cursor.getString(1)) == null) {
                                sd.add(new Entry(x, y));
                                sumCategory.put(cursor.getString(1), sd);
                            } else {
                                sumCategory.get(cursor.getString(1)).add(new Entry(x, y));
                                sumCategory.put(cursor.getString(1), sumCategory.get(cursor.getString(1)));
                            }
                        }
                    }
                }
            }
        } else {
            ArrayList<Entry> sd = new ArrayList<>();
            sd.add(new Entry(0, 0));
            for (String product : productList) {
                sumCategory.put(product, sd);
            }
        }
        cursor.close();
    }

    public void storeDataInArrays(String animalsType, String mountString, String year2) {
        Cursor cursor = myDB.readAllDataSale();

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

        cursor.moveToNext();

        if(cursor.getCount() != 0) {
            if (mount <= 12 && mount > 0) {
                while (cursor.moveToNext()) {
                    storeDataInArraysMount(cursor, animalsType, year2);
                }
            }
            //проверка за весь год
            else if (mount == 13) {
                while (cursor.moveToNext()) {
                    if (animalsType.equals(cursor.getString(1))) {
                        //проверка года
                        if (year2.equals(cursor.getString(5))) {
                            switch (Integer.parseInt(cursor.getString(4))) {
                                case 1:
                                    jan += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 2:
                                    feb += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 3:
                                    mar += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 4:
                                    apr += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 5:
                                    mai += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 6:
                                    jun += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 7:
                                    jul += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 8:
                                    aug += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 9:
                                    sep += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 10:
                                    oct += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 11:
                                    nov += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 12:
                                    dec += Float.parseFloat(cursor.getString(6));
                                    break;
                            }
                        }
                    }
                }
                cursor.close();
                visitors.add(new Entry(1, jan));
                visitors.add(new Entry(2, feb));
                visitors.add(new Entry(3, mar));
                visitors.add(new Entry(4, apr));
                visitors.add(new Entry(5, mai));
                visitors.add(new Entry(6, jun));
                visitors.add(new Entry(7, jul));
                visitors.add(new Entry(8, aug));
                visitors.add(new Entry(9, sep));
                visitors.add(new Entry(10, oct));
                visitors.add(new Entry(11, nov));
                visitors.add(new Entry(12, dec));
            }
        } else {
            visitors.add(new Entry(0, 0));
        }
        cursor.close();
    }

    public void storeDataInArraysMount(Cursor cursor, String animalsType, String year2) {

        if (animalsType.equals(cursor.getString(1))) {
            //проверка месяца
            if (mount == Integer.parseInt(cursor.getString(4))) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {

                    float x = Float.parseFloat(cursor.getString(6));
                    float y = Float.parseFloat(cursor.getString(3));

                    visitors.add(new Entry(y, x));
                }
            }
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
            //Настройка спинера с продуктами
            arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, productListAll);
            animals_spiner.setAdapter(arrayAdapterProduct);

            // настройка спинера с годами (выглядил как обычный, и год запоминал)
            arrayAdapterYear = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, yearList);
            year_spiner.setAdapter(arrayAdapterYear);
        }
    }

    // Добавляем в списки
    public void add() {
        Set<String> yearSet = new HashSet<>();
        Set<String> productSet = new HashSet<>();
        Cursor cursor = myDB.readAllDataSale();

        while (cursor.moveToNext()) {
            String year = cursor.getString(5);
            String product = cursor.getString(1);

            yearSet.add(year);
            productSet.add(product);
        }
        cursor.close();

        yearList = new ArrayList<>();
        productListAll = new ArrayList<>();
        for (String yearColum : yearSet) {
            yearList.add(yearColum);
        }
        for (String productColum : productSet) {
            productListAll.add(productColum);
        }
        productList = (ArrayList<String>) productListAll.clone();
        productListAll.add("Все");
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



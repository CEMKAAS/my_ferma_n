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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinanceChartFragment extends Fragment {
    private MyFermaDatabaseHelper myDB;
    private ArrayList<Entry> visitors, entriesFirst, entriesSecond, entriesThird;

    private ArrayList<ProductChar> entriesProductAll;
    private AutoCompleteTextView animals_spiner, mount_spiner, year_spiner;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;
    private View layout;
    private int mount = 0;
    private ArrayList<String> yearList, productList, productListAll;
    private ArrayAdapter<String> arrayAdapterProduct, arrayAdapterYear;
    private Map<String, ArrayList<Entry>> sumCategory, sumCategoryAll;
    private Map<String, ArrayList<Entry>> sumProductYan, sumProductFeb, sumProductMar, sumProductApr, sumProductMay, sumProductJun, sumProductJar, sumProductAvg, sumProductSep, sumProductOkt, sumProductNov, sumProductDec, sumProductAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());
        add();

        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false);

        // установка спинеров
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner2);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner3);

        Calendar calendar = Calendar.getInstance();
        // настройка спинеров
        animals_spiner.setText("Все", false);
        mount_spiner.setText("За весь год", false);
        year_spiner.setText(String.valueOf(calendar.get(Calendar.YEAR)), false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Финансы - Продукция");
        //Todo кнопка назад

        //установка графиков
        LineChart lineChart = layout.findViewById(R.id.lineChart);

        //Массивы
        visitors = new ArrayList<>();
        entriesFirst = new ArrayList<>();
        entriesSecond = new ArrayList<>();
        entriesThird = new ArrayList<>();
        entriesProductAll = new ArrayList<>();
        sumCategory = new HashMap<>();
        sumCategoryAll = new HashMap<>();
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
//        storeDataInArrays();
        all();
        ArrayList<ILineDataSet> dataSets = new ArrayList();
        for (String product : productList) {
            LineDataSet datasetFirst = new LineDataSet(sumProductAll.get(product), product);
            datasetFirst.setMode(LineDataSet.Mode.LINEAR);
            dataSets.add(datasetFirst);
        }

        LineData data = new LineData(dataSets);
        lineChart.invalidate();
        lineChart.setData(data);
        lineChart.animateY(500);

        xaxis(lineChart, labes);
        lineChart.invalidate();
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
        entriesFirst.clear();
        entriesSecond.clear();
        entriesThird.clear();

        if (animals_spiner.getText().toString().equals("Все")) {
            LineChart lineChart = layout.findViewById(R.id.lineChart);
            lineChart.getDescription().setText("График продукции");
            ArrayList<ILineDataSet> dataSets = new ArrayList();
            for (String product : productList) {
                LineDataSet datasetFirst = new LineDataSet(sumProductAll.get(product), product);
//                 График будет зеленого цвета
                datasetFirst.setColor(Color.GRAY);
//                 График будет плавным
                datasetFirst.setMode(LineDataSet.Mode.LINEAR);
                dataSets.add(datasetFirst);
            }
            LineData data = new LineData(dataSets);
            lineChart.invalidate();
            lineChart.setData(data);
            lineChart.animateY(500);
            if (mount != 13) {
                xaxis(lineChart, mountMass);
            } else {
                xaxis(lineChart, labes);
            }

        } else {
            storeDataInArrays();
            LineChart lineChart = layout.findViewById(R.id.lineChart);
            lineChart.getDescription().setText("График продукции");
            LineDataSet dataset = new LineDataSet(visitors, animals_spiner.getText().toString());
            LineData data = new LineData(dataset);
            lineChart.invalidate();
            lineChart.setData(data);
            lineChart.animateY(500);
            if (mount != 13) {
                xaxis(lineChart, mountMass);
            } else {
                xaxis(lineChart, labes);
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

    public void allProducts() {
        Cursor cursor = myDB.readAllDataSale();
        float x, y;

        String animalsType = animals_spiner.getText().toString();
        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();

        setMount(mountString);

        if (cursor.getCount() == 0) {

            x = 0;
            y = 0;

            visitors.add(new Entry(y, x));

        } else if (mount <= 12 && mount > 0) {
            while (cursor.moveToNext()) {
                allProductsMount(cursor, animalsType, year2);
            }

        }
        //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже
        else if (mount == 13) {
            while (cursor.moveToNext()) {
                if (animalsType.equals("Все")) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {
                        switch (Integer.parseInt(cursor.getString(4))) {
                            case 1:
                                productMount(cursor, sumProductYan, 1);
                                break;
                            case 2:
                                productMount(cursor, sumProductFeb,2);
                                break;
                            case 3:
                                productMount(cursor, sumProductMar,3);
                                break;
                            case 4:
                                productMount(cursor, sumProductApr,4);
                                break;
                            case 5:
                                productMount(cursor, sumProductMay,5);
                                break;
                            case 6:
                                productMount(cursor, sumProductJun,6);
                                break;
                            case 7:
                                productMount(cursor, sumProductJar,7);
                                break;
                            case 8:
                                productMount(cursor, sumProductAvg,8);
                                break;
                            case 9:
                                productMount(cursor, sumProductSep,9);
                                break;
                            case 10:
                                productMount(cursor, sumProductOkt,10);
                                break;
                            case 11:
                                productMount(cursor, sumProductNov,11);
                                break;
                            case 12:
                                productMount(cursor, sumProductDec,12);
                                break;
                        }
                    }
                }
            }
            cursor.close();
            // если месяц пустой
        } else {

            x = 0;
            y = 0;

            visitors.add(new Entry(y, x));
        }
        cursor.close();
    }

    public void productMount(Cursor cursor, Map<String, ArrayList<Entry>> sumProductMount,float x) {

        if (sumProductMount.get(cursor.getString(1)) == null) {
            ArrayList<Entry> sd = new ArrayList<>();
            float y = Float.parseFloat(cursor.getString(6));
            sd.add(new Entry(x, y));
            sumProductMount.put(cursor.getString(1), sd);
        } else {
            float y = Float.parseFloat(cursor.getString(6));
//            ArrayList<Entry> sd = (ArrayList<Entry>) sumProductMount.get(cursor.getString(1)).clone();
            for (Entry ds : sumProductMount.get(cursor.getString(1))){
                y += ds.getY();
            }
            sumProductMount.get(cursor.getString(1)).clear();
            sumProductMount.get(cursor.getString(1)).add(new Entry(x, y));
            sumProductMount.put(cursor.getString(1), sumProductMount.get(cursor.getString(1)));
        }
    }

    public void all() {
        for (String product : productList) {

            ArrayList<Entry> entries = new ArrayList<>();
            entries.addAll(addAll22(sumProductYan,product,1));
            entries.addAll(addAll22(sumProductFeb,product,2));
            entries.addAll(addAll22(sumProductMar,product,3));
            entries.addAll(addAll22(sumProductApr,product,4));
            entries.addAll(addAll22(sumProductMay,product,5));
            entries.addAll(addAll22(sumProductJun,product,6));
            entries.addAll(addAll22(sumProductJar,product,7));
            entries.addAll(addAll22(sumProductAvg,product,8));
            entries.addAll(addAll22(sumProductSep,product,9));
            entries.addAll(addAll22(sumProductOkt,product,10));
            entries.addAll(addAll22(sumProductNov,product,11));
            entries.addAll(addAll22(sumProductDec,product,12));

            sumProductAll.put(product,entries);
        }
    }

    public ArrayList<Entry> addAll22(Map<String, ArrayList<Entry>> sumProductYansdad,String product1, float x){
        ArrayList<Entry> entries = new ArrayList<>();
        if(sumProductYansdad.get(product1)== null){
            entries.add(new Entry(x,0));
            sumProductYansdad.put(product1, entries);
        }else {
            entries.addAll(sumProductYansdad.get(product1));}
        return entries;
    }

    public void storeDataInArraysMount(Cursor cursor) {
        float x, y;
        String animalsType = animals_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();

        if (animalsType.equals(cursor.getString(1))) {
            //проверка месяца
            if (mount == Integer.parseInt(cursor.getString(4))) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {

                    x = Float.parseFloat(cursor.getString(6));
                    y = Float.parseFloat(cursor.getString(3));

                    visitors.add(new Entry(y, x));
                }
            }
        }
    }

    public void allProductsMount(Cursor cursor, String animalsType, String year2) {
        float x, y;
        if (animalsType.equals("Все")) {
            //проверка месяца
            if (mount == Integer.parseInt(cursor.getString(4))) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {
                    ArrayList<Entry> sd = new ArrayList<>();
                    x = Float.parseFloat(cursor.getString(6));
                    y = Float.parseFloat(cursor.getString(3));
                    sd.add(new Entry(y, x));
                    sumCategory.put(cursor.getString(1), sd);

                }
            }
        }
    }

    public void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataSale();
        float x, y;
        String animalsType = animals_spiner.getText().toString();
        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();
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
            storeDataInArraysMount(cursor);
            while (cursor.moveToNext()) {
                storeDataInArraysMount(cursor);
            }
            cursor.close();

            // Если таблица не создана
        } else if (cursor.getCount() == 0) {
            x = 0;
            y = 0;
            visitors.add(new Entry(y, x));
        }

        //проверка за весь год
        else if (mount == 13) {

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

            // если месяц пустой
        } else {

            x = 0;
            y = 0;

            visitors.add(new Entry(y, x));
        }
        cursor.close();
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



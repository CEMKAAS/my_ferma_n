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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FinanceChartFragment extends Fragment {
    private ArrayAdapter<String> arrayAdapterAnimals;
    private MyFermaDatabaseHelper myDB;
    private ArrayList<Entry> visitors, entriesFirst, entriesSecond, entriesThird;
    private AutoCompleteTextView animals_spiner, mount_spiner, year_spiner;
    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};
    private String[] mountMass;

    private View layout;
    private int mount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_finance_chart, container, false);

        // установка спинеров
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner2);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner3);

        // настройка спинеров
        animals_spiner.setText("Все", false);
        mount_spiner.setText("За весь год", false);
        year_spiner.setText("2023", false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Финансы - Продукция");

        //установка графиков
        LineChart lineChart = layout.findViewById(R.id.lineChart);

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());

        //Массивы
        visitors = new ArrayList<>();
        entriesFirst = new ArrayList<>();
        entriesSecond = new ArrayList<>();
        entriesThird = new ArrayList<>();

        //Логика просчета
        allProducts();
//        storeDataInArrays();

        LineDataSet datasetFirst = new LineDataSet(entriesFirst, "Яйца");
        // График будет зеленого цвета
        datasetFirst.setColor(Color.GRAY);
        // График будет плавным
        datasetFirst.setMode(LineDataSet.Mode.LINEAR);
        lineChart.getDescription().setText("График продукции");
        LineDataSet datasetSecond = new LineDataSet(entriesSecond, "Молоко");
        // График будет зеленого цвета
        datasetSecond.setColor(Color.GREEN);
        // График будет плавным
        datasetSecond.setMode(LineDataSet.Mode.LINEAR);

        LineDataSet datasetThird = new LineDataSet(entriesThird, "Мясо");
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
            allProducts();

            LineChart lineChart = layout.findViewById(R.id.lineChart);
            lineChart.getDescription().setText("График продукции");
            LineDataSet datasetFirst = new LineDataSet(entriesFirst, "Яйца");
            // График будет зеленого цвета
            datasetFirst.setColor(Color.GRAY);
            // График будет плавным
            datasetFirst.setMode(LineDataSet.Mode.LINEAR);

            LineDataSet datasetSecond = new LineDataSet(entriesSecond, "Молоко");
            // График будет зеленого цвета
            datasetSecond.setColor(Color.GREEN);
            // График будет плавным
            datasetSecond.setMode(LineDataSet.Mode.LINEAR);

            LineDataSet datasetThird = new LineDataSet(entriesThird, "Мясо");
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

    public void xaxis(LineChart lineChart,String[] valueX ){
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

    public void allProductsMount(Cursor cursor) {
        float x, y;
        String animalsType = animals_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();
        if (animalsType.equals("Все")) {
            //проверка месяца
            if (mount == Integer.parseInt(cursor.getString(4))) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {
                    switch (cursor.getString(1)) {
                        case "Яйца":
                            x = Float.parseFloat(cursor.getString(6));
                            y = Float.parseFloat(cursor.getString(3));
                            entriesFirst.add(new Entry(y, x));
                            break;
                        case "Молоко":
                            x = Float.parseFloat(cursor.getString(6));
                            y = Float.parseFloat(cursor.getString(3));
                            entriesSecond.add(new Entry(y, x));
                            break;
                        case "Мясо":
                            x = Float.parseFloat(cursor.getString(6));
                            y = Float.parseFloat(cursor.getString(3));
                            entriesThird.add(new Entry(y, x));
                            break;
                    }
                }
            }
        }
    }

    public void allProducts() {
        Cursor cursor = myDB.readAllDataSale();
        float x, y;
        String animalsType = animals_spiner.getText().toString();
        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();
        float janEgg = 0f;
        float febEgg = 0f;
        float marEgg = 0f;
        float aprEgg = 0f;
        float maiEgg = 0f;
        float junEgg = 0f;
        float julEgg = 0f;
        float augEgg = 0f;
        float sepEgg = 0f;
        float octEgg = 0f;
        float novEgg = 0f;
        float decEgg = 0f;

        float janMilk = 0f;
        float febMilk = 0f;
        float marMilk = 0f;
        float aprMilk = 0f;
        float maiMilk = 0f;
        float junMilk = 0f;
        float julMilk = 0f;
        float augMilk = 0f;
        float sepMilk = 0f;
        float octMilk = 0f;
        float novMilk = 0f;
        float decMilk = 0f;

        float janMeat = 0f;
        float febMeat = 0f;
        float marMeat = 0f;
        float aprMeat = 0f;
        float maiMeat = 0f;
        float junMeat = 0f;
        float julMeat = 0f;
        float augMeat = 0f;
        float sepMeat = 0f;
        float octMeat = 0f;
        float novMeat = 0f;
        float decMeat = 0f;

        setMount(mountString);

        cursor.moveToNext();

        if (mount <= 12 && mount > 0) {
            allProductsMount(cursor);
            while (cursor.moveToNext()) {
                allProductsMount(cursor);
            }
            cursor.close();
        } else if (cursor.getCount() == 0) {

            x = 0;
            y = 0;

            visitors.add(new Entry(y, x));
        }
        //проверка за весь год //TODO Сократи это говно плиз и еще ниже будет его тоже
        else if (mount == 13) {
            if (animalsType.equals("Все")) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {
                    switch (cursor.getString(1)) {
                        case "Яйца":
                            switch (Integer.parseInt(cursor.getString(4))) {
                                case 1:
                                    janEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 2:
                                    febEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 3:
                                    marEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 4:
                                    aprEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 5:
                                    maiEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 6:
                                    junEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 7:
                                    julEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 8:
                                    augEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 9:
                                    sepEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 10:
                                    octEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 11:
                                    novEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 12:
                                    decEgg += Float.parseFloat(cursor.getString(6));
                                    break;
                            }
                            break;
                        case "Молоко":
                            switch (Integer.parseInt(cursor.getString(4))) {
                                case 1:
                                    janMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 2:
                                    febMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 3:
                                    marMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 4:
                                    aprMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 5:
                                    maiMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 6:
                                    junMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 7:
                                    julMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 8:
                                    augMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 9:
                                    sepMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 10:
                                    octMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 11:
                                    novMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 12:
                                    decMilk += Float.parseFloat(cursor.getString(6));
                                    break;
                            }
                            break;
                        case "Мясо":
                            switch (Integer.parseInt(cursor.getString(4))) {
                                case 1:
                                    janMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 2:
                                    febMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 3:
                                    marMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 4:
                                    aprMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 5:
                                    maiMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 6:
                                    junMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 7:
                                    julMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 8:
                                    augMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 9:
                                    sepMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 10:
                                    octMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 11:
                                    novMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                                case 12:
                                    decMeat += Float.parseFloat(cursor.getString(6));
                                    break;
                            }
                            break;
                    }
                }
            }
            while (cursor.moveToNext()) {
                if (animalsType.equals("Все")) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {
                        switch (cursor.getString(1)) {
                            case "Яйца":
                                switch (Integer.parseInt(cursor.getString(4))) {
                                    case 1:
                                        janEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 2:
                                        febEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 3:
                                        marEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 4:
                                        aprEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 5:
                                        maiEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 6:
                                        junEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 7:
                                        julEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 8:
                                        augEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 9:
                                        sepEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 10:
                                        octEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 11:
                                        novEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 12:
                                        decEgg += Float.parseFloat(cursor.getString(6));
                                        break;
                                }
                                break;
                            case "Молоко":
                                switch (Integer.parseInt(cursor.getString(4))) {
                                    case 1:
                                        janMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 2:
                                        febMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 3:
                                        marMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 4:
                                        aprMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 5:
                                        maiMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 6:
                                        junMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 7:
                                        julMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 8:
                                        augMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 9:
                                        sepMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 10:
                                        octMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 11:
                                        novMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 12:
                                        decMilk += Float.parseFloat(cursor.getString(6));
                                        break;
                                }
                                break;
                            case "Мясо":
                                switch (Integer.parseInt(cursor.getString(4))) {
                                    case 1:
                                        janMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 2:
                                        febMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 3:
                                        marMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 4:
                                        aprMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 5:
                                        maiMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 6:
                                        junMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 7:
                                        julMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 8:
                                        augMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 9:
                                        sepMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 10:
                                        octMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 11:
                                        novMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                    case 12:
                                        decMeat += Float.parseFloat(cursor.getString(6));
                                        break;
                                }
                                break;
                        }
                    }
                }
            }

            cursor.close();
            entriesFirst.add(new Entry(1, janEgg));
            entriesFirst.add(new Entry(2, febEgg));
            entriesFirst.add(new Entry(3, marEgg));
            entriesFirst.add(new Entry(4, aprEgg));
            entriesFirst.add(new Entry(5, maiEgg));
            entriesFirst.add(new Entry(6, junEgg));
            entriesFirst.add(new Entry(7, julEgg));
            entriesFirst.add(new Entry(8, augEgg));
            entriesFirst.add(new Entry(9, sepEgg));
            entriesFirst.add(new Entry(10, octEgg));
            entriesFirst.add(new Entry(11, novEgg));
            entriesFirst.add(new Entry(12, decEgg));

            entriesSecond.add(new Entry(1, janMilk));
            entriesSecond.add(new Entry(2, febMilk));
            entriesSecond.add(new Entry(3, marMilk));
            entriesSecond.add(new Entry(4, aprMilk));
            entriesSecond.add(new Entry(5, maiMilk));
            entriesSecond.add(new Entry(6, junMilk));
            entriesSecond.add(new Entry(7, julMilk));
            entriesSecond.add(new Entry(8, augMilk));
            entriesSecond.add(new Entry(9, sepMilk));
            entriesSecond.add(new Entry(10, octMilk));
            entriesSecond.add(new Entry(11, novMilk));
            entriesSecond.add(new Entry(12, decMilk));

            entriesThird.add(new Entry(1, janMeat));
            entriesThird.add(new Entry(2, febMeat));
            entriesThird.add(new Entry(3, marMeat));
            entriesThird.add(new Entry(4, aprMeat));
            entriesThird.add(new Entry(5, maiMeat));
            entriesThird.add(new Entry(6, junMeat));
            entriesThird.add(new Entry(7, julMeat));
            entriesThird.add(new Entry(8, augMeat));
            entriesThird.add(new Entry(9, sepMeat));
            entriesThird.add(new Entry(10, octMeat));
            entriesThird.add(new Entry(11, novMeat));
            entriesThird.add(new Entry(12, decMeat));


            // если месяц пустой
        } else {

            x = 0;
            y = 0;

            visitors.add(new Entry(y, x));
        }
        cursor.close();
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



package com.hfad.myferma.ExpensesPackage;

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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManager;
import com.hfad.myferma.db.MydbManagerMetod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ExpensesChartFragment extends Fragment {

    private MyFermaDatabaseHelper myDB;
    private AutoCompleteTextView animals_spiner, mount_spiner, year_spiner;
    private ArrayList<PieEntry> visitors;

    private List<String> arrayListAnaimals;
    private ArrayAdapter<String> arrayAdapterAnimals;

    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};

    private String[] mountMass;

    private int mount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_expenses_chart, container, false);
        // установка спинеров
        mount_spiner = (AutoCompleteTextView) layout.findViewById(R.id.mount_spiner);
        year_spiner = (AutoCompleteTextView) layout.findViewById(R.id.year_spiner);

        //установка графиков
        PieChart pieChart = layout.findViewById(R.id.barChart);

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());


//Создание списка с данными для графиков
        visitors = new ArrayList<>();

        // настройка спинеров
        mount_spiner.setText("За весь год", false);
        year_spiner.setText("2023", false);

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои покупки - График");

        //Логика просчета
        storeDataInArrays();

        // настройка графиков
        PieDataSet pieDataSet = new PieDataSet(visitors, "Расходы");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);


        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Расходы" + "\n" + mount_spiner.getText().toString() + "\n" + year_spiner.getText().toString());
        pieChart.animateX(2000);
        pieChart.setData(pieData);
        pieChart.invalidate();

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        mount_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();

                PieChart pieChart = layout.findViewById(R.id.barChart);
                PieDataSet pieDataSet = new PieDataSet(visitors, "Расходы");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(11f);
                pieData.setValueTextColor(Color.WHITE);


                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Расходы" + "\n" + mount_spiner.getText().toString() + "\n" + year_spiner.getText().toString());
                pieChart.animateX(2000);
                pieChart.setData(pieData);
                pieChart.invalidate();

                Legend l = pieChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);

            }
        });

        year_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();

                PieChart pieChart = layout.findViewById(R.id.barChart);
                PieDataSet pieDataSet = new PieDataSet(visitors, "Расходы");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(11f);
                pieData.setValueTextColor(Color.WHITE);


                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Расходы" + "\n" + mount_spiner.getText().toString() + "\n" + year_spiner.getText().toString());
                pieChart.animateX(2000);
                pieChart.setData(pieData);
                pieChart.invalidate();

                Legend l = pieChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);


            }
        });


        return layout;
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
        Cursor cursor = myDB.readAllDataExpenses();

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
        Cursor cursor = myDB.readAllDataExpenses();
        Map<String, Float> sumCategory = new HashMap<>();

        String mountString = mount_spiner.getText().toString();
        String year2 = year_spiner.getText().toString();

        setMount(mountString);

        cursor.moveToNext();

        if (mount <= 12 && mount > 0) {

            //проверка месяца
            if (mount == Integer.parseInt(cursor.getString(4))) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {

                    sumCategory.put(cursor.getString(1), Float.parseFloat(cursor.getString(2)));

                }
            }

            while (cursor.moveToNext()) {

                //проверка месяца
                if (mount == Integer.parseInt(cursor.getString(4))) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {

                        if (sumCategory.get(cursor.getString(1)) == null) {
                            sumCategory.put(cursor.getString(1), Float.parseFloat(cursor.getString(2)));
                        } else {
                            float sum = sumCategory.get(cursor.getString(1)) + Float.parseFloat(cursor.getString(2));
                            sumCategory.put(cursor.getString(1), sum);
                        }

                    }
                }
            }
            cursor.close();

            //Проверка сколько строк в таблице.
        } else if (cursor.getCount()==0) {

            visitors.add(new PieEntry(0, "Нет товара"));
        }

        //проверка за весь год
        else if (mount == 13) {
            //проверка года
            if (year2.equals(cursor.getString(5))) {

                sumCategory.put(cursor.getString(1), Float.parseFloat(cursor.getString(2)));

            }

            while (cursor.moveToNext()) {
                //проверка года
                if (year2.equals(cursor.getString(5))) {

                    if (sumCategory.get(cursor.getString(1)) == null) {
                        sumCategory.put(cursor.getString(1), Float.parseFloat(cursor.getString(2)));
                    } else {
                        float sum = sumCategory.get(cursor.getString(1)) + Float.parseFloat(cursor.getString(2));
                        sumCategory.put(cursor.getString(1), sum);
                    }

                }
            }
            cursor.close();
        } else {
            visitors.add(new PieEntry(0, "Нет товара"));
        }

        for (Map.Entry<String, Float> entry : sumCategory.entrySet()) {
            String name = entry.getKey();
            Float sum = entry.getValue();

            visitors.add(new PieEntry(sum, name));
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
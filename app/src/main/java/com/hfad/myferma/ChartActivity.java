package com.hfad.myferma;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChartActivity extends AppCompatActivity {

    private MyFermaDatabaseHelper myDB;
    private AutoCompleteTextView animals_spiner, animals_spiner2, animals_spiner3;
    private ArrayList<BarEntry> visitors;

    private List<String> arrayListAnaimals;
    private ArrayAdapter<String> arrayAdapterAnimals;


    private String[] labes = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь", ""};

    private String[] mountMass;

    private int mount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // установка спинеров
        animals_spiner = (AutoCompleteTextView) findViewById(R.id.animals_spiner);
        animals_spiner2 = (AutoCompleteTextView) findViewById(R.id.animals_spiner2);
        animals_spiner3 = (AutoCompleteTextView) findViewById(R.id.animals_spiner3);

        //установка графиков
        BarChart barChart = findViewById(R.id.barChart);

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(ChartActivity.this);

        //Создание списка с данными для графиков
        visitors = new ArrayList<>();

        // настройка спинеров
        animals_spiner.setText("Яйца", false);
        animals_spiner2.setText("За весь год", false);
        animals_spiner3.setText("2023", false);

        // настройка спинера с годами (выглядил как обычный, и год запоминал)
        arrayAdapterAnimals = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, add());
        animals_spiner3.setAdapter(arrayAdapterAnimals);

        //Логика просчета
        storeDataInArrays();

        // настройка графиков
        BarDataSet barDataSet = new BarDataSet(visitors, animals_spiner.getText().toString());
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("График добавленной продукции на склад");
        barChart.animateY(2000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(6); //сколько отображается
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labes));
        barChart.invalidate();

        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setLabelCount(7);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(mountMass));


                } else {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labes));

                }
            }
        });

        animals_spiner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setLabelCount(7);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(mountMass));


                } else {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labes));

                }
            }
        });

        animals_spiner3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                visitors.clear();
                storeDataInArrays();
                if (mount != 13) {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setLabelCount(7);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(mountMass));

                } else {
                    BarChart barChart = findViewById(R.id.barChart);
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
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labes));

                }
            }
        });


    }


    public ArrayList<String> add() {
        Set<String> tempList = new HashSet<>();
        Cursor cursor = myDB.readAllData();

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
        Cursor cursor = myDB.readAllData();
        int x, y;
        String animalsType = animals_spiner.getText().toString();
        String mountString = animals_spiner2.getText().toString();
        String year2 = animals_spiner3.getText().toString();
        int jan = 0;
        int feb = 0;
        int mar = 0;
        int apr = 0;
        int mai = 0;
        int jun = 0;
        int jul = 0;
        int aug = 0;
        int sep = 0;
        int oct = 0;
        int nov = 0;
        int dec = 0;

        setMount(mountString);

        cursor.moveToNext();


        if (mount == Integer.parseInt(cursor.getString(4))) {

            if (animalsType.equals(cursor.getString(1))) {
                //проверка месяца
                if (mount == Integer.parseInt(cursor.getString(4))) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {

                        x = Integer.parseInt(cursor.getString(2));
                        y = Integer.parseInt(cursor.getString(3));

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

                            x = Integer.parseInt(cursor.getString(2));
                            y = Integer.parseInt(cursor.getString(3));

                            visitors.add(new BarEntry(y, x));
                        }
                    }
                }
            }
            cursor.close();
        }

        //проверка за весь год
        else if (mount == 13) {
            if (animalsType.equals(cursor.getString(1))) {
                if (mount == 13) {
                    //проверка года
                    if (year2.equals(cursor.getString(5))) {

                        switch (Integer.parseInt(cursor.getString(4))) {
                            case 1:
                                jan += Integer.parseInt(cursor.getString(2));
                                break;
                            case 2:
                                feb += Integer.parseInt(cursor.getString(2));
                                break;
                            case 3:
                                mar += Integer.parseInt(cursor.getString(2));
                                break;
                            case 4:
                                apr += Integer.parseInt(cursor.getString(2));
                                break;
                            case 5:
                                mai += Integer.parseInt(cursor.getString(2));
                                break;
                            case 6:
                                jun += Integer.parseInt(cursor.getString(2));
                                break;
                            case 7:
                                jul += Integer.parseInt(cursor.getString(2));
                                break;
                            case 8:
                                aug += Integer.parseInt(cursor.getString(2));
                                break;
                            case 9:
                                sep += Integer.parseInt(cursor.getString(2));
                                break;
                            case 10:
                                oct += Integer.parseInt(cursor.getString(2));
                                break;
                            case 11:
                                nov += Integer.parseInt(cursor.getString(2));
                                break;
                            case 12:
                                dec += Integer.parseInt(cursor.getString(2));
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
                                    jan += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 2:
                                    feb += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 3:
                                    mar += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 4:
                                    apr += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 5:
                                    mai += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 6:
                                    jun += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 7:
                                    jul += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 8:
                                    aug += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 9:
                                    sep += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 10:
                                    oct += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 11:
                                    nov += Integer.parseInt(cursor.getString(2));
                                    break;
                                case 12:
                                    dec += Integer.parseInt(cursor.getString(2));
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
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Февраль":
                mount = 2;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28",""};
                break;
            case "Март":
                mount = 3;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Апрель":
                mount = 4;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",""};
                break;
            case "Май":
                mount = 5;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Июнь":
                mount = 6;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",""};
                break;
            case "Июль":
                mount = 7;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Август":
                mount = 8;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Сентябрь":
                mount = 9;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",""};
                break;
            case "Октябрь":
                mount = 10;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "Ноябрь":
                mount = 11;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",""};
                break;
            case "Декабрь":
                mount = 12;
                mountMass = new String[]{"", "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",""};
                break;
            case "За весь год":
                mount = 13;
                break;
        }
    }


}
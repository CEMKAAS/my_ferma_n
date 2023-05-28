package com.hfad.myferma.Finance;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.AddPackage.CustomAdapterAdd;
import com.hfad.myferma.AddPackage.ProductDB;
import com.hfad.myferma.ProductAdapter;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class FinanceFragment extends Fragment implements View.OnClickListener{

    private MyFermaDatabaseHelper myDB;
    private RecyclerView recyclerView;
    private List<String> productList;
    private DecimalFormat f;
    private TextInputLayout dataSheet;
    private Button buttonSheet;
    private BottomSheetDialog bottomSheetDialog;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;
    private Date dateFirst, dateEnd;

    // Прогрузка фрагмента и его активных частей
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Открываем БД
        myDB = new MyFermaDatabaseHelper(getActivity());

        View layout = inflater.inflate(R.layout.fragment_finance, container, false);
        f = new DecimalFormat("0.00");

        //Установка кнопки во фрагменте
        Button priceButton = (Button) layout.findViewById(R.id.financeChart_button);
        priceButton.setOnClickListener(this);

        Button price2Button = (Button) layout.findViewById(R.id.financeChart2_button);
        price2Button.setOnClickListener(this);

        //Настройка листа
        productList = new ArrayList();
        add();

        // Настраиваем адаптер
        recyclerView = layout.findViewById(R.id.recyclerView);

        ProductAdapter productAdapter = new ProductAdapter(addProduct(),"Продали ", " ₽" );

        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Общая сумма и Расходы
        double totalAmount = totalAmount();
        double totalExpenses = totalExpenses();
        double clearFinance = totalAmount - totalExpenses;

        TextView totalAmountText = (TextView) layout.findViewById(R.id.totalAmount_text);
        TextView totalExpensesText = (TextView) layout.findViewById(R.id.totalExpenses_text);
        TextView clearFinanceText = (TextView) layout.findViewById(R.id.clearFinance_text);

        totalAmountText.setText(" " +String.valueOf(f.format(totalAmount()))+ " ₽");
        totalExpensesText.setText(" " +String.valueOf(f.format(totalExpenses()))+ " ₽");
        clearFinanceText.setText(" " +String.valueOf(f.format(clearFinance))+ " ₽");


        //Создание модального bottomSheet
        showBottomSheetDialog();

        //Настройка кнопки и верхнего бара
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.filler).setVisible(true);
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filler:
                    bottomSheetDialog.show();
            }
            return true;
        });


        // Настройка календаря на период
        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(constraintsBuilder)
                .setTitleText("Выберите даты")
                .setSelection(
                        Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()
                        ))
                .build();

        dataSheet.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getActivity().getSupportFragmentManager(), "wer");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

                        Long startDate = selection.first;
                        Long endDate = selection.second;

                        calendar.setTimeInMillis(startDate);
                        calendar2.setTimeInMillis(endDate);

                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        String formattedDate1 = format.format(calendar.getTime());
                        String formattedDate2 = format.format(calendar2.getTime());

                        try {
                            dateFirst = format.parse(formattedDate1);
                            dateEnd = format.parse(formattedDate2);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        dataSheet.getEditText().setText(formattedDate1 + "-" + formattedDate2);
                    }
                });
            }
        });

        // Настройка кнопки в bottomSheet
        buttonSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                    filter();
                ProductAdapter productAdapter = new ProductAdapter(addProduct(),"Продали ", " ₽" );

                recyclerView.setAdapter(productAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                bottomSheetDialog.dismiss();

            }
        });

        //Съэкономлено пока отложим
//            TextView savedOnEggs_text = (TextView) view.findViewById(R.id.savedOnEggs_text);
//            TextView savedOnMilk_text = (TextView) view.findViewById(R.id.savedOnMilk_text);
//            TextView savedOnMeat_text = (TextView) view.findViewById(R.id.savedOnMeat_text);
//
//            savedOnEggs_text.setText(String.valueOf((mydbManager.sumSaleEggPrice())));
//            savedOnMilk_text.setText(String.valueOf((mydbManager.sumSaleMilkPrice())));
//            savedOnMeat_text.setText(String.valueOf((mydbManager.sumSaleMeatPrice())));

        return layout;
    }

    // Добавляем продукцию
    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();
    }

    //Считаем сумму по каждому продукту
    public Map addProduct() {
        Map<String, Double> tempList = new HashMap<>();
        for (String product : productList) {

            Cursor cursor = myDB.idProduct1(MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, product);

            if (cursor != null && cursor.getCount() != 0) {

                while (cursor.moveToNext()) {
                    Double productUnit = cursor.getDouble(6);
                    if (tempList.get(product) == null) {
                        tempList.put(product, productUnit);
                    } else {
                        double sum = tempList.get(product) + productUnit;
                        tempList.put(product, sum);
                    }
                }
                cursor.close();
            } else {
                tempList.put(product, 0.0);
            }
        }
        return tempList;
    }

    //расчеты
    // Общая прибыль
    public double totalAmount() {
            Cursor cursor = myDB.readAllDataSale();
            double sum = 0;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    double productUnit = cursor.getDouble(6);
                    sum += productUnit;
                    }
                cursor.close();
            } else {
               return 0;
            }
        return sum;
    }

    // Общие расходы
    public double totalExpenses() {
        Cursor cursor = myDB.readAllDataExpenses();
        double sum = 0;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                double productUnit = cursor.getDouble(2);
                sum += productUnit;
            }
            cursor.close();
        } else {
            return 0;
        }

        return sum;
    }

    //Кнопка
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.financeChart_button:
                onClickFinanceChart(v, new FinanceChartFragment());
                break;
            case R.id.financeChart2_button:
                onClickFinanceChart(v, new FinanceChart2Fragment());
                break;
        }
    }

    //Функция кнопки
    public void onClickFinanceChart(View view, Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

    //Добавляем bottobSheet
    public void showBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.fragment_bottom_finace);

        dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet);
        buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet);
    }


//    public void filter() throws ParseException {
//
//        productNow.clear();
//        String animalsSpinerSheetText = animalsSpinerSheet.getText().toString();
//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//
//        if (animalsSpinerSheetText.equals("Все") && dataSheet.getEditText().getText().toString().equals("")) {
//            productNow.addAll(product);
//        } else if (animalsSpinerSheetText.equals("Все") && !dataSheet.getEditText().getText().toString().equals("")) {
//
//            for (ProductDB productDB : product) {
//
//                Date dateNow = format.parse(productDB.getData());
//
//                if ((dateFirst.before(dateNow) && dateEnd.before(dateNow)) || dateFirst.equals(dateNow) || dateEnd.equals(dateNow)) {
//                    productNow.add(productDB);
//                }
//
//            }
//
//        } else if (!animalsSpinerSheetText.equals("Все") && dataSheet.getEditText().getText().toString().equals("")) {
//
//            for (ProductDB productDB : product) {
//
//                if (animalsSpinerSheetText.equals(productDB.getName())) {
//                    productNow.add(productDB);
//                }
//            }
//
//        } else {
//
//            for (ProductDB productDB : product) {
//
//                if (animalsSpinerSheetText.equals(productDB.getName())) {
//
//                    Date dateNow = format.parse(productDB.getData());
//
//                    if ((dateFirst.before(dateNow) && dateEnd.after(dateNow)) || dateFirst.equals(dateNow) || dateEnd.equals(dateNow)) {
//                        productNow.add(productDB);
//                    }
//                }
//            }
//        }
//    }



}
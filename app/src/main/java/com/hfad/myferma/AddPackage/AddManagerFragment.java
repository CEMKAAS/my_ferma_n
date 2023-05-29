package com.hfad.myferma.AddPackage;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.ExpensesPackage.ExpensesFragment;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.SaleFragment;
import com.hfad.myferma.WriteOff.WriteOffFragment;
import com.hfad.myferma.bottomFragment;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.ListAdapterIncubator;
import com.hfad.myferma.incubator.NowArhiveFragment;
import com.hfad.myferma.incubator.editDayIncubatorFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class AddManagerFragment extends Fragment  implements FragmentKeyeventListenerManager  {
    private RecyclerView recyclerView;
    private ImageView empty_imageview;
    private TextView no_data, sixColumn, dicsPrice;
    private MyFermaDatabaseHelper myDB;
    private CustomAdapterAdd customAdapterAdd;
    private ArrayList<String> productList, productListAll;
    private ArrayAdapter<String> arrayAdapterProduct;
    private AutoCompleteTextView animalsSpinerSheet;
    private TextInputLayout dataSheet;
    private Button buttonSheet;
    private BottomSheetDialog bottomSheetDialog;
    private MaterialDatePicker<Pair<Long, Long>> datePicker;
    private List<ProductDB> product, productNow;
    private Date dateFirst, dateEnd;
    private String appBarManager, statusPrice, priceDics;
    private Cursor cursorManager;
    private int visibility, myRow;

    public AddManagerFragment(String appBarManager, Cursor cursorManager, int visibility, String statusPrice, String priceDics, int myRow) {
        this.appBarManager = appBarManager;
        this.cursorManager = cursorManager;
        this.visibility = visibility;
        this.statusPrice = statusPrice;
        this.priceDics = priceDics;
        this.myRow = myRow;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_manager, container, false);
        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList<>();
        productListAll = new ArrayList<>();
        //Добавление товара в лист
        add();
        //Создание модального bottomSheet
        showBottomSheetDialog();

        //Настройка кнопки и верхнего бара
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.filler).setVisible(true);
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setTitle(appBarManager);
        appBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filler:
                    bottomSheetDialog.show();
            }
            return true;
        });

        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToGo();
            }
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
                try {
                    filter();
                    customAdapterAdd = new CustomAdapterAdd(productNow, myRow);

                    recyclerView.setAdapter(customAdapterAdd);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    bottomSheetDialog.dismiss();

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        //Создание отображения списка
        sixColumn = layout.findViewById(R.id.six_column);
        dicsPrice = layout.findViewById(R.id.dics_price);
        recyclerView = layout.findViewById(R.id.recyclerView);
        empty_imageview = layout.findViewById(R.id.empty_imageview);
        no_data = layout.findViewById(R.id.no_data);

        sixColumn.setVisibility(visibility);
        sixColumn.setText(statusPrice);
        dicsPrice.setText(priceDics);

        product = new ArrayList<>();
        productNow = new ArrayList<>();

        //Добавдение товаров в лист
        storeDataInArraysClass(cursorManager);

        //Создание адаптера
        customAdapterAdd = new CustomAdapterAdd(productNow, myRow);

        recyclerView.setAdapter(customAdapterAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Запускаем при нажатии
        customAdapterAdd.setListener(new CustomAdapterAdd.Listener() {
            @Override
            public void onClick(int position, ProductDB productDB) {
                addChart(productDB);
            }
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, productListAll);
            animalsSpinerSheet.setAdapter(arrayAdapterProduct);
        }
    }

    //Добавляем продукцию в список
    public void add() {
        if (!statusPrice.equals("Нет")) {

            Cursor cursor = myDB.readAllDataProduct();

            while (cursor.moveToNext()) {
                String product = cursor.getString(1);
                productList.add(product);
            }
            cursor.close();

            productListAll = (ArrayList<String>) productList.clone();


        } else {

            Set<String> tempList = new HashSet<>();
            Cursor cursor = myDB.readAllDataExpenses();

            while (cursor.moveToNext()) {
                String string1 = cursor.getString(1);
                tempList.add(string1);
            }
            cursor.close();

            for (String nameExpenses : tempList) {
                productListAll.add(nameExpenses);
            }

        }
        productListAll.add("Все");
    }

    //Добавляем базу данных в лист //TODO Надо скоратить
    void storeDataInArraysClass(Cursor cursor) {
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else if (statusPrice.equals("Нет")) {
            storeDataInArraysClassLogic(cursor, 0);
        } else {
            storeDataInArraysClassLogic(cursor, 6);
        }
        productNow.addAll(product);
    }

    public void storeDataInArraysClassLogic(Cursor cursor, int id){
        cursor.moveToLast();
        product.add(new ProductDB(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2),
                cursor.getString(3) + "." + cursor.getString(4) + "." + cursor.getString(5), cursor.getInt(id)));
        while (cursor.moveToPrevious()) {
            product.add(new ProductDB(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2),
                    cursor.getString(3) + "." + cursor.getString(4) + "." + cursor.getString(5), cursor.getInt(id)));
        }
        cursor.close();
        empty_imageview.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);
    }



    public void filter() throws ParseException {

        productNow.clear();

        String animalsSpinerSheetText = animalsSpinerSheet.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        if (animalsSpinerSheetText.equals("Все") && dataSheet.getEditText().getText().toString().equals("")) {
            productNow.addAll(product);
        } else if (animalsSpinerSheetText.equals("Все") && !dataSheet.getEditText().getText().toString().equals("")) {

            for (ProductDB productDB : product) {

                Date dateNow = format.parse(productDB.getData());

                if ((dateFirst.before(dateNow) && dateEnd.before(dateNow)) || dateFirst.equals(dateNow) || dateEnd.equals(dateNow)) {
                    productNow.add(productDB);
                }

            }

        } else if (!animalsSpinerSheetText.equals("Все") && dataSheet.getEditText().getText().toString().equals("")) {

            for (ProductDB productDB : product) {

                if (animalsSpinerSheetText.equals(productDB.getName())) {
                    productNow.add(productDB);
                }
            }

        } else {

            for (ProductDB productDB : product) {

                if (animalsSpinerSheetText.equals(productDB.getName())) {

                    Date dateNow = format.parse(productDB.getData());

                    if ((dateFirst.before(dateNow) && dateEnd.after(dateNow)) || dateFirst.equals(dateNow) || dateEnd.equals(dateNow)) {
                        productNow.add(productDB);
                    }
                }
            }
        }
    }

    //Добавляем bottobSheet
    public void showBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.fragment_bottom);

        animalsSpinerSheet = bottomSheetDialog.findViewById(R.id.animals_spiner_sheet);
        animalsSpinerSheet.setText("Все", false);
        dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet);
        buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet);
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        MainActivity mainActivity = (MainActivity) getActivity();
//
//        mainActivity.setFragmentKeyeventListenerMager(this);
//    }

    public void addChart(ProductDB productDB) {
        //todo
        UpdateProductFragment updateProductFragment = new UpdateProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("fd", productDB);
        bundle.putString("id", appBarManager);
        updateProductFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, updateProductFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();

    }
    public void backToGo(){
        if (appBarManager.equals("Мои Товар")) {
            backToGoFragment(new AddFragment());
        } else if (appBarManager.equals("Мои Продажи")) {
            backToGoFragment(new SaleFragment());
        } else if (appBarManager.equals("Мои Покупки")) {
            backToGoFragment(new ExpensesFragment());
        } else if (appBarManager.equals("Мои Списания")) {
            backToGoFragment(new WriteOffFragment());
        }
    }

    public void backToGoFragment (Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.conteiner,fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onFragmentKeyEventManager(KeyEvent event) {
        backToGo();
        return false;
    }
}
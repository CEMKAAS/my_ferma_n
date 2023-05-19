package com.hfad.myferma.AddPackage;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.bottomFragment;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.ListAdapterIncubator;
import com.hfad.myferma.incubator.editDayIncubatorFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddManagerFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView empty_imageview;
    private TextView no_data;
    private MyFermaDatabaseHelper myDB;
    private ArrayList<String> id, title, disc, day, mount, year;
    private CustomAdapterAdd customAdapterAdd;

    private int mount1 = 0;

    private ArrayList<String> productList, productListAll;
    private ArrayAdapter<String> arrayAdapterProduct;

    private AutoCompleteTextView animalsSpinerSheet;
    private TextInputLayout dataSheet;
    private Button buttonSheet;
    private BottomSheetDialog bottomSheetDialog;
    private MaterialDatePicker datePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_manager, container, false);
        myDB = new MyFermaDatabaseHelper(getActivity());
        productList = new ArrayList<>();
        productListAll = new ArrayList<>();
        add();

        showBottomSheetDialog();

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.filler).setVisible(true);
        appBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.filler:

                    bottomSheetDialog.show();
            }
            return true;
        });

        // Настройка календаря
        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder)
                .setTitleText("Выберите").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        dataSheet.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getActivity().getSupportFragmentManager(), "wer");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                        calendar.setTimeInMillis((Long) selection);
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        String formattedDate = format.format(calendar.getTime());
                        dataSheet.getEditText().setText(formattedDate);

                    }
                });
            }
        });

        //убириаем фаб кнопку
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        recyclerView = layout.findViewById(R.id.recyclerView);
        empty_imageview = layout.findViewById(R.id.empty_imageview);
        no_data = layout.findViewById(R.id.no_data);


        id = new ArrayList<>();
        title = new ArrayList<>();
        disc = new ArrayList<>();
        day = new ArrayList<>();
        mount = new ArrayList<>();
        year = new ArrayList<>();


        no_data = layout.findViewById(R.id.no_data);

        storeDataInArrays();
        customAdapterAdd = new CustomAdapterAdd(id, title, disc,
                day, mount, year);

        recyclerView.setAdapter(customAdapterAdd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        customAdapterAdd.setListener(new CustomAdapterAdd.Listener() {
            @Override
            public void onClick(int position) {

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
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            productList.add(product);
        }
        cursor.close();

        productListAll = (ArrayList<String>) productList.clone();
        productListAll.add("Все");
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            cursor.moveToLast();
            id.add(cursor.getString(0));
            title.add(cursor.getString(1));
            disc.add(cursor.getString(2));
            day.add(cursor.getString(3));
            mount.add(cursor.getString(4));
            year.add(cursor.getString(5));

            while (cursor.moveToPrevious()) {
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                disc.add(cursor.getString(2));
                day.add(cursor.getString(3));
                mount.add(cursor.getString(4));
                year.add(cursor.getString(5));
            }
            cursor.close();
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    public void showBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.fragment_bottom);

        animalsSpinerSheet = bottomSheetDialog.findViewById(R.id.animals_spiner_sheet);
        animalsSpinerSheet.setText("Все",false);
        dataSheet = bottomSheetDialog.findViewById(R.id.data_sheet);
        buttonSheet = bottomSheetDialog.findViewById(R.id.button_sheet);
    }
}
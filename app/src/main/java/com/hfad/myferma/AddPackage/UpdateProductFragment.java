package com.hfad.myferma.AddPackage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.NowArhiveFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateProductFragment extends Fragment {

    private TextView textUnit;

    private TextInputLayout titleExpenses, titleCount, titleData, titlePrice, menu;

    private Button updateButton, deleteButton;

    private AutoCompleteTextView writeOffSpiner;

    private ProductDB productDB;
    private String id;

    public UpdateProductFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_update_product, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productDB = bundle.getParcelable("fd");
            id = bundle.getString("id");
        }

        textUnit = layout.findViewById(R.id.text_unit);

        titleExpenses = layout.findViewById(R.id.tilleExpenses_input);
        titleCount = layout.findViewById(R.id.titleSale_input);
        titleData = layout.findViewById(R.id.daySale_input);
        titlePrice = layout.findViewById(R.id.priceEdit_input);
        menu = layout.findViewById(R.id.menu);
        writeOffSpiner = layout.findViewById(R.id.writeOff_spiner);

        updateButton = layout.findViewById(R.id.update_button);
        deleteButton = layout.findViewById(R.id.delete_button);

        textUnit.setText(productDB.getName());

        titleExpenses.getEditText().setText(productDB.getName());
        titleCount.getEditText().setText(productDB.getDisc().toString());
        titleData.getEditText().setText(productDB.getData());
        titlePrice.getEditText().setText(String.valueOf(productDB.getPrice()));

        if (R.drawable.baseline_cottage_24 == productDB.getPrice()) {
            writeOffSpiner.setText("На собсвенные нужды", false);
        } else {
            writeOffSpiner.setText("На утилизацию", false);
        }

        titleExpenses.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);

        if (id.equals("Мои Товар")){
            titlePrice.setVisibility(View.GONE);

        } else if (id.equals("Мои Продажи")) {


        } else if (id.equals("Мои Покупки")) {
            titleExpenses.setVisibility(View.VISIBLE);

            textUnit.setVisibility(View.GONE);
            titlePrice.setVisibility(View.GONE);

            titleCount.setHint("Цена");
            titleCount.setHelperText("Укажите цену за товар");
            titleCount.getEditText().setText(String.valueOf(productDB.getPrice()));

        } else if (id.equals("Мои Списания")) {
            titlePrice.setVisibility(View.GONE);
            menu.setVisibility(View.VISIBLE);
        }


//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //And only then we call this
//                title = textUnit.getText().toString().trim();
//                disc = disc_input.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
//                day = day_input.getEditText().getText().toString();
//                mount = mount_input.getEditText().getText().toString();
//                year = year_input.getEditText().getText().toString();
//
//                //убираем ошибку
//                disc_input.setErrorEnabled(false);
//                day_input.setErrorEnabled(false);
//                mount_input.setErrorEnabled(false);
//                year_input.setErrorEnabled(false);
//                //вывод ошибки
//                if (disc.equals("") || day.equals("") || mount.equals("") || year.equals("")) {
//                    if (disc.equals("")) {
//                        disc_input.setError("Введите кол-во!");
//                        disc_input.getError();
//                    }
//                    if (day.equals("")) {
//                        day_input.setError("Введите день!");
//                        day_input.getError();
//                    }
//                    if (mount.equals("")) {
//                        mount_input.setError("Введите месяц!");
//                        mount_input.getError();
//                    }
//                    if (year.equals("")) {
//                        year_input.setError("Введите год!");
//                        year_input.getError();
//                    }
//                } else {
//                    if (sun() < 0) {
//                        if (sumEgg()){
//                            disc_input.setError("Яйца не могут быть дробными...");
//                            disc_input.getError();
//                        }
//                        disc_input.setError("Нелья уйти в минус!"); //TODO Нормально напиши
//                        disc_input.getError();
//                    } else if (sumEgg()) {
//                        disc_input.setError("Яйца не могут быть дробными...");
//                        disc_input.getError();
//                    } else {
//                        disc.replaceAll(",", ".").replaceAll("[^\\d.]", "");
//                        myDB.updateData(id, title, disc, day, mount, year);
//                    }
//                }
//            }
//        });
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (sunDelete() < 0) {
//                    Toast.makeText(UpdateActivityAdd.this, "Нелья уйти в минус!", Toast.LENGTH_SHORT).show();
//                }else {delete();}
//            }
//        });


        return layout;
    }


//    Calendar calendar = Calendar.getInstance();
//        data.getEditText().setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
//
//    // Настройка календаря
//    CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
//            .setValidator(DateValidatorPointBackward.now())
//            .build();
//
//    datePicker = MaterialDatePicker.Builder.datePicker()
//            .setCalendarConstraints(constraintsBuilder)
//                .setTitleText("Выберите даду закладки яиц").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//            .build();
//
//    // Установка сколько всего яиц
//        eggAll.getEditText().setText("0");
//        data.getEditText().setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            datePicker.show(getActivity().getSupportFragmentManager(), "wer");
//            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//                @Override
//                public void onPositiveButtonClick(Object selection) {
//                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                    calendar.setTimeInMillis((Long) selection);
//                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//                    String formattedDate = format.format(calendar.getTime());
//                    data.getEditText().setText(formattedDate);
//
//                }
//            });
//        }
//    });
//





//    void delete() {
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
//        builder.setTitle("Удалить " + title + " ?");
//        builder.setMessage("Вы уверены, что хотите удалить " + title + " ?");
//        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateActivityAdd.this);
//                myDB.deleteOneRow(id);
//                finish();
//            }
//        });
//        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.create().show();
//    }


}
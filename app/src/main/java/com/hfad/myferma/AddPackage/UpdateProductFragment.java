package com.hfad.myferma.AddPackage;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.ExpensesPackage.ExpensesFragment;
import com.hfad.myferma.InfoFragment;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.SaleFragment;
import com.hfad.myferma.SettingsFragment;
import com.hfad.myferma.WriteOff.WriteOffFragment;
import com.hfad.myferma.db.MyConstanta;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.NowArhiveFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UpdateProductFragment extends Fragment {

    private TextView textUnit;
    private TextInputLayout titleExpenses, titleCount, titleData, titlePrice, menu;
    private Button updateButton, deleteButton;
    private AutoCompleteTextView writeOffSpiner;
    private ProductDB productDB;
    private String id, oldCount, nowCount;
    private MyFermaDatabaseHelper myDB;
    private MaterialDatePicker datePicker;
    private DecimalFormat f;
    private String unit = null;
    public UpdateProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_update_product, container, false);

        //Подкючаемся к БД
        myDB = new MyFermaDatabaseHelper(getActivity());

        // Получаем информацию из предыдущего фрагмента
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productDB = bundle.getParcelable("fd");
            id = bundle.getString("id");
        }

        // Настройка аппбара и настройка стрелки, чтобы вернутся назад
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        appBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.more:
                    replaceFragment(new InfoFragment());
                    appBar.setTitle("Информация");
                    break;
                case R.id.setting:
                    replaceFragment(new SettingsFragment());
                    appBar.setTitle("Мои настройки");
                    break;
            }
            return true;
        });

        // Подключаемся к фронту
        textUnit = layout.findViewById(R.id.text_unit);
        titleExpenses = layout.findViewById(R.id.tilleExpenses_input);
        titleCount = layout.findViewById(R.id.titleSale_input);
        titleData = layout.findViewById(R.id.daySale_input);
        titlePrice = layout.findViewById(R.id.priceEdit_input);
        menu = layout.findViewById(R.id.menu);
        writeOffSpiner = layout.findViewById(R.id.writeOff_spiner);
        updateButton = layout.findViewById(R.id.update_button);
        deleteButton = layout.findViewById(R.id.delete_button);

        // Настройка фронта
        textUnit.setText(productDB.getName());
        unitString(productDB.getName());
        titleExpenses.getEditText().setText(productDB.getName());
        titleCount.getEditText().setText(f.format(productDB.getDisc()));
        titleCount.setSuffixText(unit);
        titleData.getEditText().setText(productDB.getData());
        titlePrice.getEditText().setText(String.valueOf(productDB.getPrice()));

        if (R.drawable.baseline_cottage_24 == productDB.getPrice()) {
            writeOffSpiner.setText("На собсвенные нужды", false);
        } else {
            writeOffSpiner.setText("На утилизацию", false);
        }

        // сохраняем значение, которое было изначально
        oldCount = titleCount.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");

        // Настройка календаря
        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder)
                .setTitleText("Выберите дату").setSelection(MaterialDatePicker.todayInUtcMilliseconds()) //Todo выбирать дату из EditText
                .build();

        titleData.getEditText().setOnClickListener(new View.OnClickListener() {
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
                        titleData.getEditText().setText(formattedDate);

                    }
                });
            }
        });

        // Настройка видимости фронта
        titleExpenses.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);

        //Для товаров
        if (id.equals("Мои Товар")) {
            titlePrice.setVisibility(View.GONE);
            // для покупок
        } else if (id.equals("Мои Покупки")) {
            titleExpenses.setVisibility(View.VISIBLE);

            textUnit.setVisibility(View.GONE);
            titlePrice.setVisibility(View.GONE);

            titleCount.setHint("Цена");
            titleCount.setHelperText("Укажите цену за товар");
            titleCount.getEditText().setText(String.valueOf(productDB.getPrice()));
            // для списаний
        } else if (id.equals("Мои Списания")) {
            titlePrice.setVisibility(View.GONE);
            menu.setVisibility(View.VISIBLE);
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (id.equals("Мои Товар")) {
                    myProduct();

                } else if (id.equals("Мои Продажи")) {
                    mySale();

                } else if (id.equals("Мои Покупки")) {
                    myExpenses();

                } else if (id.equals("Мои Списания")) {
                    myWriteOff();

                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String product = textUnit.getText().toString();
                String count = oldCount;

                // Проверяем если мы удалим в продажах, товаров и списаниях, уйдем ли мы в минус
                if (id.equals("Мои Товар") || id.equals("Мои Продажи") || id.equals("Мои Списания")) {
                    // Проверяем если мы удалим, уйдем ли мы в минус
                    if (sumDelete(product, count) < 0) {
                        Toast.makeText(getActivity(), "Нелья уйти в минус!", Toast.LENGTH_SHORT).show();
                    } else {
                        delete();
                    }
                    // удаляем, если это покупки
                } else {
                    delete();
                }
            }
        });
        return layout;
    }

    // Мои продукты
    public void myProduct() {

        String product = textUnit.getText().toString();
        String count = titleCount.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
        String[] data = titleData.getEditText().getText().toString().split("\\.");

        //убираем ошибку
        titleCount.setErrorEnabled(false);
        titleData.setErrorEnabled(false);

        //вывод ошибки
        if (count.equals("") || data.equals("")) {
            if (count.equals("")) {
                titleCount.setError("Введите кол-во!");
                titleCount.getError();
            }
            if (data.equals("")) {
                titleData.setError("Укажите дату!");
                titleData.getError();
            }
        } else if (containsEgg(product, count)) {
            titleCount.setError("Яйца не могут быть дробными...");
            titleCount.getError();
        } else if (sum(product, count) < 0) {
            titleCount.setError("Нелья уйти в минус!");
            titleCount.getError();
        } else {
            myDB.updateData(String.valueOf(productDB.getId()), product, count, data[0], data[1], data[2]);
        }
    }


    //Мои продажи
    public void mySale() {
        String product = textUnit.getText().toString();
        String count = titleCount.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
        String[] data = titleData.getEditText().getText().toString().split("\\.");
        String sale = titlePrice.getEditText().getText().toString();

        //убираем ошибку
        titleCount.setErrorEnabled(false);
        titleData.setErrorEnabled(false);
        titlePrice.setErrorEnabled(false);

        //вывод ошибки
        if (count.equals("") || data.equals("") || sale.equals("")) {
            if (count.equals("")) {
                titleCount.setError("Введите кол-во!");
                titleCount.getError();
            }
            if (data.equals("")) {
                titleData.setError("Укажите дату!");
                titleData.getError();
            }
            if (sale.equals("")) {
                titlePrice.setError("Укажите цену");
                titlePrice.getError();
            }
        } else if (containsEgg(product, count)) {
            titleCount.setError("Яйца не могут быть дробными...");
            titleCount.getError();
        } else if (sum(product, count) < 0) {
            titleCount.setError("Нелья уйти в минус!");
            titleCount.getError();
        } else {
            myDB.updateDataSale(String.valueOf(productDB.getId()), product, count, data[0], data[1], data[2], Double.valueOf(productDB.getPrice()));
        }
    }

    // Мои продажи
    public void myExpenses() {
        String product = titleExpenses.getEditText().getText().toString();
        String count = titleCount.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
        String[] data = titleData.getEditText().getText().toString().split("\\.");

        //убираем ошибку
        titleExpenses.setErrorEnabled(false);
        titleCount.setErrorEnabled(false);
        titleData.setErrorEnabled(false);

        //вывод ошибки
        if (count.equals("") || data.equals("") || product.equals("")) {
            if (count.equals("")) {
                titleCount.setError("Введите кол-во!");
                titleCount.getError();
            }
            if (data.equals("")) {
                titleData.setError("Укажите дату!");
                titleData.getError();
            }
            if (product.equals("")) {
                titleExpenses.setError("Укажите название");
                titleExpenses.getError();
            }
        } else if (containsEgg(product, count)) {
            titleCount.setError("Яйца не могут быть дробными...");
            titleCount.getError();
        } else {
            myDB.updateDataExpenses(String.valueOf(productDB.getId()), product, count, data[0], data[1], data[2]);
        }
    }

    //Мои списания
    public void myWriteOff() {

        String product = textUnit.getText().toString();
        String count = titleCount.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
        String[] data = titleData.getEditText().getText().toString().split("\\.");

        //убираем ошибку
        titleCount.setErrorEnabled(false);
        titleData.setErrorEnabled(false);

        // Настройка картинок
        int statusDrawable = R.drawable.baseline_cottage_24;
        if (!writeOffSpiner.getText().equals("На собсвенные нужды")) {
            statusDrawable = R.drawable.baseline_delete_24;
        }


        //вывод ошибки
        if (count.equals("") || data.equals("")) {
            if (count.equals("")) {
                titleCount.setError("Введите кол-во!");
                titleCount.getError();
            }
            if (data.equals("")) {
                titleData.setError("Укажите дату!");
                titleData.getError();
            }
        } else if (containsEgg(product, count)) {
            titleCount.setError("Яйца не могут быть дробными...");
            titleCount.getError();
        } else if (sum(product, count) < 0) {
            titleCount.setError("Нелья уйти в минус!");
            titleCount.getError();
        } else {
            myDB.updateDataWriteOff(String.valueOf(productDB.getId()), product, count, data[0], data[1], data[2], statusDrawable);
        }

    }

    //Проверяем есть ли запятая или нет в яйцах
    public boolean containsEgg(String title, String count) {
        if (title.equals("Яйца")) {
            if (count.contains(".") || count.contains(",")) {
                return true;
            }
        }
        return false;
    }

    //Считаем сколько у нас товара на текущий момент
    public Double sum(String product, String count) {
        double a = add(product);
        double b = Double.valueOf(oldCount);
        double c = Double.valueOf(count);
        double d = a - (b - c);
        return d;
    }

    //Считаем сколько у нас товара на текущий момент
    public Double sumDelete(String product, String count) {
        double a = add(product);
        double c = Double.valueOf(count);
        double d = a - c;
        return d;
    }

    //Считаем сколько данного товара на данный момент
    public double add(String product) {

        double tempList = 0;

        Cursor cursor = myDB.idProduct1(MyConstanta.TABLE_NAME, MyConstanta.TITLE, product);

        if (cursor != null && cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                Double productUnit = cursor.getDouble(2);
                tempList += productUnit;
            }
            cursor.close();

            Cursor cursorSale = myDB.idProduct1(MyConstanta.TABLE_NAMESALE, MyConstanta.TITLESale, product);

            while (cursorSale.moveToNext()) {
                Double productUnit = cursorSale.getDouble(2);
                tempList -= productUnit;
            }
            cursorSale.close();

            Cursor cursorWriteOff = myDB.idProduct1(MyConstanta.TABLE_NAMEWRITEOFF, MyConstanta.TITLEWRITEOFF, product);

            while (cursorWriteOff.moveToNext()) {
                Double productUnit = cursorWriteOff.getDouble(2);
                tempList -= productUnit;
            }
            cursorWriteOff.close();
        }

        return tempList;
    }

    //Удаляем и возвращаемся назад
    public void delete() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Удалить " + textUnit.getText().toString() + " ?");
        builder.setMessage("Вы уверены, что хотите удалить " + textUnit.getText().toString() + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (id.equals("Мои Товар")) {
                    myDB.deleteOneRow(String.valueOf(productDB.getId()));
                } else if (id.equals("Мои Продажи")) {
                   myDB.deleteOneRowSale(String.valueOf(productDB.getId()));
                } else if (id.equals("Мои Покупки")) {
                   myDB.deleteOneRowExpenses(String.valueOf(productDB.getId()));
                } else if (id.equals("Мои Списания")) {
                    myDB.deleteOneRowWriteOff(String.valueOf(productDB.getId()));
                }

                replaceFragment(new AddManagerFragment());
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    //Возвращаемся назад при нажатии на клавишу
    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void unitString(String animals) {
        if(!id.equals("Мои Покупки")) {
            if (animals.equals("Яйца")) {
                f = new DecimalFormat("0");
                unit = " шт.";
            } else if (animals.equals("Молоко")) {
                f = new DecimalFormat("0.00");
                unit = " л.";
            } else if (animals.equals("Мясо")) {
                f = new DecimalFormat("0.00");
                unit = " кг.";
            } else {
                f = new DecimalFormat("0.00");
                unit = " ед.";
            }
        }else {unit = " ₽";
            f = new DecimalFormat("0.00");}
    }

}
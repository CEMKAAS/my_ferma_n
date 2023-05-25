package com.hfad.myferma.incubator;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.hfad.myferma.MainActivity;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AddIncubatorFragment extends Fragment implements View.OnClickListener {

    private MydbManagerMetod mydbManager;
    private MyFermaDatabaseHelper myDB;
    private TextInputLayout name_incubator;
    private TextInputLayout data;
    private TextInputLayout dayTemp1;
    private TextInputLayout dayTemp2;
    private TextInputLayout dayTemp3;
    private TextInputLayout dayTemp4;
    private TextInputLayout dayTemp5;
    private TextInputLayout dayTemp6;
    private TextInputLayout dayTemp7;
    private TextInputLayout dayTemp8;
    private TextInputLayout dayTemp9;
    private TextInputLayout dayTemp10;
    private TextInputLayout dayTemp11;
    private TextInputLayout dayTemp12;
    private TextInputLayout dayTemp13;
    private TextInputLayout dayTemp14;
    private TextInputLayout dayTemp15;
    private TextInputLayout dayTemp16;
    private TextInputLayout dayTemp17;
    private TextInputLayout dayTemp18;
    private TextInputLayout dayTemp19;
    private TextInputLayout dayTemp20;
    private TextInputLayout dayTemp21;
    private TextInputLayout dayTemp22;
    private TextInputLayout dayTemp23;
    private TextInputLayout dayTemp24;
    private TextInputLayout dayTemp25;
    private TextInputLayout dayTemp26;
    private TextInputLayout dayTemp27;
    private TextInputLayout dayTemp28;
    private TextInputLayout dayTemp29;
    private TextInputLayout dayTemp30;
    private TextInputLayout dayDamp1, dayDamp2, dayDamp3, dayDamp4, dayDamp5, dayDamp6, dayDamp7, dayDamp8, dayDamp9, dayDamp10, dayDamp11, dayDamp12, dayDamp13, dayDamp14, dayDamp15, dayDamp16, dayDamp17, dayDamp18, dayDamp19, dayDamp20, dayDamp21, dayDamp22, dayDamp23, dayDamp24, dayDamp25, dayDamp26, dayDamp27, dayDamp28, dayDamp29, dayDamp30;
    private TextView day17, day18, day19, day20, day21, day22, day23, day24, day25, day26, day27, day28, day29, day30;

    private TextInputLayout eggAll, time1, time2, time3;

    private MaterialDatePicker datePicker;

    private MaterialTimePicker timeOne, timeTwo, timeThree;

    private CheckBox friz, over;

    private ArrayList id;
    private ArrayList name;
    private ArrayList type;
    private String[] massId, massTempDamp, massOver, massAiring;
    private String dataText;
    private InterstitialAd mInterstitialAd;
    private Boolean chiken = false, geese = false, turkeys = false, quail = false, ducks = false;
    private AutoCompleteTextView animals_spiner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_incubator, container, false);

        //Подключение к базам данных
        myDB = new MyFermaDatabaseHelper(getActivity());
        mydbManager = new MydbManagerMetod(inflater.getContext());
        Cursor cursor = myDB.readAllDataIncubator();


        // Создание объекта таргетирования рекламы.


        //Убираем Фаб
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        //Настройка аппбара
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Добавить Инкубатор");
        appBar.getMenu().findItem(R.id.delete).setVisible(false);

        id = new ArrayList<>();
        name = new ArrayList<>();
        type = new ArrayList<>();
        name_incubator = layout.findViewById(R.id.name_incubator);
        data = layout.findViewById(R.id.data);
        eggAll = layout.findViewById(R.id.eggAll);
        friz = layout.findViewById(R.id.friz);
        over = layout.findViewById(R.id.overturn);
        animals_spiner = (AutoCompleteTextView) layout.findViewById(R.id.animals_spiner);

        time1 = layout.findViewById(R.id.time1);
        time2 = layout.findViewById(R.id.time2);
        time3 = layout.findViewById(R.id.time3);

        // Настройка инкубатора
        animals_spiner.setText("Курицы", false);
        if (cursor.getCount() == 0) {
            name_incubator.getEditText().setText("Инкубатор №1");
        } else {
            cursor.moveToLast();
            name_incubator.getEditText().setText("Инкубатор №" + (cursor.getInt(0) + 1));
        }
        cursor.close();

        time1.getEditText().setText("08:00");
        time2.getEditText().setText("12:00");

        storeDataInArrays();

        Button add = (Button) layout.findViewById(R.id.begin);
        add.setOnClickListener(this);

        //Установка полей, лучше не открывать толку нет, а закрывать придеться
        TextInputLayout[] damp = {
                dayDamp1 = layout.findViewById(R.id.dayDamp1),
                dayDamp2 = layout.findViewById(R.id.dayDamp2),
                dayDamp3 = layout.findViewById(R.id.dayDamp3),
                dayDamp4 = layout.findViewById(R.id.dayDamp4),
                dayDamp5 = layout.findViewById(R.id.dayDamp5),
                dayDamp6 = layout.findViewById(R.id.dayDamp6),
                dayDamp7 = layout.findViewById(R.id.dayDamp7),
                dayDamp8 = layout.findViewById(R.id.dayDamp8),
                dayDamp9 = layout.findViewById(R.id.dayDamp9),
                dayDamp10 = layout.findViewById(R.id.dayDamp10),
                dayDamp11 = layout.findViewById(R.id.dayDamp11),
                dayDamp12 = layout.findViewById(R.id.dayDamp12),
                dayDamp13 = layout.findViewById(R.id.dayDamp13),
                dayDamp14 = layout.findViewById(R.id.dayDamp14),
                dayDamp15 = layout.findViewById(R.id.dayDamp15),
                dayDamp16 = layout.findViewById(R.id.dayDamp16),
                dayDamp17 = layout.findViewById(R.id.dayDamp17),
                dayDamp18 = layout.findViewById(R.id.dayDamp18),
                dayDamp19 = layout.findViewById(R.id.dayDamp19),
                dayDamp20 = layout.findViewById(R.id.dayDamp20),
                dayDamp21 = layout.findViewById(R.id.dayDamp21),
                dayDamp22 = layout.findViewById(R.id.dayDamp22),
                dayDamp23 = layout.findViewById(R.id.dayDamp23),
                dayDamp24 = layout.findViewById(R.id.dayDamp24),
                dayDamp25 = layout.findViewById(R.id.dayDamp25),
                dayDamp26 = layout.findViewById(R.id.dayDamp26),
                dayDamp27 = layout.findViewById(R.id.dayDamp27),
                dayDamp28 = layout.findViewById(R.id.dayDamp28),
                dayDamp29 = layout.findViewById(R.id.dayDamp29),
                dayDamp30 = layout.findViewById(R.id.dayDamp30),
        };

        TextInputLayout[] temp = {
                dayTemp1 = layout.findViewById(R.id.dayTemp1),
                dayTemp2 = layout.findViewById(R.id.dayTemp2),
                dayTemp3 = layout.findViewById(R.id.dayTemp3),
                dayTemp4 = layout.findViewById(R.id.dayTemp4),
                dayTemp5 = layout.findViewById(R.id.dayTemp5),
                dayTemp6 = layout.findViewById(R.id.dayTemp6),
                dayTemp7 = layout.findViewById(R.id.dayTemp7),
                dayTemp8 = layout.findViewById(R.id.dayTemp8),
                dayTemp9 = layout.findViewById(R.id.dayTemp9),
                dayTemp10 = layout.findViewById(R.id.dayTemp10),
                dayTemp11 = layout.findViewById(R.id.dayTemp11),
                dayTemp12 = layout.findViewById(R.id.dayTemp12),
                dayTemp13 = layout.findViewById(R.id.dayTemp13),
                dayTemp14 = layout.findViewById(R.id.dayTemp14),
                dayTemp15 = layout.findViewById(R.id.dayTemp15),
                dayTemp16 = layout.findViewById(R.id.dayTemp16),
                dayTemp17 = layout.findViewById(R.id.dayTemp17),
                dayTemp18 = layout.findViewById(R.id.dayTemp18),
                dayTemp19 = layout.findViewById(R.id.dayTemp19),
                dayTemp20 = layout.findViewById(R.id.dayTemp20),
                dayTemp21 = layout.findViewById(R.id.dayTemp21),
                dayTemp22 = layout.findViewById(R.id.dayTemp22),
                dayTemp23 = layout.findViewById(R.id.dayTemp23),
                dayTemp24 = layout.findViewById(R.id.dayTemp24),
                dayTemp25 = layout.findViewById(R.id.dayTemp25),
                dayTemp26 = layout.findViewById(R.id.dayTemp26),
                dayTemp27 = layout.findViewById(R.id.dayTemp27),
                dayTemp28 = layout.findViewById(R.id.dayTemp28),
                dayTemp29 = layout.findViewById(R.id.dayTemp29),
                dayTemp30 = layout.findViewById(R.id.dayTemp30),
        };

        TextView[] day = {
                day18 = layout.findViewById(R.id.day18),
                day19 = layout.findViewById(R.id.day19),
                day20 = layout.findViewById(R.id.day20),
                day21 = layout.findViewById(R.id.day21),
                day22 = layout.findViewById(R.id.day22),
                day23 = layout.findViewById(R.id.day23),
                day24 = layout.findViewById(R.id.day24),
                day25 = layout.findViewById(R.id.day25),
                day26 = layout.findViewById(R.id.day26),
                day27 = layout.findViewById(R.id.day27),
                day28 = layout.findViewById(R.id.day28),
                day29 = layout.findViewById(R.id.day29),
                day30 = layout.findViewById(R.id.day30),
        };

        animals_spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //курицы
                        setIncubator("Курицы", temp, damp, day, "37,9", "37.5", "37.3", "37.0", "66", "60", "47", "70",
                                21, 4, 10, 16, 18);

                        if (chiken) {
                            arhiveIncubator(temp, damp, day, 21, 4, "Курицы");
                        }
                        break;
                    case 1:
                        setIncubator("Гуси", temp, damp, day, "38.0", "37.8", "37.6", "37.3", "65", "65", "70", "75",
                                30, 13, 1, 3, 9);
                        // гуси
                        if (geese) {
                            arhiveIncubator(temp, damp, day, 30, 13, "Гуси");
                        }
                        break;
                    case 2:
                        //индюки
                        setIncubator("Индюки", temp, damp, day, "38.0", "37.7", "37.5", "37.5", "60", "45", "65", "65",
                                28, 11, 7, 13, 25);

                        if (turkeys) {
                            arhiveIncubator(temp, damp, day, 28, 11, "Индюки");
                        }
                        break;

                    case 3:
                        //перепела
                        setIncubator("Перепела", temp, damp, day, "38.0", "37.7", "37.7", "37.5", "55", "55", "55", "75",
                                17, 0, 2, 3, 14);

                        if (quail) {
                            arhiveIncubator(temp, damp, day, 17, 0, "Перепела");
                        }

                        break;
                    case 4:
                        // утки
                        setIncubator("Утки", temp, damp, day, "38.0", "37.8", "37.8", "37.5", "75", "60", "60", "90",
                                28, 11, 6, 13, 24);
                        if (ducks) {
                            arhiveIncubator(temp, damp, day, 28, 11, "Утки");
                        }
                        break;

                }
            }
        });

        setIncubator("Курицы", temp, damp, day, "37,9", "37.5", "37.3", "37.0", "66", "60", "47", "70",
                21, 4, 10, 16, 18);

        if (chiken) {
            arhiveIncubator(temp, damp, day, 21, 4, "Курицы");
        }


        Calendar calendar = Calendar.getInstance();
        data.getEditText().setText(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));

        // Настройка календаря
        CalendarConstraints constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build();

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder)
                .setTitleText("Выберите дату закладки яиц").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // Установка сколько всего яиц
        eggAll.getEditText().setText("0");
        data.getEditText().setOnClickListener(new View.OnClickListener() {
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
                        data.getEditText().setText(formattedDate);

                    }
                });
            }
        });


        time1.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeOne = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(8)
                        .setMinute(0)
                        .setTitleText("Установите время для отправки уведомлений")
                        .build();

                timeOne.show(getActivity().getSupportFragmentManager(), "wer");

                timeOne.addOnPositiveButtonClickListener(v1 -> {
                    Calendar cale = Calendar.getInstance();
                    cale.set(Calendar.SECOND, 0);
                    cale.set(Calendar.MINUTE, timeOne.getMinute());
                    cale.set(Calendar.HOUR_OF_DAY, timeOne.getHour());

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
                    String formattedDate = format.format(cale.getTimeInMillis());
                    time1.getEditText().setText(formattedDate);
                });

            }
        });

        time2.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeTwo = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Установите время для отправки уведомлений")
                        .build();

                timeTwo.show(getActivity().getSupportFragmentManager(), "wer");

                timeTwo.addOnPositiveButtonClickListener(v1 -> {
                    Calendar cale1 = Calendar.getInstance();
                    cale1.set(Calendar.SECOND, 0);
                    cale1.set(Calendar.MINUTE, timeTwo.getMinute());
                    cale1.set(Calendar.HOUR_OF_DAY, timeTwo.getHour());

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
                    String formattedDate = format.format(cale1.getTimeInMillis());
                    time2.getEditText().setText(formattedDate);
                });
            }
        });

        time3.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeThree = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(18)
                        .setMinute(0)
                        .setTitleText("Установите время для отправки уведомлений")
                        .build();

                timeThree.show(getActivity().getSupportFragmentManager(), "wer");

                timeThree.addOnPositiveButtonClickListener(v1 -> {
                    Calendar cale2 = Calendar.getInstance();
                    cale2.set(Calendar.SECOND, 0);
                    cale2.set(Calendar.MINUTE, timeThree.getMinute());
                    cale2.set(Calendar.HOUR_OF_DAY, timeThree.getHour());

                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.UK);
                    String formattedDate = format.format(cale2.getTimeInMillis());
                    time3.getEditText().setText(formattedDate);
                });
            }
        });


        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            mydbManager.open();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin:
                beginIncubator(v);
                break;
        }
    }

    public void arhiveIncubator(TextInputLayout[] temp, TextInputLayout[] damp, TextView[] day, int days, int dayEnd, String typeBirds) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Выбрать данные из архива?");

        String[] typeId = new String[type.size()];
        type.toArray(typeId);


        ArrayList<String> arrayListName = new ArrayList();
        ArrayList<String> arrayListId = new ArrayList<>();

        for (int i = 0; i < type.size(); i++) {
            if (type.get(i).equals(typeBirds)) {
                arrayListName.add(String.valueOf(name.get(i)));
                arrayListId.add(String.valueOf(id.get(i)));
            }
        }

        String[] nameId = new String[arrayListName.size()];
        String[] idFragment = new String[arrayListId.size()];

        arrayListName.toArray(nameId);

        arrayListId.toArray(idFragment);

        final int[] n = {0};

        builder.setSingleChoiceItems(nameId, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                n[0] = which;
            }
        });

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(
                        getActivity(),
                        "Вы выбрали из архива: "
                                + nameId[n[0]],
                        Toast.LENGTH_SHORT).show();

                massId = setCursor(myDB.idIncubator(idFragment[n[0]]), 0, 13);
                massTempDamp = setCursor(myDB.idIncubatorTempDamp(idFragment[n[0]]), 1, 61);
                massOver = setCursor(myDB.idIncubatorOver(idFragment[n[0]]), 1, 31);
                massAiring = setCursor(myDB.idIncubatorOverAiring(idFragment[n[0]]), 1, 31);

                setIncubator1(massId[2], temp, damp, day, days, dayEnd);

                if (massId[7].equals("1")) {
                    friz.setChecked(true);
                }
                if (massId[4].equals("1")) {
                    over.setChecked(true);
                }
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//todo выбираем из основы
            }
        });

        builder.show();
    }


    public void setIncubator1(String type, TextInputLayout[] temp, TextInputLayout[] damp, TextView[] day,
                              int dayEnd, int dayEnd2) {

        for (int i = 0; i < temp.length; i++) {
            temp[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < damp.length; i++) {
            damp[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < day.length; i++) {
            day[i].setVisibility(View.VISIBLE);
        }

        //Установка температуры, и давления
        for (int i = 0; i < temp.length; i++) {
            temp[i].getEditText().setText(massTempDamp[i + 1]);
        }

        for (int i = 0; i < damp.length; i++) {
            damp[i].getEditText().setText(massTempDamp[i + 31]);
        }


        if (!type.equals("Гуси")) {
            // убираем видимость
            for (int i = dayEnd; i < temp.length; i++) {
                temp[i].setVisibility(View.GONE);//21 курицы, 26-28 утки, 28-30 гуси, 25-27 индюки
            }
            for (int i = dayEnd; i < damp.length; i++) {
                damp[i].setVisibility(View.GONE);
            }
            for (int i = dayEnd2; i < day.length; i++) {
                day[i].setVisibility(View.GONE);
            }
        }
    }

    public String[] setCursor(Cursor cursor, int sizeBegin, int size) {
        cursor.moveToNext();

        String[] mass = new String[size];

        for (int i = sizeBegin; i < size - 1; i++) {
            mass[i] = String.valueOf(cursor.getString(i));
        }

        cursor.close();
        return mass;
    }

    public void beginIncubator(View view) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Запустить " + name_incubator.getEditText().getText() + " ?");
        builder.setMessage("Вы уверены, что хотите запустить " + name_incubator.getEditText().getText() + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


//                for (int i = 0; i < temp.length; i++) {
//                    if (temp[i].getEditText().getText().toString().equals("")){
//                        temp[i].setError("Укажите кол-вл проветриваний");
//                        temp[i].getError();
//                    };
//
//                }
//                for (int i = 0; i < damp.length; i++) {
//                    damp[i].setVisibility(View.VISIBLE);
//                }
//                if (airingInput.getEditText().getText().toString().equals("")) {
//                    airingInput.setError("Укажите кол-вл проветриваний");
//                    airingInput.getError();
//
//                } else {
//                    myDB.updateIncubator(massTempDamp, massId[0]);
//                    myDB.updateIncubatorOver(massOver, massId[0]);
//                    myDB.updateIncubatorAiring(massAiring, massId[0]);
//                }

                String friz1 = "1";
                String over1 = "1";
                if (friz.isChecked()) {
                    friz1 = "0";
                }

                if (over.isChecked()) {
                    over1 = "0";
                }

                mydbManager.insertToDbIncubator(name_incubator.getEditText().getText().toString(), animals_spiner.getText().toString(), data.getEditText().getText().toString(), over1, eggAll.getEditText().getText().toString(), eggAll.getEditText().getText().toString(), friz1,
                        "0", "0", String.valueOf(time1.getEditText().getText()), String.valueOf(time2.getEditText().getText()), String.valueOf(time3.getEditText().getText()));

                mydbManager.insertToDbIncubatorTempDamp(dayTemp1.getEditText().getText().toString(), dayTemp2.getEditText().getText().toString(), dayTemp3.getEditText().getText().toString(), dayTemp4.getEditText().getText().toString(), dayTemp5.getEditText().getText().toString(), dayTemp6.getEditText().getText().toString(), dayTemp7.getEditText().getText().toString(), dayTemp8.getEditText().getText().toString(), dayTemp9.getEditText().getText().toString(), dayTemp10.getEditText().getText().toString(), dayTemp11.getEditText().getText().toString(), dayTemp12.getEditText().getText().toString(), dayTemp13.getEditText().getText().toString(), dayTemp14.getEditText().getText().toString(), dayTemp15.getEditText().getText().toString(), dayTemp16.getEditText().getText().toString(), dayTemp17.getEditText().getText().toString(), dayTemp18.getEditText().getText().toString(), dayTemp19.getEditText().getText().toString(), dayTemp20.getEditText().getText().toString(), dayTemp21.getEditText().getText().toString(), dayTemp22.getEditText().getText().toString(), dayTemp23.getEditText().getText().toString(), dayTemp24.getEditText().getText().toString(), dayTemp25.getEditText().getText().toString(), dayTemp26.getEditText().getText().toString(), dayTemp27.getEditText().getText().toString(), dayTemp28.getEditText().getText().toString(), dayTemp29.getEditText().getText().toString(), dayTemp30.getEditText().getText().toString(),
                        dayDamp1.getEditText().getText().toString(), dayDamp2.getEditText().getText().toString(), dayDamp3.getEditText().getText().toString(), dayDamp4.getEditText().getText().toString(), dayDamp5.getEditText().getText().toString(), dayDamp6.getEditText().getText().toString(), dayDamp7.getEditText().getText().toString(), dayDamp8.getEditText().getText().toString(), dayDamp9.getEditText().getText().toString(), dayDamp10.getEditText().getText().toString(), dayDamp11.getEditText().getText().toString(), dayDamp12.getEditText().getText().toString(), dayDamp13.getEditText().getText().toString(), dayDamp15.getEditText().getText().toString(), dayDamp15.getEditText().getText().toString(), dayDamp16.getEditText().getText().toString(), dayDamp17.getEditText().getText().toString(), dayDamp18.getEditText().getText().toString(), dayDamp19.getEditText().getText().toString(), dayDamp20.getEditText().getText().toString(), dayDamp21.getEditText().getText().toString(), dayDamp22.getEditText().getText().toString(), dayDamp23.getEditText().getText().toString(), dayDamp24.getEditText().getText().toString(), dayDamp25.getEditText().getText().toString(), dayDamp26.getEditText().getText().toString(), dayDamp27.getEditText().getText().toString(), dayDamp28.getEditText().getText().toString(), dayDamp29.getEditText().getText().toString(), dayDamp30.getEditText().getText().toString());

                setAiring(animals_spiner.getText().toString());
                setOver(animals_spiner.getText().toString());
                mInterstitialAd = new InterstitialAd(getActivity());
                mInterstitialAd.setAdUnitId("R-M-2139251-4");

                final AdRequest adRequest = new AdRequest.Builder().build();

                mInterstitialAd.loadAd(adRequest);
                mInterstitialAd.setInterstitialAdEventListener(new InterstitialAdEventListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {

                    }

                    @Override
                    public void onAdShown() {

                    }

                    @Override
                    public void onAdDismissed() {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onLeftApplication() {

                    }

                    @Override
                    public void onReturnedToApplication() {

                    }

                    @Override
                    public void onImpression(@Nullable ImpressionData impressionData) {

                    }
                });

                addChart();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void setAiring(String type) {
        if (massId != null) {
            if (friz.isChecked()) {
                mydbManager.insertToDbIncubatorAiring("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            } else if (massId[6].equals("1")) {
                mydbManager.insertToDbIncubatorAiring(massAiring[1], massAiring[2], massAiring[3], massAiring[4], massAiring[5], massAiring[6], massAiring[7], massAiring[8], massAiring[9], massAiring[10], massAiring[11], massAiring[12], massAiring[13], massAiring[14], massAiring[15], massAiring[16], massAiring[17], massAiring[18], massAiring[19], massAiring[20], massAiring[21], massAiring[22], massAiring[23], massAiring[24], massAiring[25], massAiring[26], massAiring[27], massAiring[28], massAiring[29], massAiring[30]);
            } else {
                mydbManager.insertToDbIncubatorAiring("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            }
        } else {
            if (!friz.isChecked()) {
                switch (type) {
                    case "Курицы":
                        mydbManager.insertToDbIncubatorAiring("нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 20 мин", "2 раза по 20 мин", "2 раза по 5 мин", "2 раза по 5 мин", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                        break;
                    case "Гуси":
                        mydbManager.insertToDbIncubatorAiring("нет", "1 раз по 20 мин", "1 раз по 20 мин", "1 раз по 20 мин", "1 раз по 20 мин", "2 раз по 20 мин", "2 раз по 20 мин", "2 раз по 20 мин", "2 раз по 20 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин", "3 раза по 45 мин");
                        break;
                    case "Перепела":
                        mydbManager.insertToDbIncubatorAiring("нет", "нет", "нет", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "1 раз по 5 мин", "нет", "нет", "нет", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                        break;
                    case "Утки":
                        mydbManager.insertToDbIncubatorAiring("нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "нет", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 20 мин", "2 раза по 20 мин", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 15 мин", "2 раза по 15 мин", "нет", "нет", "нет", "0", "0");
                        break;
                    case "Индюшки":
                        mydbManager.insertToDbIncubatorAiring("нет", "нет", "нет", "нет", "нет", "нет", "нет", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "2 раза по 5 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "4 раза по 10 мин", "нет", "нет", "нет", "0", "0");
                        break;

                }
            } else {
                mydbManager.insertToDbIncubatorAiring("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            }
        }
    }


    public void setOver(String type) {
        if (massId != null) {
            if (over.isChecked()) {
                mydbManager.insertToDbIncubatorOver("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            } else if (massId[7].equals("1")) {
                mydbManager.insertToDbIncubatorOver(massOver[1], massOver[2], massOver[3], massOver[4], massOver[5], massOver[6], massOver[7], massOver[8], massOver[9], massOver[10], massOver[11], massOver[12], massOver[13], massOver[14], massOver[15], massOver[16], massOver[17], massOver[18], massOver[19], massOver[20], massOver[21], massOver[22], massOver[23], massOver[24], massOver[25], massOver[26], massOver[27], massOver[28], massOver[29], massOver[30]);
            } else {
                mydbManager.insertToDbIncubatorOver("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            }
        } else {
            if (!over.isChecked()) {
                switch (type) {
                    case "Курицы":
                        mydbManager.insertToDbIncubatorOver("2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "2-3", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                        break;
                    case "Гуси":
                        mydbManager.insertToDbIncubatorOver("3-4", "6", "6", "6", "6", "6", "6", "6", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "0", "0", "0");
                        break;
                    case "Перепела":
                        mydbManager.insertToDbIncubatorOver("3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "3-6", "нет", "нет", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                        break;
                    case "Утки":
                        mydbManager.insertToDbIncubatorOver("4", "4", "4", "4", "4", "4", "4", "4-6", "4-6", "4-6", "4-6", "4-6", "4-6", "4-6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "нет", "нет", "нет", "0", "0");
                        break;
                    case "Индюшки":
                        mydbManager.insertToDbIncubatorOver("6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "6", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "нет", "нет", "нет", "0", "0");
                        break;
                }
            } else {
                mydbManager.insertToDbIncubatorOver("Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Авто", "Автон", "Авто", "Авто", "Авто", "Авто", "Авто");
            }
        }
    }

    public void setIncubator(String type, TextInputLayout[] temp, TextInputLayout[] damp, TextView[] day,
                             String temp1, String temp2, String temp3, String temp4, String danm1, String damp2, String damp3, String damp4,

                             int dayEnd, int dayEnd2, int day1, int day2, int day3) {
        for (int i = 0; i < temp.length; i++) {
            temp[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < damp.length; i++) {
            damp[i].setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < day.length; i++) {
            day[i].setVisibility(View.VISIBLE);
        }

        //Установка температуры, и давления
        for (int i = 0; i < temp.length; i++) {
            if (i <= day1) {
                temp[i].getEditText().setText(temp1);
            } else if (i >= day1 + 1 && i <= day2) {
                temp[i].getEditText().setText(temp2);
            } else if (i >= day2 + 1 && i <= day3) {
                temp[i].getEditText().setText(temp3);
            } else {
                temp[i].getEditText().setText(temp4);
            }
        }

        for (int i = 0; i < damp.length; i++) {
            if (i <= day1) {
                damp[i].getEditText().setText(danm1);
            } else if (i >= day1 + 1 && i <= day2) {
                damp[i].getEditText().setText(damp2);
            } else if (i >= day2 + 1 && i <= day3) {
                damp[i].getEditText().setText(damp3);
            } else {
                damp[i].getEditText().setText(damp4);
            }
        }


        if (!type.equals("Гуси")) {
            // убираем видимость
            for (int i = dayEnd; i < temp.length; i++) {
                temp[i].setVisibility(View.GONE);//21 курицы, 26-28 утки, 28-30 гуси, 25-27 индюки
            }
            for (int i = dayEnd; i < damp.length; i++) {
                damp[i].setVisibility(View.GONE);
            }
            for (int i = dayEnd2; i < day.length; i++) {
                day[i].setVisibility(View.GONE);
            }
        }
    }


    public void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataIncubator();
        if (cursor.getCount() == 0) {
            cursor.close();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(8).equals("1")) {
                    id.add(cursor.getString(0));
                    name.add(cursor.getString(1));
                    type.add(cursor.getString(2));

                    if (cursor.getString(2).equals("Курицы")) {
                        chiken = true;
                    } else if (cursor.getString(2).equals("Индюки")) {
                        turkeys = true;
                    } else if (cursor.getString(2).equals("Гуси")) {
                        geese = true;
                    } else if (cursor.getString(2).equals("Утки")) {
                        ducks = true;
                    } else if (cursor.getString(2).equals("Перепела")) {
                        quail = true;
                    }
                }
            }
            cursor.close();
        }
    }

    public void addChart() {

        IncubatorMenuFragment incubatorMenuFragment = new IncubatorMenuFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, incubatorMenuFragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

}

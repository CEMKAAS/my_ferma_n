package com.hfad.myferma.incubator;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NowIncubatorFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout addEdit, menu;
    private long diff;
    private TextView name, dataNow, tempNow, dampNow, overturnNow, airingNow, ovoskopNow, tempTomorrow, dampTomorrow, overturnTomorrow, airingTomorrow, ovoskopTomorrow, tomorrow;

    private MyFermaDatabaseHelper myDB;

    private String[] massId, massTempDamp, massOver, massAiring;

    private String dateBefore222;

    private int day;
    private Button foto, editDayIncubator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_icubator_now, container, false);

        String nameFragment = null, dataFragment = null,  idFragment = null;

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.delete).setVisible(false);
        appBar.setTitle("Мои инкубатор");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            nameFragment = bundle.getString("name");
            dataFragment = bundle.getString("data");
            idFragment = bundle.getString("id");
        }

        myDB = new MyFermaDatabaseHelper(getActivity());
        name = layout.findViewById(R.id.name);
        dataNow = layout.findViewById(R.id.data_now);
        tempNow = layout.findViewById(R.id.temp_now);
        dampNow = layout.findViewById(R.id.damp_now);
        overturnNow = layout.findViewById(R.id.overturn_now);
        airingNow = layout.findViewById(R.id.airing_now);
        ovoskopNow = layout.findViewById(R.id.ovoskop_now);
        tempTomorrow = layout.findViewById(R.id.temp_tomorrow);
        dampTomorrow = layout.findViewById(R.id.damp_tomorrow);
        overturnTomorrow = layout.findViewById(R.id.overturn_tomorrow);
        airingTomorrow = layout.findViewById(R.id.airing_tomorrow);
        ovoskopTomorrow = layout.findViewById(R.id.ovoskop_tomorrow);
        tomorrow = layout.findViewById(R.id.tomorrow);
        addEdit = layout.findViewById(R.id.add_edit);
        addEdit.setVisibility(View.GONE);

        //Кнопочки
        foto = (Button) layout.findViewById(R.id.add_button);
        foto.setOnClickListener(this);

        editDayIncubator = (Button) layout.findViewById(R.id.addChart_button);
        editDayIncubator.setOnClickListener(this);

        Button editIncubator = (Button) layout.findViewById(R.id.dayDamp1);
        editIncubator.setOnClickListener(this);

        Button endIncubator = (Button) layout.findViewById(R.id.end);
        endIncubator.setOnClickListener(this);
        // Скрываем временно не нужные кнопки
        ovoskopNow.setVisibility(View.GONE);
        ovoskopTomorrow.setVisibility(View.GONE);

        // Фаб кнопочка
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);
        // Настройка даты
        Calendar calendar = Calendar.getInstance();
        dateBefore222 = calendar.get(Calendar.DAY_OF_MONTH) + 1 + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);
//        String dateBefore2222 = "28.04.2023";
        SimpleDateFormat myFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date1 = myFormat.parse(dataFragment);
            Date date2 = myFormat.parse(dateBefore222);
            diff = date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Установка дня
        day = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        //Загружаем информацию с массивы
        massId = setCursor(myDB.idIncubator(idFragment), 0, 13);
        massTempDamp = setCursor(myDB.idIncubatorTempDamp(idFragment), 1, 61);
        massOver = setCursor(myDB.idIncubatorOver(idFragment), 1, 31);
        massAiring = setCursor(myDB.idIncubatorOverAiring(idFragment), 1, 31);

        // Установка отображения инкубатора
        name.setText(nameFragment);
        dataNow.setText("Идет " + day + " день ");
        tempNow.setText("Температура должна быть " + massTempDamp[day] + " °C");
        dampNow.setText("Влажность должна быть " + massTempDamp[30 + day] + " %");

        // установка переварота
        setOverturn(overturnNow, day);

        // установка проветривания
        setAiring(airingNow, day);

        //уставнока меню на завтра
        setMenuTomorrow(day);

        //установка овоскопа
        setOvoskop(day);

        //установка если день закончился
        setEnd(day);

        return layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end:
                endInc();
                break;

            case R.id.addChart_button:
                addChart(new editDayIncubatorFragment());
                break;

            case R.id.dayDamp1:
                addChart(new AllListFragment());
                break;

            case  R.id.add_button:
                addChart(new FotoIncubatorFragment());
                break;
        }
    }

    //Установка курсоров
    public String[] setCursor(Cursor cursor, int sizeBegin, int size) {
        cursor.moveToNext();

        String[] mass = new String[size];

        for (int i = sizeBegin; i < size; i++) {
            mass[i] = String.valueOf(cursor.getString(i));
        }

        cursor.close();
        return mass;
    }

    //настройка меню на завтра
    public void setMenuTomorrow(int day) {

        if (massId[2].equals("Курицы") && day == 21) {
            setGone();
        } else if (massId[2].equals("Индюки") && day == 28) {
            setGone();
        } else if (massId[2].equals("Гуси") && day == 30) {
            setGone();
        } else if (massId[2].equals("Утки") && day == 28) {
            setGone();
        } else if (massId[2].equals("Перепела") && day == 17) {
            setGone();
        } else {
            tempTomorrow.setText("Температура должна быть " + massTempDamp[1 + day] + " °C");
            dampTomorrow.setText("Влажность должна быть " + massTempDamp[31 + day] + " %");
            setAiring(airingTomorrow, day + 1);
            setOverturn(overturnTomorrow, day + 1);
            ovoskopTomorrow.setVisibility(View.GONE);
        }
    }

    //установка переворота
    public void setOverturn(TextView overturn, int day) {
        if (massOver[day].equals("нет")) {
            overturn.setText("Переворачивать не нужно");
        } else if (massOver[day].equals("Авто")) {
            overturn.setText("Переворот автоматический");
        } else {
            overturn.setText("Переворачивать нужно " + massOver[day] + " раз");
        }
    }

    //установка проветривания
    public void setAiring(TextView airing, int day) {
        if (massAiring[day].equals("нет")) {
            airing.setText("Провертивать не нужно");
        } else if (massAiring[day].equals("Авто")) {
            airing.setText("Провертривание автоматическое");
        } else {
            airing.setText("Провертивать нужно " + massAiring[day]);
        }
    }

    //утсановка видимости на завтра
    public void setGone() {
        tempTomorrow.setVisibility(View.GONE);
        dampTomorrow.setVisibility(View.GONE);
        overturnTomorrow.setVisibility(View.GONE);
        airingTomorrow.setVisibility(View.GONE);
        ovoskopTomorrow.setVisibility(View.GONE);
        tomorrow.setVisibility(View.GONE);
    }

    public boolean setVisibileOvoskop() {
        ovoskopNow.setVisibility(View.VISIBLE);
        return true;
    }


    // Проверяем закончилось или нет
    public void setEnd(int day) {
        if (massId[2].equals("Курицы") && day > 21) {
            beginIncubator();
        } else if (massId[2].equals("Индюки") && day > 28) {
            beginIncubator();
        } else if (massId[2].equals("Гуси") && day > 30) {
            beginIncubator();
        } else if (massId[2].equals("Утки") && day > 28) {
            beginIncubator();
        } else if (massId[2].equals("Перепела") && day > 17) {
            beginIncubator();
        }
    }

    // Завершаем работу инкубатора принудительно
    public void endInc() {
        if (massId[2].equals("Курицы") && day <= 21) {
            beginEndIncubator();
        } else if (massId[2].equals("Индюки") && day <= 28) {
            beginEndIncubator();
        } else if (massId[2].equals("Гуси") && day <= 30) {
            beginEndIncubator();
        } else if (massId[2].equals("Утки") && day <= 28) {
            beginEndIncubator();
        } else if (massId[2].equals("Перепела") && day <= 17) {
            beginEndIncubator();
        } else {
            setEnd(day); //todo
        }

    }

    public void beginEndIncubator() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Завершить " + name.getText() + " ?");
        builder.setMessage("Вы уверены, что хотите завершить " + name.getText() + " ?\n" +
                "Еще слишком рано завершать, удалим или добавим в архив?");
        builder.setPositiveButton("В архив", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                massId[8] = "1";
                massId[9] = dateBefore222;
                myDB.updateIncubatorTO(massId);
                myDB.updateIncubator(massTempDamp, massId[0]);
                myDB.updateIncubatorOver(massOver, massId[0]);
                myDB.updateIncubatorAiring(massAiring, massId[0]);
                addChart(new IncubatorMenuFragment());
            }
        });
        builder.setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                myDB.deleteOneRowIncubator(massId[0]);
                addChart(new IncubatorMenuFragment());

            }
        });
        builder.create().show();
    }

    public void beginIncubator() {
        tempNow.setVisibility(View.GONE);
        dampNow.setVisibility(View.GONE);
        overturnNow.setVisibility(View.GONE);
        airingNow.setVisibility(View.GONE);
        ovoskopNow.setVisibility(View.GONE);
        foto.setVisibility(View.GONE);
        editDayIncubator.setVisibility(View.GONE);
        tempTomorrow.setVisibility(View.GONE);
        dampTomorrow.setVisibility(View.GONE);
        overturnTomorrow.setVisibility(View.GONE);
        airingTomorrow.setVisibility(View.GONE);
        tomorrow.setVisibility(View.GONE);

        ovoskopTomorrow.setVisibility(View.VISIBLE);
        addEdit.setVisibility(View.VISIBLE);
        dataNow.setText("Поздравляем!");
        ovoskopTomorrow.setText("Вы заложили " + massId[4] + " яиц\nСколько птенцов у Вас вылупилось?");
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Поздравлем с появлением птенцов!");
        builder.setMessage("Мы сохранили Ваши данные в архив, чтобы вы не забыли параметры!\n Укажите кол-во птенцов прежде чем завершать");
        builder.setPositiveButton("Завершить!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                massId[6] = addEdit.getEditText().getText().toString();
                massId[8] = "1";
                massId[9] = dateBefore222;
                myDB.updateIncubatorTO(massId);
                myDB.updateIncubator(massTempDamp, massId[0]);
                myDB.updateIncubatorOver(massOver, massId[0]);
                myDB.updateIncubatorAiring(massAiring, massId[0]);
                addChart(new IncubatorMenuFragment());

            }
        });

        builder.setNegativeButton("Внести птенцов", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.create().show();
    }

    public void addChart(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("data", day);
        bundle.putString("type", massId[2]);
        bundle.putStringArray("id", massId);
        bundle.putStringArray("tempDamp", massTempDamp);
        bundle.putStringArray("over", massOver);
        bundle.putStringArray("airing", massAiring);


        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
        fragment.setArguments(bundle);
    }

    //установка овоскопа
    public void setOvoskop(int day) {
        //todo доделать овоскоп
        if (massId[2].equals("Курицы")) {
            switch (day) {
                case 6:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    setVisibileOvoskop();
                    break;
                case 10:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 11:
                    setVisibileOvoskop();
                    break;
                case 15:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 16:
                    setVisibileOvoskop();
                    break;
            }
        } else if (massId[2].equals("Индюки")) {
            switch (day) {
                case 7:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    setVisibileOvoskop();
                    break;
                case 13:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 14:
                    setVisibileOvoskop();
                    break;
                case 24:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 25:
                    setVisibileOvoskop();
                    break;
            }
        } else if (massId[2].equals("Гуси")) {
            switch (day) {
                case 8:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    setVisibileOvoskop();
                    break;
                case 14:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 15:
                    setVisibileOvoskop();
                    break;
                case 20:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 21:
                    setVisibileOvoskop();
                    break;
            }
        } else if (massId[2].equals("Утки")) {
            switch (day) {
                case 7:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    setVisibileOvoskop();
                    break;
                case 14:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 15:
                    setVisibileOvoskop();
                    break;
                case 25:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 26:
                    setVisibileOvoskop();
                    break;
            }
        } else if (massId[2].equals("Перепела")) {
            switch (day) {
                case 5:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    setVisibileOvoskop();
                    break;
                case 12:
                    ovoskopTomorrow.setVisibility(View.VISIBLE);
                    break;
                case 13:
                    setVisibileOvoskop();
                    break;
            }
        } else {
            ovoskopNow.setVisibility(View.GONE);
            ovoskopTomorrow.setVisibility(View.GONE);
        }

    }


}
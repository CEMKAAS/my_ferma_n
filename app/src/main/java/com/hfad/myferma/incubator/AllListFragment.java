package com.hfad.myferma.incubator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AllListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextInputLayout name, size, time1, time2, time3;
    private Button update_button;
    private MaterialTimePicker timeOne, timeTwo, timeThree;
    private String[] massId, massTempDamp, massOver, massAiring;
    private int dataFragment;
    private MyFermaDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_all_list, container, false);
        myDB = new MyFermaDatabaseHelper(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataFragment = bundle.getInt("data");
            massId = bundle.getStringArray("id");
            massTempDamp = bundle.getStringArray("tempDamp");
            massOver = bundle.getStringArray("over");
            massAiring = bundle.getStringArray("airing");
        }

        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.setTitle("Мои Инкубатор");
        appBar.getMenu().findItem(R.id.delete).setVisible(false);

        name = layout.findViewById(R.id.name_incubator);
        size = layout.findViewById(R.id.eggAll);
        time1 = layout.findViewById(R.id.time1);
        time2 = layout.findViewById(R.id.time2);
        time3 = layout.findViewById(R.id.time3);

        name.getEditText().setText(massId[1]);
        size.getEditText().setText(massId[4]);
        time1.getEditText().setText(massId[10]);
        time2.getEditText().setText(massId[11]);
        time3.getEditText().setText(massId[12]);
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

        recyclerView = layout.findViewById(R.id.recyclerView);

        ListAdapterIncubator listAdapterIncubator = new ListAdapterIncubator(massId, massTempDamp, massOver, massAiring);
        recyclerView.setAdapter(listAdapterIncubator);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapterIncubator.setListener(new ListAdapterIncubator.Listener() {
            @Override
            public void onClick(int position, int day) {
                addChart(new editDayIncubatorFragment(), day);
            }
        });

        update_button = layout.findViewById(R.id.update_button);
        update_button.setOnClickListener(this);

        return layout;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_button:
                upDate(v);
                break;
        }
    }

    public void upDate(View view) {

        massId[1] = name.getEditText().getText().toString().trim();
        massId[4] = size.getEditText().getText().toString().trim().replaceAll("\\D+", "");

        massId[10] = time1.getEditText().getText().toString();
        massId[11] = time2.getEditText().getText().toString();
        massId[12] = time3.getEditText().getText().toString();

        //убираем ошибку
        name.setErrorEnabled(false);

        //вывод ошибки
        if (name.getEditText().getText().toString().equals("")) {
            if (name.getEditText().getText().toString().equals("")) {
                name.setError("Укажите температуру!");
                name.getError();
            }
        } else {
            myDB.updateIncubatorTO(massId);
        }
    }


    public void addChart(Fragment fragment, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("data", day);
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
}

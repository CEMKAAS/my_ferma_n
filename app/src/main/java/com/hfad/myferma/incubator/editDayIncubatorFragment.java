package com.hfad.myferma.incubator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

public class editDayIncubatorFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout tempInput, dampInput, overInput, airingInput;
    private Button update_button;
    private TextView textUnit;
    private MydbManagerMetod mydbManager;
    private String[] massId, massTempDamp, massOver, massAiring;
    private int dataFragment;
    private String idFragment, tempFragment, dampFragment, overFragment, airingFragment;
    private  MyFermaDatabaseHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_edit_day_incubator, container, false);
        myDB = new MyFermaDatabaseHelper(getActivity());
        MaterialToolbar appBar = getActivity().findViewById(R.id.topAppBar);
        appBar.getMenu().findItem(R.id.delete).setVisible(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataFragment = bundle.getInt("data");
            massId = bundle.getStringArray("id");
            massTempDamp = bundle.getStringArray("tempDamp");
            massOver = bundle.getStringArray("over");
            massAiring = bundle.getStringArray("airing");
        }

        textUnit = layout.findViewById(R.id.text_unit);
        tempInput = layout.findViewById(R.id.discAdd_input);
        dampInput = layout.findViewById(R.id.dayAdd_input);
        overInput = layout.findViewById(R.id.mountAdd_input);
        airingInput = layout.findViewById(R.id.yearAdd_input);

        textUnit.setText("День " + String.valueOf(dataFragment));
        tempInput.getEditText().setText(massTempDamp[dataFragment]);
        dampInput.getEditText().setText(massTempDamp[dataFragment+30]);
        overInput.getEditText().setText(massOver[dataFragment]);
        airingInput.getEditText().setText(massAiring[dataFragment]);

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

        massTempDamp[dataFragment] = tempInput.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
        massTempDamp[dataFragment+30] = dampInput.getEditText().getText().toString().trim().replaceAll("\\D+", "");
        massOver[dataFragment] = overInput.getEditText().getText().toString().trim();
        massAiring[dataFragment] = airingInput.getEditText().getText().toString().trim();

        //убираем ошибку
        tempInput.setErrorEnabled(false);
        dampInput.setErrorEnabled(false);
        overInput.setErrorEnabled(false);
        airingInput.setErrorEnabled(false);

        //вывод ошибки
        if (tempInput.getEditText().getText().toString().equals("") || dampInput.getEditText().getText().toString().equals("") ||
                overInput.getEditText().getText().toString().equals("") || airingInput.getEditText().getText().toString().equals("")) {
            if (tempInput.getEditText().getText().toString().equals("")) {
                tempInput.setError("Укажите температуру!");
                tempInput.getError();
            }
            if (dampInput.getEditText().getText().toString().equals("")) {
                dampInput.setError("Укажите влажность");
                dampInput.getError();
            }
            if (overInput.getEditText().getText().toString().equals("")) {
                overInput.setError("Укажите кол-во переворотов");
                overInput.getError();
            }
            if (airingInput.getEditText().getText().toString().equals("")) {
                airingInput.setError("Укажите кол-вл проветриваний");
                airingInput.getError();
            }
        } else {
            myDB.updateIncubator(massTempDamp, massId[0]);
            myDB.updateIncubatorOver(massOver, massId[0]);
            myDB.updateIncubatorAiring(massAiring, massId[0]);
        }
    }
}

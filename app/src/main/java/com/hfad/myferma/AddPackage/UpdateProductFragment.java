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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.NowArhiveFragment;

public class UpdateProductFragment extends Fragment {

    private TextView textUnit;

    private TextInputLayout titleExpenses, titleCount, titleData, titlePrice;

    private Button updateButton, deleteButton;

    private AutoCompleteTextView writeOffSpiner;

    public UpdateProductFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_update_product, container, false);

        textUnit = layout.findViewById(R.id.text_unit);

        titleExpenses = layout.findViewById(R.id.tilleExpenses_input);
        titleCount = layout.findViewById(R.id.titleSale_input);
        titleData = layout.findViewById(R.id.daySale_input);
        titlePrice = layout.findViewById(R.id.priceEdit_input);
        writeOffSpiner = layout.findViewById(R.id.writeOff_spiner);

        updateButton = layout.findViewById(R.id.update_button);
        deleteButton = layout.findViewById(R.id.delete_button);

        return layout;
    }

    void delete() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Удалить " + title + " ?");
        builder.setMessage("Вы уверены, что хотите удалить " + title + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateActivityAdd.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


}
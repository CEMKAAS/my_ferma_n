package com.hfad.myferma;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private MyFermaDatabaseHelper myDB;
    private TextInputLayout menuProducts;
    private AutoCompleteTextView addProduct;
    private List<String> arrayListProduct;
    private ArrayAdapter<String> arrayAdapterProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        addProduct = (AutoCompleteTextView) layout.findViewById(R.id.add_product);
        menuProducts = layout.findViewById(R.id.menuProduct);

        arrayListProduct = new ArrayList<>();

        //Подключение к базе данных
        myDB = new MyFermaDatabaseHelper(getActivity());

        //Убираем Фаб
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) getActivity().findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);

        // устнановка кнопок
        Button addProductButton = (Button) layout.findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(this);

        Button deleteProductButton = (Button) layout.findViewById(R.id.delete_product_button);
        deleteProductButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            add();
            arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListProduct);
            addProduct.setAdapter(arrayAdapterProduct);
        }
    }

    //Добавление значий в лист из БД
    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        while (cursor.moveToNext()) {
            String product = cursor.getString(1);
            arrayListProduct.add(product);
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_product_button:
                setAddProduct();
                break;
            case R.id.delete_product_button:
                deleteProduct();
                break;
        }
    }

    public void setAddProduct() {
        String nameProduct = addProduct.getText().toString();
        menuProducts.setErrorEnabled(false);
        if (arrayListProduct.size() <= 7) {
            if (!arrayListProduct.contains(nameProduct)) {
                arrayListProduct.add(nameProduct);
                arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListProduct);
                addProduct.setAdapter(arrayAdapterProduct);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("Добавить товар "  + nameProduct + " ?");
                builder.setMessage("Добавленный товар " + nameProduct + " вы сможете добавлять на склад, продавать и списывать!");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Вы добавили товар " + nameProduct, Toast.LENGTH_SHORT).show();
                        myDB.insertDataProduct(addProduct.getText().toString(), 0);
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            } else {
                menuProducts.setError("Такой товар уже есть");
                menuProducts.getError();
            }
        } else {
            menuProducts.setError("Всего можно установить 10 товаров, вы превысили лимит!");
            menuProducts.getError();
        }
    }


    public void deleteProduct() {
        String nameProduct = addProduct.getText().toString();
        menuProducts.setErrorEnabled(false);
            if (arrayListProduct.contains(nameProduct)) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("Удалить товар " + addProduct.getText().toString()+ " ?");
                builder.setMessage("Товар " + addProduct.getText().toString() + " больше не будет отображаться на складе, его нельзя будет добавить, продать или списать. Данные внесенные в журнал будут отображаться.");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Вы удалили товар ", Toast.LENGTH_SHORT).show();

                        //Удаляем из списка
                        arrayListProduct.remove(nameProduct);
                        arrayAdapterProduct = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayListProduct);
                        addProduct.setAdapter(arrayAdapterProduct);
                        // удаляем из БД
                        myDB.deleteOneRowProduct(addProduct.getText().toString());
                        // чистим поле
                        addProduct.getText().clear();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            } else {
                menuProducts.setError("Такого товара нет");
                menuProducts.getError();
            }
        }
}
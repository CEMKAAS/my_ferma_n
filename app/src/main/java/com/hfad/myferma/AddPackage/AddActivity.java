package com.hfad.myferma.AddPackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddActivity extends AppCompatActivity {

//    private AutoCompleteTextView animals_spiner, animals_spiner2, animals_spiner3;
    private RecyclerView recyclerView;
    ;
    private ImageView empty_imageview;
    private TextView no_data;

    private MyFermaDatabaseHelper myDB;
    private ArrayList<String> id, title, disc, day, mount, year;
    private CustomAdapterAdd customAdapterAdd;

    private int mount1 = 0;

    private ArrayAdapter<String> arrayAdapterAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        recyclerView = findViewById(R.id.recyclerView);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        myDB = new MyFermaDatabaseHelper(AddActivity.this);
        id = new ArrayList<>();
        title = new ArrayList<>();
        disc = new ArrayList<>();
        day = new ArrayList<>();
        mount = new ArrayList<>();
        year = new ArrayList<>();

        no_data = findViewById(R.id.no_data);

        storeDataInArrays();
//        customAdapterAdd = new CustomAdapterAdd(AddActivity.this, this, id, title, disc,
//                day, mount, year);
//        recyclerView.setAdapter(customAdapterAdd);
//        recyclerView.setLayoutManager(new LinearLayoutManager(AddActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    public ArrayList<String> add() {
        Set<String> tempList = new HashSet<>();
        Cursor cursor = myDB.readAllData();

        while (cursor.moveToNext()) {
            String string1 = cursor.getString(5);
            tempList.add(string1);
        }
        cursor.close();

        ArrayList<String> tempList1 = new ArrayList<>();
        for (String nameExpenses : tempList) {
            tempList1.add(nameExpenses);
        }

        return tempList1;
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            cursor.moveToLast();
            id.add(cursor.getString(0));
            title.add(cursor.getString(1));
            disc.add(cursor.getString(2));
            day.add(cursor.getString(3));
            mount.add(cursor.getString(4));
            year.add(cursor.getString(5));
            while (cursor.moveToPrevious()){
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                disc.add(cursor.getString(2));
                day.add(cursor.getString(3));
                mount.add(cursor.getString(4));
                year.add(cursor.getString(5));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }


//    void storeDataInArrays() {
//        Cursor cursor = myDB.readAllData();
//        String mountString = String.valueOf(animals_spiner2.getText());
//        setMount(mountString);
//        if (cursor.getCount() == 0) {
//            empty_imageview.setVisibility(View.VISIBLE);
//            no_data.setVisibility(View.VISIBLE);
//        } else {
//            cursor.moveToLast();
//            if (animals_spiner.getText().toString().equals("Все")) {
//                addAllArrays(cursor, animals_spiner3.getText().toString());
//            } else {
//                addArrays(cursor, animals_spiner3.getText().toString());
//            }
//            while (cursor.moveToPrevious()) {
//                //показыват только что добавленное значение
//                if (animals_spiner.getText().toString().equals("Все")) {
//                    addAllArrays(cursor, animals_spiner3.getText().toString());
//                } else {
//                    addArrays(cursor, animals_spiner3.getText().toString());
//                }
//            }
//            cursor.close();
//
//
//
//            empty_imageview.setVisibility(View.GONE);
//            no_data.setVisibility(View.GONE);
//        }
//    }


//    public void addAllArrays(Cursor cursor, String year2) {
//        if (mount1 <= 12 && mount1 > 0) {
//            //проверка месяца
//            if (mount1 == Integer.parseInt(cursor.getString(4))) {
//                //проверка года
//                if (year2.equals(cursor.getString(5))) {
//                    id.add(cursor.getString(0));
//                    title.add(cursor.getString(1));
//                    disc.add(cursor.getString(2));
//                    day.add(cursor.getString(3));
//                    mount.add(cursor.getString(4));
//                    year.add(cursor.getString(5));
//                }
//            }
//        } else if (mount1 == 13) {
//            if (year2.equals(cursor.getString(5))) {
//                id.add(cursor.getString(0));
//                title.add(cursor.getString(1));
//                disc.add(cursor.getString(2));
//                day.add(cursor.getString(3));
//                mount.add(cursor.getString(4));
//                year.add(cursor.getString(5));
//            }
//        }
//    }
//
//
//    public void addArrays(Cursor cursor, String year2) {
//
//        if (mount1 <= 12 && mount1 > 0) {
//            if (animals_spiner.getText().toString().equals(cursor.getString(1))) {
//                //проверка месяца
//                if (mount1 == Integer.parseInt(cursor.getString(4))) {
//                    //проверка года
//                    if (year2.equals(cursor.getString(5))) {
//                        id.add(cursor.getString(0));
//                        title.add(cursor.getString(1));
//                        disc.add(cursor.getString(2));
//                        day.add(cursor.getString(3));
//                        mount.add(cursor.getString(4));
//                        year.add(cursor.getString(5));
//                    }
//                }
//            }
//        } else if (mount1 == 13) {
//            if (animals_spiner.getText().toString().equals(cursor.getString(1))) {
//                if (year2.equals(cursor.getString(5))) {
//                    id.add(cursor.getString(0));
//                    title.add(cursor.getString(1));
//                    disc.add(cursor.getString(2));
//                    day.add(cursor.getString(3));
//                    mount.add(cursor.getString(4));
//                    year.add(cursor.getString(5));
//                }
//            }
//        }
//    }


    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(AddActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(AddActivity.this, AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void setMount(String mountString) {
        switch (mountString) {
            case "Январь":
                mount1 = 1;
                break;
            case "Февраль":
                mount1 = 2;
                break;
            case "Март":
                mount1 = 3;
                break;
            case "Апрель":
                mount1 = 4;
                break;
            case "Май":
                mount1 = 5;
                break;
            case "Июнь":
                mount1 = 6;
                break;
            case "Июль":
                mount1 = 7;
                break;
            case "Август":
                mount1 = 8;
                break;
            case "Сентябрь":
                mount1 = 9;
                break;
            case "Октябрь":
                mount1 = 10;
                break;
            case "Ноябрь":
                mount1 = 11;
                break;
            case "Декабрь":
                mount1 = 12;
                break;
            case "За весь год":
                mount1 = 13;
                break;
        }
    }

}


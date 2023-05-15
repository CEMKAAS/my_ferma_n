package com.hfad.myferma.SalePackage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

import java.util.ArrayList;

public class SaleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView empty_imageview;
    private TextView no_data;
    private MyFermaDatabaseHelper myDB;
    private ArrayList<String> id, title, disc, day, mount, year, price;
    private CustomAdapterSale customAdapterSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        recyclerView = findViewById(R.id.recyclerView1);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        myDB = new MyFermaDatabaseHelper(SaleActivity.this);
        id = new ArrayList<>();
        title = new ArrayList<>();
        disc = new ArrayList<>();
        day = new ArrayList<>();
        mount = new ArrayList<>();
        year = new ArrayList<>();
        price = new ArrayList<>();
        storeDataInArrays();

        customAdapterSale = new CustomAdapterSale(SaleActivity.this, this, id, title, disc,
                day, mount, year, price);
        recyclerView.setAdapter(customAdapterSale);
        recyclerView.setLayoutManager(new LinearLayoutManager(SaleActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }


    void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataSale();
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
            price.add(cursor.getString(6));
            while (cursor.moveToPrevious()) {
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                disc.add(cursor.getString(2));
                day.add(cursor.getString(3));
                mount.add(cursor.getString(4));
                year.add(cursor.getString(5));
                price.add(cursor.getString(6));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

}

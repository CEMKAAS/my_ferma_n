package com.hfad.myferma.ExpensesPackage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.db.MyFermaDatabaseHelper;

public class UpdateActivityExpenses extends AppCompatActivity {

    private TextInputLayout title_input, disc_input, day_input, mount_input, year_input ;
    private Button update_button, delete_button;

    private String id, title, disc, day, mount, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expenses);

        title_input = findViewById(R.id.tilleExpenses_input);
        disc_input = findViewById(R.id.discExpenses_input);
        day_input = findViewById(R.id.dayExpenses_input);
        mount_input = findViewById(R.id.mountExpenses_input);
        year_input = findViewById(R.id.yearExpenses_input);

        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateActivityExpenses.this);
                title = title_input.getEditText().getText().toString().trim();
                disc = disc_input.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
                day = day_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");
                mount = mount_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");
                year = year_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");

                //убираем ошибку
                title_input.setErrorEnabled(false);
                disc_input.setErrorEnabled(false);
                day_input.setErrorEnabled(false);
                mount_input.setErrorEnabled(false);
                year_input.setErrorEnabled(false);

                //вывод ошибки
                if (title.equals("") || disc.equals("") || day.equals("") || mount.equals("") || year.equals("")) {
                    if (title.equals("")) {
                        title_input.setError("Введите товар!");
                        title_input.getError();
                    }
                    if (disc.equals("")) {
                        disc_input.setError("Введите цену!");
                        disc_input.getError();
                    }
                    if (day.equals("")) {
                        day_input.setError("Введите день!");
                        day_input.getError();
                    }
                    if (mount.equals("")) {
                        mount_input.setError("Введите месяц!");
                        mount_input.getError();
                    }
                    if (year.equals("")) {
                        year_input.setError("Введите год!");
                        year_input.getError();
                    }
                } else {
                    myDB.updateDataExpenses(id, title, disc, day, mount, year);
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }


    void getAndSetIntentData() {
        if (getIntent().hasExtra("disc") && getIntent().hasExtra("day")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            disc = getIntent().getStringExtra("disc");
            day = getIntent().getStringExtra("day");
            mount = getIntent().getStringExtra("mount");
            year = getIntent().getStringExtra("year");

            //Setting Intent Data
            title_input.getEditText().setText(title);
            disc_input.getEditText().setText(disc);
            day_input.getEditText().setText(day);
            mount_input.getEditText().setText(mount);
            year_input.getEditText().setText(year);
            Log.d("stev", title + " " + disc + " " + day + " " + mount + " " + year);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить " + title + " ?");
        builder.setMessage("Вы уверены, что хотите удалить " + title + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateActivityExpenses.this);
                myDB.deleteOneRowExpenses(id);//todo
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

}

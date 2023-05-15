package com.hfad.myferma.WriteOff;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hfad.myferma.R;
import com.hfad.myferma.SalePackage.UpdateActivitySale;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.db.MydbManagerMetod;

public class UpdateWriteOffActivity extends AppCompatActivity {


    private TextInputLayout disc_input, day_input, mount_input, year_input, menu;// TODO
    private Button update_button, delete_button;
    private TextView textUnit;
    private MydbManagerMetod mydbManager;

    private AutoCompleteTextView status_spiner;
    private String id, title, disc, day, mount, year, status;

    private int statusDrawable = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_write_off);

        mydbManager = new MydbManagerMetod(this);

        textUnit = findViewById(R.id.text_unit);
        disc_input = findViewById(R.id.titleSale_input);
        day_input = findViewById(R.id.daySale_input);
        mount_input = findViewById(R.id.mountSale_input);
        year_input = findViewById(R.id.yearSale_input);

        menu = (TextInputLayout) findViewById(R.id.menu);
        status_spiner = (AutoCompleteTextView) findViewById(R.id.animals_spiner); //todo

        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
//        status_spiner.setText("", false);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
        suffix();
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateWriteOffActivity.this);
                title = textUnit.getText().toString().trim();
                disc = disc_input.getEditText().getText().toString().trim().replaceAll(",", ".").replaceAll("[^\\d.]", "");
                day = day_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");
                mount = mount_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");
                year = year_input.getEditText().getText().toString().trim().replaceAll("\\D+", "");
                status = status_spiner.getText().toString().trim();
                if (status.equals("На собсвенные нужды")) {
                    statusDrawable = R.drawable.baseline_cottage_24;
                } else {
                    statusDrawable = R.drawable.baseline_delete_24;
                }


                //убираем ошибку
                disc_input.setErrorEnabled(false);
                day_input.setErrorEnabled(false);
                mount_input.setErrorEnabled(false);
                year_input.setErrorEnabled(false);
                //вывод ошибки
                if (disc.equals("") || day.equals("") || mount.equals("") || year.equals("")) {
                    if (disc.equals("")) {
                        disc_input.setError("Введите кол-во!");
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
                    // TODO
                } else {
                    if (mydbManager.sumSale(title) - Double.valueOf(disc) < 0) {
                        if (sumEgg()){
                            disc_input.setError("Яйца не могут быть дробными...");
                            disc_input.getError();
                        }
                        disc_input.setError("Нет столько товара на складе");
                        disc_input.getError();
                    } else if (sumEgg()) {
                        disc_input.setError("Яйца не могут быть дробными...");
                        disc_input.getError();
                    } else {
                        disc.replaceAll(",", ".").replaceAll("[^\\d.]", "");
                        myDB.updateDataWriteOff(id, title, disc, day, mount, year, statusDrawable);
                    }
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSale();
            }
        });

    }

    //Проверяем есть ли запятая или нет в яйцах
    public boolean sumEgg() {
        if (title.equals("Яйца")) {
            if (disc.contains(".") || disc.contains(",")) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mydbManager.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mydbManager.closeDb();
    }

    public void suffix() {
        if (title.equals("Яйца")) {
            disc_input.setSuffixText("шт.");
        } else if (title.equals("Молоко")) {
            disc_input.setSuffixText("л.");
        } else if (title.equals("Мясо")) {
            disc_input.setSuffixText("кг.");
        }
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
            status = getIntent().getStringExtra("status");
            statusDrawable = Integer.valueOf(status);

            //Setting Intent Data
            textUnit.setText(title);
            disc_input.getEditText().setText(disc);
            day_input.getEditText().setText(day);
            mount_input.getEditText().setText(mount);
            year_input.getEditText().setText(year);

            if (statusDrawable == R.drawable.baseline_cottage_24) {
                status_spiner.setText("На собсвенные нужды", false);
            } else {
                status_spiner.setText("На утилизацию", false);
            }

            Log.d("stev", title + " " + disc + " " + day + " " + mount + " " + year);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteSale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить " + title + " ?");
        builder.setMessage("Вы уверены, что хотите удалить " + title + " ?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyFermaDatabaseHelper myDB = new MyFermaDatabaseHelper(UpdateWriteOffActivity.this);
                myDB.deleteOneRowWriteOff(id);
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
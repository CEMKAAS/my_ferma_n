package com.hfad.myferma;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.content.ContentValues.TAG;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.hfad.myferma.AddPackage.AddActivity;
import com.hfad.myferma.AddPackage.AddFragment;
import com.hfad.myferma.ExpensesPackage.ExpensesActivity;
import com.hfad.myferma.ExpensesPackage.ExpensesFragment;
import com.hfad.myferma.Finance.FinanceFragment;
import com.hfad.myferma.Finance.PriceFragment;
import com.hfad.myferma.SalePackage.SaleActivity;
import com.hfad.myferma.SalePackage.SaleFragment;
import com.hfad.myferma.WriteOff.WriteOffFragment;
import com.hfad.myferma.databinding.ActivityMain2Binding;
import com.hfad.myferma.db.MyFermaDatabaseHelper;
import com.hfad.myferma.incubator.IncubatorMenuFragment;
import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.interstitial.InterstitialAd;
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private ActivityMain2Binding binding; //показывает фрагменты
    private BannerAdView mBannerAdView;//Реклама от Яндекса

    private PendingIntent pendingIntent; //Уведомление

    private MaterialToolbar appBar;
    private ExtendedFloatingActionButton fab; //Кнопка вездесущ
    private Boolean isAllFabsVisible;

    private ArrayList time1, time2, time3;

    private MyFermaDatabaseHelper myDB;

    private InterstitialAd mInterstitialAd;
    private int position = 0;

    //    WriteOffFragment writeOffFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appBar = findViewById(R.id.topAppBar);


        if (savedInstanceState == null) {  //при повороте приложение не брасывается
            replaceFragment(new WarehouseFragment());
            appBar.setTitle("Мой Склад");// При включении включает данный фрагмент
        }

        fab = findViewById(R.id.extended_fab);
        fab.setVisibility(View.GONE);
        isAllFabsVisible = false;

        myDB = new MyFermaDatabaseHelper(this);

        time1 = new ArrayList<>();
        time2 = new ArrayList<>();
        time3 = new ArrayList<>();


        //Назначение кнопок на нижней навигации
        binding.navView.setOnNavigationItemSelectedListener(item -> {
            position = item.getItemId();
            switch (position) {

                case R.id.warehouse_button:
                    replaceFragment(new WarehouseFragment());
                    appBar.setTitle("Мой Склад");
                    fab.hide();
                    fab.setVisibility(View.GONE);
                    break;

                case R.id.finance_button:
                    replaceFragment(new FinanceFragment());
                    appBar.setTitle("Мои Финансы");
                    fab.show();
                    fab.setText("Цена");
                    fab.setIconResource(R.drawable.ic_action_price);
                    fab.getIcon();

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replaceFragment(new PriceFragment());
                            appBar.setTitle("Мои Финансы");
                            fab.hide();
                            fab.setVisibility(View.GONE);
                        }
                    });
                    break;

                case R.id.add_button:
                    replaceFragment(new AddFragment());
                    appBar.setTitle("Мои Товар");
                    fba(AddActivity.class);
                    break;

                case R.id.sale_button:
                    replaceFragment(new SaleFragment());
                    appBar.setTitle("Мой Продажи");
                    fba(SaleActivity.class);
                    break;

                case R.id.expenses_button:
                    replaceFragment(new ExpensesFragment());
                    appBar.setTitle("Мои Покупки");
                    fba(ExpensesActivity.class);
                    break;

            }
            return true;
        });


        //Реклама от яндекса
//        mBannerAdView = (BannerAdView) findViewById(R.id.banner_ad_view);
//        mBannerAdView.setAdUnitId("R-M-2139251-1"); //Вставляется свой айди от яндекса
//        mBannerAdView.setAdSize(AdSize.stickySize(320));//Размер банера
//        final AdRequest adRequest = new AdRequest.Builder().build();
//        mBannerAdView.loadAd(adRequest);

        // AppBar

        appBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.more:
                    replaceFragment(new InfoFragment());
                    appBar.setTitle("Информация");
                    fab.hide();
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.setting:
                    replaceFragment(new SettingsFragment());
                    appBar.setTitle("Мои настройки");
                    fab.hide();
                    fab.setVisibility(View.GONE);
                    break;
                case R.id.delete:
                    beginIncubator();

            }
            return true;
        });

       appBar.getMenu().findItem(R.id.delete).setVisible(false);


        //убираем ботом навигацию и фабкнопку при вызове клавиатуры
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d(TAG, "onVisibilityChanged: Keyboard visibility changed");
                        if (isOpen) {
                            Log.d(TAG, "onVisibilityChanged: Keyboard is open");
                            binding.navView.setVisibility(View.GONE);
//                            fab.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Invisible");
                        } else {
                            Log.d(TAG, "onVisibilityChanged: Keyboard is closed");
                            binding.navView.setVisibility(View.VISIBLE);
//                            fab.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onVisibilityChanged: NavBar got Visible");
                        }
                    }
                });


        if (getFragmentManager().findFragmentByTag("WriteOff") != null && getFragmentManager().findFragmentByTag("WriteOff").isVisible()) {
            fba(WriteOffFragment.class);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("visible_fragment");
                if (fragment instanceof WarehouseFragment) {
                    appBar.setTitle("Мой Склад");
                    position = 0;
                    fab.setVisibility(View.GONE);
                    appBar.setNavigationIcon(null);
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                }
                if (fragment instanceof FinanceFragment) {
                    position = 1;
                    appBar.setTitle("Мои Финансы");
                    fab.show();
                    fab.setText("Цена");
                    fab.setIconResource(R.drawable.ic_action_price);
                    fab.getIcon();
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof PriceFragment) {
                    appBar.setTitle("Мои Финансы");
                    position = 1;
                    fab.hide();
                    fab.setVisibility(View.GONE);
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof AddFragment) {
                    appBar.setTitle("Мои Товары");
                    fbaShowBack();
                    position = 2;
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof SaleFragment) {
                    appBar.setTitle("Мои Продажи");
                    fbaShowBack();
                    position = 3;
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof ExpensesFragment) {
                    appBar.setTitle("Мои Покупки");
                    fbaShowBack();
                    position = 4;
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof InfoFragment) {
                    appBar.setTitle("Информация");
                    fab.hide();
                    fab.setVisibility(View.GONE);
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                }
                if (fragment instanceof WriteOffFragment) {
                    appBar.setTitle("Мои Списания");
                    fbaShowBack();
                    position = 0;
                    appBar.getMenu().findItem(R.id.delete).setVisible(false);
                    appBar.setNavigationIcon(null);
                }
                if (fragment instanceof IncubatorMenuFragment) {
                    fbaShowBackIncubator();
                    position = 0;
                }
                binding.navView.getMenu().getItem(position).setChecked(true);
            }
        }
        );

        // Добавляем основые продукты, если приложение открыто впервые
        add();

        // рекламав
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("R-M-2139251-3");
//
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setInterstitialAdEventListener(new InterstitialAdEventListener() {
//            @Override
//            public void onAdLoaded() {
//                mInterstitialAd.show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
//
//            }
//
//            @Override
//            public void onAdShown() {
//
//            }
//
//            @Override
//            public void onAdDismissed() {
//
//            }
//
//            @Override
//            public void onAdClicked() {
//
//            }
//
//            @Override
//            public void onLeftApplication() {
//
//            }
//
//            @Override
//            public void onReturnedToApplication() {
//
//            }
//
//            @Override
//            public void onImpression(@Nullable ImpressionData impressionData) {
//
//            }
//        });


    }


    //Выводит приложение из сна для уведомления
    @Override
    protected void onStart() {
        super.onStart();

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        storeDataInArrays();

        Date dat = new Date();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);

        Calendar cal_alarm = Calendar.getInstance();

        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, 20);
        cal_alarm.set(Calendar.MINUTE, 0);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, FLAG_IMMUTABLE);
        manager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);

        sda(time1);
        sda(time2);
        sda(time3);
    }

    public void add() {
        Cursor cursor = myDB.readAllDataProduct();

        if (cursor.getCount() == 0){
            // Добавить в список
            myDB.insertDataProduct("Яйца", 1);
            myDB.insertDataProduct("Молоко", 0);
            myDB.insertDataProduct("Мясо", 0);

            // Добавить в добавление
            myDB.insertToDb("Яйца",0);
            myDB.insertToDb("Молоко",0);
            myDB.insertToDb("Мясо",0);

            // Добавить в продажи
            myDB.insertToDbSale("Яйца", 0, 0);
            myDB.insertToDbSale("Молоко", 0, 0);
            myDB.insertToDbSale("Мясо", 0, 0);

        }

        cursor.close();
    }

    public void sda(ArrayList time) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);

        Calendar cal_alarm = Calendar.getInstance();

        for (int i = 0; i < time.size(); i++) {
            if (time.get(i).equals("0") || time.get(i).equals("")) {
                continue;
            } else {
                String[] time11 = String.valueOf(time.get(i)).split(":");
                cal_alarm.setTime(dat);
                cal_alarm.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time11[0]));
                cal_alarm.set(Calendar.MINUTE, Integer.parseInt(time11[1]));
                cal_alarm.set(Calendar.SECOND, 0);

                if (cal_alarm.before(cal_now)) {
                    cal_alarm.add(Calendar.DATE, 1);
                }

                Intent myIntent1 = new Intent(MainActivity.this, AlarmIncubator.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent1, FLAG_IMMUTABLE);
                manager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
            }
        }
    }


    public void storeDataInArrays() {
        Cursor cursor = myDB.readAllDataIncubator();
        if (cursor.getCount() == 0) {
            cursor.close();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(8).equals("0")) {
                    time1.add(cursor.getString(10));
                    time2.add(cursor.getString(11));
                    time3.add(cursor.getString(12));
                }
            }
            cursor.close();
        }
    }


    //Переход на фрагменты
//    private void replaceFragment(Fragment fragment) {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.conteiner, fragment, "visible_fragment");
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
//    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteiner, fragment, "visible_fragment")
                .addToBackStack(null)
                .commit();
    }

    //Закрытие клавиатуры после нажатия на кнопку
    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //Сворачивание клавиатуры при нажатие на любую часть экрана
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

    // gпеоеключение значков FBA
    public void fba(Class clas) {

        fab.show();
        fab.setText("Журнал");
        fab.setIconResource(R.drawable.ic_action_book);
        fab.getIcon();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, clas);
                startActivity(intent);
            }
        });

    }

    public void beginIncubator() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Удаляем ?");
        builder.setMessage("Вы уверены, что хотите удалить все инкубаторы, включая архивные?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteAllIncubator();
                replaceFragment(new WarehouseFragment());
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }




    // показывать фаб кнопку при нажатие назад
    public void fbaShowBack() {
        fab.show();
        fab.setText("Журнал");
        fab.setIconResource(R.drawable.ic_action_book);
        fab.getIcon();
    }

    // показывать фаб кнопку при нажатие назад
    public void fbaShowBackIncubator() {
        fab.show();
        fab.setText("Добавить");
        fab.setIconResource(R.drawable.baseline_add_24);
        fab.getIcon();
    }
}
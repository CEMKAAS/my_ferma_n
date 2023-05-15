package com.hfad.myferma.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MydbManager {
    private Context context;
    private MyFermaDatabaseHelper myFermaData;
    private SQLiteDatabase db;

    public MydbManager(Context context) {
        this.context = context;
    }


    // Открывает базу даных
    public MydbManager open() throws SQLException {
        myFermaData = new MyFermaDatabaseHelper(context);
        db = myFermaData.getWritableDatabase();
        return this;
    }

    public void closeDb() {
        myFermaData.close();
    }


    public Cursor fetchAllTAble1data() {
        return db.query(MyConstanta.TABLE_NAME, null, null, null, null, null, null);
    }


    public Cursor fetchAllTable2data() {
        return db.query(MyConstanta.TABLE_NAMESALE, null, null, null, null, null, null);
    }

    public void deleteTable(String tablename) {
        db.execSQL("drop table if exists " + tablename + ';');
    }

    public void createIndividualTable(String query) {
        db.execSQL(query);
    }


    //    Кладет в саписок покупок
    public void insertToDb(String title, double disc) {
        ContentValues cv = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        double priceAdd = disc * price(title);
        cv.put(MyConstanta.TITLE, title);
        cv.put(MyConstanta.DISCROTION, disc);
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1);
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR));
        cv.put(MyConstanta.PRICEALL, priceAdd);
        db.insert(MyConstanta.TABLE_NAME, null, cv);
    }

    //    Кладет в список трат
    public void insertToDbSale(String title, double disc, double priceIndi) {
        Calendar calendar = Calendar.getInstance();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLESale, title);
        cv.put(MyConstanta.DISCROTIONSale, disc);
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1);
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR));
        cv.put(MyConstanta.PRICEALL, priceIndi);
        db.insert(MyConstanta.TABLE_NAMESALE, null, cv);
    }

    //    Кладет в список покупок
    public void insertToDbExpenses(String title, double disc) {
        ContentValues cv = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        cv.put(MyConstanta.TITLEEXPENSES, title);
        cv.put(MyConstanta.DISCROTIONEXPENSES, disc);
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1);
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR));
        db.insert(MyConstanta.TABLE_NAMEEXPENSES, null, cv);
    }

    //    Назначается цена
    public void insertToDbPrice(String title, int disc) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEPRISE, title);
        cv.put(MyConstanta.DISCROTIONPRICE, disc);
        db.insert(MyConstanta.TABLE_NAMEPRICE, null, cv);
    }

    public void insertToDbWriteOff(String title, double disc, int status) {
        ContentValues cv = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        cv.put(MyConstanta.TITLEWRITEOFF, title);
        cv.put(MyConstanta.DISCROTIONSWRITEOFF, disc);
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1);
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR));
        cv.put(MyConstanta.STASTUSWRITEOFF, status);
        db.insert(MyConstanta.TABLE_NAMEWRITEOFF, null, cv);
    }

    //    Назначается цена
    public void insertToDbProduct(String title, int status) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEPRODUCT, title);
        cv.put(MyConstanta.STATUSPRODUCT, status);
        db.insert(MyConstanta.TABLE_NAMEPRODUCT, null, cv);
    }


    //    Выдает список добавленных
    @SuppressLint("Range")
    public List<Double> getFromDb(String string) {
        List<Double> tempList = new ArrayList<>();
        Cursor cursorSale = db.query(MyConstanta.TABLE_NAME, null, null, null, null, null, null);

        while (cursorSale.moveToNext()) {
            String string1 = cursorSale.getString(cursorSale.getColumnIndex(MyConstanta.TITLE));
            if (string1.equals(string)) {
                double title = cursorSale.getDouble(cursorSale.getColumnIndex(MyConstanta.DISCROTION));
                tempList.add(title);
            }
        }
        cursorSale.close();
        return tempList;
    }

    //    Выдает список трат
    @SuppressLint("Range")
    public List<Double> getFromDbSale(String string) {
        List<Double> tempList = new ArrayList<>();
        Cursor cursorSale = db.query(MyConstanta.TABLE_NAMESALE, null, null, null, null, null, null);

        while (cursorSale.moveToNext()) {
            String string1 = cursorSale.getString(cursorSale.getColumnIndex(MyConstanta.TITLESale));
            if (string1.equals(string)) {
                double title = cursorSale.getDouble(cursorSale.getColumnIndex(MyConstanta.DISCROTIONSale));
                tempList.add(title);
            }
        }
        cursorSale.close();
        return tempList;
    }

    //    Выдает список определенных купленых товаров
    @SuppressLint("Range")
    public List<Integer> getFromDbExpenses(String string) {
        List<Integer> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEEXPENSES, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String string1 = cursor.getString(cursor.getColumnIndex(MyConstanta.TITLEEXPENSES));
            if (string1.equals(string)) {
                int title = cursor.getInt(cursor.getColumnIndex(MyConstanta.DISCROTIONEXPENSES));
                tempList.add(title);
            }
        }
        cursor.close();
        return tempList;
    }


    //    Выдает список наименований товаров
    @SuppressLint("Range")
    public ArrayList<String> getFromDbNameExpenses() {
        Set<String> tempList = new HashSet<>();
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEEXPENSES, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String string1 = cursor.getString(cursor.getColumnIndex(MyConstanta.TITLEEXPENSES));
            tempList.add(string1);
        }
        cursor.close();

        ArrayList<String> tempList1 = new ArrayList<>();
        for (String nameExpenses : tempList) {
            tempList1.add(nameExpenses);
        }

        return tempList1;
    }


    // выдает список всех проданых товаров
    @SuppressLint("Range")
    public List<Double> getFromDbAllExpenses() {
        List<Double> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEEXPENSES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            double title = cursor.getDouble(cursor.getColumnIndex(MyConstanta.DISCROTIONEXPENSES));
            tempList.add(title);
        }
        cursor.close();
        return tempList;
    }

    @SuppressLint("Range")
    public int price(String string) {
        int priceUnit = 0;
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEPRICE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String string1 = cursor.getString(cursor.getColumnIndex(MyConstanta.TITLEPRISE));
            if (string1.equals(string)) {
                priceUnit = cursor.getInt(cursor.getColumnIndex(MyConstanta.DISCROTIONPRICE));
            }
        }
        cursor.close();
        return priceUnit;
    }

    @SuppressLint("Range")
    public List<Integer> getFromDbAllPrice(String string) {
        List<Integer> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEPRICE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String string1 = cursor.getString(cursor.getColumnIndex(MyConstanta.TITLEPRISE));
            if (string1.equals(string)) {
                int title = cursor.getInt(cursor.getColumnIndex(MyConstanta.DISCROTIONPRICE));
                tempList.add(title);
            }
        }
        cursor.close();
        return tempList;
    }

    // Проверка была ли установена цена на 1 ед. товара, если нету, то true
    @SuppressLint("Range")
    public boolean getFromBooleanPrice(String string) {
        String string1 = null;
        Cursor cursor = db.query(MyConstanta.TABLE_NAMEPRICE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            string1 = cursor.getString(cursor.getColumnIndex(MyConstanta.TITLEPRISE));
            if (string1.equals(string)) {
                break;
            }
            continue;
        }

        cursor.close();


        if (string1 == null) {
            return true;
        } else if (string1.equals(string)) {
            return false;
        } else {
            return true;
        }
    }

    //Обновление цена на 1 ед. товара
    public void updateEgg(String name, int price) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DISCROTIONPRICE, price);
        try {
            db.update(MyConstanta.TABLE_NAMEPRICE, cv, MyConstanta.TITLEPRISE + " = ?", new String[]{name});
        } catch (SQLiteException e) {
        }
    }


    // Выдает список цены добавленных
    @SuppressLint("Range")
    public List<Integer> getFromDbPrice(String string) {
        List<Integer> tempList = new ArrayList<>();
        Cursor cursorSale = db.query(MyConstanta.TABLE_NAME, null, null, null, null, null, null);

        while (cursorSale.moveToNext()) {
            String string1 = cursorSale.getString(cursorSale.getColumnIndex(MyConstanta.TITLE));
            if (string1.equals(string)) {
                int title = cursorSale.getInt(cursorSale.getColumnIndex(MyConstanta.PRICEALL));
                tempList.add(title);
            }
        }
        cursorSale.close();
        return tempList;
    }

    //    Выдает cписок цены по продуктам проданным
    @SuppressLint("Range")
    public List<Double> getFromDbSalePrice(String string) {
        List<Double> tempList = new ArrayList<>();
        Cursor cursorSale = db.query(MyConstanta.TABLE_NAMESALE, null, null, null, null, null, null);

        while (cursorSale.moveToNext()) {
            String string1 = cursorSale.getString(cursorSale.getColumnIndex(MyConstanta.TITLESale));
            if (string1.equals(string)) {
                double title = cursorSale.getDouble(cursorSale.getColumnIndex(MyConstanta.PRICEALL));
                tempList.add(title);
            }
        }
        cursorSale.close();
        return tempList;
    }

    //    Выдает список добавленных на списание
    @SuppressLint("Range")
    public List<Double> getFromDbWriteOff(String string) {
        List<Double> tempList = new ArrayList<>();
        Cursor cursorWriteOff = db.query(MyConstanta.TABLE_NAMEWRITEOFF, null, null, null, null, null, null);

        while (cursorWriteOff.moveToNext()) {
            String string1 = cursorWriteOff.getString(cursorWriteOff.getColumnIndex(MyConstanta.TITLEWRITEOFF));
            if (string1.equals(string)) {
                double title = cursorWriteOff.getDouble(cursorWriteOff.getColumnIndex(MyConstanta.DISCROTIONSWRITEOFF));
                tempList.add(title);
            }
        }
        cursorWriteOff.close();
        return tempList;
    }



    public void insertToDbIncubatorTempDamp(String dayTemp1, String dayTemp2, String dayTemp3, String dayTemp4, String dayTemp5, String dayTemp6, String dayTemp7, String dayTemp8, String dayTemp9, String dayTemp10, String dayTemp11, String dayTemp12, String dayTemp13, String dayTemp14, String dayTemp15, String dayTemp16, String dayTemp17, String dayTemp18, String dayTemp19, String dayTemp20, String dayTemp21, String dayTemp22, String dayTemp23, String dayTemp24, String dayTemp25, String dayTemp26, String dayTemp27, String dayTemp28, String dayTemp29, String dayTemp30,
                                            String dayDamp1, String dayDamp2, String dayDamp3, String dayDamp4, String dayDamp5, String dayDamp6, String dayDamp7, String dayDamp8, String dayDamp9, String dayDamp10, String dayDamp11, String dayDamp12, String dayDamp13, String dayDamp14, String dayDamp15, String dayDamp16, String dayDamp17, String dayDamp18, String dayDamp19, String dayDamp20, String dayDamp21, String dayDamp22, String dayDamp23, String dayDamp24, String dayDamp25, String dayDamp26, String dayDamp27, String dayDamp28, String dayDamp29, String dayDamp30) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYTEMP1, dayTemp1);
        cv.put(MyConstanta.DAYTEMP2, dayTemp2);
        cv.put(MyConstanta.DAYTEMP3, dayTemp3);
        cv.put(MyConstanta.DAYTEMP4, dayTemp4);
        cv.put(MyConstanta.DAYTEMP5, dayTemp5);
        cv.put(MyConstanta.DAYTEMP6, dayTemp6);
        cv.put(MyConstanta.DAYTEMP7, dayTemp7);
        cv.put(MyConstanta.DAYTEMP8, dayTemp8);
        cv.put(MyConstanta.DAYTEMP9, dayTemp9);
        cv.put(MyConstanta.DAYTEMP10, dayTemp10);
        cv.put(MyConstanta.DAYTEMP11, dayTemp11);
        cv.put(MyConstanta.DAYTEMP12, dayTemp12);
        cv.put(MyConstanta.DAYTEMP13, dayTemp13);
        cv.put(MyConstanta.DAYTEMP14, dayTemp14);
        cv.put(MyConstanta.DAYTEMP15, dayTemp15);
        cv.put(MyConstanta.DAYTEMP16, dayTemp16);
        cv.put(MyConstanta.DAYTEMP17, dayTemp17);
        cv.put(MyConstanta.DAYTEMP18, dayTemp18);
        cv.put(MyConstanta.DAYTEMP19, dayTemp19);
        cv.put(MyConstanta.DAYTEMP20, dayTemp20);
        cv.put(MyConstanta.DAYTEMP21, dayTemp21);
        cv.put(MyConstanta.DAYTEMP22, dayTemp22);
        cv.put(MyConstanta.DAYTEMP23, dayTemp23);
        cv.put(MyConstanta.DAYTEMP24, dayTemp24);
        cv.put(MyConstanta.DAYTEMP25, dayTemp25);
        cv.put(MyConstanta.DAYTEMP26, dayTemp26);
        cv.put(MyConstanta.DAYTEMP27, dayTemp27);
        cv.put(MyConstanta.DAYTEMP28, dayTemp28);
        cv.put(MyConstanta.DAYTEMP29, dayTemp29);
        cv.put(MyConstanta.DAYTEMP30, dayTemp30);
        cv.put(MyConstanta.DAYDAMP1, dayDamp1);
        cv.put(MyConstanta.DAYDAMP2, dayDamp2);
        cv.put(MyConstanta.DAYDAMP3, dayDamp3);
        cv.put(MyConstanta.DAYDAMP4, dayDamp4);
        cv.put(MyConstanta.DAYDAMP5, dayDamp5);
        cv.put(MyConstanta.DAYDAMP6, dayDamp6);
        cv.put(MyConstanta.DAYDAMP7, dayDamp7);
        cv.put(MyConstanta.DAYDAMP8, dayDamp8);
        cv.put(MyConstanta.DAYDAMP9, dayDamp9);
        cv.put(MyConstanta.DAYDAMP10, dayDamp10);
        cv.put(MyConstanta.DAYDAMP11, dayDamp11);
        cv.put(MyConstanta.DAYDAMP12, dayDamp12);
        cv.put(MyConstanta.DAYDAMP13, dayDamp13);
        cv.put(MyConstanta.DAYDAMP14, dayDamp14);
        cv.put(MyConstanta.DAYDAMP15, dayDamp15);
        cv.put(MyConstanta.DAYDAMP16, dayDamp16);
        cv.put(MyConstanta.DAYDAMP17, dayDamp17);
        cv.put(MyConstanta.DAYDAMP18, dayDamp18);
        cv.put(MyConstanta.DAYDAMP19, dayDamp19);
        cv.put(MyConstanta.DAYDAMP20, dayDamp20);
        cv.put(MyConstanta.DAYDAMP21, dayDamp21);
        cv.put(MyConstanta.DAYDAMP22, dayDamp22);
        cv.put(MyConstanta.DAYDAMP23, dayDamp23);
        cv.put(MyConstanta.DAYDAMP24, dayDamp24);
        cv.put(MyConstanta.DAYDAMP25, dayDamp25);
        cv.put(MyConstanta.DAYDAMP26, dayDamp26);
        cv.put(MyConstanta.DAYDAMP27, dayDamp27);
        cv.put(MyConstanta.DAYDAMP28, dayDamp28);
        cv.put(MyConstanta.DAYDAMP29, dayDamp29);
        cv.put(MyConstanta.DAYDAMP29, dayDamp30);
        db.insert(MyConstanta.TABLE_INCUBATORTEMPDAMP,null, cv);
    }

    public void insertToDbIncubator(String name, String type, String data, String overturn, String eggAll,
                                    String eggEndAll, String airing, String arhive, String dataEnd, String timePush1, String timePush2, String timePush3) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.NAMEINCUBATOR, name);
        cv.put(MyConstanta.TYPEINCUBATOR, type);
        cv.put(MyConstanta.DATAINCUBATOR, data);
        cv.put(MyConstanta.OVERTURNINCUBATOR, overturn);
        cv.put(MyConstanta.EGGALL, eggAll);
        cv.put(MyConstanta.EGGALLEND, eggEndAll);
        cv.put(MyConstanta.AIRING, airing);
        cv.put(MyConstanta.ARHIVE, arhive);
        cv.put(MyConstanta.DATAEND, dataEnd);
        cv.put(MyConstanta.TIMEPUSH1,timePush1 );
        cv.put(MyConstanta.TIMEPUSH2, timePush2);
        cv.put(MyConstanta.TIMEPUSH3,timePush3);

        db.insert(MyConstanta.TABLE_INCUBATOR, null, cv);
    }

    public void insertToDbIncubatorOver(String dayTemp1, String dayTemp2, String dayTemp3, String dayTemp4, String dayTemp5, String dayTemp6, String dayTemp7, String dayTemp8, String dayTemp9, String dayTemp10, String dayTemp11, String dayTemp12, String dayTemp13, String dayTemp14, String dayTemp15, String dayTemp16, String dayTemp17, String dayTemp18, String dayTemp19, String dayTemp20, String dayTemp21, String dayTemp22, String dayTemp23, String dayTemp24, String dayTemp25, String dayTemp26, String dayTemp27, String dayTemp28, String dayTemp29, String dayTemp30) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYOVERTURN1, dayTemp1);
        cv.put(MyConstanta.DAYOVERTURN2, dayTemp2);
        cv.put(MyConstanta.DAYOVERTURN3, dayTemp3);
        cv.put(MyConstanta.DAYOVERTURN4, dayTemp4);
        cv.put(MyConstanta.DAYOVERTURN5, dayTemp5);
        cv.put(MyConstanta.DAYOVERTURN6, dayTemp6);
        cv.put(MyConstanta.DAYOVERTURN7, dayTemp7);
        cv.put(MyConstanta.DAYOVERTURN8, dayTemp8);
        cv.put(MyConstanta.DAYOVERTURN9, dayTemp9);
        cv.put(MyConstanta.DAYOVERTURN10, dayTemp10);
        cv.put(MyConstanta.DAYOVERTURN11, dayTemp11);
        cv.put(MyConstanta.DAYOVERTURN12, dayTemp12);
        cv.put(MyConstanta.DAYOVERTURN13, dayTemp13);
        cv.put(MyConstanta.DAYOVERTURN14, dayTemp14);
        cv.put(MyConstanta.DAYOVERTURN15, dayTemp15);
        cv.put(MyConstanta.DAYOVERTURN16, dayTemp16);
        cv.put(MyConstanta.DAYOVERTURN17, dayTemp17);
        cv.put(MyConstanta.DAYOVERTURN18, dayTemp18);
        cv.put(MyConstanta.DAYOVERTURN19, dayTemp19);
        cv.put(MyConstanta.DAYOVERTURN20, dayTemp20);
        cv.put(MyConstanta.DAYOVERTURN21, dayTemp21);
        cv.put(MyConstanta.DAYOVERTURN22, dayTemp22);
        cv.put(MyConstanta.DAYOVERTURN23, dayTemp23);
        cv.put(MyConstanta.DAYOVERTURN24, dayTemp24);
        cv.put(MyConstanta.DAYOVERTURN25, dayTemp25);
        cv.put(MyConstanta.DAYOVERTURN26, dayTemp26);
        cv.put(MyConstanta.DAYOVERTURN27, dayTemp27);
        cv.put(MyConstanta.DAYOVERTURN28, dayTemp28);
        cv.put(MyConstanta.DAYOVERTURN29, dayTemp29);
        cv.put(MyConstanta.DAYOVERTURN30, dayTemp30);

        db.insert(MyConstanta.TABLE_INCUBATOROVER,null, cv);
    }

    public void insertToDbIncubatorAiring(String dayDamp1, String dayDamp2, String dayDamp3, String dayDamp4, String dayDamp5, String dayDamp6, String dayDamp7, String dayDamp8, String dayDamp9, String dayDamp10, String dayDamp11, String dayDamp12, String dayDamp13, String dayDamp14, String dayDamp15, String dayDamp16, String dayDamp17, String dayDamp18, String dayDamp19, String dayDamp20, String dayDamp21, String dayDamp22, String dayDamp23, String dayDamp24, String dayDamp25, String dayDamp26, String dayDamp27, String dayDamp28, String dayDamp29, String dayDamp30) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYAIRING1, dayDamp1);
        cv.put(MyConstanta.DAYAIRING2, dayDamp2);
        cv.put(MyConstanta.DAYAIRING3, dayDamp3);
        cv.put(MyConstanta.DAYAIRING4, dayDamp4);
        cv.put(MyConstanta.DAYAIRING5, dayDamp5);
        cv.put(MyConstanta.DAYAIRING6, dayDamp6);
        cv.put(MyConstanta.DAYAIRING7, dayDamp7);
        cv.put(MyConstanta.DAYAIRING8, dayDamp8);
        cv.put(MyConstanta.DAYAIRING9, dayDamp9);
        cv.put(MyConstanta.DAYAIRING10, dayDamp10);
        cv.put(MyConstanta.DAYAIRING11, dayDamp11);
        cv.put(MyConstanta.DAYAIRING12, dayDamp12);
        cv.put(MyConstanta.DAYAIRING13, dayDamp13);
        cv.put(MyConstanta.DAYAIRING14, dayDamp14);
        cv.put(MyConstanta.DAYAIRING15, dayDamp15);
        cv.put(MyConstanta.DAYAIRING16, dayDamp16);
        cv.put(MyConstanta.DAYAIRING17, dayDamp17);
        cv.put(MyConstanta.DAYAIRING18, dayDamp18);
        cv.put(MyConstanta.DAYAIRING19, dayDamp19);
        cv.put(MyConstanta.DAYAIRING20, dayDamp20);
        cv.put(MyConstanta.DAYAIRING21, dayDamp21);
        cv.put(MyConstanta.DAYAIRING22, dayDamp22);
        cv.put(MyConstanta.DAYAIRING23, dayDamp23);
        cv.put(MyConstanta.DAYAIRING24, dayDamp24);
        cv.put(MyConstanta.DAYAIRING25, dayDamp25);
        cv.put(MyConstanta.DAYAIRING26, dayDamp26);
        cv.put(MyConstanta.DAYAIRING27, dayDamp27);
        cv.put(MyConstanta.DAYAIRING28, dayDamp28);
        cv.put(MyConstanta.DAYAIRING29, dayDamp29);
        cv.put(MyConstanta.DAYAIRING30, dayDamp30);

        db.insert(MyConstanta.TABLE_INCUBATORAIRING,null, cv);
    }
}

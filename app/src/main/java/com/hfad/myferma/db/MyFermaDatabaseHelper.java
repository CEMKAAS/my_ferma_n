package com.hfad.myferma.db;

import static com.hfad.myferma.db.MyConstanta.DISCROTION;
import static com.hfad.myferma.db.MyConstanta.DISCROTIONPRICE;
import static com.hfad.myferma.db.MyConstanta.STATUSPRODUCT;
import static com.hfad.myferma.db.MyConstanta.TABLE_INCUBATOR;
import static com.hfad.myferma.db.MyConstanta.TABLE_INCUBATORAIRING;
import static com.hfad.myferma.db.MyConstanta.TABLE_INCUBATOROVER;
import static com.hfad.myferma.db.MyConstanta.TABLE_INCUBATORTEMPDAMP;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAME;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAMEEXPENSES;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAMEPRICE;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAMEPRODUCT;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAMESALE;
import static com.hfad.myferma.db.MyConstanta.TABLE_NAMEWRITEOFF;
import static com.hfad.myferma.db.MyConstanta.TITLEPRISE;
import static com.hfad.myferma.db.MyConstanta.TITLEPRODUCT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;


public class MyFermaDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    public MyFermaDatabaseHelper(Context context) {
        super(context, MyConstanta.DB_NAME, null, MyConstanta.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstanta.TABLE_STRUCTURE);
        db.execSQL(MyConstanta.TABLE_STRUCTURESale);
        db.execSQL(MyConstanta.TABLE_STRUCTUREEXPENSES);
        db.execSQL(MyConstanta.TABLE_STRUCTUREPRICE);
        db.execSQL(MyConstanta.TABLE_STRUCTUREWRITEOFF);
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATOR);
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORTEMPDAMP);
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATOROVER);
        db.execSQL(MyConstanta.TABLE_STRUCTUREINCUBATORAIRING);
        db.execSQL(MyConstanta.TABLE_STRUCTUREPRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MyFermaDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLE");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLESale");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEEXPENSES");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEPRICE");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEWRITEOFF");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEINCUBATOR");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEINCUBATORTEMPDAMP");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEINCUBATOROVER");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEINCUBATORAIRING");
        db.execSQL("DROP TABLE IF EXISTS MyConstanta.DROP_TABLEPRODUCT");
        onCreate(db);
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(MyConstanta.DB_NAME);
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataSale() {
        String query = "SELECT * FROM " + TABLE_NAMESALE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataExpenses() {
        String query = "SELECT * FROM " + TABLE_NAMEEXPENSES;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataWriteOff() {
        String query = "SELECT * FROM " + TABLE_NAMEWRITEOFF;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataProduct() {
        String query = "SELECT * FROM " + TABLE_NAMEPRODUCT;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataIncubator() {
        String query = "SELECT * FROM " + TABLE_INCUBATOR;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataIncubatorTempDamp() {
        String query = "SELECT * FROM " + TABLE_INCUBATORTEMPDAMP;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataIncubatorOver() {
        String query = "SELECT * FROM " + TABLE_INCUBATOROVER;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllDataIncubatorAiring() {
        String query = "SELECT * FROM " + TABLE_INCUBATORAIRING;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor readAllDataPrice() {
        String query = "SELECT * FROM " + TABLE_NAMEPRICE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor dataPrice(String nameProduct) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_NAMEPRICE,
                    null,
                    TITLEPRISE + " = ?",
                    new String[]{nameProduct},
                    null, null, null);
        }
        return cursor;
    }
    public Cursor idIncubator(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_INCUBATOR,
                    null,
                    "_id = ?",
                    new String[]{id},
                    null, null, null);
        }
        return cursor;
    }

    public Cursor idIncubatorTempDamp(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_INCUBATORTEMPDAMP,
                    null,
                    "_id = ?",
                    new String[]{id},
                    null, null, null);
        }
        return cursor;
    }

    public Cursor idIncubatorOver(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_INCUBATOROVER,
                    null,
                    "_id = ?",
                    new String[]{id},
                    null, null, null);
        }
        return cursor;
    }

    public Cursor idIncubatorOverAiring(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(TABLE_INCUBATORAIRING,
                    null,
                    "_id = ?",
                    new String[]{id},
                    null, null, null);
        }
        return cursor;
    }


    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowSale(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAMESALE, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteOneRowExpenses(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAMEEXPENSES, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowWriteOff(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAMEWRITEOFF, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowProduct(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAMEPRODUCT, "Product=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowIncubator(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_INCUBATOR, "_id=?", new String[]{row_id});
        db.delete(TABLE_INCUBATORTEMPDAMP, "_id=?", new String[]{row_id});
        db.delete(TABLE_INCUBATOROVER, "_id=?", new String[]{row_id});
        db.delete(TABLE_INCUBATORAIRING, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно удаленно.", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateData(String row_id, String title, String author, String pages, String mount, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLE, title);
        cv.put(MyConstanta.DISCROTION, author);
        cv.put(MyConstanta.DAY, pages);
        cv.put(MyConstanta.MOUNT, mount);
        cv.put(MyConstanta.YEAR, year);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDataSale(String row_id, String title, String author, String pages, String mount, String year, Double priceSale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLESale, title);
        cv.put(MyConstanta.DISCROTIONSale, author);
        cv.put(MyConstanta.DAY, pages);
        cv.put(MyConstanta.MOUNT, mount);
        cv.put(MyConstanta.YEAR, year);
        cv.put(MyConstanta.PRICEALL, priceSale);

        long result = db.update(TABLE_NAMESALE, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDataExpenses(String row_id, String title, String author, String pages, String mount, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEEXPENSES, title);
        cv.put(MyConstanta.DISCROTIONEXPENSES, author);
        cv.put(MyConstanta.DAY, pages);
        cv.put(MyConstanta.MOUNT, mount);
        cv.put(MyConstanta.YEAR, year);

        long result = db.update(TABLE_NAMEEXPENSES, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDataWriteOff(String row_id, String title, String author, String pages, String mount, String year, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEWRITEOFF, title);
        cv.put(MyConstanta.DISCROTIONSWRITEOFF, author);
        cv.put(MyConstanta.DAY, pages);
        cv.put(MyConstanta.MOUNT, mount);
        cv.put(MyConstanta.YEAR, year);
        cv.put(MyConstanta.STASTUSWRITEOFF, status);

        long result = db.update(TABLE_NAMEWRITEOFF, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDataProduct(String row_id, String title, int product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLEPRODUCT, title);
        cv.put(STATUSPRODUCT, product);
        long result = db.update(TABLE_NAMEPRODUCT, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDataPrice(String title, double product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLEPRISE, title);
        cv.put(DISCROTIONPRICE, product);
        long result = db.update(TABLE_NAMEPRICE, cv, TITLEPRISE + " =?", new String[]{title});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }


    public void insertDataProduct(String title, int product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEPRODUCT, title);
        cv.put(MyConstanta.STATUSPRODUCT, product);
        long result = db.insert(TABLE_NAMEPRODUCT, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно добавлено!", Toast.LENGTH_SHORT).show();
        }

    }


    public void insertToDb(String title, double disc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Calendar calendar = Calendar.getInstance();
//        double priceAdd = disc * price(title); // todo цена!!
        cv.put(MyConstanta.TITLE, title);
        cv.put(MyConstanta.DISCROTION, disc);
        cv.put(MyConstanta.DAY, calendar.get(Calendar.DAY_OF_MONTH));
        cv.put(MyConstanta.MOUNT, calendar.get(Calendar.MONTH) + 1);
        cv.put(MyConstanta.YEAR, calendar.get(Calendar.YEAR));
        cv.put(MyConstanta.PRICEALL, 0);
        db.insert(MyConstanta.TABLE_NAME, null, cv);
    }

    public void insertToDbSale(String title, double disc, double priceIndi) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void insertToDbWriteOff(String title, double disc, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void insertToDbPrice(String title, double disc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.TITLEPRISE, title);
        cv.put(MyConstanta.DISCROTIONPRICE, disc);
        db.insert(MyConstanta.TABLE_NAMEPRICE, null, cv);
    }
    public void updateIncubatorTO(String mass[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.NAMEINCUBATOR, mass[1]);
        cv.put(MyConstanta.TYPEINCUBATOR, mass[2]);
        cv.put(MyConstanta.DATAINCUBATOR, mass[3]);
        cv.put(MyConstanta.OVERTURNINCUBATOR, mass[4]);
        cv.put(MyConstanta.EGGALL, mass[5]);
        cv.put(MyConstanta.EGGALLEND, mass[6]);
        cv.put(MyConstanta.AIRING, mass[7]);
        cv.put(MyConstanta.ARHIVE, mass[8]);
        cv.put(MyConstanta.DATAEND, mass[9]);
        cv.put(MyConstanta.TIMEPUSH1, mass[10]);
        cv.put(MyConstanta.TIMEPUSH2, mass[11]);
        cv.put(MyConstanta.TIMEPUSH3, mass[12]);

        long result = db.update(TABLE_INCUBATOR, cv, "_id=?", new String[]{mass[0]});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateIncubator(String mass[], String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYTEMP1, mass[1]);
        cv.put(MyConstanta.DAYTEMP2, mass[2]);
        cv.put(MyConstanta.DAYTEMP3, mass[3]);
        cv.put(MyConstanta.DAYTEMP4, mass[4]);
        cv.put(MyConstanta.DAYTEMP5, mass[5]);
        cv.put(MyConstanta.DAYTEMP6, mass[6]);
        cv.put(MyConstanta.DAYTEMP7, mass[7]);
        cv.put(MyConstanta.DAYTEMP8, mass[8]);
        cv.put(MyConstanta.DAYTEMP9, mass[9]);
        cv.put(MyConstanta.DAYTEMP10, mass[10]);
        cv.put(MyConstanta.DAYTEMP11, mass[11]);
        cv.put(MyConstanta.DAYTEMP12, mass[12]);
        cv.put(MyConstanta.DAYTEMP13, mass[13]);
        cv.put(MyConstanta.DAYTEMP14, mass[14]);
        cv.put(MyConstanta.DAYTEMP15, mass[15]);
        cv.put(MyConstanta.DAYTEMP16, mass[16]);
        cv.put(MyConstanta.DAYTEMP17, mass[17]);
        cv.put(MyConstanta.DAYTEMP18, mass[18]);
        cv.put(MyConstanta.DAYTEMP19, mass[19]);
        cv.put(MyConstanta.DAYTEMP20, mass[20]);
        cv.put(MyConstanta.DAYTEMP21, mass[21]);
        cv.put(MyConstanta.DAYTEMP22, mass[22]);
        cv.put(MyConstanta.DAYTEMP23, mass[23]);
        cv.put(MyConstanta.DAYTEMP24, mass[24]);
        cv.put(MyConstanta.DAYTEMP25, mass[25]);
        cv.put(MyConstanta.DAYTEMP26, mass[26]);
        cv.put(MyConstanta.DAYTEMP27, mass[27]);
        cv.put(MyConstanta.DAYTEMP28, mass[28]);
        cv.put(MyConstanta.DAYTEMP29, mass[29]);
        cv.put(MyConstanta.DAYTEMP30, mass[30]);

        cv.put(MyConstanta.DAYDAMP1, mass[31]);
        cv.put(MyConstanta.DAYDAMP2, mass[32]);
        cv.put(MyConstanta.DAYDAMP3, mass[33]);
        cv.put(MyConstanta.DAYDAMP4, mass[34]);
        cv.put(MyConstanta.DAYDAMP5, mass[35]);
        cv.put(MyConstanta.DAYDAMP6, mass[36]);
        cv.put(MyConstanta.DAYDAMP7, mass[37]);
        cv.put(MyConstanta.DAYDAMP8, mass[38]);
        cv.put(MyConstanta.DAYDAMP9, mass[39]);
        cv.put(MyConstanta.DAYDAMP10, mass[40]);
        cv.put(MyConstanta.DAYDAMP11, mass[41]);
        cv.put(MyConstanta.DAYDAMP12, mass[42]);
        cv.put(MyConstanta.DAYDAMP13, mass[43]);
        cv.put(MyConstanta.DAYDAMP14, mass[44]);
        cv.put(MyConstanta.DAYDAMP15, mass[45]);
        cv.put(MyConstanta.DAYDAMP16, mass[46]);
        cv.put(MyConstanta.DAYDAMP17, mass[47]);
        cv.put(MyConstanta.DAYDAMP18, mass[48]);
        cv.put(MyConstanta.DAYDAMP19, mass[49]);
        cv.put(MyConstanta.DAYDAMP20, mass[50]);
        cv.put(MyConstanta.DAYDAMP21, mass[51]);
        cv.put(MyConstanta.DAYDAMP22, mass[52]);
        cv.put(MyConstanta.DAYDAMP23, mass[53]);
        cv.put(MyConstanta.DAYDAMP24, mass[54]);
        cv.put(MyConstanta.DAYDAMP25, mass[55]);
        cv.put(MyConstanta.DAYDAMP26, mass[56]);
        cv.put(MyConstanta.DAYDAMP27, mass[57]);
        cv.put(MyConstanta.DAYDAMP28, mass[58]);
        cv.put(MyConstanta.DAYDAMP29, mass[59]);
        cv.put(MyConstanta.DAYDAMP30, mass[60]);

        long result = db.update(TABLE_INCUBATORTEMPDAMP, cv, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateIncubatorOver(String mass[], String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYOVERTURN1, mass[1]);
        cv.put(MyConstanta.DAYOVERTURN2, mass[2]);
        cv.put(MyConstanta.DAYOVERTURN3, mass[3]);
        cv.put(MyConstanta.DAYOVERTURN4, mass[4]);
        cv.put(MyConstanta.DAYOVERTURN5, mass[5]);
        cv.put(MyConstanta.DAYOVERTURN6, mass[6]);
        cv.put(MyConstanta.DAYOVERTURN7, mass[7]);
        cv.put(MyConstanta.DAYOVERTURN8, mass[8]);
        cv.put(MyConstanta.DAYOVERTURN9, mass[9]);
        cv.put(MyConstanta.DAYOVERTURN10, mass[10]);
        cv.put(MyConstanta.DAYOVERTURN11, mass[11]);
        cv.put(MyConstanta.DAYOVERTURN12, mass[12]);
        cv.put(MyConstanta.DAYOVERTURN13, mass[13]);
        cv.put(MyConstanta.DAYOVERTURN14, mass[14]);
        cv.put(MyConstanta.DAYOVERTURN15, mass[15]);
        cv.put(MyConstanta.DAYOVERTURN16, mass[16]);
        cv.put(MyConstanta.DAYOVERTURN17, mass[17]);
        cv.put(MyConstanta.DAYOVERTURN18, mass[18]);
        cv.put(MyConstanta.DAYOVERTURN19, mass[19]);
        cv.put(MyConstanta.DAYOVERTURN20, mass[20]);
        cv.put(MyConstanta.DAYOVERTURN21, mass[21]);
        cv.put(MyConstanta.DAYOVERTURN22, mass[22]);
        cv.put(MyConstanta.DAYOVERTURN23, mass[23]);
        cv.put(MyConstanta.DAYOVERTURN24, mass[24]);
        cv.put(MyConstanta.DAYOVERTURN25, mass[25]);
        cv.put(MyConstanta.DAYOVERTURN26, mass[26]);
        cv.put(MyConstanta.DAYOVERTURN27, mass[27]);
        cv.put(MyConstanta.DAYOVERTURN28, mass[28]);
        cv.put(MyConstanta.DAYOVERTURN29, mass[29]);
        cv.put(MyConstanta.DAYOVERTURN30, mass[30]);

        long result = db.update(TABLE_INCUBATOROVER, cv, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateIncubatorAiring(String mass[], String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstanta.DAYAIRING1, mass[1]);
        cv.put(MyConstanta.DAYAIRING2, mass[2]);
        cv.put(MyConstanta.DAYAIRING3, mass[3]);
        cv.put(MyConstanta.DAYAIRING4, mass[4]);
        cv.put(MyConstanta.DAYAIRING5, mass[5]);
        cv.put(MyConstanta.DAYAIRING6, mass[6]);
        cv.put(MyConstanta.DAYAIRING7, mass[7]);
        cv.put(MyConstanta.DAYAIRING8, mass[8]);
        cv.put(MyConstanta.DAYAIRING9, mass[9]);
        cv.put(MyConstanta.DAYAIRING10, mass[10]);
        cv.put(MyConstanta.DAYAIRING11, mass[11]);
        cv.put(MyConstanta.DAYAIRING12, mass[12]);
        cv.put(MyConstanta.DAYAIRING13, mass[13]);
        cv.put(MyConstanta.DAYAIRING14, mass[14]);
        cv.put(MyConstanta.DAYAIRING15, mass[15]);
        cv.put(MyConstanta.DAYAIRING16, mass[16]);
        cv.put(MyConstanta.DAYAIRING17, mass[17]);
        cv.put(MyConstanta.DAYAIRING18, mass[18]);
        cv.put(MyConstanta.DAYAIRING19, mass[19]);
        cv.put(MyConstanta.DAYAIRING20, mass[20]);
        cv.put(MyConstanta.DAYAIRING21, mass[21]);
        cv.put(MyConstanta.DAYAIRING22, mass[22]);
        cv.put(MyConstanta.DAYAIRING23, mass[23]);
        cv.put(MyConstanta.DAYAIRING24, mass[24]);
        cv.put(MyConstanta.DAYAIRING25, mass[25]);
        cv.put(MyConstanta.DAYAIRING26, mass[26]);
        cv.put(MyConstanta.DAYAIRING27, mass[27]);
        cv.put(MyConstanta.DAYAIRING28, mass[28]);
        cv.put(MyConstanta.DAYAIRING29, mass[29]);
        cv.put(MyConstanta.DAYAIRING30, mass[30]);


        long result = db.update(TABLE_INCUBATORAIRING, cv, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Успешно обновлено!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void deleteAllIncubator() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_INCUBATOR);
        db.execSQL("DELETE FROM " + TABLE_INCUBATORTEMPDAMP);
        db.execSQL("DELETE FROM " + TABLE_INCUBATOROVER);
        db.execSQL("DELETE FROM " + TABLE_INCUBATORAIRING);
    }

    public Cursor idProduct1(String nameTable,String nameCount, String nameProduct) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.query(nameTable,
                    null,
                    nameCount + " = ?",
                    new String[]{nameProduct},
                    null, null, null);
        }
        return cursor;
    }





}


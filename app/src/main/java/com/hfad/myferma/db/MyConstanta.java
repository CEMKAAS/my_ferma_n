package com.hfad.myferma.db;

public class MyConstanta {

    public static final String DB_NAME = "my_db.db"; //База данных
    public static final int DB_VERSION = 8; //Версия базы данных

    public static final String TABLE_NAME = "МyFerma"; // Название таблицы
    public static final String _ID = "_id"; // Индефикатор НУМЕРАЦИЯ СТРОК
    public static final String TITLE = "title"; // Название описание (название продукта)
    public static final String DISCROTION = "disc"; //Заголовок (кол-во)
    public static final String DAY = "DAY"; //Заголовок (кол-во)
    public static final String MOUNT = "MOUNT"; //Заголовок (кол-во)
    public static final String YEAR = "YEAR"; //Заголовок (кол-во)
    public static final String PRICEALL = "PRICE"; //Заголовок (кол-во)

    public static final String TABLE_NAMESALE = "МyFermaSale"; // Название таблицы
    public static final String TITLESale = "titleSale"; // Название описание (название продукта) название проданного товара
    public static final String DISCROTIONSale = "discSale"; //Заголовок (кол-во) проданного товара


    public static final String TABLE_NAMEEXPENSES = "МyFermaEXPENSES"; // Название таблицы
    public static final String TITLEEXPENSES = "titleEXPENSES"; // Название описание (название продукта) название Купленого товара
    public static final String DISCROTIONEXPENSES = "discEXPENSES"; //Заголовок (кол-во) цена товара
    public static final String COUNTEXPENSES = "countEXPENSES"; //Заголовок кол-во товара
    public static final String TABLE_NAMEPRICE = "МyFermaPRICE"; // Название таблицы
    public static final String TITLEPRISE = "titlePRICE"; // Название описание (название продукта) название товара
    public static final String DISCROTIONPRICE = "discPRICE"; //Заголовок (кол-во) цена товара

    public static final String TABLE_NAMEWRITEOFF = "МyFermaWRITEOFF"; // Название таблицы
    public static final String TITLEWRITEOFF = "titleWRITEOFF"; // Название описание (название продукта) название списанного товара
    public static final String DISCROTIONSWRITEOFF = "discWRITEOFF"; //Заголовок (кол-во) кол-во товара
    public static final String STASTUSWRITEOFF = "statusWRITEOFF";

    public static final String TABLE_NAMEPRODUCT = "TableProduct"; // Название таблицы
    public static final String TITLEPRODUCT = "Product"; // Название описание (название продукта) название списанного товара
    public static final String STATUSPRODUCT = "Status"; // Статус продукта, нужен, для отображения

    //Инкубатор
    public static final String TABLE_INCUBATOR = "МyINCUBATOR"; // Название таблицы
    public static final String TABLE_INCUBATORTEMPDAMP = "МyINCUBATORTEMP"; // Название таблицы
    public static final String TABLE_INCUBATOROVER = "МyINCUBATOROVER"; // Название таблицы
    public static final String TABLE_INCUBATORAIRING = "МyINCUBATORAIRING"; // Название таблицы
    public static final String NAMEINCUBATOR = "NAME"; // Название инкубатора
    public static final String TYPEINCUBATOR = "TYPE"; // Тип яиц инкубатора
    public static final String DATAINCUBATOR = "DATA"; // Дата начала яиц инкубатора
    public static final String DATAEND = "DATAEND"; // Дата окончания яиц инкубатора
    public static final String EGGALL = "EGGALL"; // переворот в инкубаторе
    public static final String EGGALLEND = "EGGALLEND"; // переворот в инкубаторе
    public static final String OVERTURNINCUBATOR = "OVERTURN"; // переворот в инкубаторе
    public static final String AIRING = "AIRING"; // Охлаждение в инкубаторе
    public static final String ARHIVE = "ARHIVE"; // Статус в архиве
    public static final String TIMEPUSH1 = "TIMEPUSH1"; // Статус в архиве
    public static final String TIMEPUSH2 = "TIMEPUSH2"; // Статус в архиве
    public static final String TIMEPUSH3 = "TIMEPUSH3"; // Статус в архиве


    //Температура
    public static final String DAYTEMP1 = "DAYTEMP1"; // Температура в инкубаторе день 1
    public static final String DAYTEMP2 = "DAYTEMP2"; // Температура в инкубаторе день 1
    public static final String DAYTEMP3 = "DAYTEMP3"; // Температура в инкубаторе день 1
    public static final String DAYTEMP4 = "DAYTEMP4"; // Температура в инкубаторе день 1
    public static final String DAYTEMP5 = "DAYTEMP5"; // Температура в инкубаторе день 1
    public static final String DAYTEMP6 = "DAYTEMP6"; // Температура в инкубаторе день 1
    public static final String DAYTEMP7 = "DAYTEMP7"; // Температура в инкубаторе день 1
    public static final String DAYTEMP8 = "DAYTEMP8"; // Температура в инкубаторе день 1
    public static final String DAYTEMP9 = "DAYTEMP9"; // Температура в инкубаторе день 1
    public static final String DAYTEMP10 = "DAYTEMP10"; // Температура в инкубаторе день 1
    public static final String DAYTEMP11 = "DAYTEMP11"; // Температура в инкубаторе день 1
    public static final String DAYTEMP12 = "DAYTEMP12"; // Температура в инкубаторе день 1
    public static final String DAYTEMP13 = "DAYTEMP13"; // Температура в инкубаторе день 1
    public static final String DAYTEMP14 = "DAYTEMP14"; // Температура в инкубаторе день 1
    public static final String DAYTEMP15 = "DAYTEMP15"; // Температура в инкубаторе день 1
    public static final String DAYTEMP16 = "DAYTEMP16"; // Температура в инкубаторе день 1
    public static final String DAYTEMP17 = "DAYTEMP17"; // Температура в инкубаторе день 1
    public static final String DAYTEMP18 = "DAYTEMP18"; // Температура в инкубаторе день 1
    public static final String DAYTEMP19 = "DAYTEMP19"; // Температура в инкубаторе день 1
    public static final String DAYTEMP20 = "DAYTEMP20"; // Температура в инкубаторе день 1
    public static final String DAYTEMP21 = "DAYTEMP21"; // Температура в инкубаторе день 1
    public static final String DAYTEMP22 = "DAYTEMP22"; // Температура в инкубаторе день 1
    public static final String DAYTEMP23 = "DAYTEMP23"; // Температура в инкубаторе день 1
    public static final String DAYTEMP24 = "DAYTEMP24"; // Температура в инкубаторе день 1
    public static final String DAYTEMP25 = "DAYTEMP25"; // Температура в инкубаторе день 1
    public static final String DAYTEMP26 = "DAYTEMP26"; // Температура в инкубаторе день 1
    public static final String DAYTEMP27 = "DAYTEMP27"; // Температура в инкубаторе день 1
    public static final String DAYTEMP28 = "DAYTEMP28"; // Температура в инкубаторе день 1
    public static final String DAYTEMP29 = "DAYTEMP29"; // Температура в инкубаторе день 1
    public static final String DAYTEMP30 = "DAYTEMP30"; // Температура в инкубаторе день 1
    //Влажность
    public static final String DAYDAMP1 = "DAYDAMP1"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP2 = "DAYDAMP2"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP3 = "DAYDAMP3"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP4 = "DAYDAMP4"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP5 = "DAYDAMP5"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP6 = "DAYDAMP6"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP7 = "DAYDAMP7"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP8 = "DAYDAMP8"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP9 = "DAYDAMP9"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP10 = "DAYDAMP10"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP11 = "DAYDAMP11"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP12 = "DAYDAMP12"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP13 = "DAYDAMP13"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP14 = "DAYDAMP14"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP15 = "DAYDAMP15"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP16 = "DAYDAMP16"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP17 = "DAYDAMP17"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP18 = "DAYDAMP18"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP19 = "DAYDAMP19"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP20 = "DAYDAMP20"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP21 = "DAYDAMP21"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP22 = "DAYDAMP22"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP23 = "DAYDAMP23"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP24 = "DAYDAMP24"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP25 = "DAYDAMP25"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP26 = "DAYDAMP26"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP27 = "DAYDAMP27"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP28 = "DAYDAMP28"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP29 = "DAYDAMP29"; // Влажность в инкубаторе день 1
    public static final String DAYDAMP30 = "DAYDAMP30"; // Влажность в инкубаторе день 1
    //Перевороты
    public static final String DAYOVERTURN1 = "DATOVERTURN1"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN2 = "DATOVERTURN2"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN3 = "DATOVERTURN3"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN4 = "DATOVERTURN4"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN5 = "DATOVERTURN5"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN6 = "DATOVERTURN6"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN7 = "DATOVERTURN7"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN8 = "DATOVERTURN8"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN9 = "DATOVERTURN9"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN10 = "DATOVERTURN10"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN11 = "DATOVERTURN11"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN12 = "DATOVERTURN12"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN13 = "DATOVERTURN13"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN14 = "DATOVERTURN14"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN15 = "DATOVERTURN15"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN16 = "DATOVERTURN16"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN17 = "DATOVERTURN17"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN18 = "DATOVERTURN18"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN19 = "DATOVERTURN19"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN20 = "DATOVERTURN20"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN21 = "DATOVERTURN21"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN22 = "DATOVERTURN22"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN23 = "DATOVERTURN23"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN24 = "DATOVERTURN24"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN25 = "DATOVERTURN25"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN26 = "DATOVERTURN26"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN27 = "DATOVERTURN27"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN28 = "DATOVERTURN28"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN29 = "DATOVERTURN29"; // переворот в инкубаторе день 1
    public static final String DAYOVERTURN30 = "DATOVERTURN30"; // переворот в инкубаторе день 1

    //Проветривание
    public static final String DAYAIRING1 = "DAYAIRING1"; // Проветривание в инкубаторе
    public static final String DAYAIRING2 = "DAYAIRING2"; // Проветривание в инкубаторе
    public static final String DAYAIRING3 = "DAYAIRING3"; // Проветривание в инкубаторе
    public static final String DAYAIRING4 = "DAYAIRING4"; // Проветривание в инкубаторе
    public static final String DAYAIRING5 = "DAYAIRING5"; // Проветривание в инкубаторе
    public static final String DAYAIRING6 = "DAYAIRING6"; // Проветривание в инкубаторе
    public static final String DAYAIRING7 = "DAYAIRING7"; // Проветривание в инкубаторе
    public static final String DAYAIRING8 = "DAYAIRING8"; // Проветривание в инкубаторе
    public static final String DAYAIRING9 = "DAYAIRING9"; // Проветривание в инкубаторе
    public static final String DAYAIRING10 = "DAYAIRING10"; // Проветривание в инкубаторе
    public static final String DAYAIRING11 = "DAYAIRING11"; // Проветривание в инкубаторе
    public static final String DAYAIRING12 = "DAYAIRING12"; // Проветривание в инкубаторе
    public static final String DAYAIRING13 = "DAYAIRING13"; // Проветривание в инкубаторе
    public static final String DAYAIRING14 = "DAYAIRING14"; // Проветривание в инкубаторе
    public static final String DAYAIRING15 = "DAYAIRING15"; // Проветривание в инкубаторе
    public static final String DAYAIRING16 = "DAYAIRING16"; // Проветривание в инкубаторе
    public static final String DAYAIRING17 = "DAYAIRING17"; // Проветривание в инкубаторе
    public static final String DAYAIRING18 = "DAYAIRING18"; // Проветривание в инкубаторе
    public static final String DAYAIRING19 = "DAYAIRING19"; // Проветривание в инкубаторе
    public static final String DAYAIRING20 = "DAYAIRING20"; // Проветривание в инкубаторе
    public static final String DAYAIRING21 = "DAYAIRING21"; // Проветривание в инкубаторе
    public static final String DAYAIRING22 = "DAYAIRING22"; // Проветривание в инкубаторе
    public static final String DAYAIRING23 = "DAYAIRING23"; // Проветривание в инкубаторе
    public static final String DAYAIRING24 = "DAYAIRING24"; // Проветривание в инкубаторе
    public static final String DAYAIRING25 = "DAYAIRING25"; // Проветривание в инкубаторе
    public static final String DAYAIRING26 = "DAYAIRING26"; // Проветривание в инкубаторе
    public static final String DAYAIRING27 = "DAYAIRING27"; // Проветривание в инкубаторе
    public static final String DAYAIRING28 = "DAYAIRING28"; // Проветривание в инкубаторе
    public static final String DAYAIRING29 = "DAYAIRING29"; // Проветривание в инкубаторе
    public static final String DAYAIRING30 = "DAYAIRING30"; // Проветривание в инкубаторе


    //Таблица по добавлению
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," +
            DISCROTION + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + PRICEALL + " REAL)"; // Структура таблицы 1


    //Таблица по продажам
    public static final String TABLE_STRUCTURESale = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMESALE + " (" + _ID + " INTEGER PRIMARY KEY," + TITLESale + " TEXT," +
            DISCROTIONSale + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + PRICEALL + " REAL)"; // Структура таблицы

    //Таблица по покупкам //TODO Возможно тут ошибка! REAL
    public static final String TABLE_STRUCTUREEXPENSES = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEEXPENSES + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEEXPENSES + " TEXT," +
            DISCROTIONEXPENSES + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + COUNTEXPENSES + " REAL)";  // Структура таблицы

    //Таблица по списанию
    public static final String TABLE_STRUCTUREWRITEOFF = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEWRITEOFF + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEWRITEOFF + " TEXT," +
            DISCROTIONSWRITEOFF + " REAL," + DAY + " INTEGER," + MOUNT + " INTEGER," + YEAR + " INTEGER," + STASTUSWRITEOFF + " INTEGER)";  // Структура таблицы


    //Таблица по ценам
    public static final String TABLE_STRUCTUREPRICE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEPRICE + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEPRISE + " TEXT," +
            DISCROTIONPRICE + " REAL)";  // Структура таблицы

    //Таблица по товару
    public static final String TABLE_STRUCTUREPRODUCT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAMEPRODUCT + " (" + _ID + " INTEGER PRIMARY KEY," + TITLEPRODUCT + " TEXT," + STATUSPRODUCT + " TEXT)";

    //Таблица по инкубатору столбцов 13
    public static final String TABLE_STRUCTUREINCUBATOR = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATOR + " (" + _ID + " INTEGER PRIMARY KEY," + NAMEINCUBATOR + " TEXT," +
            TYPEINCUBATOR + " TEXT," + DATAINCUBATOR + " TEXT," + EGGALL + " TEXT," + EGGALLEND + " TEXT," + AIRING + " TEXT," + OVERTURNINCUBATOR + " TEXT," +
            ARHIVE + " TEXT," + DATAEND  + " TEXT," +  TIMEPUSH1 + " TEXT," + TIMEPUSH2 + " TEXT,"+ TIMEPUSH3 + " TEXT)";


    // Таблица по теплу и влажности инкубатора
    public static final String TABLE_STRUCTUREINCUBATORTEMPDAMP = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORTEMPDAMP + " (" + _ID + " INTEGER PRIMARY KEY," + DAYTEMP1 + " TEXT," + DAYTEMP2 + " TEXT," + DAYTEMP3 + " TEXT," + DAYTEMP4 + " TEXT," + DAYTEMP5 + " TEXT," + DAYTEMP6 + " TEXT," + DAYTEMP7 + " TEXT," + DAYTEMP8 + " TEXT," + DAYTEMP9 + " TEXT," + DAYTEMP10 + " TEXT," + DAYTEMP11 + " TEXT," + DAYTEMP12 + " TEXT," + DAYTEMP13 + " TEXT," + DAYTEMP14 + " TEXT," + DAYTEMP15 + " TEXT," + DAYTEMP16 + " TEXT," + DAYTEMP17 + " TEXT," + DAYTEMP18 + " TEXT," + DAYTEMP19 + " TEXT," + DAYTEMP20 + " TEXT," + DAYTEMP21 + " TEXT," + DAYTEMP22 + " TEXT," + DAYTEMP23 + " TEXT," + DAYTEMP24 + " TEXT," + DAYTEMP25 + " TEXT," + DAYTEMP26 + " TEXT," + DAYTEMP27 + " TEXT," + DAYTEMP28 + " TEXT," + DAYTEMP29 + " TEXT," + DAYTEMP30 + " TEXT,"
            + DAYDAMP1 + " TEXT," + DAYDAMP2 + " TEXT," + DAYDAMP3 + " TEXT," + DAYDAMP4 + " TEXT," + DAYDAMP5 + " TEXT," + DAYDAMP6 + " TEXT," + DAYDAMP7 + " TEXT," + DAYDAMP8 + " TEXT," + DAYDAMP9 + " TEXT," + DAYDAMP10 + " TEXT," + DAYDAMP11 + " TEXT," + DAYDAMP12 + " TEXT," + DAYDAMP13 + " TEXT," + DAYDAMP14 + " TEXT," + DAYDAMP15 + " TEXT," + DAYDAMP16 + " TEXT," + DAYDAMP17 + " TEXT," + DAYDAMP18 + " TEXT," + DAYDAMP19 + " TEXT," + DAYDAMP20 + " TEXT," + DAYDAMP21 + " TEXT," + DAYDAMP22 + " TEXT," + DAYDAMP23 + " TEXT," + DAYDAMP24 + " TEXT," + DAYDAMP25 + " TEXT," + DAYDAMP26 + " TEXT," + DAYDAMP27 + " TEXT," + DAYDAMP28 + " TEXT," + DAYDAMP29 + " TEXT," + DAYDAMP30 + " TEXT)";

    // Таблица по перевороту инкубатора
    public static final String TABLE_STRUCTUREINCUBATOROVER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATOROVER + " (" + _ID + " INTEGER PRIMARY KEY," + DAYOVERTURN1 + " TEXT," + DAYOVERTURN2 + " TEXT," + DAYOVERTURN3 + " TEXT," + DAYOVERTURN4 + " TEXT," + DAYOVERTURN5 + " TEXT," + DAYOVERTURN6 + " TEXT," + DAYOVERTURN7 + " TEXT," + DAYOVERTURN8 + " TEXT," + DAYOVERTURN9 + " TEXT," + DAYOVERTURN10 + " TEXT," + DAYOVERTURN11 + " TEXT," + DAYOVERTURN12 + " TEXT," + DAYOVERTURN13 + " TEXT," + DAYOVERTURN14 + " TEXT," + DAYOVERTURN15 + " TEXT," + DAYOVERTURN16 + " TEXT," + DAYOVERTURN17 + " TEXT," + DAYOVERTURN18 + " TEXT," + DAYOVERTURN19 + " TEXT," + DAYOVERTURN20 + " TEXT," + DAYOVERTURN21 + " TEXT," + DAYOVERTURN22 + " TEXT," + DAYOVERTURN23 + " TEXT," + DAYOVERTURN24 + " TEXT," + DAYOVERTURN25 + " TEXT," + DAYOVERTURN26 + " TEXT," + DAYOVERTURN27 + " TEXT," + DAYOVERTURN28 + " TEXT," + DAYOVERTURN29 + " TEXT," + DAYOVERTURN30 + " TEXT)";

    // Таблица по охлаждению инкубатора
    public static final String TABLE_STRUCTUREINCUBATORAIRING = "CREATE TABLE IF NOT EXISTS " +
            TABLE_INCUBATORAIRING + " (" + _ID + " INTEGER PRIMARY KEY," + DAYAIRING1 + " TEXT," + DAYAIRING2 + " TEXT," + DAYAIRING3 + " TEXT," + DAYAIRING4 + " TEXT," + DAYAIRING5 + " TEXT," + DAYAIRING6 + " TEXT," + DAYAIRING7 + " TEXT," + DAYAIRING8 + " TEXT," + DAYAIRING9 + " TEXT," + DAYAIRING10 + " TEXT," + DAYAIRING11 + " TEXT," + DAYAIRING12 + " TEXT," + DAYAIRING13 + " TEXT," + DAYAIRING14 + " TEXT," + DAYAIRING15 + " TEXT," + DAYAIRING16 + " TEXT," + DAYAIRING17 + " TEXT," + DAYAIRING18 + " TEXT," + DAYAIRING19 + " TEXT," + DAYAIRING20 + " TEXT," + DAYAIRING21 + " TEXT," + DAYAIRING22 + " TEXT," + DAYAIRING23 + " TEXT," + DAYAIRING24 + " TEXT," + DAYAIRING25 + " TEXT," + DAYAIRING26 + " TEXT," + DAYAIRING27 + " TEXT," + DAYAIRING28 + " TEXT," + DAYAIRING29 + " TEXT," + DAYAIRING30 + " TEXT)";

    public static final String DROP_TABLESale = "DROP TABLE IF EXISTS" + TABLE_NAMESALE; // сброс продаж
    public static final String DROP_TABLEEXPENSES = "DROP TABLE IF EXISTS" + TABLE_NAMEEXPENSES; // сброс покупок
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME; // сброс обычной
    public static final String DROP_TABLEPRICE = "DROP TABLE IF EXISTS" + TABLE_NAMEPRICE; // сброс цен
    public static final String DROP_TABLEWRITEOFF = "DROP TABLE IF EXISTS" + TABLE_NAMEWRITEOFF; // сброс цен
    public static final String DROP_TABLEPRODUCT = "DROP TABLE IF EXISTS" + TABLE_NAMEPRODUCT; // сброс продуктов
    public static final String DROP_TABLEINCUBATOR = "DROP TABLE IF EXISTS" + TABLE_INCUBATOR; // сброс Инкубатора
    public static final String DROP_TABLEINCUBATORTEMPDAMP = "DROP TABLE IF EXISTS" + TABLE_INCUBATORTEMPDAMP; // сброс Инкубатора
    public static final String DROP_TABLEINCUBATOROVER = "DROP TABLE IF EXISTS" + TABLE_INCUBATOROVER; // сброс Инкубатора

    public static final String DROP_TABLEINCUBATORAIRING = "DROP TABLE IF EXISTS" + TABLE_INCUBATORAIRING; // сброс Инкубатора

}
package com.hfad.myferma.db;

import android.content.Context;

public class MydbManagerMetod extends MydbManager {


    public MydbManagerMetod(Context context) {
        super(context);
    }


    public double sumSale(String animalsType) {
        double sumEggSale = 0;
        double sumMilkSale = 0;
        double sumMeatSale = 0;

        double sumEgg = 0;
        double sumMilk = 0;
        double sumMeat = 0;

        double sumEggWriteOff = 0;
        double sumMilkWriteOff = 0;
        double sumMeatWriteOff = 0;

        double sumSale = 0;

//    Сколько яиц на данный момент
        if (animalsType.equals("Яйца")) {
            for (int i = 0; i < getFromDb(animalsType).size(); i++) {
                sumEgg += Double.valueOf(getFromDb(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbSale(animalsType).size(); i++) {
                sumEggSale += Double.valueOf(getFromDbSale(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumEggWriteOff += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            sumSale = sumEgg - sumEggSale - sumEggWriteOff;
            return sumSale;
        }
        //    Сколько Молока на данный момент
        if (animalsType.equals("Молоко")) {
            for (int i = 0; i < getFromDb(animalsType).size(); i++) {
                sumMilk += Double.valueOf(getFromDb(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbSale(animalsType).size(); i++) {
                sumMilkSale += Double.valueOf(getFromDbSale(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumMilkWriteOff += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            sumSale = sumMilk - sumMilkSale - sumMilkWriteOff;
            return sumSale;
        }
        //    Сколько яиц на данный момент
        if (animalsType.equals("Мясо")) {
            for (int i = 0; i < getFromDb(animalsType).size(); i++) {
                sumMeat += Double.valueOf(getFromDb(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbSale(animalsType).size(); i++) {
                sumMeatSale += Double.valueOf(getFromDbSale(animalsType).get(i));
            }
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumMeatWriteOff += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            sumSale = sumMeat - sumMeatSale - sumMeatWriteOff;
            return sumSale;
        }

        return sumSale;
    }

    //    Сколько яиц на данный момент
    public double sumSaleEgg() {
        double sumEggSale = 0;
        double sumEgg = 0;
        double sumSale = 0;
        double sumEggWriteOff = 0;
        String egg = "Яйца";

        for (int i = 0; i < getFromDb(egg).size(); i++) {
            sumEgg += Double.valueOf(getFromDb(egg).get(i));
        }
        for (int i = 0; i < getFromDbSale(egg).size(); i++) {
            sumEggSale += Double.valueOf(getFromDbSale(egg).get(i));
        }
        for (int i = 0; i < getFromDbWriteOff(egg).size(); i++) {
            sumEggWriteOff += Double.valueOf(getFromDbWriteOff(egg).get(i));
        }
        sumSale = sumEgg - sumEggSale - sumEggWriteOff ;
        return sumSale;
    }

    //    Сколько молока на данный момент
    public double  sumSaleMilk() {
        double sumMilkSale = 0;
        double  sumMilk = 0;
        double  sumSale = 0;
        double  sumMilkWriteOff = 0;
        String milk = "Молоко";

        for (int i = 0; i < getFromDb(milk).size(); i++) {
            sumMilk += Double.valueOf(getFromDb(milk).get(i));
        }
        for (int i = 0; i < getFromDbSale(milk).size(); i++) {
            sumMilkSale += Double.valueOf(getFromDbSale(milk).get(i));
        }
        for (int i = 0; i < getFromDbWriteOff(milk).size(); i++) {
            sumMilkWriteOff += Double.valueOf(getFromDbWriteOff(milk).get(i));
        }
        sumSale = sumMilk - sumMilkSale - sumMilkWriteOff ;
        return sumSale;
    }

    //    Сколько Мяса на данный момент
    public double sumSaleMeat() {
        double sumMeatSale = 0;
        double sumMeat = 0;
        double sumSale = 0;
        double sumMeatWriteOff = 0;
        String meat = "Мясо";

        for (int i = 0; i < getFromDb(meat).size(); i++) {
            sumMeat += Double.valueOf(getFromDb(meat).get(i));
        }
        for (int i = 0; i < getFromDbSale(meat).size(); i++) {
            sumMeatSale += Double.valueOf(getFromDbSale(meat).get(i));
        }
        for (int i = 0; i < getFromDbWriteOff(meat).size(); i++) {
            sumMeatWriteOff += Double.valueOf(getFromDbWriteOff(meat).get(i));
        }
        sumSale = sumMeat - sumMeatSale - sumMeatWriteOff;
        return sumSale;
    }

    public double sumFinanceAll() {
        double sumFinance = 0;
        for (int i = 0; i < getFromDbAllExpenses().size(); i++) {
            sumFinance += Double.valueOf(getFromDbAllExpenses().get(i));
        }
        return sumFinance;
    }


    //    Сколько яиц проданно на данный момент
    public double sumFinanceEgg() {
        double sumEggSale = 0;
        String egg = "Яйца";
        for (int i = 0; i < getFromDbSale(egg).size(); i++) {
            sumEggSale += (Double.valueOf(getFromDbSalePrice(egg).get(i)));
        }

        return sumEggSale;
    }

    //    Сколько молока проданно на данный момент
    public double sumFinanceMilk() {
        double sumMilkSale = 0;
        String milk = "Молоко";
        for (int i = 0; i < getFromDbSale(milk).size(); i++) {
            sumMilkSale += (Double.valueOf(getFromDbSalePrice(milk).get(i)));
        }
        return sumMilkSale;
    }

    //    Сколько Мяса проданно на данный момент
    public double sumFinanceMeat() {
        double sumMeatSale = 0;
        String meat = "Мясо";
        for (int i = 0; i < getFromDbSale(meat).size(); i++) {
            sumMeatSale += (Double.valueOf(getFromDbSalePrice(meat).get(i)));
        }
        return sumMeatSale;
    }


    public double sumSaleEggPrice() {
        double sumEggSale = 0;
        double sumEgg = 0;
        double sumSale = 0;
        String egg = "Яйца";

        for (int i = 0; i < getFromDb(egg).size(); i++) {
            sumEgg += (Double.valueOf(getFromDb(egg).get(i)) * Double.valueOf(getFromDbPrice(egg).get(i)));
        }
        for (int i = 0; i < getFromDbSale(egg).size(); i++) {
            sumEggSale += (Double.valueOf(getFromDbSale(egg).get(i)) * Double.valueOf(getFromDbSalePrice(egg).get(i)));
        }
        sumSale = sumEgg - sumEggSale;
        return sumSale;
    }

    //    Сколько молока на данный момент
    public double sumSaleMilkPrice() {
        double sumMilkSale = 0;
        double sumMilk = 0;
        double sumSale = 0;
        String milk = "Молоко";

        for (int i = 0; i < getFromDb(milk).size(); i++) {
            sumMilk += (Double.valueOf(getFromDb(milk).get(i)) * Double.valueOf(getFromDbPrice(milk).get(i)));
        }
        for (int i = 0; i < getFromDbSale(milk).size(); i++) {
            sumMilkSale += (Double.valueOf(getFromDbSale(milk).get(i)) * Double.valueOf(getFromDbSalePrice(milk).get(i)));
        }
        sumSale = sumMilk - sumMilkSale;
        return sumSale;
    }

    //    Сколько Мяса на данный момент
    public  double sumSaleMeatPrice() {
        double sumMeatSale = 0;
        double sumMeat = 0;
        double sumSale = 0;
        String meat = "Мясо";

        for (int i = 0; i < getFromDb(meat).size(); i++) {
            sumMeat += (Double.valueOf(getFromDb(meat).get(i)) * Double.valueOf(getFromDbPrice(meat).get(i)));
        }
        for (int i = 0; i < getFromDbSale(meat).size(); i++) {
            sumMeatSale += (Double.valueOf(getFromDbSale(meat).get(i)) * Double.valueOf(getFromDbSalePrice(meat).get(i)));
        }
        sumSale = sumMeat - sumMeatSale;
        return sumSale;
    }

    // Сколько списанн
    public double sumSaleWriteOff(String animalsType) {
        double sumSale = 0;

//    Сколько яиц на данный момент
        if (animalsType.equals("Яйца")) {
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumSale += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            return sumSale;
        }
        //    Сколько Молока на данный момент
        if (animalsType.equals("Молоко")) {
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumSale += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            return sumSale;
        }
        //    Сколько яиц на данный момент
        if (animalsType.equals("Мясо")) {
            for (int i = 0; i < getFromDbWriteOff(animalsType).size(); i++) {
                sumSale += Double.valueOf(getFromDbWriteOff(animalsType).get(i));
            }
            return sumSale;
        }

        return sumSale;
    }


}

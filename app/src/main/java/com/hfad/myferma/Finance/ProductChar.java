package com.hfad.myferma.Finance;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class ProductChar {
    private String name;
    private ArrayList<Entry> entries;

   public ProductChar(String name, ArrayList<Entry>  entries ){
        this.name = name;
        this.entries = entries;
    }
    public String getName() {
        return name;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public void setName(String name) {
        this.name = name;
    }

}

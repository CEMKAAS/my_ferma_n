package com.hfad.myferma.AddPackage;

public class ProductDB {

    private int id;

    private  String name;

    private  Double disc;

    private  String data;

    private  int price;

    public ProductDB(int id, String name, Double disc, String data, int price) {
        this.id = id;
        this.name = name;
        this.disc = disc;
        this.data = data;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDisc() {
        return disc;
    }

    public void setDisc(Double disc) {
        this.disc = disc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}



package com.hfad.myferma.AddPackage;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDB implements Parcelable {

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

    protected ProductDB(Parcel in) {
        id = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            disc = null;
        } else {
            disc = in.readDouble();
        }
        data = in.readString();
        price = in.readInt();
    }

    public static final Creator<ProductDB> CREATOR = new Creator<ProductDB>() {
        @Override
        public ProductDB createFromParcel(Parcel in) {
            return new ProductDB(in);
        }

        @Override
        public ProductDB[] newArray(int size) {
            return new ProductDB[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        if (disc == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(disc);
        }
        dest.writeString(data);
        dest.writeInt(price);
    }
}



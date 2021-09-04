package com.synnlabz.scanme;

import java.io.Serializable;

public class Product implements Serializable {
    private String Name;
    private String Net;
    private float Price;
    private String Veg;
    private float Fat;
    private float Salt;
    private float Sugar;
    private String ImgURL;

    public Product(String name, String net, float price, String veg, float fat, float salt, float sugar, String imgURL) {
        Name = name;
        Net = net;
        Price = price;
        Veg = veg;
        Fat = fat;
        Salt = salt;
        Sugar = sugar;
        ImgURL = imgURL;
    }

    public Product() {
        Name = "name";
        Net = "0g";
        Price = 0;
        Veg = "veg";
        Fat = 0;
        Salt = 0;
        Sugar = 0;
        ImgURL = "imgURL";
    }

    public String getImgURL() {
        return ImgURL;
    }

    public void setImgURL(String imgURL) {
        ImgURL = imgURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNet() {
        return Net;
    }

    public void setNet(String net) {
        Net = net;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getVeg() {
        return Veg;
    }

    public void setVeg(String veg) {
        Veg = veg;
    }

    public float getFat() {
        return Fat;
    }

    public void setFat(float fat) {
        Fat = fat;
    }

    public float getSalt() {
        return Salt;
    }

    public void setSalt(float salt) {
        Salt = salt;
    }

    public float getSugar() {
        return Sugar;
    }

    public void setSugar(float sugar) {
        Sugar = sugar;
    }
}

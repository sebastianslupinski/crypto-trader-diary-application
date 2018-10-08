package com.diary.cryptotraderdiaryapplication.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Position {

    private String name;
    private double buyPrice;
    private double sellPrice;
    private Date openDate;
    private Date closeDate;
    private boolean open = true;

    public Position(){
        this.openDate = new Date();
    }

    public String getOpenDate() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(this.openDate);
        return date;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void closePosition(){
        this.open = false;
    }

    @Override
    public String toString() {
        return getOpenDate() + " " + buyPrice + "-> " + name + " -> " + sellPrice;
    }
}

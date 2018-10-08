package com.diary.cryptotraderdiaryapplication.models;

public class Position {

    private String name;
    private double buyPrice;
    private double sellPrice;
    private boolean open = true;

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
        return buyPrice + "-> " + name + " -> " + sellPrice;
    }
}

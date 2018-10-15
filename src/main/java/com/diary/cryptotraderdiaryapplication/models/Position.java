package com.diary.cryptotraderdiaryapplication.models;

import org.apache.commons.math3.util.Precision;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "buy_price")
    private double buyPrice;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "open_date")
    private Date openDate;

    @Column(name = "close_date")
    private Date closeDate;

    @Column(name = "open_status")
    private boolean open;

    public Position(){
        this.openDate = new Date();
        this.open = true;
    }

    public Integer getId() {
        return id;
    }

    public String getPercent(){
        if(this.openDate != null && this.closeDate !=null){
            double percent = (this.sellPrice/this.buyPrice) * 100 -100 ;
            double rounded = Precision.round(percent,1 );
            return String.valueOf(rounded)+"%";
        }
        return "0%";
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getOpenDate() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(this.openDate);
        return date;
    }

    public String getCloseDate() {
        if(this.closeDate != null) {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            String date = DATE_FORMAT.format(this.closeDate);
            return date;
        }
        return "-";
    }

    public void setCloseDate() {
        this.closeDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBuyPrice() {
        return Precision.round(buyPrice,5 );
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
        return getOpenDate() + " " + buyPrice + " -> " + name + " -> " + sellPrice;
    }
}

package com.diary.cryptotraderdiaryapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="budget")
public class Budget implements Comparable<Budget>{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="id")
    private int id;

    @Column
    private boolean added;

    @Column
    private Double frozenBtc;

    @Column
    private Double freeBtc;

    @Column
    private Date actualDate;

    public Budget(){
        this.frozenBtc = 0.0;
        this.freeBtc = 0.0;
    }

    public boolean isAdded(){
        return this.added;
    }

    public Budget(Double frozenBudget, Double freeBudget){
        this.frozenBtc = frozenBudget;
        this.freeBtc = freeBudget;
        this.actualDate = new Date();
    }

    //for testing purpose & manipulating date
    public Budget(Double frozenBudget, Double freeBudget, Date date){
        this.frozenBtc = frozenBudget;
        this.freeBtc = freeBudget;
        this.actualDate = date;
    }

    public void addBudget(Double amount){
        this.freeBtc+=amount;
        this.added = true;
    }

    //this method might be unnecessary
    public void freezeBudget(Double amount){
        if(amount>freeBtc){
            System.out.println("Balance is too low");
            return;
        }

        this.freeBtc-=amount;
        this.frozenBtc+=amount;
    }

    public void unfreezeBudget(Double amountGiven, Double amountReceived){
        this.frozenBtc-=amountGiven;
        this.freeBtc+=amountReceived;

    }

    public Double getGeneralBudget(){
        return roundValue(this.freeBtc + this.frozenBtc);
    }

    public Date getActualDate() {
        return this.actualDate;
    }

    public int getId() {
        return id;
    }

    public Double getFrozenBtc() {
        return roundValue(this.frozenBtc);
    }

    public Double getFreeBtc() {
        return roundValue(this.freeBtc);
    }

    public double roundValue(Double value){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int compareTo(Budget bud){
        return getActualDate().compareTo(bud.getActualDate());
    }
}

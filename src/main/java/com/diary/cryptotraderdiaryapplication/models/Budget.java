package com.diary.cryptotraderdiaryapplication.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name="budget")
public class Budget {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name="id")
    private int Id;

    @Column
    private Double frozenBtc;

    @Column
    private Double freeBtc;

    @Column
    private Date actualDate;

    public Budget(){
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
    }

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
        return this.freeBtc+this.frozenBtc;
    }


}

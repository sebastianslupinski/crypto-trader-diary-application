package com.diary.cryptotraderdiaryapplication.models;

public class Budget {

    private Double frozenBtc;
    private Double freeBtc;

    public Budget(){
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

    
}

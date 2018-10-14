package com.diary.cryptotraderdiaryapplication.models;

import java.util.*;

public class Statistics {

    public static List<Budget> budgets = new ArrayList<>();

    public static List getUniqueBudgets(){
        HashMap<Date, Budget> mapa = new HashMap<>();
        for(Budget budget : budgets){
            if(mapa.containsKey(budget.getActualDate())) {
                continue;
            }
            else {
                mapa.put(budget.getActualDate(),budget);
            }
        }

        List<Budget> uniqueBudgets = new ArrayList<>();
        for(Map.Entry entry : mapa.entrySet()){
            uniqueBudgets.add((Budget) entry.getValue());
        }
        Collections.sort(uniqueBudgets);
        return uniqueBudgets;
    }
}

package com.diary.cryptotraderdiaryapplication.models;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Statistics {

    private static List<Budget> budgets = new ArrayList<>();

    //gets unique budgets conditions, unique - only one per day
    private static List getUniqueBudgets(){
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

    //calculate average percentage income or outcome per day
    public static Double getAveragePercent(){
        List<Budget> uniqueBudgets = getUniqueBudgets();
        double sumOfPercent = 0;
        double average = 0;
        for(int i = 0 ; i<uniqueBudgets.size() ; i++){
            if(i==0){
                continue;
            }
            double percent = (uniqueBudgets.get(i).getGeneralBudget()/uniqueBudgets.get(i-1).getGeneralBudget()) *100 -100;
            sumOfPercent+=percent;
        }

        long sumOfDays = getDifferenceDays();
        average = sumOfPercent/sumOfDays;
        return average;
    }

    private static long getDifferenceDays() {
        Date oldestDate = findOldestBudget().getActualDate();
        Date newestDate = findNewestBudget().getActualDate();
        long diff = newestDate.getTime() - oldestDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static Budget findOldestBudget(){
        Collections.sort(budgets);
        System.out.println(budgets.get(0).getActualDate().toString());
        return budgets.get(0);
    }

    private static Budget findNewestBudget(){
        Collections.sort(budgets);
        System.out.println(budgets.get(budgets.size()-1).getActualDate().toString());
        return budgets.get(budgets.size()-1);
    }

}

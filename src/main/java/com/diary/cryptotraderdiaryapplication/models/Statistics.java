package com.diary.cryptotraderdiaryapplication.models;

import com.diary.cryptotraderdiaryapplication.dao.BudgetDao;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Statistics {

    private List<Budget> budgets;

    public Statistics(List<Budget> budgets){
        this.budgets = budgets;
    }

    //gets unique budgets conditions, unique - only one per day
    private List getUniqueBudgets(){
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
    public Double getAveragePercent(){
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
        return Precision.round(average,1 );
    }

    private long getDifferenceDays() {
        Date oldestDate = findOldestBudget().getActualDate();
        Date newestDate = findNewestBudget().getActualDate();
        long diff = newestDate.getTime() - oldestDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private Budget findOldestBudget(){
        Collections.sort(budgets);
        System.out.println(budgets.get(0).getActualDate().toString());
        return budgets.get(0);
    }

    public Budget findNewestBudget(){
        if(budgets.size()==0){
            return new Budget();
        }
        Collections.sort(budgets);
        return budgets.get(budgets.size()-1);
    }
}

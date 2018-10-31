package com.diary.cryptotraderdiaryapplication.models;

import com.diary.cryptotraderdiaryapplication.dao.BudgetDao;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Statistics {

    private List<Budget> budgets;

    public Statistics(List<Budget> budgets){
        this.budgets = budgets;
    }

    public Double getDaysPrediction(int days){

        Double averageDayPercent = Double.valueOf(getAveragePercent());
        averageDayPercent = averageDayPercent+100;
        averageDayPercent = averageDayPercent/100;
        Double result = findNewestBudget().getGeneralBudget();

        for(int i = 1 ; i<=days ; i++){
            result = result*averageDayPercent;
        }

        return roundValue(result);
    }


    public String getAveragePercent(){

        if(budgets.size()==0){
            return "0";
        }

        double sumOfPercent = 0;
        double sumOfPercentInDay = 0;
        double average;
        Budget firstBudgetInDay = null;
        Double addedBudgetToIgnore = 0.0;
        for(int i = 0 ; i<budgets.size() ; i++){
            if(i==0){
                firstBudgetInDay = budgets.get(i);
            }
            else if(areSameDay(firstBudgetInDay.getActualDate(),budgets.get(i).getActualDate())){
                if(budgets.get(i).isChanged()){
                    addedBudgetToIgnore+=budgets.get(i).getGeneralBudget()-budgets.get(i-1).getGeneralBudget();
                }
            }
            else{
                firstBudgetInDay = budgets.get(i);
                if(budgets.get(i).isChanged()){
                    addedBudgetToIgnore+=budgets.get(i).getGeneralBudget()-budgets.get(i-1).getGeneralBudget();
                }
                sumOfPercentInDay += (((budgets.get(i).getGeneralBudget()-addedBudgetToIgnore)/budgets.get(i-1).getGeneralBudget()) *100) -100;
                addedBudgetToIgnore = 0.0;
                sumOfPercent += sumOfPercentInDay;
                sumOfPercentInDay = 0;
            }
            if(i>0 && i==budgets.size()-1 && (firstBudgetInDay!=budgets.get(i)) && areSameDay(firstBudgetInDay.getActualDate(),budgets.get(i).getActualDate())){
                sumOfPercent += (((budgets.get(i).getGeneralBudget()-addedBudgetToIgnore)/firstBudgetInDay.getGeneralBudget()) *100) -100;
            }
        }

        long sumOfDays = getDifferenceDays();
        if(sumOfDays==0){ sumOfDays=1; }
        average = sumOfPercent/sumOfDays;
        double averageRounded = Precision.round(average,1 );
        return String.valueOf(averageRounded);
    }

    public static boolean areSameDay(Date date1, Date date2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        return sameDay;
    }

    private long getDifferenceDays() {
        Date oldestDate = findOldestBudget().getActualDate();
        Date newestDate = findNewestBudget().getActualDate();
        long diff = newestDate.getTime() - oldestDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private Budget findOldestBudget(){
        Collections.sort(budgets);
        return budgets.get(0);
    }

    public Budget findNewestBudget(){
        if(budgets.size()==0){
            return new Budget();
        }
        Collections.sort(budgets);
        return budgets.get(budgets.size()-1);
    }

    public Boolean checkIfBuyingIsPossible(Double buyPrice){
        Budget latestBudget = findNewestBudget();
        if(latestBudget.getFreeBtc()<buyPrice){
            return false;
        }
        return true;
    }

    public double roundValue(Double value){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(6, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}

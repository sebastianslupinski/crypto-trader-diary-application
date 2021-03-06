package com.diary.cryptotraderdiaryapplication.controllers;

import com.diary.cryptotraderdiaryapplication.dao.BudgetDao;
import com.diary.cryptotraderdiaryapplication.dao.PositionDao;
import com.diary.cryptotraderdiaryapplication.models.Budget;
import com.diary.cryptotraderdiaryapplication.models.Position;
import com.diary.cryptotraderdiaryapplication.models.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private BudgetDao budgetDao;

    @RequestMapping(value="/add-trade", method = RequestMethod.GET)
    public String addTrade(){
        return "add-trade";
    }

    @RequestMapping(value="/add-budget", method = RequestMethod.GET)
    public String addBudget(){
        return "add-budget";
    }

    @RequestMapping(value="/edit-trade", method = RequestMethod.GET)
    public String editTrade(){
        return "edit-trade";
    }

    @RequestMapping(value="/decrease-budget", method = RequestMethod.GET)
    public String decreaseBudget(Model model){
        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latest = latestStatistics.findNewestBudget();
        model.addAttribute("btcFree",latest.getFreeBtc());
        return "decrease-budget";
    }

    @RequestMapping(value = "/statement-site", method = RequestMethod.POST)
    public String showStatementAfterDecreasingBudget(HttpServletRequest request, Model model){

        if(request.getParameter("budget").length()==0){
            model.addAttribute("Message","You must provide some btc value" );
            return "statement-site";
        }

        Double decreasedValue = Double.valueOf(request.getParameter("budget"));

        if(decreasedValue<0){
            model.addAttribute("Message","Value must be positive" );
            return "statement-site";
        }

        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();

        if(decreasedValue>latestBudget.getFreeBtc()){
            model.addAttribute("Message","You don't have enough free BTC to \"withdraw\" such amount" );
            return "statement-site";
        }

        Budget newBudget = new Budget(latestBudget.getFrozenBtc(), latestBudget.getFreeBtc());
        newBudget.decreaseBudget(decreasedValue);

        budgetDao.save(newBudget);

        model.addAttribute("Message","You decreased your budget successfully" );
        return "statement-site";
    }

    @RequestMapping(value="/main-site", method = RequestMethod.GET)
    public String showMainSite(Model model){

        //get all trades
        List<Position> allTrades = positionDao.findAll();
        model.addAttribute("trades",allTrades );
        return "main-site";
    }

    @RequestMapping(value="/active-trades", method = RequestMethod.GET)
    public String showActiveTrades(Model model){

        //get all trades
        List<Position> openTrades = positionDao.findByOpen(true);
        model.addAttribute("trades",openTrades );
        return "active-trades";
    }

    @RequestMapping(value="/statistics", method = RequestMethod.GET)
    public String showStatistics(Model model){

//        Budget testBudget = new Budget(0.6456,0.456);
        //get all trades
        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();
        model.addAttribute("latestBudget",latestBudget);
        model.addAttribute("freeBudget",latestBudget.getFreeBtc());
        model.addAttribute("frozenBudget",latestBudget.getFrozenBtc());
        model.addAttribute("averageDay", latestStatistics.getAveragePercent()+"%");
        model.addAttribute("prediction30", latestStatistics.getDaysPrediction(30));
        model.addAttribute("prediction90", latestStatistics.getDaysPrediction(90));
        model.addAttribute("prediction180", latestStatistics.getDaysPrediction(180));
        model.addAttribute("prediction360", latestStatistics.getDaysPrediction(360));

        return "statistics";
    }

    @RequestMapping(value="/statistics", method = RequestMethod.POST)
    public String showStatementAfterAddingBudget(HttpServletRequest request, Model model){

        if(request.getParameter("budget").length()==0){
            model.addAttribute("Message","You must provide some value");
            return "statement-site";
        }

        Double addedBudget = Double.valueOf(request.getParameter("budget"));

        if(addedBudget<0){
            model.addAttribute("Message","You cannot add negative number");
            return "statement-site";
        }

        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();

        //creating new instance of budget and fulfill it with
        //latest state of btc amount
        Budget newBudget = new Budget(latestBudget.getFrozenBtc(), latestBudget.getFreeBtc());
        newBudget.addBudget(addedBudget);
        budgetDao.save(newBudget);

        model.addAttribute("Message","You increased your budget successfully");
        return "statement-site";
    }


    @RequestMapping(value="/close-trade", method = RequestMethod.GET)
    public String closeTrade(Model model){

        //get all trades
        List<Position> openTrades = positionDao.findByOpen(true);

        if(openTrades.size()==0){
            model.addAttribute("Message","You don't have any opened positions" );
            return "statement-site";
        }

        model.addAttribute("trades",openTrades );
        return "close-trade";
    }

    @RequestMapping(value="/edit-coin-name", method = RequestMethod.GET)
    public String editCoinName(Model model){

        //get all trades
        List<Position> allTrades = positionDao.findAll();

        if(allTrades.size()==0){
            model.addAttribute("Message","You don't have any positions" );
            return "statement-site";
        }

        model.addAttribute("trades",allTrades );
        return "edit-coin-name";
    }

    @RequestMapping(value="/edit-coin-name", method = RequestMethod.POST)
    public String submitNewCoinName(HttpServletRequest request, Model model){

        if((request.getParameter("name").length()==0) || (request.getParameter("trade").length()==0)){
            model.addAttribute("Message","Trade not selected or name not provided");
            return "statement-site";
        }

        Integer id = Integer.valueOf(request.getParameter("trade"));
        Position tradeToEdit = positionDao.findById(id);

        tradeToEdit.setName(request.getParameter("name"));
        positionDao.save(tradeToEdit);

        model.addAttribute("Message","Coin name edited successfully !");

        return "statement-site";
    }

    @RequestMapping(value="/edit-sell-price", method = RequestMethod.GET)
    public String editSellPrice(Model model){

        //get all trades
        List<Position> allTrades = positionDao.findByOpen(false);

        if(allTrades.size()==0){
            model.addAttribute("Message","You don't have any closed positions" );
            return "statement-site";
        }

        model.addAttribute("trades",allTrades );
        return "edit-sell-price";
    }

    @RequestMapping(value="/edit-sell-price", method = RequestMethod.POST)
    public String submitEditingSellPrice(HttpServletRequest request, Model model){

        if((request.getParameter("sell price").length()==0) || (request.getParameter("trade").length()==0)){
            model.addAttribute("Message","Trade not selected or sell price not given");
            return "statement-site";
        }

        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();

        Integer id = Integer.valueOf(request.getParameter("trade"));
        Position tradeToEdit = positionDao.findById(id);

        Double difference = tradeToEdit.getSellPrice() - Double.valueOf(request.getParameter("sell price"));

        latestBudget.updateFreeBudget(-difference);
        budgetDao.save(latestBudget);
        positionDao.save(tradeToEdit);

        model.addAttribute("Message","Trade sell price edited successfully !");

        return "statement-site";
    }

    @RequestMapping(value="/edit-open-price", method = RequestMethod.GET)
    public String editOpenPrice(Model model){

        //get all trades
        List<Position> allTrades = positionDao.findByOpen(true);

        if(allTrades.size()==0){
            model.addAttribute("Message","You don't have any opened positions" );
            return "statement-site";
        }

        model.addAttribute("trades",allTrades );
        return "edit-open-price";
    }

    @RequestMapping(value="/edit-open-price", method = RequestMethod.POST)
    public String submitEditingOpenPrice(HttpServletRequest request, Model model){

        if((request.getParameter("open price").length()==0) || (request.getParameter("trade").length()==0)){
            model.addAttribute("Message","Trade not selected or open price not given");
            return "statement-site";
        }

        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();

        Integer id = Integer.valueOf(request.getParameter("trade"));
        Position tradeToEdit = positionDao.findById(id);

        Double editedOpenPrice = Double.valueOf(request.getParameter("open price"));

        if(editedOpenPrice>tradeToEdit.getBuyPrice()){
            if(editedOpenPrice-tradeToEdit.getBuyPrice()>latestBudget.getFreeBtc()){
                model.addAttribute("Message","You don't have enough free btc");
                return "statement-site";
            }
        }

        latestBudget.unfreezeBudget(tradeToEdit.getBuyPrice(), tradeToEdit.getBuyPrice());
        tradeToEdit.setBuyPrice(editedOpenPrice);
        latestBudget.freezeBudget(editedOpenPrice);
        budgetDao.save(latestBudget);
        positionDao.save(tradeToEdit);

        model.addAttribute("Message","Trade open price edited successfully !");

        return "statement-site";
    }

    @RequestMapping(value="/closed-trades", method = RequestMethod.GET)
    public String showClosedTrades(Model model){

        //get all trades
        List<Position> closedTrades = positionDao.findByOpen(false);
        model.addAttribute("trades",closedTrades );
        return "closed-trades";
    }

    @RequestMapping(value="/trade-closed", method = RequestMethod.POST)
    public String processClosedTrade(HttpServletRequest request, Model model){

        if((request.getParameter("sell price").length()==0) || (request.getParameter("trade").length()==0)){
            model.addAttribute("Message","Trade not selected or selling price not given");
            return "statement-site";
        }

        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();
        Budget updatedBudget = new Budget(latestBudget.getFrozenBtc(), latestBudget.getFreeBtc());

        Integer id = Integer.valueOf(request.getParameter("trade"));
        Position tradeToClose = positionDao.findById(id);

        Double sellPrice = Double.valueOf(request.getParameter("sell price"));

        updatedBudget.unfreezeBudget(tradeToClose.getBuyPrice(),sellPrice);

        tradeToClose.setCloseDate();
        tradeToClose.setSellPrice(sellPrice);
        tradeToClose.closePosition();
        positionDao.save(tradeToClose);
        budgetDao.save(updatedBudget);

        return "trade-closed";
    }

    @RequestMapping(value="/main-site", method = RequestMethod.POST)
    public String showMainSiteAfterAdding(HttpServletRequest request, Model model){

        String name = request.getParameter("name");
        Double buyPrice = Double.valueOf(request.getParameter("buy price"));

        Statistics latestStatistics = new Statistics(budgetDao.findAll());

        if(!latestStatistics.checkIfBuyingIsPossible(buyPrice)){
            model.addAttribute("Message","Your budget is too small" );
            return "statement-site";
        }

        Position newTrade = new Position();
        newTrade.setName(name);
        newTrade.setBuyPrice(buyPrice);

        positionDao.save(newTrade);
        updateBudgetAfterTradeOpening(buyPrice);

        //get all trades
        List<Position> allTrades = positionDao.findAll();
        model.addAttribute("trades",allTrades );
        return "main-site";
    }

    public void updateBudgetAfterTradeOpening(Double buyPrice){
        Statistics latestStatistics = new Statistics(budgetDao.findAll());
        Budget latestBudget = latestStatistics.findNewestBudget();
        latestBudget.freezeBudget(buyPrice);
        budgetDao.save(latestBudget);
    }
}

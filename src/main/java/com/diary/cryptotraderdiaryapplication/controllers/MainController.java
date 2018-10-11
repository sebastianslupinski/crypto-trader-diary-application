package com.diary.cryptotraderdiaryapplication.controllers;

import com.diary.cryptotraderdiaryapplication.dao.PositionDao;
import com.diary.cryptotraderdiaryapplication.models.Position;
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

    @RequestMapping(value="/add-trade.html", method = RequestMethod.GET)
    public String addTrade(){
        return "add-trade";
    }

    @RequestMapping(value="/main-site.html", method = RequestMethod.GET)
    public String showMainSite(Model model){

        //get all trades
        List<Position> allTrades = positionDao.findAll();
        model.addAttribute("trades",allTrades );
        return "main-site";
    }

    @RequestMapping(value="/active-trades.html", method = RequestMethod.GET)
    public String showActiveTrades(Model model){

        //get all trades
        List<Position> openTrades = positionDao.findByOpen(true);
        model.addAttribute("trades",openTrades );
        return "active-trades";
    }

    @RequestMapping(value="/close-trade.html", method = RequestMethod.GET)
    public String closeTrade(Model model){

        //get all trades
        List<Position> openTrades = positionDao.findByOpen(true);
        model.addAttribute("trades",openTrades );
        return "close-trade";
    }

    @RequestMapping(value="/main-site.html", method = RequestMethod.POST)
    public String showMainSiteAfterAdding(HttpServletRequest request, Model model){
        String name = request.getParameter("name");
        Double buyPrice = Double.valueOf(request.getParameter("buy price"));

        Position newTrade = new Position();
        newTrade.setName(name);
        newTrade.setBuyPrice(buyPrice);

        positionDao.save(newTrade);

        //get all trades
        List<Position> allTrades = positionDao.findAll();
        model.addAttribute("trades",allTrades );
        return "main-site";
    }
}

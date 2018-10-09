package com.diary.cryptotraderdiaryapplication.controllers;

import com.diary.cryptotraderdiaryapplication.models.Position;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @RequestMapping(value="/trades", method = RequestMethod.GET)
    public String addTrade(){
        return "helloform";
    }

    @RequestMapping(value="/trades", method = RequestMethod.POST)
    public String showTrades(HttpServletRequest request, Model model){

        String name = request.getParameter("name");
        Double buyPrice = Double.valueOf(request.getParameter("buy price"));

        Position newTrade = new Position();
        newTrade.setName(name);
        newTrade.setBuyPrice(buyPrice);

        model.addAttribute("trade", newTrade.toString());
        return "hello";
    }
}

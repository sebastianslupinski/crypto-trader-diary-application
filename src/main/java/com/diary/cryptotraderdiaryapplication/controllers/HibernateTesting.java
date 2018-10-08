package com.diary.cryptotraderdiaryapplication.controllers;


import com.diary.cryptotraderdiaryapplication.models.Position;

public class HibernateTesting {

    public static void createPosition(){
        String name = "BTC";
        Position pozycja = new Position();
        pozycja.setName(name);
        System.out.println(pozycja);
    }
}

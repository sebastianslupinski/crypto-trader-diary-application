package com.diary.cryptotraderdiaryapplication;

import com.diary.cryptotraderdiaryapplication.controllers.HibernateTesting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptoTraderDiaryApplication {

	public static void main(String[] args) {
		HibernateTesting.createPosition();
		System.out.println("dupa");
		SpringApplication.run(CryptoTraderDiaryApplication.class, args);
	}
}

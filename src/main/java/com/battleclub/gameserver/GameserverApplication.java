package com.battleclub.gameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {".", "player","user","utils"})
public class GameserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameserverApplication.class, args);
	}

}

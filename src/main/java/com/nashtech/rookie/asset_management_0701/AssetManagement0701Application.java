package com.nashtech.rookie.asset_management_0701;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AssetManagement0701Application {

	public static void main(String[] args) {
		SpringApplication.run(AssetManagement0701Application.class, args);
	}

}

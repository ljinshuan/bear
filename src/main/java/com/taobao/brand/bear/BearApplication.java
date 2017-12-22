package com.taobao.brand.bear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.taobao.brand"})
public class BearApplication {

	public static void main(String[] args) {
		SpringApplication.run(BearApplication.class, args);
	}
}

package com.draculavenom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class NotificationServiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceSystemApplication.class, args);
	}

}

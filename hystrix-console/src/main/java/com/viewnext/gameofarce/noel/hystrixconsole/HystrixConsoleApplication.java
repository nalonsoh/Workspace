package com.viewnext.gameofarce.noel.hystrixconsole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableHystrixDashboard
//@EnableTurbineStream
@EnableTurbine
public class HystrixConsoleApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HystrixConsoleApplication.class, args);
	}

}

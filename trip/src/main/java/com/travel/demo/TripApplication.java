package com.travel.demo;

//import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.travel.demo.*.mapper")
public class TripApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripApplication.class, args);
	}

}

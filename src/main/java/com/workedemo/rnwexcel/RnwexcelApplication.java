package com.workedemo.rnwexcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.workedemo.rnwexcel.*"})
public class RnwexcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(RnwexcelApplication.class, args);
    }

}

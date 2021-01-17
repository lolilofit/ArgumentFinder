package ru.nsu.usova.dipl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories("ru.nsu.usova.dipl.situation")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

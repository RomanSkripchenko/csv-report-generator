package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "ru.otus")
public class ReportGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReportGeneratorApplication.class, args);
    }
}

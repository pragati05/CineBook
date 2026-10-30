package com.cinebook.insights;

import org.springframework.boot.SpringApplication;

public class TestInsightsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.from(InsightsServiceApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}

package com.isp.sitesurvey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SiteSurveyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiteSurveyApplication.class, args);
    }
}

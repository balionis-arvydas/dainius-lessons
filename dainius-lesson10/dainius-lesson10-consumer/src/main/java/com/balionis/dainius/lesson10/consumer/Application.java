package com.balionis.dainius.lesson10.consumer;

import com.balionis.dainius.lesson10.consumer.configuration.AppConfigurationProperties;
import com.balionis.dainius.lesson10.consumer.configuration.KafkaConsumerConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppConfigurationProperties.class, KafkaConsumerConfigurationProperties.class})
@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("starting");

        SpringApplication.run(Application.class, args);

        log.info("finishing");
    }
}


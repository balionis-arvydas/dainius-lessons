package com.balionis.dainius.lesson8.consumer.configuration;

import com.balionis.dainius.lesson8.consumer.service.ConsumerService;
import com.balionis.dainius.lesson8.consumer.service.HeartbeatService;
import com.balionis.dainius.lesson8.consumer.stream.mappers.KafkaMessageMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final AppConfigurationProperties appConfigurationProperties;

    @Bean
    public HeartbeatService heartbeatService() {
        return new HeartbeatService(appConfigurationProperties.getName());
    }

    @Bean
    public ConsumerService consumerService() {
        return new ConsumerService(appConfigurationProperties.getPageSize());
    }

    @Bean
    public KafkaMessageMapper kafkaMessageMapper() {
        return Mappers.getMapper(KafkaMessageMapper.class);
    }

}

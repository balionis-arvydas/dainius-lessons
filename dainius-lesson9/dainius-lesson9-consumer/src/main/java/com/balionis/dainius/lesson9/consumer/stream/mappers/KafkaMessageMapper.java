package com.balionis.dainius.lesson9.consumer.stream.mappers;

import com.balionis.dainius.lesson9.consumer.generated.model.Message;
import com.balionis.dainius.lesson9.consumer.stream.model.KafkaMessage;
import org.mapstruct.Mapper;

@Mapper(implementationPackage = "com.balionis.dainius.lesson9.consumer.stream.mappers.impl")
public interface KafkaMessageMapper {
    Message apply(KafkaMessage message);
}

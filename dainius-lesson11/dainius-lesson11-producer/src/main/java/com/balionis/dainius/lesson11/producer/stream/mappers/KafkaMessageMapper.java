package com.balionis.dainius.lesson11.producer.stream.mappers;

import com.balionis.dainius.lesson11.producer.generated.model.SendMessageRequest;
import com.balionis.dainius.lesson11.avro.generated.v1.KafkaMessage;

public class KafkaMessageMapper {
    public KafkaMessage apply(SendMessageRequest message) {
        return KafkaMessage.newBuilder()
                .setMessageId(message.getMessageId().toString())
                .setMessage(message.getMessage())
                .build();
    }
}

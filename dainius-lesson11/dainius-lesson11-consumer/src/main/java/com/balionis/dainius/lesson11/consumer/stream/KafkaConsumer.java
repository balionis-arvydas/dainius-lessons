package com.balionis.dainius.lesson11.consumer.stream;

import com.balionis.dainius.lesson11.avro.generated.v1.KafkaMessage;
import com.balionis.dainius.lesson11.consumer.service.ConsumerService;
import com.balionis.dainius.lesson11.consumer.stream.mappers.KafkaMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @Autowired
    private final ConsumerService consumerService;

    @Autowired
    private final KafkaMessageMapper kafkaMessageMapper;

    @KafkaListener(topics = "${app.service.kafka.topic-name}", containerFactory = "messageContainerFactory")
    public void listen(ConsumerRecord<String, KafkaMessage> record) {
        log.info("record.key={}, record.value={}", record.key(), record.value());
        var message = kafkaMessageMapper.apply(record.value());
        consumerService.addMessage(message);
    }
}

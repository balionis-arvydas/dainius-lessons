package com.balionis.dainius.lesson11.consumer.stream;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.ONE_SECOND;
import static org.awaitility.Durations.FIVE_SECONDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.balionis.dainius.lesson11.avro.generated.v1.KafkaMessage;
import com.balionis.dainius.lesson11.consumer.Application;
import com.balionis.dainius.lesson11.consumer.generated.model.Message;
import com.balionis.dainius.lesson11.consumer.service.ConsumerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"})
@SpringBootTest(classes = {Application.class})
@DirtiesContext
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Value("${app.service.kafka.topic-name}")
    private String topicName;

    @MockBean
    private ConsumerService consumerService;

    @Test
    void should_listen_success() {

        var message = KafkaMessage.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setMessage("hello")
                .build();
        kafkaTemplate.send(topicName, message.getMessageId().toString(), message);

        await().pollDelay(ONE_SECOND).atMost(FIVE_SECONDS)
                .untilAsserted(() -> verify(consumerService).addMessage(any(Message.class)));

    }

}
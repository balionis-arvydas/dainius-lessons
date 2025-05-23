package com.balionis.dainius.lesson10.producer.rest;

import com.balionis.dainius.lesson10.producer.generated.api.ProducerApi;
import com.balionis.dainius.lesson10.producer.generated.model.SendMessageRequest;
import com.balionis.dainius.lesson10.producer.service.ProducerService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController implements ProducerApi {

    private final ProducerService producerService;

    @Override
    public ResponseEntity<Void> sendMessage(
            @Parameter(name = "SendMessageRequest", description = "") @Valid @RequestBody(required = false) SendMessageRequest sendMessageRequest
    ) {
        log.info("sendMessageRequest={}", sendMessageRequest);
        producerService.sendMessage(sendMessageRequest);
        return ResponseEntity.ok().build();
    }
}

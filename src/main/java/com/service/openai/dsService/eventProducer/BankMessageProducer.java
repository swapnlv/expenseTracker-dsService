package com.service.openai.dsService.eventProducer;

import com.service.openai.dsService.model.PaymentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BankMessageProducer {

    private final KafkaTemplate<String, PaymentDetails> kafkaTemplate;


    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    public BankMessageProducer(KafkaTemplate<String, PaymentDetails> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(PaymentDetails paymentDetails) {
        Message<PaymentDetails> message = MessageBuilder.withPayload(paymentDetails)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME).build();
        kafkaTemplate.send(message);
    }
}

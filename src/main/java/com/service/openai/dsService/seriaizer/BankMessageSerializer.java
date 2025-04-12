package com.service.openai.dsService.seriaizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.openai.dsService.model.PaymentDetails;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class BankMessageSerializer implements Serializer<PaymentDetails>{


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, PaymentDetails data) {
        byte[] resultVal=null;
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            resultVal=objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVal;

    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}

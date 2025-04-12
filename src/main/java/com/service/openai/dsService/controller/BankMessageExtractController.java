package com.service.openai.dsService.controller;

import com.service.openai.dsService.model.BankMessageRequest;
import com.service.openai.dsService.model.BankMessageResponse;
import com.service.openai.dsService.service.OpenAiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/bankMessage")
@RestController
public class BankMessageExtractController {


    @Autowired
    private  OpenAiServiceImpl bankMessageService;

    @PostMapping("/extract")
    public ResponseEntity<BankMessageResponse> extractPaymentDetails(@RequestBody BankMessageRequest request) {
        BankMessageResponse response = bankMessageService.extractPaymentDetails(request);
        return ResponseEntity.ok(response);
    }
}

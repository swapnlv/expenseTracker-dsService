package com.service.openai.dsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankMessageResponse {
    private PaymentDetails extractedInfo;
}

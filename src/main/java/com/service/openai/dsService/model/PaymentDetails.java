package com.service.openai.dsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    private String amount;
    private String transactionType;
    private String accountNumber;
    private String transactionId;
    private String date;
    private String merchant;
}

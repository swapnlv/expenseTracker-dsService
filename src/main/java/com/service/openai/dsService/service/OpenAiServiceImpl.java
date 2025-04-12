package com.service.openai.dsService.service;

import com.service.openai.dsService.eventProducer.BankMessageProducer;
import com.service.openai.dsService.model.BankMessageRequest;
import com.service.openai.dsService.model.BankMessageResponse;
import com.service.openai.dsService.model.PaymentDetails;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl {

    private final OpenAiService openAiService;

    @Autowired
    private BankMessageProducer bankMessageProducer;

    public BankMessageResponse extractPaymentDetails(BankMessageRequest request) {
        String prompt = "Extract the payment information from the following bank message:\n" +
                request.getMessage() +
                "\n\nExtract details like Amount, Transaction Type (Credit/Debit), Account Number, Transaction ID, Date.";

        // Use "user" role for actual input messages
        ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new ChatMessage("user", prompt)))
                .maxTokens(200)
                .temperature(0.2)
                .n(1)
                .build();

        int retryCount = 0;
        int maxRetries = 5;
        long waitTime = 1000; // Start with 1 second

        while (retryCount < maxRetries) {
            try {
                // Send request to OpenAI
                ChatCompletionResult response = openAiService.createChatCompletion(chatRequest);

                // Extract the response text from the first choice
                String result = response.getChoices().isEmpty() ?
                        "No information extracted." :
                        response.getChoices().get(0).getMessage().getContent().trim();

                PaymentDetails paymentDetails=extractPaymentDetails(result);
                bankMessageProducer.sendEventToKafka(paymentDetails);
                return new BankMessageResponse(paymentDetails);

            } catch (HttpException e) {
                if (e.code() == 429 || e.code() == 500) { // Retry for rate limits & internal server errors
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    waitTime *= 2; // Exponential backoff
                    retryCount++;
                } else {
                    throw e; // If it's another type of error, do not retry
                }
            }
        }

        throw new RuntimeException("Exceeded maximum retries for OpenAI API request");
    }

    public PaymentDetails extractPaymentDetails(String request) {
        String amount = extractAttributes(request, "Amount:\\s*(.*)");
        String transactionType = extractAttributes(request, "Transaction Type:\\s*(.*)");
        String accountNumber = extractAttributes(request, "Account Number:\\s*(.*)");
        String transactionId = extractAttributes(request, "Transaction ID:\\s*(.*)");
        String date = extractAttributes(request, "Date:\\s*(.*)");
        String merchant = extractAttributes(request, "Merchant:\\s*(.*)");

        PaymentDetails paymentDetails=new PaymentDetails();
        paymentDetails.setDate(date);
        paymentDetails.setAmount(amount);
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setTransactionId(transactionId);
        paymentDetails.setTransactionType(transactionType);
        paymentDetails.setMerchant(merchant);
        return paymentDetails;
    }

    public String extractAttributes(String request, String regex) {
        Pattern pattern=Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher=pattern.matcher(request);

        return matcher.find() ? matcher.group(1).trim() : "Not Provided";

    }

}

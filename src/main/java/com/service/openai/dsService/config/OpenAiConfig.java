package com.service.openai.dsService.config;

import com.service.openai.dsService.service.SecretManagerService;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    private final SecretManagerService secretsManagerService;

    @Value("${openai.api.key}")
    private String openAiApiKey;


    @Autowired
    public OpenAiConfig(SecretManagerService secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
        initApiKey();
    }

    @PostConstruct
    private void initApiKey() {
        String secretJson = secretsManagerService.getSecret("expenseTracker/openai");
        JSONObject jsonObject = new JSONObject(secretJson);
        this.openAiApiKey = jsonObject.getString("OPENAI_API_KEY");
    }

    @Bean
    public OpenAiService openAiService() {
        String apiKey = openAiApiKey;
//        System.out.println(apiKey);
        return new OpenAiService(apiKey);
    }

    @PostConstruct
    public void init() {
        System.out.println("OpenAI API Key: " + openAiApiKey);
    }
}
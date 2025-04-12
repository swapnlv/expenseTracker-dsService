package com.service.openai.dsService.config;

import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {
    @Value("${openai.ai.api.key}")
    private String openApiKey;

    @Bean
    public OpenAiService openAiService() {
        String apiKey = openApiKey;
        System.out.println(apiKey);
        return new OpenAiService(apiKey);
    }

    @PostConstruct
    public void init() {
        System.out.println("OpenAI API Key: " + openApiKey);
    }
}
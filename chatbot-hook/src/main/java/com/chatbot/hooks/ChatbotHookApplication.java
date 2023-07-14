package com.chatbot.hooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.google.api.client.json.gson.GsonFactory;

@SpringBootApplication
@EnableFeignClients
public class ChatbotHookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotHookApplication.class, args);
	}

    @Bean
    GsonFactory getGsonFactory() {
        return new GsonFactory();
    }

}

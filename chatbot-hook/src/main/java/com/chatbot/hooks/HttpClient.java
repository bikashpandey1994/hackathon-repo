package com.chatbot.hooks;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chatbot.hooks.models.SentimentRequest;
import com.chatbot.hooks.models.SentimentResponse;

@FeignClient(name = "sentimentService", url = "${sentiment.service.url}")
public interface HttpClient {

	@RequestMapping(method = RequestMethod.POST, value = "/textSentimentAnalysis", produces = "application/json")
	public SentimentResponse sentimentAnalysis(@RequestBody SentimentRequest request);
}

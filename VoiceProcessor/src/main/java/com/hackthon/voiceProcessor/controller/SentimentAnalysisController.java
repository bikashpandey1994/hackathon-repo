package com.hackthon.voiceProcessor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackthon.voiceProcessor.SentimentAnalysisService;
import com.hackthon.voiceProcessor.SentimentRequest;
import com.hackthon.voiceProcessor.SentimentRequestText;

@RestController
public class SentimentAnalysisController {

	@Autowired
	private SentimentAnalysisService sentimentAnalysisService;
	
	@PostMapping("/analyzeSentiment")
	public String analyzeSentimentFromText(@RequestBody SentimentRequest sentimentRequest) throws IOException {
		
		return sentimentAnalysisService.analyzeSentiment(sentimentRequest);
		
	}
	
	@PostMapping("/textSentimentAnalysis")
	public void analyzeSentimentFromTextReq(@RequestBody SentimentRequestText sentimentRequestText) throws IOException {
		
		sentimentAnalysisService.analyzeSentimentText(sentimentRequestText);
		
		System.out.println("Records updated Succefully based on sentiment derived");
		
	}
	
	@GetMapping("/getRecommendedProducts")
	public List<String> getRecommendedProducts(@RequestParam String uuid) throws IOException {
		
		return sentimentAnalysisService.getRecommendedProducts(uuid);
		
	}
}

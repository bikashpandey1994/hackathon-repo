package com.chatbot.hooks.models;

public class SentimentResponse {
	
	private String requirementId;
	
	private String text;
	
	private String sentimentScore;
	
	private String sentimentMagnitude;
	
	private String suggestedProductIds;

	public String getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(String sentimentScore) {
		this.sentimentScore = sentimentScore;
	}

	public String getSentimentMagnitude() {
		return sentimentMagnitude;
	}

	public void setSentimentMagnitude(String sentimentMagnitude) {
		this.sentimentMagnitude = sentimentMagnitude;
	}

	public String getSuggestedProductIds() {
		return suggestedProductIds;
	}

	public void setSuggestedProductIds(String suggestedProductIds) {
		this.suggestedProductIds = suggestedProductIds;
	}

}

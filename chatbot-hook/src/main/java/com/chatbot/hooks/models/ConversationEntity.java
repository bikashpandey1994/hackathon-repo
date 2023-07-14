package com.chatbot.hooks.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONVERSATIONS")
public class ConversationEntity {

	@Id
	@Column
	private String responseId;

	@Column
	private String queryText;

	@Column
	private String parameters;

	@Column
	private String intent;

	@Column
	private String intentConfidence;
	
	@Column
	private String action;
	
	@Column
	private String sentiment;

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getIntentConfidence() {
		return intentConfidence;
	}

	public void setIntentConfidence(String intentConfidence) {
		this.intentConfidence = intentConfidence;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
}

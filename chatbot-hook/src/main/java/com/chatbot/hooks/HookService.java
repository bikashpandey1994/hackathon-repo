package com.chatbot.hooks;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chatbot.hooks.models.ConversationEntity;
import com.chatbot.hooks.models.SentimentRequest;
import com.chatbot.hooks.models.SentimentResponse;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.spring.bigquery.core.BigQueryTemplate;
import com.google.cloud.storage.Storage;

@Component
@SuppressWarnings("unused")
public class HookService {

	@Autowired
	private BigQuery bigQuery;

	@Autowired
	private Storage storage;

	@Autowired
	private ConversationRepository repository;

	@Autowired
	private BigQueryTemplate bigQueryTemplate;

	@Autowired
	private HttpClient httpClient;

	public String fundDetails(String fund) {
		return null;
	}

	public void saveConversation(GoogleCloudDialogflowV2WebhookRequest request, String rawData)
			throws InterruptedException, ExecutionException, FileNotFoundException {

		if (request.getQueryResult() != null) {

			SentimentRequest sentimentRequest = new SentimentRequest();

			sentimentRequest.setUuid(request.getResponseId());
			sentimentRequest.setText(request.getQueryResult().getFulfillmentText());

			SentimentResponse sentiment = httpClient.sentimentAnalysis(sentimentRequest);
		}

		ConversationEntity conversation = new ConversationEntity();

		conversation.setResponseId(request.getResponseId());

		if (request.getQueryResult() != null) {

			conversation.setQueryText(request.getQueryResult().getQueryText());

			if (request.getQueryResult().getParameters() != null) {
				conversation.setParameters(request.getQueryResult().getParameters().toString());
			}

			conversation.setIntentConfidence(String.valueOf(request.getQueryResult().getIntentDetectionConfidence()));
			conversation.setIntent(request.getQueryResult().getIntent().getDisplayName());
			conversation.setAction(request.getQueryResult().getAction());

			if (request.getQueryResult().getSentimentAnalysisResult() != null) {
				conversation.setSentiment(request.getQueryResult().getSentimentAnalysisResult().toString());
			}
		}

		repository.save(conversation);
	}
}

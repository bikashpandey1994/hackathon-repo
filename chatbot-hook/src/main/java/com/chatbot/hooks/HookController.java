package com.chatbot.hooks;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2EventInput;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessage;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessageText;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookResponse;

@RestController
public class HookController {

	Logger logger = LoggerFactory.getLogger(HookController.class);

	@Autowired
	private GsonFactory jacksonFactory;

	@Autowired
	private HookService service;

	@GetMapping("/health")
	public String health() throws IOException, InterruptedException, ExecutionException {

		logger.info("/health end point invoked.");

		return "SERVICE IS UP AND RUNNING";
	}

	@PostMapping("/invest-assist/hook")
	public ResponseEntity<String> hook(@RequestBody String rawData)
			throws IOException, InterruptedException, ExecutionException {

		logger.info("/clare/hook end point invoked.");
		logger.info("Request DATA : {}", rawData);

		GoogleCloudDialogflowV2WebhookRequest request = jacksonFactory.createJsonParser(rawData)
				.parse(GoogleCloudDialogflowV2WebhookRequest.class);

		service.saveConversation(request, rawData);

		String fulfilmentTxt = "Thank you. you will get recomended products in the dashboard in a while";

		GoogleCloudDialogflowV2WebhookResponse response = generateResponse(fulfilmentTxt, null);

		String responseAsJson = getResponseAsJsonString(response);

		return new ResponseEntity<String>(responseAsJson, HttpStatus.OK);
	}

	private GoogleCloudDialogflowV2WebhookResponse generateResponse(String fullFillmentMessage, String event) {

		GoogleCloudDialogflowV2IntentMessage msg = new GoogleCloudDialogflowV2IntentMessage();
		GoogleCloudDialogflowV2IntentMessageText text = new GoogleCloudDialogflowV2IntentMessageText();
		text.setText(Arrays.asList(fullFillmentMessage));
		msg.setText(text);

		GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
		response.setFulfillmentMessages(Arrays.asList(msg));
		GoogleCloudDialogflowV2EventInput eventInput = new GoogleCloudDialogflowV2EventInput();
		eventInput.setName(event);
		response.setFollowupEventInput(eventInput);

		return response;

	}

	private String getResponseAsJsonString(GoogleCloudDialogflowV2WebhookResponse response) throws IOException {

		StringWriter stringWriter = new StringWriter();
		JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(stringWriter);
		jsonGenerator.enablePrettyPrint();
		jsonGenerator.serialize(response);
		jsonGenerator.flush();

		return stringWriter.toString();
	}
}

/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package functions;

// [START functions_cloudevent_storage]
import com.google.cloud.functions.CloudEventsFunction;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;
import io.cloudevents.CloudEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class SpeechConverter implements CloudEventsFunction {
  private static final Logger logger = Logger.getLogger(SpeechConverter.class.getName());

  @Override
  public void accept(CloudEvent event) throws IOException {
    System.out.println("Event: " + event.getId());
    logger.info("Event Type: " + event.getType());

    if (event.getData() == null) {
      logger.warning("No data found in cloud event payload!");
      return;
    }

    String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
    StorageObjectData.Builder builder = StorageObjectData.newBuilder();
    JsonFormat.parser().merge(cloudEventData, builder);
    StorageObjectData data = builder.build();
    String uuid=data.getName();
    
    logger.info("Bucket: " + data.getBucket());
    logger.info("File: " + data.getName());
    logger.info("Metageneration: " + data.getMetageneration());
    logger.info("Created: " + data.getTimeCreated());
    logger.info("Updated: " + data.getUpdated());
    
    String speechText=convertSeepchToText(data.getBucket(),data.getName());
    logger.info("speechText: " + speechText);
    SentimentAnalysisClient.postUser(speechText,uuid);
    logger.info("Post Successfull");
    
  }

private String convertSeepchToText(String bucket, String objectName) throws IOException {
	
	try (SpeechClient speechClient = SpeechClient.create()) {
		System.out.println("bucket : " + bucket);
		// The path to the audio file to transcribe
		String gcsUri = "gs://"+bucket+"/"+objectName;
		// String gcsUri = "gs://hack-2023/samplename";

		// Builds the sync recognize request
		RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.MP3)
				.setSampleRateHertz(24000).setLanguageCode("en-US").build();
		RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(gcsUri).build();

		// Performs speech recognition on the audio file
		RecognizeResponse response = speechClient.recognize(config, audio);
		List<SpeechRecognitionResult> results = response.getResultsList();
		System.out.println("SpeechRecognitionResults : " + results);
		for (SpeechRecognitionResult result : results) {
			System.out.println("result.getAlternativesList() : " + result.getAlternativesList());
			SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
			System.out.printf("Transcription: %s%n", alternative.getTranscript());
			return alternative.getTranscript();
		}
	}
	return null;
	
}
}

// [END functions_cloudevent_storage]

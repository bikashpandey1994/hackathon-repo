package com.hackthon.voiceProcessor.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.WriteChannel;
/*import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;*/
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import com.hackthon.voiceProcessor.TextToSpeech;

@RestController
public class VoiveAndTextProcessor {

	@PostMapping("/textToSpeech")
	public void textToSpeech(@RequestBody TextToSpeech textToSppech) throws FileNotFoundException, IOException {

		try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
			SynthesisInput input = SynthesisInput.newBuilder().setText(textToSppech.getText()).build();

			VoiceSelectionParams voice = VoiceSelectionParams.newBuilder().setLanguageCode("en-US")
					.setSsmlGender(SsmlVoiceGender.NEUTRAL).build();
			AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
			SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
			ByteString audioContents = response.getAudioContent();
			writeFiletoStorage(audioContents);

		}
	}

	
	private void writeFiletoStorage(ByteString audioContents) throws IOException {
		String projectId = "hack-team-thewarroom1";
		String bucketName = "hack-voice-bucket";

		// The ID of your GCS object
		String objectName = "textToSpeech.mp3";
		Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
		BlobId blobId = BlobId.of(bucketName, objectName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

		Blob blob = storage.create(blobInfo);
		WriteChannel channel = blob.writer();
		channel.write(ByteBuffer.wrap(audioContents.toByteArray()));
		channel.close();

		System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);

	}

}

package com.hackthon.voiceProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.hackthon.voiceProcessor.repository.ClientRequirementReportEntity;
import com.hackthon.voiceProcessor.repository.ClientRequirementReportRepository;
import com.hackthon.voiceProcessor.repository.ProductSuggestionRepository;

@Service
public class SentimentAnalysisService {

	@Autowired
	private ClientRequirementReportRepository clReportRepository;
	
	@Autowired
	private ProductSuggestionRepository prRepository;
	
	public String analyzeSentiment(SentimentRequest sentimentRequest) throws IOException {
		
		try (LanguageServiceClient language = LanguageServiceClient.create()) {
			  Document doc = sentimentRequest.getDoc();
			  AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
			  Sentiment sentiment = response.getDocumentSentiment();
			  if (sentiment == null) {
			    System.out.println("No sentiment found");
			  } else {
				  System.out.printf("Sentiment magnitude: %.3f\n", sentiment.getMagnitude());
				  System.out.printf("Sentiment score: %.3f\n", sentiment.getScore());
				  if(sentiment.getScore()>0.5) {
					  return "interested";
				  }else {
					  return "not interested";
				  }
			  }
			}
		
		return null;
	}

private List<String> analyzeEntityAndGetProductIds(String text) throws IOException {
	
	List<String>entities = new ArrayList<String>();
	try (LanguageServiceClient language = LanguageServiceClient.create()) {
		  Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
		  AnalyzeEntitiesRequest request =
		      AnalyzeEntitiesRequest.newBuilder()
		          .setDocument(doc)
		          .setEncodingType(EncodingType.UTF16)
		          .build();

		  AnalyzeEntitiesResponse response = language.analyzeEntities(request);

		  // Print the response
		  for (Entity entity : response.getEntitiesList()) {
		    System.out.printf("Entity: %s", entity.getName());
		    System.out.printf("Salience: %.3f\n", entity.getSalience());
		    System.out.println("Metadata: ");
		    entities.add(entity.getName());
		  }
		  System.out.println("List of entitis : "+ entities);
		  List<String>productIds=prRepository.getSuggestedProductIds(entities);
		  System.out.println("List of productIds : "+ productIds);
		  return productIds;
		}
	}

public void analyzeSentimentText(SentimentRequestText sentimentRequestText) throws IOException {
	
		try (LanguageServiceClient language = LanguageServiceClient.create()) {
			Document doc = Document.newBuilder().setContent(sentimentRequestText.getText()).setType(Type.PLAIN_TEXT).build();
			  AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
			  Sentiment sentiment = response.getDocumentSentiment();
			  if (sentiment == null) {
			    System.out.println("No sentiment found");
			  } else {
				  System.out.printf("Sentiment magnitude: %.3f\n", sentiment.getMagnitude());
				  System.out.printf("Sentiment score: %.3f\n", sentiment.getScore());
				  
				  String translatedText=getGermanTranslationOfText(sentimentRequestText.getText());
				  System.out.println("translatedText : "+translatedText);				 
				  
				  List <String> productIds = analyzeEntityAndGetProductIds(sentimentRequestText.getText());
				  System.out.println("productIds : "+productIds);
				  ClientRequirementReportEntity cle = new ClientRequirementReportEntity();
				  cle.setRequirement_id(sentimentRequestText.getUuid());
				  cle.setSentiment_magnitude(String.valueOf(sentiment.getMagnitude()));
				  cle.setSentiment_score(String.valueOf(sentiment.getScore()));
				  cle.setText(sentimentRequestText.getText());
				  cle.setText_de(translatedText);
				  if(productIds !=null) {
					  String products = productIds.stream()
					  .map(Object::toString)
					  .collect(Collectors.joining(","));
				  cle.setSuggested_product_ids(products);
				  }
				  clReportRepository.save(cle);
				  System.out.printf("Client Requirement data based on sentiment analysis saved successfully!!!");
			  }
		}
		 
	}

private String getGermanTranslationOfText(String text) throws IOException {

	try (TranslationServiceClient client = TranslationServiceClient.create()) {
	      // Supported Locations: `global`, [glossary location], or [model location]
	      // Glossaries must be hosted in `us-central1`
	      // Custom Models must use the same location as your model. (us-central1)
	      LocationName parent = LocationName.of("hack-team-thewarroom1", "global");

	      // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
	      TranslateTextRequest request =
	          TranslateTextRequest.newBuilder()
	              .setParent(parent.toString())
	              .setMimeType("text/plain")
	              .setTargetLanguageCode("de")
	              .addContents(text)
	              .build();

	      TranslateTextResponse response = client.translateText(request);

	      // Display the translation for each input text provided
	      for (Translation translation : response.getTranslationsList()) {
	        System.out.printf("Translated text: %s\n", translation.getTranslatedText());
	      }
	      return response.getTranslationsList().get(0).getTranslatedText();
	    }
	
}

public List<String> getRecommendedProducts(String uuid) {

	Optional<ClientRequirementReportEntity> cliOptional = clReportRepository.findById(uuid);
	if (cliOptional.isPresent()) {
		if (cliOptional.get().getSuggested_product_ids() != null) {
			return Arrays.asList(cliOptional.get().getSuggested_product_ids().split(","));
		}
	}
	return Arrays.asList("4", "5", "6");
}

}

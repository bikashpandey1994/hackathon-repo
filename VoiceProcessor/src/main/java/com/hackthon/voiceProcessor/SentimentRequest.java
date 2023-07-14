package com.hackthon.voiceProcessor;

import com.google.cloud.language.v1.Document;

public class SentimentRequest {

	private Document doc;
	private String encodingType;
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
}

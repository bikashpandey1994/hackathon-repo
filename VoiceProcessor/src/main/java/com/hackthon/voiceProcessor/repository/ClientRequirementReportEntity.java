package com.hackthon.voiceProcessor.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client_requirement_report")
public class ClientRequirementReportEntity {

	@Id
	@Column
	private String requirement_id;
	@Column
	private String text;
	@Column
	private String sentiment_score;
	@Column
	private String sentiment_magnitude;
	@Column
	private String text_de;
	@Column
	private String suggested_product_ids;
	public String getRequirement_id() {
		return requirement_id;
	}
	public void setRequirement_id(String requirement_id) {
		this.requirement_id = requirement_id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSentiment_score() {
		return sentiment_score;
	}
	public void setSentiment_score(String sentiment_score) {
		this.sentiment_score = sentiment_score;
	}
	public String getSentiment_magnitude() {
		return sentiment_magnitude;
	}
	public void setSentiment_magnitude(String sentiment_magnitude) {
		this.sentiment_magnitude = sentiment_magnitude;
	}
	public String getText_de() {
		return text_de;
	}
	public void setText_de(String text_de) {
		this.text_de = text_de;
	}
	public String getSuggested_product_ids() {
		return suggested_product_ids;
	}
	public void setSuggested_product_ids(String suggested_product_ids) {
		this.suggested_product_ids = suggested_product_ids;
	}
}

package com.assemblyai.demo.model;

import java.util.List;

public class Transcript {

	private String id;
	
	private String audio_url;
	
	private String status;
	
	private String language_code;
	
	private String text;
	
	private List<Utterances> utterances;
	
	private boolean speaker_labels;
	
	private boolean filter_profanity;

	public String getAudio_url() {
		return audio_url;
	}

	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

	public String getLanguage_code() {
		return language_code;
	}

	public void setLanguage_code(String language_code) {
		this.language_code = language_code;
	}

	
	public boolean isFilter_profanity() {
		return filter_profanity;
	}

	public void setFilter_profanity(boolean filter_profanity) {
		this.filter_profanity = filter_profanity;
	}

	
	public boolean isSpeaker_labels() {
		return speaker_labels;
	}

	public void setSpeaker_labels(boolean speaker_labels) {
		this.speaker_labels = speaker_labels;
	}
	
	

	public List<Utterances> getUtterances() {
		return utterances;
	}

	public void setUtterances(List<Utterances> utterances) {
		this.utterances = utterances;
	}

	@Override
	public String toString() {
		return "Transcript [id=" + id + ", audio_url=" + audio_url + ", status=" + status + ", text=" + text
				+ ", getAudio_url()=" + getAudio_url() + ", getId()=" + getId() + ", getStatus()=" + getStatus()
				+ ", getText()=" + getText() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
}

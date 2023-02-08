package com.assemblyai.demo.model;

public class Utterances {

	
	private String speaker;
	
	private String text;
	
	public Utterances() {
		
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Utterances [speaker=" + speaker + ", text=" + text + "]";
	}
	
	
}

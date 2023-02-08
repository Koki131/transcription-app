package com.assemblyai.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.assemblyai.demo.model.Transcript;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;


@Controller
public class FileUploadController {
	
	@Value("${assemblyai.api.key}")
    private String apiKey;
	
	private String audio_url;
	
	private boolean filter_profanity;
	
	private boolean speaker_labels;
	
	private String language_code;
	
	private HashMap<String, String> languages;


	@Autowired
	private RestTemplate restTemplate;
	
    private final int CHUNK_SIZE = 5242880;
    
    @PostConstruct
    public void construct() {
    	
    	this.languages = new HashMap<>();
    	
    	languages.put("en", "Global English");
    	languages.put("en_au", "Australian English");
    	languages.put("en_uk", "British English");
    	languages.put("en_us", "English US");
    	languages.put("es", "Spanish");
    	languages.put("fr", "French");
    	languages.put("de", "German");
    	languages.put("it", "Italian");
    	languages.put("pt", "Portugese");
    	languages.put("nl", "Dutch");
    	languages.put("hi", "Hindi");
    	languages.put("ja", "Japanese");
    	

    }
    
    @GetMapping("/")
    public String index(Model model) {
    	
    	
    	model.addAttribute("languages", languages);
    	
    	
    	
    	return "index";
    }
    
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("language") String language, 
    		@RequestParam("profanity") String profanity, @RequestParam("labels") String labels) throws IOException {

    	
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);

        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[CHUNK_SIZE];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] audioBytes = buffer.toByteArray();

        HttpEntity<byte[]> httpEntity = new HttpEntity<>(audioBytes, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://api.assemblyai.com/v2/upload", httpEntity, String.class);
        
        String responseBody = response.getBody();


        
        
	     ObjectMapper mapper = new ObjectMapper();
	     JsonNode rootNode = mapper.readTree(responseBody);
	     String url = rootNode.get("upload_url").textValue();
	     
	     
	     
	     audio_url = url;
	     language_code = language;
	     filter_profanity = Boolean.parseBoolean(profanity);
	     speaker_labels = Boolean.parseBoolean(labels);
	     
	     
        return "transcribe";
    }
    
    
    @PostMapping("/transcribe")
    public String transcribeAudio(Model model) throws IOException, InterruptedException {
        
    	HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("audio_url", audio_url);
        requestBody.addProperty("language_code", language_code);
        requestBody.addProperty("filter_profanity", filter_profanity);
        requestBody.addProperty("speaker_labels", speaker_labels);
        
        
        
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody.toString(), headers);
        
        ResponseEntity<Transcript> response = restTemplate.postForEntity("https://api.assemblyai.com/v2/transcript", httpEntity, Transcript.class);
        
        Transcript transcript = response.getBody();
        
        while (true) {

        	ResponseEntity<Transcript> getResponse = restTemplate.exchange("https://api.assemblyai.com/v2/transcript/" + transcript.getId(), HttpMethod.GET, httpEntity, Transcript.class);

            transcript = getResponse.getBody();
            
            System.out.println(transcript.getStatus().toUpperCase());

            
            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            } 
            
            Thread.sleep(1000);
        }
        

        model.addAttribute("transcription", transcript.getText());
        
        if (speaker_labels) {
        	model.addAttribute("utterances", transcript.getUtterances());
        	
        	return "text-with-labels";
        }
        
        
        
        
        return "text";
        
        
    }


}

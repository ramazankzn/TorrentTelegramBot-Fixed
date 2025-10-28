package com.torrentbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class SonarrService {
    
    private static final Logger logger = LoggerFactory.getLogger(SonarrService.class);
    
    @Value("${sonarr.url:http://localhost:8989}")
    private String sonarrUrl;
    
    @Value("${sonarr.api-key:}")
    private String apiKey;
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public SonarrService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public boolean isHealthy() {
        if (apiKey.isEmpty()) {
            logger.warn("Sonarr API key not configured");
            return false;
        }
        
        try {
            Request request = new Request.Builder()
                    .url(sonarrUrl + "/api/v3/system/status")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            logger.error("Error checking Sonarr health", e);
            return false;
        }
    }
    
    public JsonNode getSystemStatus() {
        try {
            Request request = new Request.Builder()
                    .url(sonarrUrl + "/api/v3/system/status")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting Sonarr system status", e);
        }
        return null;
    }
    
    public JsonNode getSeries() {
        try {
            Request request = new Request.Builder()
                    .url(sonarrUrl + "/api/v3/series")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting series from Sonarr", e);
        }
        return null;
    }
    
    public JsonNode getQueue() {
        try {
            Request request = new Request.Builder()
                    .url(sonarrUrl + "/api/v3/queue")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting Sonarr queue", e);
        }
        return null;
    }
    
    public boolean addSeries(String tvdbId, String title) {
        try {
            String jsonBody = String.format(
                    "{\"title\":\"%s\",\"tvdbId\":%s,\"monitored\":true}",
                    title, tvdbId
            );
            
            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.get("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(sonarrUrl + "/api/v3/series")
                    .addHeader("X-Api-Key", apiKey)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Error adding series to Sonarr", e);
            return false;
        }
    }
}
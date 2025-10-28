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
public class RadarrService {
    
    private static final Logger logger = LoggerFactory.getLogger(RadarrService.class);
    
    @Value("${radarr.url:http://localhost:7878}")
    private String radarrUrl;
    
    @Value("${radarr.api-key:}")
    private String apiKey;
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public RadarrService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public boolean isHealthy() {
        if (apiKey.isEmpty()) {
            logger.warn("Radarr API key not configured");
            return false;
        }
        
        try {
            Request request = new Request.Builder()
                    .url(radarrUrl + "/api/v3/system/status")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            logger.error("Error checking Radarr health", e);
            return false;
        }
    }
    
    public JsonNode getSystemStatus() {
        try {
            Request request = new Request.Builder()
                    .url(radarrUrl + "/api/v3/system/status")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting Radarr system status", e);
        }
        return null;
    }
    
    public JsonNode getMovies() {
        try {
            Request request = new Request.Builder()
                    .url(radarrUrl + "/api/v3/movie")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting movies from Radarr", e);
        }
        return null;
    }
    
    public JsonNode getQueue() {
        try {
            Request request = new Request.Builder()
                    .url(radarrUrl + "/api/v3/queue")
                    .addHeader("X-Api-Key", apiKey)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting Radarr queue", e);
        }
        return null;
    }
    
    public boolean addMovie(String imdbId, String title) {
        try {
            String jsonBody = String.format(
                    "{\"title\":\"%s\",\"imdbId\":\"%s\",\"monitored\":true}",
                    title, imdbId
            );
            
            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.get("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                    .url(radarrUrl + "/api/v3/movie")
                    .addHeader("X-Api-Key", apiKey)
                    .post(body)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Error adding movie to Radarr", e);
            return false;
        }
    }
}
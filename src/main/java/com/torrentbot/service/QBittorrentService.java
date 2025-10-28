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
public class QBittorrentService {
    
    private static final Logger logger = LoggerFactory.getLogger(QBittorrentService.class);
    
    @Value("${qbittorrent.url:http://localhost:8080}")
    private String qbittorrentUrl;
    
    @Value("${qbittorrent.username:admin}")
    private String username;
    
    @Value("${qbittorrent.password:}")
    private String password;
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String sessionCookie;
    
    public QBittorrentService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public boolean isHealthy() {
        if (password.isEmpty()) {
            logger.warn("qBittorrent password not configured");
            return false;
        }
        
        try {
            if (login()) {
                return getVersion() != null;
            }
        } catch (Exception e) {
            logger.error("Error checking qBittorrent health", e);
        }
        return false;
    }
    
    private boolean login() {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
            
            Request request = new Request.Builder()
                    .url(qbittorrentUrl + "/api/v2/auth/login")
                    .post(formBody)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String setCookie = response.header("Set-Cookie");
                    if (setCookie != null && setCookie.contains("SID=")) {
                        sessionCookie = setCookie.split(";")[0];
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error logging into qBittorrent", e);
        }
        return false;
    }
    
    public String getVersion() {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(qbittorrentUrl + "/api/v2/app/version");
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", sessionCookie);
            }
            
            try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                }
            }
        } catch (IOException e) {
            logger.error("Error getting qBittorrent version", e);
        }
        return null;
    }
    
    public JsonNode getTorrents() {
        try {
            Request.Builder requestBuilder = new Request.Builder()
                    .url(qbittorrentUrl + "/api/v2/torrents/info");
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", sessionCookie);
            }
            
            try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return objectMapper.readTree(response.body().string());
                }
            }
        } catch (IOException e) {
            logger.error("Error getting torrents from qBittorrent", e);
        }
        return null;
    }
    
    public boolean addTorrent(String torrentUrl) {
        try {
            if (!login()) {
                logger.error("Failed to login to qBittorrent");
                return false;
            }
            
            RequestBody formBody;
            if (torrentUrl.startsWith("magnet:")) {
                formBody = new FormBody.Builder()
                        .add("urls", torrentUrl)
                        .build();
            } else {
                formBody = new FormBody.Builder()
                        .add("urls", torrentUrl)
                        .build();
            }
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(qbittorrentUrl + "/api/v2/torrents/add")
                    .post(formBody);
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", sessionCookie);
            }
            
            try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Error adding torrent to qBittorrent", e);
            return false;
        }
    }
    
    public boolean pauseTorrent(String hash) {
        return torrentAction("/api/v2/torrents/pause", hash);
    }
    
    public boolean resumeTorrent(String hash) {
        return torrentAction("/api/v2/torrents/resume", hash);
    }
    
    public boolean deleteTorrent(String hash, boolean deleteFiles) {
        try {
            if (!login()) {
                return false;
            }
            
            RequestBody formBody = new FormBody.Builder()
                    .add("hashes", hash)
                    .add("deleteFiles", String.valueOf(deleteFiles))
                    .build();
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(qbittorrentUrl + "/api/v2/torrents/delete")
                    .post(formBody);
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", sessionCookie);
            }
            
            try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Error deleting torrent from qBittorrent", e);
            return false;
        }
    }
    
    private boolean torrentAction(String endpoint, String hash) {
        try {
            if (!login()) {
                return false;
            }
            
            RequestBody formBody = new FormBody.Builder()
                    .add("hashes", hash)
                    .build();
            
            Request.Builder requestBuilder = new Request.Builder()
                    .url(qbittorrentUrl + endpoint)
                    .post(formBody);
            
            if (sessionCookie != null) {
                requestBuilder.addHeader("Cookie", sessionCookie);
            }
            
            try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
                return response.isSuccessful();
            }
        } catch (IOException e) {
            logger.error("Error performing torrent action: {}", endpoint, e);
            return false;
        }
    }
}
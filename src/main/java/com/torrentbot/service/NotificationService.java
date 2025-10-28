package com.torrentbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    private final Set<Long> subscribedUsers = ConcurrentHashMap.newKeySet();
    
    @Autowired
    private TelegramClient telegramClient;
    
    public void subscribeUser(Long chatId) {
        subscribedUsers.add(chatId);
        logger.info("User {} subscribed to notifications", chatId);
    }
    
    public void unsubscribeUser(Long chatId) {
        subscribedUsers.remove(chatId);
        logger.info("User {} unsubscribed from notifications", chatId);
    }
    
    public boolean isSubscribed(Long chatId) {
        return subscribedUsers.contains(chatId);
    }
    
    public void notifyAllSubscribed(String message) {
        for (Long chatId : subscribedUsers) {
            try {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(message)
                        .build();
                
                telegramClient.execute(sendMessage);
                logger.debug("Notification sent to user {}", chatId);
            } catch (TelegramApiException e) {
                logger.error("Failed to send notification to user {}", chatId, e);
            }
        }
    }
    
    public void notifyUser(Long chatId, String message) {
        if (isSubscribed(chatId)) {
            try {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text(message)
                        .build();
                
                telegramClient.execute(sendMessage);
                logger.debug("Notification sent to user {}", chatId);
            } catch (TelegramApiException e) {
                logger.error("Failed to send notification to user {}", chatId, e);
            }
        }
    }
    
    public void notifyTorrentCompleted(String torrentName) {
        String message = String.format("✅ Торрент завершен: %s", torrentName);
        notifyAllSubscribed(message);
    }
    
    public void notifyTorrentAdded(String torrentName) {
        String message = String.format("➕ Торрент добавлен: %s", torrentName);
        notifyAllSubscribed(message);
    }
    
    public void notifyTorrentError(String torrentName, String error) {
        String message = String.format("❌ Ошибка торрента %s: %s", torrentName, error);
        notifyAllSubscribed(message);
    }
    
    public Set<Long> getSubscribedUsers() {
        return Set.copyOf(subscribedUsers);
    }
    
    public int getSubscribedUsersCount() {
        return subscribedUsers.size();
    }
}
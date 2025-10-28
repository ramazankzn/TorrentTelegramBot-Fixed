package com.torrentbot.service;

import com.torrentbot.service.keyboard.KeyboardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class MessageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    @Autowired
    private TelegramClient telegramClient;
    
    @Autowired
    private KeyboardFactory keyboardFactory;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private RadarrService radarrService;
    
    @Autowired
    private SonarrService sonarrService;
    
    @Autowired
    private QBittorrentService qBittorrentService;
    
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }
    
    private void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        
        logger.info("Received message: {} from chat: {}", messageText, chatId);
        
        try {
            switch (messageText) {
                case "/start":
                    sendWelcomeMessage(chatId);
                    break;
                case "/help":
                    sendHelpMessage(chatId);
                    break;
                case "/status":
                    sendStatusMessage(chatId);
                    break;
                case "/subscribe":
                    notificationService.subscribeUser(chatId);
                    sendMessage(chatId, "Вы подписались на уведомления!");
                    break;
                case "/unsubscribe":
                    notificationService.unsubscribeUser(chatId);
                    sendMessage(chatId, "Вы отписались от уведомлений!");
                    break;
                default:
                    if (messageText.startsWith("magnet:") || messageText.endsWith(".torrent")) {
                        handleTorrentLink(chatId, messageText);
                    } else {
                        sendMessage(chatId, "Неизвестная команда. Используйте /help для справки.");
                    }
            }
        } catch (Exception e) {
            logger.error("Error handling text message", e);
            sendMessage(chatId, "Произошла ошибка при обработке сообщения.");
        }
    }
    
    private void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        
        logger.info("Received callback: {} from chat: {}", callbackData, chatId);
        
        try {
            // Handle callback queries here
            sendMessage(chatId, "Обработка: " + callbackData);
        } catch (Exception e) {
            logger.error("Error handling callback query", e);
        }
    }
    
    private void sendWelcomeMessage(Long chatId) {
        String welcomeText = "Добро пожаловать в TorrentBot!\n\n" +
                "Я могу помочь вам управлять торрентами через Radarr, Sonarr и qBittorrent.\n\n" +
                "Используйте /help для получения списка команд.";
        
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(welcomeText)
                .replyMarkup(keyboardFactory.getMainMenuKeyboard())
                .build();
        
        sendMessage(message);
    }
    
    private void sendHelpMessage(Long chatId) {
        String helpText = "Доступные команды:\n\n" +
                "/start - Начать работу с ботом\n" +
                "/help - Показать справку\n" +
                "/status - Показать статус сервисов\n" +
                "/subscribe - Подписаться на уведомления\n" +
                "/unsubscribe - Отписаться от уведомлений\n\n" +
                "Вы также можете отправить магнитную ссылку или ссылку на .torrent файл для добавления в очередь загрузки.";
        
        sendMessage(chatId, helpText);
    }
    
    private void sendStatusMessage(Long chatId) {
        StringBuilder statusText = new StringBuilder("Статус сервисов:\n\n");
        
        statusText.append("Radarr: ").append(radarrService.isHealthy() ? "✅ Работает" : "❌ Недоступен").append("\n");
        statusText.append("Sonarr: ").append(sonarrService.isHealthy() ? "✅ Работает" : "❌ Недоступен").append("\n");
        statusText.append("qBittorrent: ").append(qBittorrentService.isHealthy() ? "✅ Работает" : "❌ Недоступен");
        
        sendMessage(chatId, statusText.toString());
    }
    
    private void handleTorrentLink(Long chatId, String torrentLink) {
        sendMessage(chatId, "Добавляю торрент в очередь загрузки...");
        
        // Add torrent logic here
        boolean success = qBittorrentService.addTorrent(torrentLink);
        
        if (success) {
            sendMessage(chatId, "✅ Торрент успешно добавлен в очередь!");
        } else {
            sendMessage(chatId, "❌ Ошибка при добавлении торрента.");
        }
    }
    
    private void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        
        sendMessage(message);
    }
    
    private void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error sending message", e);
        }
    }
}
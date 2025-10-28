package com.torrentbot.bot;

import com.torrentbot.service.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TorrentBot implements SpringLongPollingBot, LongPollingUpdateConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(TorrentBot.class);
    
    @Value("${bot.token}")
    private String botToken;
    
    private final MessageHandler messageHandler;
    
    public TorrentBot(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    
    @Override
    public String getBotToken() {
        return botToken;
    }
    
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
    
    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            try {
                messageHandler.handleUpdate(update);
            } catch (Exception e) {
                logger.error("Error processing update: {}", update.getUpdateId(), e);
            }
        }
    }
}
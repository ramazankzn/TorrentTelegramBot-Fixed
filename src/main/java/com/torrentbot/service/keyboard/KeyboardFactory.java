package com.torrentbot.service.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {
    
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
        
        List<KeyboardRow> keyboard = new ArrayList<>();
        
        // First row
        KeyboardRow row1 = new KeyboardRow();
        row1.add(KeyboardButton.builder().text("⚙️ Статус сервисов").build());
        row1.add(KeyboardButton.builder().text("📊 Очередь").build());
        keyboard.add(row1);
        
        // Second row
        KeyboardRow row2 = new KeyboardRow();
        row2.add(KeyboardButton.builder().text("🔔 Уведомления").build());
        row2.add(KeyboardButton.builder().text("❓ Помощь").build());
        keyboard.add(row2);
        
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
    
    public InlineKeyboardMarkup getServicesStatusKeyboard() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        // First row
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        
        InlineKeyboardButton radarrButton = InlineKeyboardButton.builder()
                .text("🎥 Radarr")
                .callbackData("radarr_status")
                .build();
        rowInline1.add(radarrButton);
        
        InlineKeyboardButton sonarrButton = InlineKeyboardButton.builder()
                .text("📺 Sonarr")
                .callbackData("sonarr_status")
                .build();
        rowInline1.add(sonarrButton);
        
        rowsInline.add(rowInline1);
        
        // Second row
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton qbitButton = InlineKeyboardButton.builder()
                .text("⬇️ qBittorrent")
                .callbackData("qbittorrent_status")
                .build();
        rowInline2.add(qbitButton);
        
        rowsInline.add(rowInline2);
        
        return InlineKeyboardMarkup.builder()
                .keyboard(rowsInline)
                .build();
    }
    
    public InlineKeyboardMarkup getNotificationKeyboard(boolean isSubscribed) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button;
        
        if (isSubscribed) {
            button = InlineKeyboardButton.builder()
                    .text("🔕 Отписаться")
                    .callbackData("unsubscribe")
                    .build();
        } else {
            button = InlineKeyboardButton.builder()
                    .text("🔔 Подписаться")
                    .callbackData("subscribe")
                    .build();
        }
        
        rowInline.add(button);
        rowsInline.add(rowInline);
        
        return InlineKeyboardMarkup.builder()
                .keyboard(rowsInline)
                .build();
    }
    
    public InlineKeyboardMarkup getTorrentActionsKeyboard(String torrentHash) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        // First row - Pause/Resume
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        
        InlineKeyboardButton pauseButton = InlineKeyboardButton.builder()
                .text("⏸️ Пауза")
                .callbackData("pause_" + torrentHash)
                .build();
        rowInline1.add(pauseButton);
        
        InlineKeyboardButton resumeButton = InlineKeyboardButton.builder()
                .text("▶️ Продолжить")
                .callbackData("resume_" + torrentHash)
                .build();
        rowInline1.add(resumeButton);
        
        rowsInline.add(rowInline1);
        
        // Second row - Delete
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        
        InlineKeyboardButton deleteButton = InlineKeyboardButton.builder()
                .text("🗑️ Удалить")
                .callbackData("delete_" + torrentHash)
                .build();
        rowInline2.add(deleteButton);
        
        InlineKeyboardButton deleteWithFilesButton = InlineKeyboardButton.builder()
                .text("🗑️ Удалить с файлами")
                .callbackData("delete_files_" + torrentHash)
                .build();
        rowInline2.add(deleteWithFilesButton);
        
        rowsInline.add(rowInline2);
        
        return InlineKeyboardMarkup.builder()
                .keyboard(rowsInline)
                .build();
    }
    
    public InlineKeyboardMarkup getBackButton() {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("⬅️ Назад")
                .callbackData("back")
                .build();
        rowInline.add(backButton);
        
        rowsInline.add(rowInline);
        
        return InlineKeyboardMarkup.builder()
                .keyboard(rowsInline)
                .build();
    }
}
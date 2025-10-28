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
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        
        List<KeyboardRow> keyboard = new ArrayList<>();
        
        // First row
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("⚙️ Статус сервисов"));
        row1.add(new KeyboardButton("📊 Очередь"));
        keyboard.add(row1);
        
        // Second row
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🔔 Уведомления"));
        row2.add(new KeyboardButton("❓ Помощь"));
        keyboard.add(row2);
        
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
    
    public InlineKeyboardMarkup getServicesStatusKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        // First row
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton radarrButton = new InlineKeyboardButton();
        radarrButton.setText("🎥 Radarr");
        radarrButton.setCallbackData("radarr_status");
        rowInline1.add(radarrButton);
        
        InlineKeyboardButton sonarrButton = new InlineKeyboardButton();
        sonarrButton.setText("📺 Sonarr");
        sonarrButton.setCallbackData("sonarr_status");
        rowInline1.add(sonarrButton);
        
        rowsInline.add(rowInline1);
        
        // Second row
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton qbitButton = new InlineKeyboardButton();
        qbitButton.setText("⬇️ qBittorrent");
        qbitButton.setCallbackData("qbittorrent_status");
        rowInline2.add(qbitButton);
        
        rowsInline.add(rowInline2);
        
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    
    public InlineKeyboardMarkup getNotificationKeyboard(boolean isSubscribed) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        
        if (isSubscribed) {
            button.setText("🔕 Отписаться");
            button.setCallbackData("unsubscribe");
        } else {
            button.setText("🔔 Подписаться");
            button.setCallbackData("subscribe");
        }
        
        rowInline.add(button);
        rowsInline.add(rowInline);
        
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    
    public InlineKeyboardMarkup getTorrentActionsKeyboard(String torrentHash) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        // First row - Pause/Resume
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        
        InlineKeyboardButton pauseButton = new InlineKeyboardButton();
        pauseButton.setText("⏸️ Пауза");
        pauseButton.setCallbackData("pause_" + torrentHash);
        rowInline1.add(pauseButton);
        
        InlineKeyboardButton resumeButton = new InlineKeyboardButton();
        resumeButton.setText("▶️ Продолжить");
        resumeButton.setCallbackData("resume_" + torrentHash);
        rowInline1.add(resumeButton);
        
        rowsInline.add(rowInline1);
        
        // Second row - Delete
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        
        InlineKeyboardButton deleteButton = new InlineKeyboardButton();
        deleteButton.setText("🗑️ Удалить");
        deleteButton.setCallbackData("delete_" + torrentHash);
        rowInline2.add(deleteButton);
        
        InlineKeyboardButton deleteWithFilesButton = new InlineKeyboardButton();
        deleteWithFilesButton.setText("🗑️ Удалить с файлами");
        deleteWithFilesButton.setCallbackData("delete_files_" + torrentHash);
        rowInline2.add(deleteWithFilesButton);
        
        rowsInline.add(rowInline2);
        
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    
    public InlineKeyboardMarkup getBackButton() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅️ Назад");
        backButton.setCallbackData("back");
        rowInline.add(backButton);
        
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        
        return markupInline;
    }
}
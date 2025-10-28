package com.torrentbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TorrentTelegramBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TorrentTelegramBotApplication.class, args);
    }
}
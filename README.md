# TorrentTelegramBot - Fixed Version

Улучшенная версия Telegram бота для управления торрентами через Radarr, Sonarr и qBittorrent.

## Особенности

- ✅ **Исправлены ошибки компиляции** - все зависимости настроены правильно
- 🎆 **Spring Boot 3.1.0** - современный фреймворк
- 🤖 **Telegram Bot API 7.10.0** - последняя версия API
- 🚀 **OkHttp 4.12.0** - надёжные HTTP-запросы
- 📊 **Мониторинг** - проверка состояния сервисов
- 🔔 **Уведомления** - подписка на статусы торрентов

## Исправленные ошибки

### Оригинальные проблемы:
- `cannot find symbol` для RadarrService, SonarrService, QBittorrentService
- `package okhttp3 does not exist` в нескольких файлах

### Решения:
✅ Добавлена зависимость `com.squareup.okhttp3:okhttp:4.12.0`
✅ Созданы все отсутствующие сервисные классы
✅ Обновлёна структура проекта под Spring Boot 3
✅ Исправлена совместимость с новым Telegram Bot API

## Установка

### Предварительные требования

- Java 17+
- Maven 3.8+
- Токен Telegram бота от @BotFather

### Клонирование и сборка

```bash
git clone https://github.com/ramazankzn/TorrentTelegramBot-Fixed.git
cd TorrentTelegramBot-Fixed
mvn clean install
```

### Конфигурация

Отредактируйте `src/main/resources/application.yml` или установите переменные окружения:

```yaml
bot:
  token: YOUR_BOT_TOKEN_HERE

radarr:
  url: http://localhost:7878
  api-key: YOUR_RADARR_API_KEY

sonarr:
  url: http://localhost:8989
  api-key: YOUR_SONARR_API_KEY

qbittorrent:
  url: http://localhost:8080
  username: admin
  password: YOUR_PASSWORD
```

Или через переменные окружения:

```bash
export BOT_TOKEN="your_bot_token"
export RADARR_URL="http://localhost:7878"
export RADARR_API_KEY="your_radarr_api_key"
export SONARR_URL="http://localhost:8989"
export SONARR_API_KEY="your_sonarr_api_key"
export QBITTORRENT_URL="http://localhost:8080"
export QBITTORRENT_USERNAME="admin"
export QBITTORRENT_PASSWORD="your_password"
```

### Запуск

```bash
mvn spring-boot:run
```

Или собрать JAR и запустить:

```bash
mvn clean package
java -jar target/torrent-telegram-bot-2.0.0.jar
```

## Использование

### Команды

- `/start` - Начать работу с ботом
- `/help` - Показать справку
- `/status` - Показать статус сервисов
- `/subscribe` - Подписаться на уведомления
- `/unsubscribe` - Отписаться от уведомлений

### Добавление торрентов

Отправьте боту:
- Магнитную ссылку (`magnet:?xt=...`)
- Ссылку на .torrent файл

## Архитектура

```
src/main/java/com/torrentbot/
├── TorrentTelegramBotApplication.java  # Основной класс
├── bot/
│   └── TorrentBot.java                 # Telegram бот
└── service/
    ├── MessageHandler.java             # Обработчик сообщений
    ├── NotificationService.java        # Сервис уведомлений
    ├── RadarrService.java              # Интеграция с Radarr
    ├── SonarrService.java              # Интеграция с Sonarr
    ├── QBittorrentService.java         # Интеграция с qBittorrent
    └── keyboard/
        └── KeyboardFactory.java        # Фабрика клавиатур
```

## Технологии

- **Spring Boot 3.1.0** - основной фреймворк
- **Telegram Bot API 7.10.0** - Spring Boot стартер
- **OkHttp 4.12.0** - HTTP клиент
- **Jackson** - JSON обработка
- **H2 Database** - встроенная база данных

## Лицензия

MIT License

## Контрибьюторы

Приветствуются любые вклады! Откройте issue или создайте pull request.

## Поддержка

Если у вас возникли проблемы, откройте issue в данном репозитории.
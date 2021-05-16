package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс для учёта подключенных клиентов
 */
public class ClientManager {
    private static final Logger log = Logger.getLogger(ClientManager.class);
    /** Содержит список сессий клиентов */
    private static Map<String, ClientWorker> sessions = new ConcurrentHashMap<>();

    /**
     * Регистрация новой сессии
     * @param username имя пользователя
     * @param clientWorker ссылка на экземпляр клиента
     */
    public static void registerUserSession(String username, ClientWorker clientWorker) {
        sessions.put(username, clientWorker);
        log.debug("New user registered: " + username);
    }

    /**
     * Удаление сессии
     * @param username имя пользователя
     */
    public static void removeUserSession(String username) {
        sessions.remove(username);
        log.debug("User disconnected: " + username);
    }

    /**
     * Возвращает список имён пользователей
     * @return список
     */
    public static Set<String> getActiveUsers() {
        log.debug("Get active users request");
        return sessions.keySet();
    }

    /**
     * Возвращает сессию пользователя по имени пользователя
     * @param username имя пользователя
     * @return список
     */
    public static ClientWorker getUserSession(String username) {
        log.debug("Get user session request: " + username);
        return sessions.get(username);
    }

    /**
     * Возвращает мапу сессии
     * @return мапа
     */
    public static Map<String, ClientWorker> getSessions() {
        log.debug("Get users sessions request");
        return sessions;
    }
}

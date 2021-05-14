package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final Logger log = Logger.getLogger(ClientManager.class);
    private static Map<String, ClientWorker> sessions = new ConcurrentHashMap<>();

    public static void registerUserSession(String username, ClientWorker clientWorker) {
        sessions.put(username, clientWorker);
        log.debug("New user registered: " + username);
    }

    public static void removeUserSession(String username) {
        sessions.remove(username);
        log.debug("User disconnected: " + username);
    }

    public static Set<String> getActiveUsers() {
        log.debug("Get active users request");
        return sessions.keySet();
    }

    public static ClientWorker getUserSession(String username) {
        log.debug("Get user session request: " + username);
        return sessions.get(username);
    }

    public static Map<String, ClientWorker> getSessions() {
        log.debug("Get users sessions request");
        return sessions;
    }
}

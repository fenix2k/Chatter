package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Main класс клиента
 */
public class AppClient {
    private static final Logger log = Logger.getLogger(AppClient.class);

    public static void main(String[] args) {
        // Создание экземпляра клиента
        Client client = new Client("localhost", 9090);
        try {
            // Запуск подключения
            client.connect();
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }
}

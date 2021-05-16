package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Класс сервера
 */
public class Server implements Runnable {
    private static final Logger log = Logger.getLogger(Server.class);
    /** Порт сервера */
    protected int serverPort = 9090;
    /** Ссылка на серверный сокет сервера */
    protected ServerSocket serverSocket = null;
    /** Флаг остановки сервера */
    protected boolean isStopped = false;
    /** Текущий поток сервера */
    protected Thread runningThread = null;

    public Server(int port) {
        this.serverPort = port;
    }

    public void run() {
        log.info("Starting server...");
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        // Создаём серверный сокет
        openServerSocket();
        // Цикл приёма входящих соединений
        while (!isStopped()) {
            // Клиентский сокет
            Socket clientSocket = null;
            try {
                // Ожидаем подключения клиента
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    log.info("Server stopped");
                    return;
                }
                log.debug("Error accepting client connection");
                throw new RuntimeException("Error accepting client connection", e);
            }
            // Создаём поток для общения с клиентом и запускаем
            ClientWorker clientWorker = new ClientWorker(clientSocket);
            clientWorker.start();
        }
        log.info("Server stopped");
    }

    /**
     * Возвращает статус сервера
     * @return bool
     */
    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Останавливет сервер и закрывает сокет
     */
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
            log.info("Server stopped");
        } catch (IOException e) {
            log.debug("Failed to normally stop server");
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Открывает серверный сокет
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            log.info("Server started on port " + this.serverPort);
        } catch (IOException e) {
            log.debug("Failed to start server. Cannot open port " + this.serverPort);
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

}

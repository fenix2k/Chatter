package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private static final Logger log = Logger.getLogger(Server.class);

    protected int serverPort = 9090;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public Server(int port) {
        this.serverPort = port;
    }

    public void run() {
        log.info("Starting server...");
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    log.info("Server stopped");
                    return;
                }
                log.debug("Error accepting client connection");
                throw new RuntimeException("Error accepting client connection", e);
            }
            ClientWorker clientWorker = new ClientWorker(clientSocket);
            new Thread(clientWorker).start();
        }
        log.info("Server stopped");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

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

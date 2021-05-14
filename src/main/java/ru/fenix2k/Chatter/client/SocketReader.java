package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketReader implements Runnable {
    private static final Logger log = Logger.getLogger(SocketReader.class);
    private Thread currentThread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ObjectInputStream ois;

    public SocketReader(ObjectInputStream ois) {
        this.ois = ois;
    }

    public void start() {
        currentThread = new Thread(this);
        currentThread.start();
    }

    public void stop() {
        running.set(false);
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    @Override
    public void run() {
        Packet packet = null;

        while (running.get()) {
            try {
                packet = (Packet) ois.readObject();
                processingPacket(packet);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processingPacket(Packet packet) throws IOException {
        switch (packet.getType()) {
            case DISCONNECT -> stop();
            case ERROR -> errorMessageHandler(packet);
            case SUCCESS -> successMessageHandler(packet);
            case AUTHENTICATED -> authenticatedMessageHandler(packet);
            default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
        }
    }

    private void errorMessageHandler(Packet packet) {

    }
    private void successMessageHandler(Packet packet) {

    }
    private void authenticatedMessageHandler(Packet packet) {

    }
}

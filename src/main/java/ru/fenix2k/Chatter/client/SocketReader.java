package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс служит для приёма пакетов от сервера через сокет
 */
public class SocketReader implements Runnable {
    private static final Logger log = Logger.getLogger(SocketReader.class);
    /** Ссылка на экземпляр клиента **/
    private final Client client;
    /** Текущий поток **/
    private Thread currentThread;
    /** Потокобезопасная переменная для останоки потока **/
    private final AtomicBoolean running = new AtomicBoolean(false);
    /** Входящий поток **/
    private final ObjectInputStream ois;

    public SocketReader(Client client, ObjectInputStream ois) {
        this.client = client;
        this.ois = ois;
    }

    /**
     * Старует поток
     */
    public void start() {
        currentThread = new Thread(this);
        currentThread.start();
    }

    /**
     * Останавливает поток
     */
    public void stop() {
        log.debug("Stopping input stream");
        running.set(false);
    }

    /**
     * Возвращает текущий поток
     * @return поток
     */
    public Thread getCurrentThread() {
        return currentThread;
    }

    @Override
    public void run() {
        log.debug("SocketReader started");
        running.set(true);
        Packet packet = null;
        // Цикл чтения пакетов от сервера
        while (running.get()) {
            try {
                log.debug("Read packet from stream");
                packet = (Packet) ois.readObject();
                // Обработка полученного пакета
                processingPacket(packet);
            } catch (ClassNotFoundException | IOException e) {
                log.debug("Socket closed");
            }
        }
        log.debug("SocketReader stopped");
    }

    /**
     * Метод обрабатывает пакет
     * @param packet пакет
     * @throws IOException
     */
    private void processingPacket(Packet packet) throws IOException {
        log.debug("Processing packet");
        // Удаляем пакет из списка ожидания ответа
        if(client.packetResponseWaitingList.containsKey(packet.getId()))
            client.packetResponseWaitingList.remove(packet.getId());

        if(packet.getId() == 0) {
            // получен новый пакет от сервера
            log.debug("Packet from server received");
        }
        else {
            // получен ответ на запрос от сервера
            log.debug("Response packet received");
            switch (packet.getType()) {
                case DISCONNECT -> stop();
                case ERROR -> errorMessageHandler(packet);
                case SUCCESS -> successMessageHandler(packet);
                case AUTHENTICATED -> authenticatedMessageHandler(packet);
                default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
            }
        }
    }

    /**
     * Обрабатывает пакет ERROR
     * @param packet пакет
     */
    private void errorMessageHandler(Packet packet) {
        log.debug("Error message packet received");
    }

    /**
     * Обрабатывает пакет SUCCESS
     * @param packet пакет
     */
    private void successMessageHandler(Packet packet) {
        log.debug("Success message packet received");
    }

    /**
     * Обрабатывает пакет AUTHENTICATED
     * @param packet пакет
     */
    private void authenticatedMessageHandler(Packet packet) {
        log.debug("Authenticated message packet received");
    }
}

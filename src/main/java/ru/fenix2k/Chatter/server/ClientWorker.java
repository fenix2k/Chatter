package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.protocol.packets.Packet_AuthenticatedResponse;
import ru.fenix2k.Chatter.protocol.packets.Packet_Connect;
import ru.fenix2k.Chatter.protocol.packets.Packet_Disconnect;
import ru.fenix2k.Chatter.protocol.packets.Packet_ErrorResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс для обработки сообщений клиента
 */
public class ClientWorker implements Runnable {
    private static final Logger log = Logger.getLogger(ClientWorker.class);
    /** Сокет клиента */
    protected Socket clientSocket = null;
    /** Текущий поток **/
    private Thread currentThread;
    /** Входящий поток */
    protected ObjectInputStream ois = null;
    /** Исходящий поток */
    protected ObjectOutputStream oos = null;
    /** Потокобезопасная переменная для останоки потока **/
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ClientWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
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
        running.set(false);
    }

    /**
     * Возвращает текущий поток
     * @return поток
     */
    public Thread getCurrentThread() {
        return currentThread;
    }

    public void run() {
        log.debug("New Client request received");
        running.set(true);
        try {
            // Открываем стримы
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            // Цикл получения пакетов от клиентов и их обработка
            while (running.get()) {
                Packet packet = readPacket();
                processingPacket(packet);
            }
            // Закрываем стримы
            oos.close();
            ois.close();
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage(), e);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    /**
     * Чтение пакета из потока
     * @return пакет
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Packet readPacket() throws ClassNotFoundException, IOException {
        return (Packet) ois.readObject();
    }

    /**
     * Запись пакета в поток
     * @param packet пакет
     * @throws IOException
     */
    private void sendPacket(Packet packet) throws IOException {
        oos.writeObject(packet);
    }

    /**
     * Обработка пакета в соответствии с типом
     * @param packet пакет
     * @throws IOException
     */
    private void processingPacket(Packet packet) throws IOException {
        switch (packet.getType()) {
            case CONNECT -> authenticateUser(packet);
            case DISCONNECT -> closeConnection();
            case SENDMSG -> sendMessage(packet);
            default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
        }
    }

    /**
     * Закрытие соединения с клиентом
     * @throws IOException
     */
    private void closeConnection() {
        log.debug("Receive packet: " + PacketType.DISCONNECT);
        this.stop();
    }

    /**
     * Авторизация клиента
     * @param packet пакет
     * @throws IOException
     */
    private void authenticateUser(Packet packet) throws IOException {
        log.debug("Receive packet: " + PacketType.CONNECT);
        Packet_Connect packetConnectToServer = (Packet_Connect) packet;
        if (packetConnectToServer.getUsername().equals("admin")
                && packetConnectToServer.getPassword().equals("password")) {
            log.debug("Authentication success: " + packetConnectToServer.getUsername());
            ClientManager.registerUserSession("admin", this);
            sendPacket(new Packet_AuthenticatedResponse());
        } else {
            log.debug("Authentication failed. Wrong credentials: " + packetConnectToServer.getUsername());
            sendPacket(new Packet_ErrorResponse("Authentication failed. Wrong credentials"));
        }
    }

    /**
     * Отправка сообщения клиенту
     * @param packet
     */
    private void sendMessage(Packet packet) {
        log.debug("Receive packet: " + PacketType.SENDMSG);
    }
}

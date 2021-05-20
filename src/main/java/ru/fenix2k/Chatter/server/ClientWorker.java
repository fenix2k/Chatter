package ru.fenix2k.Chatter.server;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.protocol.packets.*;
import ru.fenix2k.Chatter.server.DAO.UserDAO;
import ru.fenix2k.Chatter.server.DAO.UserDAOImpl;
import ru.fenix2k.Chatter.server.Service.UserService;

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
    /** ИД пакета. Нужен для отслеживания получения ответа сервера **/
    private int packetId = 100;

    private final UserService userService;

    public ClientWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.userService = new UserService();
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
                processingPacket(readPacket());
            }
            // Закрываем стримы
            oos.close();
            ois.close();
        } catch (ClassNotFoundException | IOException e) {
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
        Packet packet = (Packet) ois.readObject();
        packetId = packet.getId();
        return packet;
    }

    /**
     * Запись пакета в поток
     * @param packet пакет
     * @throws IOException
     */
    private void writePacket(Packet packet) throws IOException {
        packet.setId(packetId);
        oos.writeObject(packet);
    }

    /**
     * Обработка пакета в соответствии с типом
     * @param packet пакет
     * @throws IOException
     */
    private void processingPacket(Packet packet) throws IOException {
        switch (packet.getType()) {
            case CONNECT        -> authenticateUser(packet);
            case QUIT           -> closeConnection();
            case SEND_MSG       -> sendMessage(packet);
            case GET_CONTACTS   -> getContacts(packet);
            default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
        }
    }

    private void getContacts(Packet packet) throws IOException {
        if(!(packet instanceof Packet_GetContacts))
            throw new ClassCastException();
        writePacket(new Packet_ContactsResponse(userService.findAll()));
    }

    /**
     * Закрытие соединения с клиентом
     * @throws IOException
     */
    private void closeConnection() {
        log.debug("Receive packet: " + PacketType.QUIT);
        this.stop();
    }

    /**
     * Авторизация клиента
     * @param packet пакет
     * @throws IOException
     */
    private void authenticateUser(Packet packet) throws IOException {
        log.debug("Receive packet: " + PacketType.CONNECT);
        Packet_Connect pktConnect = (Packet_Connect) packet;
        if(ClientManager.users.containsKey(pktConnect.getUsername())
                && ClientManager.users.get(pktConnect.getUsername()).equals(pktConnect.getPassword())) {
            log.debug("Authentication success: " + pktConnect.getUsername());
            ClientManager.registerUserSession("admin", this);
            writePacket(new Packet_AuthenticatedResponse());
        } else {
            log.debug("Authentication failed. Wrong credentials: " + pktConnect.getUsername());
            writePacket(new Packet_ErrorResponse("Authentication failed. Wrong credentials"));
        }
    }

    /**
     * Отправка сообщения клиенту
     * @param packet
     */
    private void sendMessage(Packet packet) {
        log.debug("Receive packet: " + PacketType.SEND_MSG);
    }
}

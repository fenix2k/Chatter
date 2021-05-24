package ru.fenix2k.Chatter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.protocol.packets.*;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.Service.UserService;
import ru.fenix2k.Chatter.server.View.Views;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс для обработки сообщений клиента
 */
public class ClientWorker implements Runnable {
    private static final Logger log = Logger.getLogger(ClientWorker.class);

    private final UserService userService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    protected Socket clientSocket = null;
    private Thread currentThread;
    protected ObjectInputStream ois = null;
    protected ObjectOutputStream oos = null;
    /**
     * Потокобезопасная переменная для останоки потока
     **/
    private final AtomicBoolean running = new AtomicBoolean(false);
    /**
     * ИД пакета. Нужен для отслеживания получения ответа сервера
     **/
    private int packetId = 100;
    /**
     * Текущее состояние клиента
     */
    private ClientState state = ClientState.NOAUTH;
    /**
     * ID клиента
     */
    private Long clientId = null;
    /**
     * Логин клиента
     */
    private String clientLogin = null;

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
     *
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
        } catch (IllegalStateException | ClassNotFoundException ex) {
            try {
                writePacket(new Packet_Error("Wrong command"));
                log.debug(ex.getMessage(), ex);
            } catch (IOException e) {
                log.info(e.getMessage(), e);
            }
        } catch (IOException ex) {
            log.info(ex.getMessage(), ex);
        }
    }

    /**
     * Чтение пакета из потока
     *
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
     *
     * @param packet пакет
     * @throws IOException
     */
    private void writePacket(Packet packet) throws IOException {
        packet.setId(packetId);
        oos.writeObject(packet);
    }

    /**
     * Обработка пакета в соответствии с типом
     *
     * @param packet пакет
     * @throws IOException
     */
    private void processingPacket(Packet packet) throws IOException, IllegalStateException {
        if (this.state.equals(ClientState.NOAUTH)) {
            switch (packet.getType()) {
                case CONNECT -> authenticateUser(packet);
                default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
            }
        } else {
            switch (packet.getType()) {
                case QUIT -> closeConnection();
                case SEND_MSG -> sendMessage(packet);
                case GET_SESSIONS -> getSessions(packet);
                case GET_CONTACTS -> getContacts(packet);
                default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
            }
        }
    }

    private void getSessions(Packet packet) {
        log.debug("Received packet: " + PacketType.SESSIONS);
        if (!(packet instanceof Packet_GetSessions))
            throw new ClassCastException();
        Map<String, ClientWorker> sessions = ClientManager.getSessions();
        sessions
    }

    private void getContacts(Packet packet) throws IOException {
        log.debug("Received packet: " + PacketType.GET_CONTACTS);
        if (!(packet instanceof Packet_GetContacts))
            throw new ClassCastException();

        List<User> users = userService.findAll();
        List<String> jsonUsers = new ArrayList<>();
        for (User user : users) {
            jsonUsers.add(
                    jsonMapper
                            .writerWithView(Views.Min.class)
                            .writeValueAsString(user));
        }
        writePacket(new Packet_Contacts(jsonUsers));
    }

    /**
     * Закрытие соединения с клиентом
     *
     * @throws IOException
     */
    private void closeConnection() {
        log.debug("Received packet: " + PacketType.QUIT);
        ClientManager.removeUserSession(this.clientLogin);
        this.state = ClientState.NOAUTH;
        this.clientId = null;
        this.clientLogin = null;
        this.stop();
    }

    /**
     * Авторизация клиента
     *
     * @param packet пакет
     * @throws IOException
     */
    private void authenticateUser(Packet packet) throws IOException {
        log.debug("Received packet: " + PacketType.CONNECT);
        if (!(packet instanceof Packet_Connect))
            throw new ClassCastException();
        var pkt = (Packet_Connect) packet;

        try {
            User user = ClientManager.authenticate(pkt.getUsername(), pkt.getPassword());
            this.clientId = user.getId();
            this.clientLogin = user.getLogin();
            ClientManager.registerUserSession(user.getLogin(), this);
            this.state = ClientState.AUTHENTICATED;
            writePacket(new Packet_AuthSuccess());
        } catch (AuthenticationException e) {
            log.debug("Authentication failed. " + e.getMessage());
            writePacket(new Packet_AuthFail("Authentication failed. Wrong credentials."));
        }
    }

    /**
     * Отправка сообщения клиенту
     *
     * @param packet
     */
    private void sendMessage(Packet packet) {
        log.debug("Received packet: " + PacketType.SEND_MSG);
    }
}

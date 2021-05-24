package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.protocol.packets.*;

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

    /**
     * Возвращает статус потока
     * @return bool
     */
    public boolean isRunning() {
        return running.get();
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
                //client.processingPacketQueue.add(packet);
                // Обработка полученного пакета
                packetHandler(packet);
            } catch (ClassCastException | IllegalIdentifierException e) {
                log.debug("Packet type error: " + e.getMessage());
            } catch (ClassNotFoundException | IOException e) {
                log.debug("Socket closed");
            }
        }
        client.closeConnection();
        log.debug("SocketReader stopped");
    }

    /**
     * Метод обрабатывает пакет
     * @param packet пакет
     * @throws IOException
     */
    private void packetHandler(Packet packet) throws IllegalIdentifierException, IOException {
        log.debug("Processing packet");
        if(packet.getId() == 0) {
            // получен новый пакет от сервера
            log.debug("Packet from server received");
            switch(packet.getType()) {
                case MESSAGE        -> handleMessagePacket(packet);
                case STATUS         -> handleStatusPacket(null, packet);
                case USERINFO       -> handleUserinfoPacket(null, packet);
                case SQUIT          -> handleSquitPacket(packet);
                case INVITED        -> handleInvitedPacket(packet);
                case KICKED         -> handleKickedPacket(packet);
                default -> throw new IllegalStateException("Unknown command: " + packet.getType());
            };
        }
        else {
            // Если пакета нет в списке ожидания, то прерываем оработку пакета
            if(!client.packetResponseWaitingList.containsKey(packet.getId())) {
                log.warn("Received packet was not expected: ID=" + packet.getId() + " [" + packet.getType() + "]");
                return;
            }
            Packet sentPacket = client.packetResponseWaitingList.get(packet.getId());
            // Удаляем пакет из списка ожидания ответа
            client.packetResponseWaitingList.remove(sentPacket.getId());
            // получен ответ на запрос от сервера
            log.debug("Response packet received");
            switch(packet.getType()) {
                case AUTH_SUCCESS   -> handleAuthSuccessPacket(sentPacket, packet);
                case AUTH_FAIL      -> handleAuthFailPacket(sentPacket, packet);
                case ERROR          -> handleErrorPacket(sentPacket, packet);
                case SUCCESS        -> handleSuccessPacket(sentPacket, packet);
                case STATUS         -> handleStatusPacket(sentPacket, packet);
                case CONTACTS       -> handleContactsPacket(sentPacket, packet);
                case CONTACTS_STATUS -> handleContactsStatusPacket(sentPacket, packet);
                case USERINFO       -> handleUserinfoPacket(sentPacket, packet);
                default -> throw new IllegalStateException("Unknown command: " + packet.getType());
            };


        }
    }

    /**
     * Проверка типа пакета по значению поля type
     * @param packet пакет
     * @param type ожидаемый тип
     * @throws IllegalIdentifierException
     */
    private void checkPacketType(Packet packet, PacketType type)
            throws IllegalIdentifierException {
        if(!packet.getType().equals(type))
            throw new IllegalIdentifierException(
                    "Packet does not match the expected type. Given: " + packet.getType()
                            + ". Expected: " + type);
    }

    /**
     * Обрабатывает пакет CONTACTS_STATUS
     * @param rcvPacket
     */
    private void handleContactsStatusPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle CONTACTS_STATUS packet");
        this.checkPacketType(sentPacket, PacketType.GET_CONTACTS_STATUS);
        if(rcvPacket instanceof Packet_ContactsStatus) {
            var pkt = (Packet_ContactsStatus) rcvPacket;
            System.out.println("Your CONTACTS with statuses: " + pkt.getContacts());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет CONTACTS
     * @param rcvPacket
     */
    private void handleContactsPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle CONTACTS packet");
        this.checkPacketType(sentPacket, PacketType.GET_CONTACTS);
        if(rcvPacket instanceof Packet_Contacts) {
            var pkt = (Packet_Contacts) rcvPacket;
            System.out.println("Your CONTACTS: " + pkt.getContacts());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет KICKED
     * @param rcvPacket
     */
    private void handleKickedPacket(Packet rcvPacket) throws ClassCastException {
        log.debug("Handle KICKED packet");
        if(rcvPacket instanceof Packet_Kicked) {
            var pkt = (Packet_Kicked) rcvPacket;
            System.out.println("You was kicked from group: " + pkt.getGroup());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет INVITED
     * @param rcvPacket
     */
    private void handleInvitedPacket(Packet rcvPacket) throws ClassCastException {
        log.debug("Handle INVITE packet");
        if(rcvPacket instanceof Packet_Invited) {
            var pkt = (Packet_Invited) rcvPacket;
            System.out.println("You was invited to group: " + pkt.getGroup());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет SQUIT
     * @param rcvPacket
     */
    private void handleSquitPacket(Packet rcvPacket) throws ClassCastException {
        log.debug("Handle SQUIT packet");
        if(rcvPacket instanceof Packet_SQuit) {
            var pkt = (Packet_SQuit) rcvPacket;
            System.out.println("The server forcibly disconnected you from the server");
            client.closeConnection();
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет MESSAGE
     * @param rcvPacket
     */
    private void handleMessagePacket(Packet rcvPacket) throws ClassCastException {
        log.debug("Handle MESSAGE packet");
        if(rcvPacket instanceof Packet_Message) {
            var pkt = (Packet_Message) rcvPacket;
            System.out.println(pkt.getSender() + " was wrote: " + pkt.getMessage());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет INFO
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleUserinfoPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle INFO packet");
        if(rcvPacket instanceof Packet_Userinfo) {
            var pkt = (Packet_Userinfo) rcvPacket;
            System.out.println("USERINFO: " + pkt.getUserinfo());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет STATUS
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleStatusPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle STATUS packet");
        this.checkPacketType(sentPacket, PacketType.GET_STATUS);
        if(rcvPacket instanceof Packet_Status) {
            var pkt = (Packet_Status) rcvPacket;
            System.out.println("User " + pkt.getUser() + " has status " + pkt.getStatus());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет ERROR
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleErrorPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle ERROR packet");
        //this.checkPacketType(sentPacket, PacketType.GET_CONTACTS_STATUS);
        if(rcvPacket instanceof Packet_Error) {
            var pkt = (Packet_Error) rcvPacket;
            System.out.println("The server responded with an error:" + pkt.getMessage() + ". Previous request: " + sentPacket.getType());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет SUCCESS
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleSuccessPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle SUCCESS packet");
        //this.checkPacketType(sentPacket, PacketType.GET_CONTACTS_STATUS);
        if(rcvPacket instanceof Packet_Success) {
            var pkt = (Packet_Success) rcvPacket;
            System.out.println("The server responded success:" + pkt.getMessage() + ". Previous request: " + sentPacket.getType());
            // дописать обработку
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет AUTH_SUCCESS
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleAuthSuccessPacket(Packet sentPacket, Packet rcvPacket)
            throws IllegalIdentifierException, ClassCastException {
        log.debug("Handle AUTH_SUCCESS packet");
        this.checkPacketType(sentPacket, PacketType.CONNECT);
        if(rcvPacket instanceof Packet_AuthSuccess) {
            client.sendPacket(new Packet_GetContacts());
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }

    /**
     * Обрабатывает пакет AUTH_FAIL
     * @param sentPacket отправленный пакет
     * @param rcvPacket полученный пакет
     */
    private void handleAuthFailPacket(Packet sentPacket, Packet rcvPacket) {
        log.debug("Handle AUTH_FAIL packet");
        this.checkPacketType(sentPacket, PacketType.CONNECT);
        if(rcvPacket instanceof Packet_AuthFail) {
            var pkt = (Packet_AuthFail) rcvPacket;
            System.out.println("Authentication failed with error:" + pkt.getMessage());
        } else
            throw new ClassCastException("Packet type is wrong: " + rcvPacket.getType());
    }
}

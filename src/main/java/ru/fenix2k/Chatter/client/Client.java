package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Класс клиента
 */
public class Client {
    private static final Logger log = Logger.getLogger(Client.class);
    /** адрес сервера */
    private String HOST = "localhost";
    /** порт сервера */
    private int PORT = 9090;
    /** исходящий стрим */
    private ObjectOutputStream oos;
    /** входящий стрим */
    private ObjectInputStream ois;
    /** сокет для установки соединения с сервером */
    private Socket socket;
    /** поток для чтения команд с консоли */
    private ConsoleReader consoleReader;
    /** поток для отправки */
    private SocketWriter socketWriter;
    /** поток для приёма */
    private SocketReader socketReader;
    /** Очередь пакетов на отправку */
    public final BlockingQueue<Packet> writingPacketQueue = new LinkedBlockingQueue<>();
    /** Очередь пакетов на обработку */
    public final BlockingQueue<Packet> processingPacketQueue = new LinkedBlockingQueue<>();
    /** Хранит id и timestamp отправки отправленных пакетов на которые ожидается ответ */
    public final Map<Integer, Packet> packetResponseWaitingList = new ConcurrentHashMap<>();


    public Client(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    /**
     * Метод инициирует подключение к серверу
     * @throws IOException
     */
    public void connect() throws IOException {
        socket = new Socket(HOST, PORT);
        log.info("Connected to server " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            // создаём поток для отправки
            socketWriter = new SocketWriter(this, oos);
            // создаём поток для приёма
            socketReader = new SocketReader(this, ois);
            // создаём поток для чтения команд с консоли
            consoleReader = new ConsoleReader(this);
            // старутем потоки
            socketWriter.start();
            socketReader.start();
            consoleReader.start();

        } catch (IOException e) {
            closeConnection();
        }
    }

    /**
     * Метод закрывает все соединения и сокеты
     */
    public void closeConnection() {
        if(consoleReader.isRunning())
            consoleReader.stop();
        if(socketWriter.isRunning())
            socketWriter.stop();
        if(socketReader.isRunning())
            socketReader.stop();
        try {
            if (!socket.isClosed()) {
                oos.close();
                ois.close();
                socket.close();
                log.info("Disconnected from server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

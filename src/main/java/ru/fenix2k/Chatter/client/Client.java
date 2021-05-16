package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс клиента
 */
public class Client {
    private static final Logger log = Logger.getLogger(Client.class);
    /** адрес сервера **/
    private String HOST = "localhost";
    /** порт сервера **/
    private int PORT = 9090;
    /** исходящий стрим **/
    private ObjectOutputStream oos;
    /** входящий стрим **/
    private ObjectInputStream ois;
    /** сокет для установки соединения с сервером **/
    private Socket socket;
    /** Хранит id и timestamp отправки отправленных пакетов на которые ожидается ответ **/
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
            SocketWriter socketWriter = new SocketWriter(this, oos);
            // создаём поток для приёма
            SocketReader socketReader = new SocketReader(this, ois);
            // старутем потоки
            socketWriter.start();
            socketReader.start();

            // ждем остановки потока отправки после чего завершаем работу сервера
            socketWriter.getCurrentThread().join();
            socketReader.stop();
        } catch (IOException | InterruptedException e) {
            closeConnection();
        }
        // Закрытие соединений
        closeConnection();
        log.info("Disconnected from server");
    }

    /**
     * Метод закрывает все соединения и сокеты
     */
    private void closeConnection() {
        try {
            if (!socket.isClosed()) {
                oos.close();
                ois.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

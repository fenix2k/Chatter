package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс служит для отправки пакетов сервера через сокет
 */
public class SocketWriter implements Runnable {
    private static final Logger log = Logger.getLogger(SocketWriter.class);
    /** Ссылка на экземпляр клиента **/
    private final Client client;
    /** Текущий поток **/
    private Thread currentThread;
    /** Потокобезопасная переменная для останоки потока **/
    private final AtomicBoolean running = new AtomicBoolean(false);
    /** Исходящий поток **/
    private final ObjectOutputStream oos;
    /** Начальный ИД пакета. Нужен для отслеживания получения ответа сервера **/
    private int packetId = 100;

    public SocketWriter(Client client, ObjectOutputStream oos) {
        this.client = client;
        this.oos = oos;
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

    @Override
    public void run() {
        log.debug("SocketWriter started");
        running.set(true);
        Packet packet = null;

        // Основной цикл отправки пакетов серверу
        while (running.get()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                // Чтение команы с консоли
                System.out.println("Write a command:");
                String command = reader.readLine();
                // Если Quit, то останавливаем поток
                if(command.equals("quit")) break;
                // Парсим команду и создаём соответствующий пакет
                packet = PacketBuilder.buildFromCommand(command);
                // Устанавливает ИД пакета
                packet.setId(this.getNextId());
            } catch (IllegalStateException | IOException ex) {
                log.info(ex.getMessage(), ex);
                continue;
            }

            try {
                // Запоминаем пакет в лист ожидания ответа от сервера
                client.packetResponseWaitingList.put(packet.getId(), packet);
                // Отправка пакета серверу
                oos.writeObject(packet);
                oos.flush();
                log.debug("Send packet to server: " + packet);
            } catch (IOException ex) {
                client.packetResponseWaitingList.remove(packet.getId());
                log.info(ex.getMessage(), ex);
            }
        }
        log.debug("SocketWriter stopped");
    }

    /**
     * Возвращает следующий ИД пакета.
     * @return следующий ИД пакета
     */
    private int getNextId() {
        if(packetId == Integer.MAX_VALUE)
            return 100;
        return packetId++;
    }
}

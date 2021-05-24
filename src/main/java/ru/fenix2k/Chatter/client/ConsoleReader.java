package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsoleReader implements Runnable {
    private static final Logger log = Logger.getLogger(ConsoleReader.class);
    /** Ссылка на экземпляр клиента **/
    private final Client client;
    /** Текущий поток **/
    private Thread currentThread;
    /** Потокобезопасная переменная для останоки потока **/
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ConsoleReader(Client client) {
        this.client = client;
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

    /**
     * Возвращает статус потока
     * @return bool
     */
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void run() {
        log.debug("ConsoleReader started");
        running.set(true);
        Packet packet = null;

        // Основной цикл отправки пакетов серверу
        while (running.get()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                // Чтение команы с консоли
                System.out.print("console# ");
                String command = reader.readLine();
                // Если Quit, то останавливаем поток
                if(command.equals("quit")) {
                    running.set(false);
                    break;
                }
                // Парсим команду и создаём соответствующий пакет
                packet = PacketBuilder.buildFromCommand(command);
                // Пишем пакет в очередь отправки
                client.sendPacket(packet);
            } catch (IllegalStateException ex) {
                if(!ex.getMessage().isEmpty())
                    System.out.println("Unknown command: " + ex.getMessage());
            } catch (IOException ex) {
                log.info(ex.getMessage(), ex);
            }
        }
        client.closeConnection();
        log.debug("ConsoleReader stopped");
    }
}

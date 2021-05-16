package ru.fenix2k.Chatter.server;

/**
 * Main класс сервера
 */
public class AppServer {
    /** Порт сервера **/
    public static final int PORT = 9090;

    public static void main(String[] args) {
        // Создание экземпляра сервера и запуск потока
        Server server = new Server(PORT);
        new Thread(server).start();
    }

}

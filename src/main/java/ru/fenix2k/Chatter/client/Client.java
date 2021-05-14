package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final Logger log = Logger.getLogger(Client.class);

    private String HOST = "localhost";
    private int PORT = 9090;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean connected = true;
    private Socket socket;

    public Client(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public void connect() throws IOException {
        socket = new Socket(HOST, PORT);
        log.info("Connected to server " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            SocketWriter socketWriter = new SocketWriter(oos);
            SocketReader socketReader = new SocketReader(ois);
            socketWriter.start();
            socketReader.start();

            socketWriter.getCurrentThread().join();
            socketReader.stop();
        } catch (IOException | InterruptedException e) {
            closeConnection();
        }

        closeConnection();
        log.info("Disconnected from server");
    }

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

    private void disconnect() {
        this.connected = false;
    }

}

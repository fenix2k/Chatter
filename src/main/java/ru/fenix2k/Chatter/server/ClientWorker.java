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

public class ClientWorker implements Runnable {
    private static final Logger log = Logger.getLogger(ClientWorker.class);

    protected Socket clientSocket = null;
    protected ObjectInputStream ois = null;
    protected ObjectOutputStream oos = null;
    protected boolean connected = false;

    public ClientWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.connected = true;
    }

    public void run() {
        log.debug("New Client request received");
        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            while (connected) {
                Packet packet = readPacket();
                processingPacket(packet);
            }

            oos.close();
            ois.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Packet readPacket() throws ClassNotFoundException, IOException {
        return (Packet) ois.readObject();
    }

    private void sendPacket(Packet packet) throws IOException {
        oos.writeObject(packet);
    }

    private void processingPacket(Packet packet) throws IOException {
        switch (packet.getType()) {
            case CONNECT -> authenticateUser(packet);
            case DISCONNECT -> closeConnection();
            case SENDMSG -> sendMessage(packet);
            default -> throw new IllegalStateException("Invalid packet type: " + packet.getType());
        }
    }

    private void closeConnection() throws IOException {
        log.debug("Receive packet: " + PacketType.DISCONNECT);
        sendPacket(new Packet_Disconnect());
        this.connected = false;
    }

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

    private void sendMessage(Packet packet) {
        log.debug("Receive packet: " + PacketType.SENDMSG);
    }
}

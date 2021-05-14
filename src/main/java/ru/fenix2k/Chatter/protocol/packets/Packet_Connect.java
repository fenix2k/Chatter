package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Connect implements Packet {

    private final PacketType type = PacketType.CONNECT;
    private String username;
    private String password;

    public Packet_Connect(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

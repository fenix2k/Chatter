package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_StatusResponse extends Packet {

    private final PacketType type = PacketType.STATUS;
    private String user;
    private String status;

    public Packet_StatusResponse() {
    }

    public Packet_StatusResponse(String user, String status) {
        this.user = user;
        this.status = status;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}

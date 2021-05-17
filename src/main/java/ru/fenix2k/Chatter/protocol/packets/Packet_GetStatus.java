package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_GetStatus extends Packet {

    private final PacketType type = PacketType.GET_STATUS;
    private String user;

    public Packet_GetStatus() {
    }

    public Packet_GetStatus(String user) {
        this.user = user;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

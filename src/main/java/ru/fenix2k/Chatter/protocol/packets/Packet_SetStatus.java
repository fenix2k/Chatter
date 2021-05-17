package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_SetStatus extends Packet {

    private final PacketType type = PacketType.SET_STATUS;
    private String status;

    public Packet_SetStatus(String status) {
        this.status = status;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

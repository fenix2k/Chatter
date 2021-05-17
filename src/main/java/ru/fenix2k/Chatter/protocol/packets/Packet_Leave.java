package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Leave extends Packet {

    private final PacketType type = PacketType.LEAVE;
    private String group;

    public Packet_Leave() {
    }

    public Packet_Leave(String group) {
        this.group = group;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

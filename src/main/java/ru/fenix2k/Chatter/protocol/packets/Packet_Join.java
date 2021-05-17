package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_Join extends Packet {

    private final PacketType type = PacketType.JOIN;
    private String group;

    public Packet_Join(String group) {
        this.group = group;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

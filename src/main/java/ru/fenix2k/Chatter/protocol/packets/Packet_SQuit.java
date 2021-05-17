package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_SQuit extends Packet {
    private final PacketType type = PacketType.SQUIT;

    public Packet_SQuit() {
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

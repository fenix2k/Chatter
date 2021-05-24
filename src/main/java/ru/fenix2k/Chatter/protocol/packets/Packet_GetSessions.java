package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_GetSessions extends Packet {

    private final PacketType type = PacketType.GET_SESSIONS;

    public Packet_GetSessions() {
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

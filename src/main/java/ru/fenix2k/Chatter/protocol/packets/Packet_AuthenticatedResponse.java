package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_AuthenticatedResponse extends Packet {

    private final PacketType type = PacketType.AUTHENTICATED;

    public Packet_AuthenticatedResponse() {
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

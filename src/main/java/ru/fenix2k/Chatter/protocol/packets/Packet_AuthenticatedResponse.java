package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_AuthenticatedResponse implements Packet {

    private final PacketType type = PacketType.AUTHENTICATED;

    @Override
    public PacketType getType() {
        return type;
    }
}

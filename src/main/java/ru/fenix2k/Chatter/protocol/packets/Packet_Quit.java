package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Quit extends Packet {
    private final PacketType type = PacketType.QUIT;

    public Packet_Quit() {
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

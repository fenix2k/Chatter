package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_GetContacts extends Packet {

    private final PacketType type = PacketType.GET_CONTACTS;

    @Override
    public PacketType getType() {
        return type;
    }
}

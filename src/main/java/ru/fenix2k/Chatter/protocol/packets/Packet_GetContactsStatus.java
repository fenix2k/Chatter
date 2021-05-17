package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_GetContactsStatus extends Packet {

    private final PacketType type = PacketType.GET_CONTACTS_STATUS;

    public Packet_GetContactsStatus() {
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_RemoveContact extends Packet {

    private final PacketType type = PacketType.REMOVE_CONTACT;
    private String user;

    public Packet_RemoveContact(String user) {
        this.user = user;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

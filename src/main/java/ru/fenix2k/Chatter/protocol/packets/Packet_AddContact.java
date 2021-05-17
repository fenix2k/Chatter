package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_AddContact extends Packet {

    private final PacketType type = PacketType.ADD_CONTACT;
    private String user;

    public Packet_AddContact() {
    }

    public Packet_AddContact(String user) {
        this.user = user;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_Contacts extends Packet {

    private final PacketType type = PacketType.CONTACTS;
    private List<String> contacts;

    public Packet_Contacts() {
    }

    public Packet_Contacts(List<String> contacts) {
        this.contacts = contacts;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public List<String> getContacts() {
        return contacts;
    }
}

package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;
import java.util.Map;

public class Packet_ContactsStatusResponse extends Packet {

    private final PacketType type = PacketType.CONTACTS_STATUS;
    private Map<String, String> contacts;

    public Packet_ContactsStatusResponse(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }
}

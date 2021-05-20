package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.EntityView.UserView;

import java.util.List;

public class Packet_ContactsResponse extends Packet {

    private final PacketType type = PacketType.CONTACTS;
    private List<UserView> contacts;

    public Packet_ContactsResponse() {
    }

    public Packet_ContactsResponse(List<UserView> contacts) {
        this.contacts = contacts;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public List<UserView> getContacts() {
        return contacts;
    }
}

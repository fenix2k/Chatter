package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_Sessions extends Packet {

    private final PacketType type = PacketType.SESSIONS;
    private List<String> sessions;

    public Packet_Sessions() {
    }

    public Packet_Sessions(List<String> sessions) {
        this.sessions = sessions;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public List<String> getContacts() {
        return sessions;
    }
}

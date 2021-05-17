package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Invite extends Packet {

    private final PacketType type = PacketType.INVITE;
    private String group;
    private String user;

    public Packet_Invite(String group, String user) {
        this.group = group;
        this.user = user;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_GetGroupMembers extends Packet {

    private final PacketType type = PacketType.GET_GROUPMEMBERS;
    private String group;

    public Packet_GetGroupMembers() {
    }

    public Packet_GetGroupMembers(String group) {
        this.group = group;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

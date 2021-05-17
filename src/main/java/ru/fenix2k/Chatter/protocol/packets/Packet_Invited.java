package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Invited extends Packet {
    private final PacketType type = PacketType.INVITED;
    private String group;

    public Packet_Invited(String group) {
        this.group = group;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }
}

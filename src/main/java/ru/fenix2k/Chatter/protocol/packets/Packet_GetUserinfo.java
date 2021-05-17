package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_GetUserinfo extends Packet {

    private final PacketType type = PacketType.GET_USERINFO;
    private String user;

    public Packet_GetUserinfo(String user) {
        this.user = user;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Userinfo extends Packet {

    private final PacketType type = PacketType.USERINFO;
    private String userinfo;

    public Packet_Userinfo() {
    }

    public Packet_Userinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getUserinfo() {
        return userinfo;
    }
}

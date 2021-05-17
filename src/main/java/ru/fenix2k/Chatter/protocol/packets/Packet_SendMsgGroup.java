package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_SendMsgGroup extends Packet {

    private final PacketType type = PacketType.SEND_MSGGROUP;
    private String group;
    private String message;

    public Packet_SendMsgGroup() {
    }

    public Packet_SendMsgGroup(String group, String message) {
        this.group = group;
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

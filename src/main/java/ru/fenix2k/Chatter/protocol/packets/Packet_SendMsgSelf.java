package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_SendMsgSelf extends Packet {

    private final PacketType type = PacketType.SEND_MSGSELF;
    private String message;

    public Packet_SendMsgSelf() {
    }

    public Packet_SendMsgSelf(String message) {
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

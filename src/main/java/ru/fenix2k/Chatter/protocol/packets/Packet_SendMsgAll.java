package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_SendMsgAll extends Packet {

    private final PacketType type = PacketType.SEND_MSGALL;
    private String message;

    public Packet_SendMsgAll(String message) {
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

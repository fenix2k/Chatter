package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_SendMsg extends Packet {

    private final PacketType type = PacketType.SEND_MSG;
    private List<String> recipients;
    private String message;

    public Packet_SendMsg() {
    }

    public Packet_SendMsg(List<String> recipients, String message) {
        this.recipients = recipients;
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }
}

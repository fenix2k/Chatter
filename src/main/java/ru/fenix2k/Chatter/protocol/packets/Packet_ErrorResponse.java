package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_ErrorResponse extends Packet {

    private final PacketType type = PacketType.ERROR;
    private String message;

    public Packet_ErrorResponse() {
    }

    public Packet_ErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}

package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_SuccessResponse extends Packet {

    private final PacketType type = PacketType.SUCCESS;
    private String message;

    public Packet_SuccessResponse(String message) {
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

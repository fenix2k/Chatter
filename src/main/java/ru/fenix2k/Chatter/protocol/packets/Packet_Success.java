package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Success extends Packet {

    private final PacketType type = PacketType.SUCCESS;
    private String message;

    public Packet_Success() {
    }

    public Packet_Success(String message) {
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

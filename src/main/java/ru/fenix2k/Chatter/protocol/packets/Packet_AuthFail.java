package ru.fenix2k.Chatter.protocol.packets;


import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_AuthFail extends Packet {

    private final PacketType type = PacketType.AUTH_FAIL;
    private String message;

    public Packet_AuthFail(String message) {
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

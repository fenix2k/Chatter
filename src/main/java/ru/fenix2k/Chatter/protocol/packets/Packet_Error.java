package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

public class Packet_Error extends Packet {

    private final PacketType type = PacketType.ERROR;
    private String message;

    public Packet_Error() {
    }

    public Packet_Error(String message) {
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

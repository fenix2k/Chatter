package ru.fenix2k.Chatter.protocol.packets;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketType;

import java.util.List;

public class Packet_Message extends Packet {

    private final PacketType type = PacketType.MESSAGE;
    private String sender;
    private String message;

    public Packet_Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public PacketType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}

package ru.fenix2k.Chatter.protocol;

import java.io.Serializable;

public interface Packet extends Serializable {
    PacketType getType();
}

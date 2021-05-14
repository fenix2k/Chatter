package ru.fenix2k.Chatter.client;

import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.packets.Packet_Connect;
import ru.fenix2k.Chatter.protocol.packets.Packet_Disconnect;
import ru.fenix2k.Chatter.protocol.packets.Packet_SendMessage;

import java.util.List;

public class PacketBuilder {

    public static Packet buildFromCommand(String command)
            throws IllegalStateException {
        String[] cmd = command.split(" ");

        return switch(cmd[0]) {
            case "connect" -> connectPacketBuilder(cmd);
            case "quit" -> disconnectPacketBuilder();
            case "sendmsg" -> sendmsgPacketBuilder(cmd);
            default -> throw new IllegalStateException("Unknown command: " + cmd[0]);
        };
    }

    private static Packet disconnectPacketBuilder() {
        return new Packet_Disconnect();
    }

    private static Packet connectPacketBuilder(String[] cmd)
            throws IllegalStateException {
        if(cmd.length == 3) {
            String username = cmd[1].trim();
            String password = cmd[2].trim();
            return new Packet_Connect(username, password);
        }
        throw new IllegalStateException("Invalid command");
    }

    private static Packet sendmsgPacketBuilder(String[] cmd)
            throws IllegalStateException {
        if(cmd.length == 3) {
            List<String> username = List.of(cmd[1].trim().split(","));
            String message = cmd[2].trim();
            return new Packet_SendMessage(username, message);
        }
        throw new IllegalStateException("Invalid command");
    }

}

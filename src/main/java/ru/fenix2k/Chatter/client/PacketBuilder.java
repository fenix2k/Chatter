package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.packets.Packet_Connect;
import ru.fenix2k.Chatter.protocol.packets.Packet_Disconnect;
import ru.fenix2k.Chatter.protocol.packets.Packet_SendMessage;

import java.util.List;

/**
 * Класс служит для создания различных пакетов
 */
public class PacketBuilder {
    private static final Logger log = Logger.getLogger(PacketBuilder.class);

    /**
     * Получает на вход строку-команду, парсит ее и формирует соответсвующий пакет
     * @param command команда с консоли
     * @return пакет
     * @throws IllegalStateException
     */
    public static Packet buildFromCommand(String command)
            throws IllegalStateException {
        log.debug("Parsing command: " + command);
        String[] cmd = command.split(" ");

        return switch(cmd[0]) {
            case "connect" -> connectPacketBuilder(cmd);
            case "quit" -> disconnectPacketBuilder();
            case "sendmsg" -> sendmsgPacketBuilder(cmd);
            default -> throw new IllegalStateException("Unknown command: " + cmd[0]);
        };
    }

    /**
     * Формирует пакет Packet_Disconnect
     * @return пакет Packet_Disconnect
     */
    private static Packet disconnectPacketBuilder() {
        log.debug("Build Packet_Disconnect");
        return new Packet_Disconnect();
    }

    /**
     * Формирует пакет Packet_Connect
     * @return пакет Packet_Connect
     */
    private static Packet connectPacketBuilder(String[] cmd)
            throws IllegalStateException {
        log.debug("Build Packet_Connect");
        if(cmd.length == 3) {
            String username = cmd[1].trim();
            String password = cmd[2].trim();
            return new Packet_Connect(username, password);
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет Packet_SendMessage
     * @return пакет Packet_SendMessage
     */
    private static Packet sendmsgPacketBuilder(String[] cmd)
            throws IllegalStateException {
        log.debug("Build Packet_SendMessage");
        if(cmd.length == 3) {
            List<String> username = List.of(cmd[1].trim().split(","));
            String message = cmd[2].trim();
            return new Packet_SendMessage(username, message);
        }
        throw new IllegalStateException("Invalid command");
    }

}

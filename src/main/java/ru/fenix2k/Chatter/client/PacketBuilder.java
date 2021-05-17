package ru.fenix2k.Chatter.client;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.Packet;
import ru.fenix2k.Chatter.protocol.PacketFactory;
import ru.fenix2k.Chatter.protocol.PacketType;
import ru.fenix2k.Chatter.protocol.packets.Packet_Connect;
import ru.fenix2k.Chatter.protocol.packets.Packet_Quit;
import ru.fenix2k.Chatter.protocol.packets.Packet_SendMsg;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        return switch(cmd[0].trim().toLowerCase(Locale.ROOT)) {
            case "connect"      -> buildConnectPacket(cmd);
            case "quit"         -> buildQuitPacket();
            case "sendmsg"      -> buildSendmsgPacket(cmd);
            case "getcontacts"  -> buildGetContactsPacket(cmd);
            case "getstatus"    -> buildGetStatusPacket(cmd);
            case "getuserinfo"  -> buildGetUserinfoPacket(cmd);
            case "addcontact"   -> buildAddContactPacket(cmd);
            default -> throw new IllegalStateException(cmd[0]);
        };
    }

    private static Packet buildAddContactPacket(String[] cmd) {
    }

    private static Packet buildGetUserinfoPacket(String[] cmd) {
    }

    private static Packet buildGetStatusPacket(String[] cmd) {
    }

    private static Packet buildGetContactsPacket(String[] cmd) {
    }

    /**
     * Формирует пакет Packet_Quit
     * @return пакет Packet_Quit
     */
    private static Packet buildQuitPacket() {
        log.debug("Build Packet_Quit");
        return PacketFactory.build(PacketType.QUIT);
    }

    /**
     * Формирует пакет Packet_Connect
     * @return пакет Packet_Connect
     */
    private static Packet buildConnectPacket(String[] cmd)
            throws IllegalStateException {
        log.debug("Build Packet_Connect");
        if(cmd.length == 3) {
            String username = cmd[1].trim();
            String password = cmd[2].trim();
            return PacketFactory.build(PacketType.CONNECT,
                    Map.of("username", username, "password", password));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет Packet_SendMessage
     * @return пакет Packet_SendMessage
     */
    private static Packet buildSendmsgPacket(String[] cmd)
            throws IllegalStateException {
        log.debug("Build Packet_SendMessage");
        if(cmd.length == 3) {
            List<String> username = List.of(cmd[1].trim().split(","));
            String message = cmd[2].trim();
            return PacketFactory.build(PacketType.SEND_MSG,
                    Map.of("username", username, "message", message));
        }
        throw new IllegalStateException("Invalid command");
    }

}

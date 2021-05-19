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
            case "sendmsgself"  -> buildSendmsgSelfPacket(cmd);
            case "getcontacts"  -> buildGetContactsPacket();
            case "getcontactsstatus"  -> buildGetContactsStatusPacket();
            case "getstatus"    -> buildGetStatusPacket(cmd);
            case "getuserinfo"  -> buildGetUserinfoPacket(cmd);
            case "addcontact"   -> buildAddContactPacket(cmd);
            default -> throw new IllegalStateException(cmd[0]);
        };
    }

    /**
     * Формирует пакет ADD_CONTACT
     * @param cmd параметры
     * @return пакет ADD_CONTACT
     */
    private static Packet buildAddContactPacket(String[] cmd) {
        log.debug("Build packet ADD_CONTACT");
        if(cmd.length == 2) {
            String username = cmd[1].trim();
            return PacketFactory.build(PacketType.ADD_CONTACT,
                    Map.of("username", username));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет GET_USERINFO
     * @param cmd параметры
     * @return пакет GET_USERINFO
     */
    private static Packet buildGetUserinfoPacket(String[] cmd) {
        log.debug("Build packet GET_USERINFO");
        if(cmd.length == 2) {
            String username = cmd[1].trim();
            return PacketFactory.build(PacketType.GET_USERINFO,
                    Map.of("username", username));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет GET_STATUS
     * @param cmd параметры
     * @return пакет GET_STATUS
     */
    private static Packet buildGetStatusPacket(String[] cmd) {
        log.debug("Build packet GET_STATUS");
        if(cmd.length == 2) {
            String username = cmd[1].trim();
            return PacketFactory.build(PacketType.GET_STATUS,
                    Map.of("username", username));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет GET_CONTACTS
     * @return пакет GET_CONTACTS
     */
    private static Packet buildGetContactsPacket() {
        log.debug("Build packet GET_CONTACTS");
        return PacketFactory.build(PacketType.GET_CONTACTS);
    }

    /**
     * Формирует пакет GET_CONTACTS_STATUS
     * @return пакет GET_CONTACTS_STATUS
     */
    private static Packet buildGetContactsStatusPacket() {
        log.debug("Build packet GET_CONTACTS_STATUS");
        return PacketFactory.build(PacketType.GET_CONTACTS_STATUS);
    }

    /**
     * Формирует пакет QUIT
     * @return пакет QUIT
     */
    private static Packet buildQuitPacket() {
        log.debug("Build packet QUIT");
        return PacketFactory.build(PacketType.QUIT);
    }

    /**
     * Формирует пакет CONNECT
     * @param cmd параметры
     * @return пакет CONNECT
     */
    private static Packet buildConnectPacket(String[] cmd)
            throws IllegalStateException {
        log.debug("Build packet CONNECT");
        if(cmd.length == 3) {
            String username = cmd[1].trim();
            String password = cmd[2].trim();
            return PacketFactory.build(PacketType.CONNECT,
                    Map.of("username", username, "password", password));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет SEND_MSG
     * @param cmd параметры
     * @return пакет SEND_MSG
     */
    private static Packet buildSendmsgPacket(String[] cmd)
            throws IllegalStateException {
        log.debug("Build packet SEND_MSG");
        if(cmd.length == 3) {
            List<String> username = List.of(cmd[1].trim().split(","));
            String message = cmd[2].trim();
            return PacketFactory.build(PacketType.SEND_MSG,
                    Map.of("username", username, "message", message));
        }
        throw new IllegalStateException("Invalid command");
    }

    /**
     * Формирует пакет SEND_MSGSELF
     * @param cmd параметры
     * @return пакет SEND_MSGSELF
     */
    private static Packet buildSendmsgSelfPacket(String[] cmd) {
        log.debug("Build packet SEND_MSG");
        if(cmd.length == 2) {
            String message = cmd[1].trim();
            return PacketFactory.build(PacketType.SEND_MSGSELF,
                    Map.of("message", message));
        }
        throw new IllegalStateException("Invalid command");
    }

}

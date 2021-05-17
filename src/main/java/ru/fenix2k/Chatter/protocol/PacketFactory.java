package ru.fenix2k.Chatter.protocol;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.client.PacketBuilder;
import ru.fenix2k.Chatter.protocol.packets.*;

import java.util.Map;

public class PacketFactory {
    private static final Logger log = Logger.getLogger(PacketFactory.class);

    public static Packet build(PacketType type) {
        return build(type, null);
    }

    public static Packet build(PacketType type, Map<String, Object> params) {
        return switch (type) {
            case QUIT           -> new Packet_Quit();
            case SQUIT          -> new Packet_SQuit();
            case CONNECT        -> buildConnectPacket(params);
            case AUTHENTICATED  -> new Packet_AuthenticatedResponse();
            case ERROR          -> buildErrorPacket(params);
            case SUCCESS        -> buildSuccessPacket(params);

            case STATUS         -> buildStatusPacket(params);
            case GET_STATUS     -> buildGetStatusPacket(params);
            case SET_STATUS     -> buildSetStatusPacket(params);

            case MESSAGE        -> buildMessagePacket(params);
            case SEND_MSG       -> buildSendMsgPacket(params);
            case SEND_MSGALL    -> buildSendMsgAllPacket(params);
            case SEND_MSGGROUP  -> buildSendMsgGroupPacket(params);

            case USERINFO       -> buildUserinfoPacket(params);
            case GET_USERINFO   -> buildGetUserinfoPacket(params);

            case CONTACTS       -> buildContactsPacket(params);
            case CONTACTS_STATUS -> buildContactsStatusPacket(params);
            case GET_CONTACTS   -> new Packet_GetContacts();
            case GET_CONTACTS_STATUS -> new Packet_GetContactsStatus();
            case ADD_CONTACT    -> buildAddContactPacket(params);
            case REMOVE_CONTACT -> buildRemoveContactPacket(params);

            case JOIN           -> buildJoinPacket(params);
            case LEAVE          -> buildLeavePacket(params);
            case GET_GROUPMEMBERS -> buildGetGroupsMembersPacket(params);
            case KICK           -> buildKickPacket(params);
            case KICKED         -> buildKickedPacket(params);
            case INVITE         -> buildInvitePacket(params);
            case INVITED        -> buildInvitedPacket(params);
        };
    }

    private static Packet buildInvitedPacket(Map<String, Object> params) {
        return new Packet_Invited();
    }

    private static Packet buildInvitePacket(Map<String, Object> params) {
        return new Packet_Invite();
    }

    private static Packet buildKickedPacket(Map<String, Object> params) {
        return new Packet_Kicked();
    }

    private static Packet buildKickPacket(Map<String, Object> params) {
        return new Packet_Kick();
    }

    private static Packet buildGetGroupsMembersPacket(Map<String, Object> params) {
        return new Packet_GetGroupMembers();
    }

    private static Packet buildLeavePacket(Map<String, Object> params) {
        return new Packet_Leave();
    }

    private static Packet buildJoinPacket(Map<String, Object> params) {
        return new Packet_Join();
    }

    private static Packet buildRemoveContactPacket(Map<String, Object> params) {
        return new Packet_RemoveContact();
    }

    private static Packet buildAddContactPacket(Map<String, Object> params) {
        return new Packet_AddContact();
    }

    private static Packet buildContactsStatusPacket(Map<String, Object> params) {
        return new Packet_ContactsStatusResponse();
    }

    private static Packet buildContactsPacket(Map<String, Object> params) {
        return new Packet_ContactsResponse();
    }

    private static Packet buildGetUserinfoPacket(Map<String, Object> params) {
        return new Packet_GetUserinfo();
    }

    private static Packet buildUserinfoPacket(Map<String, Object> params) {
        return new Packet_UserinfoResponse();
    }

    private static Packet buildSendMsgGroupPacket(Map<String, Object> params) {
        return new Packet_SendMsgGroup();
    }

    private static Packet buildSendMsgAllPacket(Map<String, Object> params) {
        return new Packet_SendMsgAll();
    }

    private static Packet buildSendMsgPacket(Map<String, Object> params) {
        return new Packet_SendMsg();
    }

    private static Packet buildMessagePacket(Map<String, Object> params) {
        return new Packet_Message();
    }

    private static Packet buildSetStatusPacket(Map<String, Object> params) {
        return new Packet_SetStatus();
    }

    private static Packet buildGetStatusPacket(Map<String, Object> params) {
        return new Packet_GetStatus();
    }

    private static Packet buildStatusPacket(Map<String, Object> params) {
        return new Packet_StatusResponse();
    }

    private static Packet buildSuccessPacket(Map<String, Object> params) {
        return new Packet_SuccessResponse();
    }

    private static Packet buildErrorPacket(Map<String, Object> params) {
        return new Packet_ErrorResponse();
    }

    private static Packet buildConnectPacket(Map<String, Object> params) {
        return new Packet_Connect();
    }


}

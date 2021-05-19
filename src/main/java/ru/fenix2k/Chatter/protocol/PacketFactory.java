package ru.fenix2k.Chatter.protocol;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.protocol.packets.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public class PacketFactory {
    private static final Logger log = Logger.getLogger(PacketFactory.class);

    public static Packet build(PacketType type) throws InvalidParameterException {
        return build(type, null);
    }

    public static Packet build(PacketType type, Map<String, Object> params) throws InvalidParameterException {
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
            case SEND_MSGSELF   -> buildSendMsgSelfPacket(params);

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

    private static void checkStringParametersByKey(Map<String, Object> params, List<String> keys) throws InvalidParameterException {
        for (String key : keys) {
            if(!params.containsKey(key) || params.get(key).toString().isEmpty())
                throw new InvalidParameterException("Missing parameter or invalid: " + key);
        }
    }

    private static void checkListParametersByKey(Map<String, Object> params, List<String> keys) throws InvalidParameterException {
        for (String key : keys) {
            if(!params.containsKey(key))
                throw new InvalidParameterException("Missing parameter or invalid: " + key);
            try {
                List<String> list = (List<String>) params.get(key);
                if(list.isEmpty())
                    throw new InvalidParameterException("Missing parameter or invalid: " + key);
            } catch (ClassCastException e) {
                throw new InvalidParameterException("Missing parameter or invalid: " + key);
            }
        }
    }

    private static void checkMapParametersByKey(Map<String, Object> params, List<String> keys) throws InvalidParameterException {
        for (String key : keys) {
            if(!params.containsKey(key))
                throw new InvalidParameterException("Missing parameter or invalid: " + key);
            try {
                Map<String, String> map = (Map<String, String>) params.get(key);
                if(map.isEmpty())
                    throw new InvalidParameterException("Missing parameter or invalid: " + key);
            } catch (ClassCastException e) {
                throw new InvalidParameterException("Missing parameter or invalid: " + key);
            }
        }
    }

    private static Packet buildInvitedPacket(Map<String, Object> params) throws InvalidParameterException {
        if(params.isEmpty())
            return new Packet_Invited();
        checkStringParametersByKey(params, List.of("group"));
        return new Packet_Invited(params.get("group").toString());
    }

    private static Packet buildInvitePacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Invite();
        checkStringParametersByKey(params, List.of("group", "username"));
        return new Packet_Invite(params.get("group").toString(), params.get("username").toString());
    }

    private static Packet buildKickedPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Kicked();
        checkStringParametersByKey(params, List.of("group"));
        return new Packet_Kicked(params.get("group").toString());
    }

    private static Packet buildKickPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Kick();
        checkStringParametersByKey(params, List.of("group", "username"));
        return new Packet_Kick(params.get("group").toString(), params.get("username").toString());
    }

    private static Packet buildGetGroupsMembersPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_GetGroupMembers();
        checkStringParametersByKey(params, List.of("group"));
        return new Packet_GetGroupMembers(params.get("group").toString());
    }

    private static Packet buildLeavePacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Leave();
        checkStringParametersByKey(params, List.of("group"));
        return new Packet_Leave(params.get("group").toString());
    }

    private static Packet buildJoinPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Join();
        checkStringParametersByKey(params, List.of("group"));
        return new Packet_Join(params.get("group").toString());
    }

    private static Packet buildRemoveContactPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_RemoveContact();
        checkStringParametersByKey(params, List.of("username"));
        return new Packet_RemoveContact(params.get("username").toString());
    }

    private static Packet buildAddContactPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_AddContact();
        checkStringParametersByKey(params, List.of("username"));
        return new Packet_AddContact(params.get("username").toString());
    }

    private static Packet buildContactsStatusPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_ContactsStatusResponse();
        checkMapParametersByKey(params, List.of("contacts"));
        return new Packet_ContactsStatusResponse((Map<String, String>) params.get("contacts"));
    }

    private static Packet buildContactsPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_ContactsResponse();
        checkListParametersByKey(params, List.of("contacts"));
        return new Packet_ContactsResponse((List<String>) params.get("contacts"));
    }

    private static Packet buildGetUserinfoPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_GetUserinfo();
        checkStringParametersByKey(params, List.of("username"));
        return new Packet_GetUserinfo(params.get("username").toString());
    }

    private static Packet buildUserinfoPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_UserinfoResponse();
        checkStringParametersByKey(params, List.of("userinfo"));
        return new Packet_UserinfoResponse(params.get("userinfo").toString());
    }

    private static Packet buildSendMsgSelfPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SendMsgSelf();
        checkStringParametersByKey(params, List.of("message"));
        return new Packet_SendMsgSelf(params.get("message").toString());
    }

    private static Packet buildSendMsgGroupPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SendMsgGroup();
        checkStringParametersByKey(params, List.of("group", "message"));
        return new Packet_SendMsgGroup(params.get("group").toString(), params.get("message").toString());
    }

    private static Packet buildSendMsgAllPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SendMsgAll();
        checkStringParametersByKey(params, List.of("message"));
        return new Packet_SendMsgAll(params.get("message").toString());
    }

    private static Packet buildSendMsgPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SendMsg();
        checkStringParametersByKey(params, List.of("message"));
        checkListParametersByKey(params, List.of("recipients"));
        return new Packet_SendMsg((List<String>) params.get("recipients"), params.get("message").toString());
    }

    private static Packet buildMessagePacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Message();
        checkStringParametersByKey(params, List.of("sender", "message"));
        return new Packet_Message(params.get("sender").toString(), params.get("message").toString());
    }

    private static Packet buildSetStatusPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SetStatus();
        checkStringParametersByKey(params, List.of("status"));
        return new Packet_SetStatus(params.get("status").toString());
    }

    private static Packet buildGetStatusPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_GetStatus();
        checkStringParametersByKey(params, List.of("username"));
        return new Packet_GetStatus(params.get("username").toString());
    }

    private static Packet buildStatusPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Message();
        checkStringParametersByKey(params, List.of("username", "status"));
        return new Packet_Message(params.get("username").toString(), params.get("status").toString());
    }

    private static Packet buildSuccessPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_SuccessResponse();
        checkStringParametersByKey(params, List.of("message"));
        return new Packet_SuccessResponse(params.get("message").toString());
    }

    private static Packet buildErrorPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_ErrorResponse();
        checkStringParametersByKey(params, List.of("message"));
        return new Packet_ErrorResponse(params.get("message").toString());
    }

    private static Packet buildConnectPacket(Map<String, Object> params) {
        if(params.isEmpty())
            return new Packet_Connect();
        checkStringParametersByKey(params, List.of("username", "password"));
        return new Packet_Connect(params.get("username").toString(), params.get("password").toString());
    }


}

package ru.fenix2k.Chatter.protocol;

/**
 * Определяет все возможные типы сообщений
 */
public enum PacketType {
    /** Client communication commands **/
    SENDMSG       ,              // \send <куда> <сообщение>, где <куда> - пользователь или несколько через запятую
    SENDMSGGROUP    ,              // \sendgroup <куда> <сообщение>, где <куда> - группа или несколько групп через запятую
    SENDMSGALL     ,              // \sendall <сообщение>

    /** Client-Server commands **/
    CONNECT        ,              // \connect <username> <password>
    DISCONNECT    ,              // \quit
    ACTIVE       ,              // \active
    INACTIVE      ,              // \inactive
    VISIBLE       ,              // \visible
    INVISIBLE      ,              // \invisible
    USERINFO        ,              // \\userinfo <username>
    JOIN            ,              // \join <groupname> <password>
    INVITE          ,              // \invite <groupname> <username>
    KICK           ,              // \kick <groupname> <username>
    GROUPMEMBERS    ,              // \groupmembers <groupname>

    /** Server-Client commands **/
    RCVMSG,
    RCVSTATUS,
    ERROR         ,              // \err <сообщение об ощибке>
    SUCCESS,              // \success <сообщение cервера>
    AUTHENTICATED
}

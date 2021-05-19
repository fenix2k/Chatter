package ru.fenix2k.Chatter.protocol;

/**
 * Определяет все возможные типы сообщений
 */
public enum PacketType {
    /** Client communication commands **/
    SEND_MSG,           // send <куда> <сообщение>, где <куда> - пользователь или несколько через запятую
    SEND_MSGGROUP,      // sendgroup <куда> <сообщение>, где <куда> - группа или несколько групп через запятую
    SEND_MSGALL,        // sendall <сообщение>
    SEND_MSGSELF,       // sendself <сообщение>

    /** Client-Server commands **/
    CONNECT,            // connect <username> <password>
    QUIT,               // quit
    GET_CONTACTS,       // список контактов
    GET_CONTACTS_STATUS,// список статусов контактов
    ADD_CONTACT,        // добавить пользователя в список контактов
    REMOVE_CONTACT,     // удалить пользователей из списка контактов
    SET_STATUS,         // setstatus [visible, invisible, active, inactive]
    GET_STATUS,         // status <username>
    GET_USERINFO,       // userinfo <username>
    JOIN,               // join <groupname> <password>
    INVITE,             // invite <groupname> <username>
    KICK,               // kick <groupname> <username>
    LEAVE,              // leave <group>
    GET_GROUPMEMBERS,   // groupmembers <groupname>

    /** Server-Client commands **/
    MESSAGE,            // <откуда> <сообщение> , где <откуда> - пользователь или несколько через запятую
    STATUS,             // <чей> <статус>
    USERINFO,           // <о ком?> <информация>
    CONTACTS,           // список контактов
    CONTACTS_STATUS,    // список контактов со статусами
    INVITED,            // оповещение о добавлении в группу
    KICKED,             // оповещение об исключении из группы
    ERROR,              // <сообщение об ощибке>
    SUCCESS,            // <сообщение cервера>
    AUTHENTICATED,      // успешное сообщение об авторизации
    SQUIT               // принудительное отключение от сервера
}

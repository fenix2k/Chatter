package ru.fenix2k.Chatter.protocol;

import java.io.Serializable;

/**
 * Базовый класс пакета
 */
public abstract class Packet implements Serializable {
    /** ИД пакета **/
    private int id = 100;
    /** Таймстамп создания пакета **/
    private long timestamp = System.currentTimeMillis();

    /**
     * Возвращает тип пакета
     * @return тип пакета
     */
    abstract public PacketType getType();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

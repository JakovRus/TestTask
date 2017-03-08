package ru.jakovRus.entity;

import java.io.Serializable;

/**
 * @author Jakov Rogov
 */
public class Entity implements Cloneable, Serializable {
    protected Long ID;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}

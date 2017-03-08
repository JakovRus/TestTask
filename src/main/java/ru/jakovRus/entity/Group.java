package ru.jakovRus.entity;

/**
 * @author Jakov Rogov
 */
public class Group extends Entity {
    private String numberOfGroup;
    private String nameOfFaculty;

    public String getNumberOfGroup() {
        return numberOfGroup;
    }

    public String getNameOfFaculty() {
        return nameOfFaculty;
    }

    public void setNumberOfGroup(String numberOfGroup) {
        this.numberOfGroup = numberOfGroup;
    }

    public void setNameOfFaculty(String nameOfFaculty) {
        this.nameOfFaculty = nameOfFaculty;
    }

    @Override
    public String toString() {
        return numberOfGroup + " " + nameOfFaculty;
    }

    @Override
    public Group clone() throws CloneNotSupportedException {
        return (Group) super.clone();
    }
}

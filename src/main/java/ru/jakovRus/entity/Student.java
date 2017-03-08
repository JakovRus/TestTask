package ru.jakovRus.entity;

import java.util.Date;

/**
 * @author Jakov Rogov
 */
public class Student extends Entity {
    private String name;
    private String surname;
    private String patronymic;
    private Date birthdate;
    private String numberOfGroup;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getNumberOfGroup() {
        return numberOfGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setNumberOfGroup(String namberOfGroup) {
        this.numberOfGroup = namberOfGroup;
    }


    @Override
    public String toString() {
        return name + " " + surname + " " + patronymic + " " + birthdate + " " + numberOfGroup;
    }

    @Override
    public Student clone() throws CloneNotSupportedException {
        return (Student) super.clone();
    }
}
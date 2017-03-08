package ru.jakovRus.service;

import ru.jakovRus.database.Database;
import ru.jakovRus.entity.Student;
import ru.jakovRus.util.EntityNotUniqueException;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jakov Rogov
 */
public class StudentService implements Service<Student> {
    private static final String INSERTION_QUERY = "INSERT INTO students(name, surname, patronymic, birthdate, number_of_group) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String DELETION_QUERY = "DELETE FROM students WHERE ID = ?";
    private static final String SELECTION_QUERY = "SELECT * FROM students WHERE LCASE(surname) LIKE ? " +
            "AND LCASE(number_of_group) LIKE ?";
    private static final String UPDATING_QUERY = "UPDATE students SET " +
            "name = ?, surname = ?, patronymic = ?, " +
            "birthdate = ?, number_of_group = ? WHERE id = ?";

    private Database database;
    private static StudentService instance;

    private StudentService() {
        database = Database.getInstance();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    @Override
    public List<Student> findAll() {
        return findAll("%", "%");
    }

    @Override
    public List<Student> findAll(@Nullable String surnameFilter, @Nullable String groupFilter) {
        if (surnameFilter == null) {
            surnameFilter = "%";
        }

        if (groupFilter == null) {
            groupFilter = "%";
        }

        List<Student> students = new ArrayList<>();
        ResultSet rs = database.execute(SELECTION_QUERY, "%" + surnameFilter.toLowerCase() + "%",
                "%" + groupFilter.toLowerCase() + "%");

        try {
            while (rs.next()) {
                Student student = new Student();
                student.setID(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setSurname(rs.getString("surname"));
                student.setPatronymic(rs.getString("patronymic"));
                student.setBirthdate(rs.getDate("birthdate"));
                student.setNumberOfGroup(rs.getString("number_of_group"));

                students.add(student);
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL exception");
        }

        Collections.sort(students, Comparator.comparing(student -> student.toString().toLowerCase()));
        return students;
    }

    @Override
    public void save(Student entity) throws EntityNotUniqueException {
        if (entity == null) {
            return;
        }

        try {
            if (entity.getID() == null) {
                database.executeUpdate(INSERTION_QUERY, entity.getName(), entity.getSurname(),
                        entity.getPatronymic(), entity.getBirthdate(), entity.getNumberOfGroup());
            } else {
                database.executeUpdate(UPDATING_QUERY, entity.getName(), entity.getSurname(),
                        entity.getPatronymic(), entity.getBirthdate(),
                        entity.getNumberOfGroup(), entity.getID());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EntityNotUniqueException(e);
        }
    }

    @Override
    public void delete(Student entity) {
        if (entity == null || entity.getID() == null) {
            return;
        }

        try {
            database.executeUpdate(DELETION_QUERY, entity.getID());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("[ERROR] Could not delete student");
        }
    }
}

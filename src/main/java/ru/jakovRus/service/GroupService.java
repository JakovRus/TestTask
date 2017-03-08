package ru.jakovRus.service;

import ru.jakovRus.database.Database;
import ru.jakovRus.entity.Group;
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
public class GroupService implements Service<Group> {
    private static final String INSERTION_QUERY = "INSERT INTO groups(number_of_group, name_of_faculty) VALUES (?, ?)";
    private static final String DELETION_QUERY = "DELETE FROM groups WHERE ID = ?";
    private static final String SELECTION_QUERY = "SELECT * FROM groups WHERE name_of_faculty LIKE ? " +
            "AND number_of_group LIKE ?";
    private static final String UPDATING_QUERY = "UPDATE groups SET number_of_group = ?, name_of_faculty = ? WHERE id = ?";

    private Database database;

    private static GroupService instance;

    private GroupService() {
        database = Database.getInstance();
    }

    public static GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }

        return instance;
    }

    @Override
    public List<Group> findAll() {
        return findAll(null, null);
    }

    @Override
    public List<Group> findAll(@Nullable String facultyFilter, @Nullable String groupFilter) {
        if (facultyFilter == null) {
            facultyFilter = "%";
        }

        if (groupFilter == null) {
            groupFilter = "%";
        }

        List<Group> groups = new ArrayList<>();
        ResultSet rs = database.execute(SELECTION_QUERY, "%" + facultyFilter + "%", "%" + groupFilter + "%");

        if (rs == null) {
            return groups;
        }

        try {
            while (rs.next()) {
                Group group = new Group();
                group.setID(rs.getLong("id"));
                group.setNumberOfGroup(rs.getString("number_of_group"));
                group.setNameOfFaculty(rs.getString("name_of_faculty"));

                groups.add(group);
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] SQL exception");
        }

        Collections.sort(groups, Comparator.comparing(group -> group.toString().toLowerCase()));
        return groups;
    }

    @Override
    public void save(Group entity) throws EntityNotUniqueException {
        if (entity == null) {
            return;
        }

        try {
            if (entity.getID() == null) {
                database.executeUpdate(INSERTION_QUERY, entity.getNumberOfGroup(), entity.getNameOfFaculty());
            } else {
                database.executeUpdate(UPDATING_QUERY, entity.getNumberOfGroup(), entity.getNameOfFaculty(), entity.getID());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EntityNotUniqueException(e);
        }
    }

    @Override
    public void delete(Group entity) {
        if (entity == null || entity.getID() == null) {
            return;
        }

        try {
            database.executeUpdate(DELETION_QUERY, entity.getID());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("[ERROR] Could not delete group");
        }
    }
}
